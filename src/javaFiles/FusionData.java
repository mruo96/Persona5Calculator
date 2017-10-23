package javaFiles;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * All data structures:
 * - map from arcana names to list of persona objects ordered by level
 * - map from persona names to a set of fused persona pairs (unordered)
 * - master map from persona names to persona objects
 * - map from arcana name to the highest level persona in that arcana, used in 3)
 * - map from persona name to a set of a pair of persona objects, the 1st being the fusion partner and the
 *   2nd being the fused persona
 * 
 * 1) make a parser to parse data into: a map with arcana names as keys and a map as a value, 
 *    which has levels as its keys and persona objects as its values
 *    - a persona object contains: the name, base level, location, list of stats, list of weakness, and 
 *      whether it's dlc or not (boolean), whether it's a special triple fusion only persona
 * 2) need a list of which arcanas can't fuse with other arcana
 * 3) for each persona p1 in arcana a1: 
 * 		for each persona p2 in arcana a2 that a1 can fuse with:
 *      	do the fusion calculation between p1 and p2, look up the corresponding persona
 *          in the parsed data, then add the fused persona to its own map (key = persona name,
 *          value = set of pairs of the two personas that were fused, ordered by level)
 * 4) user options:
 *    - information about a specific persona given its name:
 *      get the data out of a master map of all persona
 *    - a list of all the arcana:
 *      key set of the parsed data
 *    - a list of all the personas in a specific arcana
 *      return a copy of the set storing the persona objects for the specified arcana
 *    - a list of all the persona pairs that will fuse into a specific persona
 *      look up in the map from persona name to set of pairs
 *    - the result of the fusion of two specific personas
 *      the same calculation as in step 3: look up the list in the arcana, start from the front 
 *      of the list and find the right persona
 *    - all the fusions a specific persona can be in:
 * 
 */
public class FusionData {
	
	// map from arcana names to a list of persona objects ordered by level
	private HashMap<String, List<Persona>> arcana;
	
	// map from persona names to a set of pairs that can be fused to make the persona
	private HashMap<String, HashSet<Pair>> fusedPairs;
	
	// map from persona names to the corresponding object
	private HashMap<String, Persona> personas;
	
	// map from each arcana name to the highest level persona in the arcana
	private HashMap<String, Integer> highestLevels;
	
	// map from persona name to a set of a pair of persona objects, the 1st of the pair being the 
	// persona to fuse with and the 2nd being the persona that is the result of the fusion
	private HashMap<String, HashSet<Pair>> possibleFusions;
	
	// graph with personas as nodes and the results of the fusion as edge
	
	public FusionData() {
		arcana = new HashMap<String, List<Persona>>();
		personas = new HashMap<String, Persona>();
		highestLevels = new HashMap<String, Integer>();
		
		Persona5Parser.parsePersonas("src/data/personaData.txt", arcana, personas, highestLevels);
		
		fusedPairs = new HashMap<String, HashSet<Pair>>();
		possibleFusions = new HashMap<String, HashSet<Pair>>();
	}

	private void calculateFusions() {
		
	}
	
	// for testing purposes
	public void printParsedData() {
		System.out.println(highestLevels.toString());
	}
}
