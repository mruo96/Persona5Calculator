package javaFiles;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * This program is a calculator for the fusion of personas in the game Persona 5. 
 * 
 * About Persona 5 and Persona Fusion:
 * Persona 5 was developed by Atlus and released in September 2016 for the Playstation 4 and
 * Playstation 3. In Persona 5, the player collects different “persona,” which are based off
 * of characters from myths or deities from numerous religions. Each persona can be identified
 * by its name, its base level, and its arcana. There are 20 different arcanas 
 * (ex. Sun, Fool, Emperor, Chariot) and around 10 personas belong to each arcana. 
 * 
 * Players can fuse together different personas to create a new one. Generally, the results of
 * these fusions are determined by rules that differ based on the arcana and base levels of the
 * ingredient personas. However, some special personas, called “treasure demons,” follow different
 * rules when fused together with different personas. Also, there are usually multiple ways to
 * fuse a specific persona, from two to more than a hundred.
 * 
 * Even though it is simple to calculate the result of a fusion between two specific personas,
 * knowing all the possible pairs of personas that fuse into a specific persona is difficult
 * without calculating the possible fusions between all personas. This is where the calculator
 * may be useful: if a player wishes to fuse a specific persona p but does not know which pairs
 * of personas will fuse together to create p.
 * 
 * Calculator Functions:
 * 1. Provide information about a specific persona given its name.
 * 2. List the personas in a specific arcana given the name of the arcana.
 * 3. List all the arcana in the game.
 * 4. List all the possible fusions for a specific persona given its name.
 * 5. Provide the resulting persona of a fusion between two specific personas, given their names.
 */
public class FusionCalculator {

	public static void main(String[] args) {
		FusionDataGraph fd = new FusionDataGraph();
		
		printMenu();

		Scanner scan = new Scanner(System.in);
		while (true) {
			System.out.print("Please enter i, a, p, f, r, k, q, or m for menu: ");
			
			String input = scan.nextLine();
			if (input.equals("i")) {
				System.out.print("Enter the name of a persona (first letter capitalized): ");
				
				boolean invalidName = true;
				while (invalidName) {
					input = scan.nextLine();
					Persona p = fd.getPersona(input);
					
					if (input.equals("b")) {
						invalidName = false;
					} else if (p == null) {
						System.out.println("Invalid persona name. Enter another name or b to go back to main menu: ");
					} else { // valid name; print out the info of the user-specified persona
						printPersonaInfo(p);
						
						invalidName = false;
					}
				}
			} else if (input.equals("a")) {
				printArcana(fd);
			} else if (input.equals("p")) {
				Set<String> arcana = fd.getAllArcana();
				
				System.out.print("Enter the name of an arcana (first letter capitalized): ");

				boolean invalidName = true;
				while (invalidName) {
					input = scan.nextLine();
					
					if (input.equals("b")) {
						invalidName = false;
					} else if (!arcana.contains(input)) {
						System.out.print("Invalid arcana name. Enter another name or b to go back to main menu: ");
					} else { // valid name; print out the information of each persona in the user-specified arcana
						List<Persona> personas = fd.getArcPersonas(input);
						System.out.println("There are " + personas.size() + " personas in the " + input + " arcana:");
						for (int i = 0; i < personas.size(); i++) {
							printPersonaInfo(personas.get(i));
						}
						
						invalidName = false;
					}
				}
			} else if (input.equals("f")) {
				System.out.print("Enter the name of a persona (first letter capitalized): ");
				
				boolean invalidName = true;
				while (invalidName) {
					input = scan.nextLine();
					Persona p = fd.getPersona(input);
					
					if (input.equals("b")) {
						invalidName = false;
					} else if (p == null) {
						System.out.println("Invalid persona name. Enter another name or b to go back to main menu: ");
					} else if (p.getSpecialCase().equals("guillotine")) {
						printGuillotineFusion(fd, input);
						
						invalidName = false;
					} else { // valid name; print out the pairs that fuse into the user-specified persona
						printFusionPairs(fd, input);
						
						invalidName = false;
					}
				}
			} else if (input.equals("r")) {
				System.out.print("Enter the name of the first persona (first letter capitalized): ");
				
				boolean invalidName = true;
				while (invalidName) {
					input = scan.nextLine();
					Persona p1 = fd.getPersona(input);
					
					if (input.equals("b")) {
						invalidName = false;
					} else {
						System.out.print("Enter the name of the second persona (first letter capitalized): ");
						input = scan.nextLine();
						Persona p2 = fd.getPersona(input);
						
						if (p1 == null || p2 == null) {
							System.out.print("Invalid persona name(s). Enter the name of the first persona or b to go back to main menu: ");
						} else { // valid name; print out the info of the user-specified persona
							printFusionResult(fd, p1, p2);
							
							invalidName = false;
						}
					}
				}
			} else if (input.equals("m")) {
				printMenu();
			} else if (input.equals("k")) { 
				printAbbreviationKey();
			} else if (input.equals("q")) {
				scan.close();
				return;
			} else { // invalid input
				System.out.println("Invalid command :(\n");
			}
		}
	}
	
