package javaFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class calculates and stores the data pertaining to the results of fusions between 
 * all the personas in Persona 5. The data is taken from the personaData.txt, possibleFusions.txt
 * and treasureFusions.txt datasets. 
 */
public class FusionDataGraph {
	
	public String troubleshooting = "";
	public boolean wtf = true;

	// nodes are personas and edges is the persona that is the result of fusing the two
	// nodes it connects
	public Graph<String, String> fusions;
	
	// map from persona names to the corresponding persona object
	public HashMap<String, Persona> personas;
	
	// map from arcana names to an ordered (by base level) list of persona in that arcana
	public HashMap<String, List<Persona>> arcanas;
	
	// map from persona names to a list of pairs of persona that fuse together to make the key persona
	public HashMap<String, List<Pair>> fusionResults;
	
	// map from each arcana name to the highest level persona in the arcana
	public HashMap<String, Integer> highestLevels;
	
	// nodes are arcanas and edges are the resulting arcana from fusing the two nodes the edge
	// connects
	public Graph<String, String> possibleFusions;
	
	// map from treasure demon names to a map of arcana names to integers that represent
	// how many levels to go up/down when fusing a regular persona with a treasure demon
	public HashMap<String, HashMap<String, Integer>> treasureFusions;
			
    /**
     * @effects Constructs a FusionDataGraph
     */
	public FusionDataGraph() {
		fusions = new Graph<String, String>();
		personas = new HashMap<String, Persona>();
		arcanas = new HashMap<String, List<Persona>>();
		fusionResults = new HashMap<String, List<Pair>>();
		highestLevels = new HashMap<String, Integer>();
		possibleFusions = new Graph<String, String>();
		treasureFusions = new HashMap<String, HashMap<String, Integer>>();
				
		// parse the data in personaData.txt and fill arcanas, personas, and highestLevels
		Persona5ParserGraph.parsePersonas("src/data/personaData.txt", arcanas, personas, highestLevels, 
				fusions);
		
		// parse the data in possibleFusions.txt and fill in possibleFusions
		Persona5ParserGraph.parsePossibleFusions("src/data/possibleFusions.txt", possibleFusions);
		
		// parse the data in treasureFusions.txt and fill in treasureFusions
		Persona5ParserGraph.parseTreasureFusions("src/data/treasureFusions.txt", treasureFusions);
		
		// calculate and store all possible fusions between personas
		calculateFusions();
	}
	
    /**
     * @param p	The name of the persona for which the persona object will be returned
     * @return A persona object with the name p. Returns null if p is not a valid persona
     * 		   name.
     */
	public Persona getPersona(String p) {
		return personas.get(p);
	}
	
    /**
     * @return A set of Strings that represent the arcanas
     */
	public Set<String> getAllArcana() {
		return new HashSet<String>(arcanas.keySet());
	}
	
	/**
	 * @param arc The arcana for which the list of persona will be returned
     * @return A list of persona objects in the arcana arc
     */
	public List<Persona> getArcPersonas(String arc) {
		return new ArrayList<Persona>(arcanas.get(arc));
	}
	
	/**
	 * @param p The name of the persona for which a list of possible fusions will 
	 *        be returned
     * @return A list of pair objects, each consisting of two persona that fuse 
     *         together to produce the persona with the name p. Returns null if:
     *         - p is a guillotine-only persona
     *         - p is not a valid persona name
     */
	public List<Pair> getFusions(String p) {
		if (personas.containsKey(p)) {
			if (personas.get(p).getSpecialCase().equals("guillotine") ||
					personas.get(p).getSpecialCase().equals("treasure")) {
				return null;
			}
			return new ArrayList<Pair>(fusionResults.get(p));
		} else {
			return null;
		}
	}
	
	/**
	 * @param p The guillotine persona for which the list of ingredient personas will be 
	 * 		  returned
     * @return A list of personas that can fuse together to create the guillotine-only
     * 		   persona p. Returns null if:
     *         - p is not a valid persona name
     *         - p is not a guillotine-only persona
     */
	public List<String> getGuillotineFusion(String p) {
		if (personas.containsKey(p)) {
			Persona pers = personas.get(p);
			if (pers.getSpecialCase().equals("guillotine")) {
				return new ArrayList<String>(pers.getSpecialFusion());
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * @param p1 The first persona involved in the fusion
	 * @param p2 The second persona involved in the fusion
     * @return A persona object that is the result of the fusion between personas p1 and
     *         p2. Returns null if:
     *         - fusion between p1 and p2 is impossible
     *         - p1, p2, or both are not valid persona names
     */
	public Persona getFusionResult(String p1, String p2) {
		Set<String> temp = fusions.getEdges(p1, p2);
		if (temp != null) {
			Iterator<String> itr = temp.iterator();
			return personas.get(itr.next());
		} else {
			return null;
		}
	}
	
    /**
     * private helper method that calculates all the possible fusions and stores the results
     * in private fields for later access
     */
	private void calculateFusions() {
		List<String> arc = new ArrayList<String>(arcanas.keySet()); // list of all the arcanas
		
		// for the personas of each arcana i, calculate their fusions with each of the other
		// personas in each of the other arcanas. 
		for (int i = 0; i < arc.size(); i++) {
			List<Persona> arc1Personas = arcanas.get(arc.get(i));
			for (int j = i; j < arc.size(); j++) { // j is initialized to i to avoid repeating calculations
				List<Persona> arc2Personas = arcanas.get(arc.get(j));
				
				if (i == j) { // special case: calculate same-arcana fusions
					for (int k = 0; k < arc1Personas.size(); k++) {
						// l is initialized to k + 1 to avoid calculating fusions between two identical persona
						for (int l = k + 1; l < arc1Personas.size(); l++) {
							calculateSingleSAFusion(arc1Personas.get(k), arc2Personas.get(l), arc1Personas.get(k).getArcana());
						}
					}
				} else { // regular case: calculate different-arcana fusions
					Set<String> result = possibleFusions.getEdges(arc.get(i), arc.get(j));
					Iterator<String> itr = result.iterator();
					String resultingArcana = itr.next();
					
					for (int k = 0; k < arc1Personas.size(); k++) {
						for (int l = 0; l < arc2Personas.size(); l++) {
							calculateSingleDAFusion(arc1Personas.get(k), arc2Personas.get(l), resultingArcana);
						}
					}
				}
			}
		}
	}

    /**
     * private helper method that calculates a single fusion between p1 and p2 and
     * stores it in fusions. Handles the fusion cases of 1) regular fusion or treasure
     * demon x treasure demon, and 2) treasure demon x regular persona
     * 
     * @param p1 The first persona involved in the fusion
     * @param p2 The second persona involved in the fusion
     * @param resArc The resulting arcana that the fusion between p1 and p2 will produce
     * @modifies fusions
     * @effects adds an entry to fusions if the fusion between p1 and p2 is possible
     */
	private void calculateSingleDAFusion(Persona p1, Persona p2, String resArc) {
		if (p1.getSpecialCase().equals("treasure") && !p2.getSpecialCase().equals("treasure")) {
			calculateTreasureXNonTreasureFusion(p1, p2);
		} else if (p2.getSpecialCase().equals("treasure") && !p1.getSpecialCase().equals("treasure")) {
			calculateTreasureXNonTreasureFusion(p2, p1);
		} else { // fusion between two treasure demons or two regular/guillotine/dlc personas
			double calculatedLevel = (p1.getBaseLevel() + p2.getBaseLevel()) / 2.0 + 0.5;

			if (highestLevels.get(resArc) >= calculatedLevel) {
				// find the corresponding persona of the resulting arcana with the base level 
				// calculatedLevel or the persona with the next highest base level that is not a 
				// treasure demon, dlc persona or a guillotine fusion only persona
				List<Persona> pers = arcanas.get(resArc);
				boolean doneSearching = false;
				int i = pers.size() - 1;
				Persona finalPersona = null;
				while (!doneSearching) {
					if (i < 0) { 
						doneSearching = true;
					} else {
						Persona currPersona = pers.get(i);
									
						if (calculatedLevel == currPersona.getBaseLevel()) { // found an exact match
							// check to see that currPersona is not a treasure demon/guillotine persona
							if (!currPersona.getSpecialCase().equals("treasure") && 
									!currPersona.getSpecialCase().equals("guillotine")) {
								finalPersona = currPersona;
							}
							doneSearching = true;
						} else if (calculatedLevel > currPersona.getBaseLevel()) { // went over the limit: backtrack
							doneSearching = true;
						} else { // the base level of the current persona is still greater than calculatedLevel; keep searching down
							if (!currPersona.getSpecialCase().equals("treasure") && 
									!currPersona.getSpecialCase().equals("guillotine")) {
								finalPersona = currPersona;
							}
							i--;
						}
					}
				}
				
				if (finalPersona != null) { // the fusion is possible
					storeFusionResults(finalPersona.getName(), p1, p2);
				}
			}
		}
	}
	
    /**
     * private helper method that calculates a single fusion between a treasure demon and
     * a non-treasure persona.
     * 
     * @param treasureDemon The persona that is the treasure demon in the fusion
     * @param regularPersona The regular (non-treasure demon) persona in the fusion
     * @modifies fusions, fusionResults
     * @effects adds an entry to fusions and to fusionResults if the fusion between p1 
     * 		    and p2 is possible
     */
	private void calculateTreasureXNonTreasureFusion(Persona treasureDemon, Persona regularPersona) {
		String resArc = regularPersona.getArcana();
		
		// get the index of the regular persona and the number that represents how many
		// levels to go up/down
		int levelShift = treasureFusions.get(treasureDemon.getName()).get(resArc);
		int personaIndex = arcanas.get(resArc).indexOf(regularPersona);
		
		// if the resulting index is still in the range of the list of personas (and is
		// therefore a valid fusion), add it to fusions
		if (personaIndex + levelShift >= 0 && 
				personaIndex + levelShift < arcanas.get(resArc).size()) {
			Persona finalPersona = arcanas.get(resArc).get(personaIndex + levelShift);
			storeFusionResults(finalPersona.getName(), regularPersona, treasureDemon);
		}
	}
	
    /**
     * private helper method that calculates a single fusion between p1 and p2 and
     * stores it in fusions. Handles the special case of same-arcana fusions.
     * 
     * @param p1 The first persona involved in the fusion (of the same arcana as p2)
     * @param p2 The second persona involved in the fusion (of the same arcana as p1)
     * @param resArc The resulting arcana that the fusion between p1 and p2 will produce
     * @modifies fusions
     * @effects adds an entry to fusions if the fusion between p1 and p2 is possible
     */
	private void calculateSingleSAFusion(Persona p1, Persona p2, String resArc) {
		if (p1.getSpecialCase().equals("treasure") || p2.getSpecialCase().equals("treasure")) {
			calculateSingleDAFusion(p1, p2, resArc);
		} else {	
			double calculatedLevel = (p1.getBaseLevel() + p2.getBaseLevel()) / 2.0 + 0.5;
			
			// find the corresponding persona of the resulting arcana with the base level 
			// calculatedLevel or the persona with the next lowest base level that is not a 
			// treasure demon, dlc persona or a guillotine fusion only persona
			List<Persona> pers = arcanas.get(p1.getArcana());
			boolean doneSearching = false;
			int i = 0;
			Persona finalPersona = null;
			while (!doneSearching) {
				Persona currPersona = pers.get(i);
							
				if (calculatedLevel == currPersona.getBaseLevel()) { // found an exact match
					// treasure demons can't be fused and guillotine personas need more than two ingredient 
					// personas. Also, it's impossible for a fusion to result in one of the ingredient personas.
					if (!currPersona.getSpecialCase().equals("treasure") && 
							!currPersona.getSpecialCase().equals("guillotine") &&
							!currPersona.equals(p1) &&
							!currPersona.equals(p2)) {
						finalPersona = currPersona;
					}
					doneSearching = true;
				} else if (calculatedLevel < currPersona.getBaseLevel()) { // went over the limit: backtrack
					doneSearching = true;
				} else { // the base level of the current persona is still less than calculatedLevel; keep searching up
					if (currPersona.getSpecialCase().equals("") && !currPersona.equals(p1) && !currPersona.equals(p2)) {
						finalPersona = currPersona;
					}
					i++;
				}
			}
			
			if (finalPersona != null) { // the fusion is possible
				storeFusionResults(finalPersona.getName(), p1, p2);
			}
		}
	}
		
    /**
     * private helper method that updates fusionResults when a new fusion calculation
     * is made.
     * 
     * @param result The name of the resulting persona of the fusion between p1 and p2
     * @param p1 The first persona involved in the fusion (of the same arcana as p2)
     * @param p2 The second persona involved in the fusion (of the same arcana as p1)
     * @modifies fusionResults
     * @effects adds a new mapping from persona to list of pairs or updates the list of
     * 			pairs for previously existing keys
     */
	private void storeFusionResults(String result, Persona p1, Persona p2) {
		fusions.insertEdge(p1.getName(), p2.getName(), result);
		fusions.insertEdge(p2.getName(), p1.getName(), result);
		
		if (fusionResults.containsKey(result)) {
			fusionResults.get(result).add(new Pair(p1, p2));
		} else {
			List<Pair> temp = new ArrayList<Pair>();
			temp.add(new Pair(p1, p2));
			fusionResults.put(result, temp);
		}
	}
	
	// for testing purposes
	public void printParsedData() {
		System.out.println(highestLevels.toString());
	}
	
	public HashMap<String, Integer> getHighestLevels() {
		return highestLevels;
	}
}