	private static void printPersonaInfo(Persona p) {
		System.out.println(p.getName());
		System.out.println("Arcana: " + p.getArcana());
		System.out.println("Base Level: " + p.getBaseLevel());
		
		List<Integer> stats = p.getStats();
		System.out.print("Stats: " + stats.get(0));
		for (int i = 1; i < stats.size(); i++) {
			System.out.print(", " + stats.get(i));
		}
		System.out.println(" (strength, magic, endurance, agility, luck)");
		
		List<String> wr = p.getWR();
		System.out.println("Weaknesses/Resistances: ");
		System.out.println("phys\tgun\tfire\tice\telec\twind\tpsych\tnucl\tbless\tcurse");
		for (int i = 0; i < wr.size(); i++) {
			System.out.print(wr.get(i) + "\t");
		}
		System.out.println();
		
		if (p.getSpecialCase().equals("")) {
			System.out.println("regular persona\n");
		} else {
			System.out.println(p.getSpecialCase() + " persona\n");
		}
	}
	
	private static void printArcana(FusionDataGraph fd) {
		Set<String> arcana = fd.getAllArcana();
		Iterator<String> itr = arcana.iterator();
		System.out.print("Arcana: " + itr.next());
		while (itr.hasNext()) {
			String arc = itr.next();
			System.out.print(", " + arc);
		}
		System.out.println("\n");
	}
	
	private static void printGuillotineFusion(FusionDataGraph fd, String input) {
		List<String> ingredientPersonas = fd.getGuillotineFusion(input);
		
		System.out.print("Guillotine Fusion for " + input + ": " + ingredientPersonas.get(0));
		for (int i = 1; i < ingredientPersonas.size(); i++) {
			System.out.print(", " + ingredientPersonas.get(i));
		}
		System.out.println("\n");
	}
	
	private static void printFusionPairs(FusionDataGraph fd, String input) {
		List<Pair> fusions = fd.getFusions(input);
		if (fusions == null) { // the user-specified persona is a treasure demon; no possible fusions
			System.out.println("Treasure demons cannot be fused.\n");
		} else {
			System.out.println(fusions.size() + " fusions for " + input + ":");
			for (int i = 0; i < fusions.size(); i++) {
				System.out.println(fusions.get(i).getP1() + ", " + fusions.get(i).getP2());
			}
			System.out.println();
		}
	}
	
	private static void printFusionResult(FusionDataGraph fd, Persona p1, Persona p2) {
		Persona result = fd.getFusionResult(p1.getName(), p2.getName());
		
		if (result == null) { // fusion is impossible between p1 and p2
			System.out.println("Fusion is impossible between " + p1.getName() + " and " + p2.getName() + "\n");
		} else {
			System.out.println(p1.getName() + " x " + p2.getName() + " = " + result.getName() + "\n");
		}
	}
	
	private static void printMenu() {
		System.out.println("Menu Options:");
		System.out.println("i: Information about a specific persona");
		System.out.println("a: A list of the 20 arcana");
		System.out.println("p: A list of all the persona in a specific arcana");
		System.out.println("f: A list of all the possible fusions to a specific persona");
		System.out.println("r: The resulting persona of a fusion between two specific persona");
		System.out.println("k: Abbreviation key");
		System.out.println("q: Quit the program\n");
	}
	
	private static void printAbbreviationKey() {
		System.out.println("Abbreviation Key:");
		System.out.println("phys = physical");
		System.out.println("elec = electricity");
		System.out.println("psych = psychic");
		System.out.println("nucl = nuclear");
		System.out.println("- = no weakness or resistance");
		System.out.println("wk = weak");
		System.out.println("rs = resist");
		System.out.println("nu = null");
		System.out.println("ab = absorb");
		System.out.println("rp = repel\n");
	}

}