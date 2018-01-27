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
 * of characters from myths or deities from different religions. Each persona can be identified
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
		Scanner scan = new Scanner(System.in);

		System.out.println("Welcome to Mimi's Persona 5 Fusion Calculator!");

		// ask the user if they want to include dlc personas in the fusion calculations
		FusionDataGraph fd = askForDLC(scan);

		printMenu();

		// until the user quits the program, keep prompting for a commands and execute them if
		// the user inputs valid commands
		while (true) {
			System.out.print("\nPlease enter a command, or m for menu: ");

			String input = scan.nextLine().toLowerCase();
			if (input.equals("about")) { // print a short description of Persona 5, personas and fusion
				printDescription();
			} else if (input.equals("persona")) { // print information about a specific persona
				Persona persona = getValidPersonaName(scan, 
						"Enter the name of a persona (first letter capitalized): ", fd);

				if (persona != null) {
					printPersonaInfo(persona);
				}
			} else if (input.equals("arcanas")) { // print a list of the 20 arcana
				printArcana(fd);
			} else if (input.equals("personas")) { // print all the personas in a specified arcana
				String arcana = getValidArcanaName(scan, "Enter the name of an arcana (first letter of each word capitalized): ", fd);

				if (arcana != null) {
					printPersonasInArcana(arcana, fd);
				}
			} else if (input.equals("fusions to")) { // print a list of all the possible fusions to a specific persona
				Persona persona = getValidPersonaName(scan, 
						"Enter the name of a persona (first letter of each word capitalized): ", fd);

				if (persona != null) {
					if (persona.getSpecialCase().equals("guillotine")) {
						printGuillotineFusion(fd, persona.getName());
					} else {
						printFusionPairs(fd, persona.getName());
					}
				}
			} else if (input.equals("fusion result")) { // print the resulting persona of a fusion between two specific persona
				Persona persona1 = getValidPersonaName(scan, 
						"Enter the name of the first persona (first letter of each word capitalized): ", fd);

				Persona persona2 = getValidPersonaName(scan, 
						"Enter the name of the second persona (first letter of each word capitalized): ", fd);

				if (persona1 != null && persona2 != null) {
					printFusionResult(fd, persona1, persona2);
				}
			} else if (input.equals("m")) { // print the menu
				printMenu();
			} else if (input.equals("related fusions")) { // print the related fusions of a persona
				Persona persona = getValidPersonaName(scan, 
						"Enter the name of a persona (first letter capitalized): ", fd);

				if (persona != null) {
					printRelatedFusions(persona, fd);
				}
			} else if (input.equals("key")) { // print the abbreviation key
				printAbbreviationKey();
			} else if (input.equals("quit")) { // quit the program
				scan.close();
				return;
			} else { // invalid input
				System.out.println("Invalid command");
			}
		}
	}
	
	/**
	 * returns the valid name of an persona based on user input. If the user decides to quit,
	 * returns an empty string.
	 * 
	 * @param scan The Scanner from which input will be read
	 * @param userPrompt The message that prompts the user to input a persona name
	 * @param fd The FusionGraphData for this particular run of the program
	 * @return a valid persona name if the user provides one, or "" if the user decides to quit back
	 *         to the menu to choose another option
	 */
	private static Persona getValidPersonaName(Scanner scan, String userPrompt, FusionDataGraph fd) {
		System.out.println(userPrompt);

		boolean invalidName = true;
		while (invalidName) {
			String input = scan.nextLine();
			Persona p = fd.getPersona(input);

			if (input.equals("b")) {
				invalidName = false;
			} else if (p == null) {
				System.out.println("Invalid persona name. Enter another name or b to go back to main menu: ");
			} else { // valid name; print out the fusions p is an ingredient in
				return p;
			}
		}

		return null;
	}

	/**
	 * returns the valid name of an arcana based on user input. If the user decides to quit,
	 * returns an empty string.
	 * 
	 * @param scan The Scanner from which input will be read
	 * @param userPrompt The prompt to the user to input an arcana name
	 * @param fd The FusionGraphData for this particular run of the program
	 * @return a valid arcana name if the user provides one, or "" if the user decides to quit back
	 *         to the menu to choose another option
	 */
	private static String getValidArcanaName(Scanner scan, String userPrompt, FusionDataGraph fd) {
		Set<String> arcana = fd.getAllArcana();

		System.out.print(userPrompt);

		boolean invalidName = true;
		while (invalidName) {
			String input = scan.nextLine();

			if (input.equals("b")) {
				invalidName = false;
			} else if (!arcana.contains(input)) {
				System.out.print("Invalid arcana name. Enter another name or b to go back to main menu: ");
			} else { // valid name; print out the information of each persona in the user-specified arcana
				return input;
			}
		}

		return "";
	}

	/**
	 * configures the program according to whether the user wants to include dlc persona
	 * in the fusion calculations or not
	 * 
	 * @param scan The Scanner from which input will be read
	 * @param fd The FusionGraphData for this particular run of the program
	 * @return a FusionDataGraph that has been configured to either use dlc persona
	 *         in the fusion calculations or not use dlc persona
	 */
	private static FusionDataGraph askForDLC(Scanner scan) {
		FusionDataGraph fd = null;
		boolean invalidInput = true;
		while (invalidInput) {
			System.out.print("Include DLC personas? Answer y or n: ");

			String input = scan.nextLine();
			if (input.equals("y")) {
				fd = new FusionDataGraph(true);
				invalidInput = false;
			} else if (input.equals("n")) {
				fd = new FusionDataGraph(false);
				invalidInput = false;
			} else {
				System.out.println("Invalid answer.\n");
			}
		}

		return fd;
	}

	/**
	 * prints the information associated with the persona p in the format:
	 * Name
	 * Arcana:
	 * Base Level:
	 * Stats: strength, magic, endurance, agility, luck
	 * Weaknesses/Resistances: physical, gun, fire, ice, electricity, wind, psychic, nuclear, bless, curse
	 * Regular, dlc, or guillotine persona
	 * 
	 * @param p The persona for which information will be printed
	 */
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
			System.out.println("regular persona");
		} else {
			System.out.println(p.getSpecialCase() + " persona");
		}
	}

	/**
	 * prints out a list of all the arcanas in the game, separated by commas
	 * 
	 * @param fd The FusionDataGraph from which data will be drawn
	 */
	private static void printArcana(FusionDataGraph fd) {
		Set<String> arcana = fd.getAllArcana();
		Iterator<String> itr = arcana.iterator();
		System.out.print("Arcana: " + itr.next());
		while (itr.hasNext()) {
			String arc = itr.next();
			System.out.print(", " + arc);
		}
		System.out.println();
	}

	/**
	 * prints the ingredient personas of a guillotine fusion for the persona input
	 * 
	 * @param fd The FusionDataGraph from which data will be drawn
	 * @param personaName The resulting persona for which the guillotine fusion will be printed
	 */
	private static void printGuillotineFusion(FusionDataGraph fd, String personaName) {
		List<String> ingredientPersonas = fd.getGuillotineFusion(personaName);

		System.out.print("Guillotine Fusion for " + personaName + ": " + ingredientPersonas.get(0));
		for (int i = 1; i < ingredientPersonas.size(); i++) {
			System.out.print(", " + ingredientPersonas.get(i));
		}
	}

	/**
	 * prints the pairs of persona (p1, p2) that fuse together to make input in the format:
	 * Name of p1 (base level of p1 / arcana of p1) x Name of p2 (base level of p2 / arcana of p2)
	 * 
	 * @param fd The FusionDataGraph from which data will be drawn
	 * @param personaName The persona for which the fusions pairs will be printed
	 */
	private static void printFusionPairs(FusionDataGraph fd, String personaName) {
		List<Pair> fusions = fd.getFusions(personaName);
		if (fusions == null) { // the user-specified persona is a treasure demon; no possible fusions
			System.out.println("Treasure demons cannot be fused.");
		} else {
			System.out.println(fusions.size() + " fusions for " + personaName + ":");
			for (int i = 0; i < fusions.size(); i++) {
				Persona p1 = fusions.get(i).getP1();
				Persona p2 = fusions.get(i).getP2();
				
				printPersonaInPairFormat(p1, p2);
				System.out.println();
			}
		}
	}

	/**
	 * prints the result of a fusion between p1 and p2 in the format:
	 * Name of p1 (base level of p1 / arcana of p1) x Name of p2 (base level of p2 / arcana of p2) = Name of resulting persona
	 * 
	 * @param fd The FusionDataGraph from which data will be drawn
	 * @param p1 The first ingredient persona
	 * @param p2 The second ingredient persona
	 */
	private static void printFusionResult(FusionDataGraph fd, Persona p1, Persona p2) {
		Persona result = fd.getFusionResult(p1.getName(), p2.getName());

		if (result == null) { // fusion is impossible between p1 and p2
			System.out.println("Fusion is impossible between " + p1.getName() + " and " + p2.getName());
		} else {
			printPersonaInPairFormat(p1, p2);
			System.out.println(" = " + result.getName());
		}
	}

	/**
	 * prints a fusion between p1 and p2 in the following format:
	 * Name of p1 (base level of p1 / arcana of p1) x Name of p2 (base level of p2 / arcana of p2)
	 * 
	 * @param p1 The first ingredient persona
	 * @param p2 The second ingredient persona
	 */
	private static void printPersonaInPairFormat(Persona p1, Persona p2) {
		System.out.print(p1.getName() + " (" + p1.getBaseLevel() + " / "+ p1.getArcana() + ")" + " x " + 
				p2.getName() + " (" + p2.getBaseLevel() + " / "+ p2.getArcana() + ")");
	}
	
	/**
	 * prints all the personas in the specified arcana
	 * 
	 * @param arcana The arcana for which all the personas will be printed
	 * @param fd The FusionGraphData for this particular run of the program
	 */
	private static void printPersonasInArcana(String arcana, FusionDataGraph fd) {
		List<Persona> personas = fd.getArcPersonas(arcana);
		System.out.println("There are " + personas.size() + " personas in the " + arcana + " arcana:");
		for (int i = 0; i < personas.size(); i++) {
			printPersonaInfo(personas.get(i));
			if (i != personas.size() - 1) {
				System.out.println();
			}
		}
	}

	/**
	 * prints all the fusions that include the specified persona as one of the ingredient
	 * personas
	 * 
	 * @param persona The persona for which the related fusions will be printed
	 * @param fd The FusionGraphData for this particular run of the program
	 */
	private static void printRelatedFusions(Persona persona, FusionDataGraph fd) {
		System.out.println("Fusions " + persona.getName() + " is an ingredient persona in:");

		Set<String> ingredients = fd.getIncludedFusions(persona.getName());
		Iterator<String> itr = ingredients.iterator();
		while (itr.hasNext()) {
			String s = itr.next();
			printFusionResult(fd, persona, fd.getPersona(s));
		}
	}
	
	/**
	 * prints the menu
	 */
	private static void printMenu() {
		System.out.println("Menu Commands:");
		System.out.println("about: Information about Persona 5, personas and fusion");
		System.out.println("persona: Information about a specific persona");
		System.out.println("arcanas: A list of the 20 arcana");
		System.out.println("personas: A list of all the persona in a specific arcana");
		System.out.println("fusions to: A list of all the possible fusions to a specific persona");
		System.out.println("fusion result: The resulting persona of a fusion between two specific persona");
		System.out.println("related fusions: A list of all the fusions a specific persona is an ingredient of");
		System.out.println("key: Abbreviation key");
		System.out.println("quit: Quit the program");
	}

	/**
	 * prints a basic description of Persona 5, personas and fusion
	 */
	private static void printDescription() {
		System.out.println("About Persona 5 and Persona Fusion:");
		System.out.println("Persona 5 was developed by Atlus and released in September 2016 for the Playstation 4 and");
		System.out.println("Playstation 3. In Persona 5, the player collects different “persona,” which are based off");
		System.out.println("of characters from myths or deities from different religions. Each persona can be identified");
		System.out.println("by its name, its base level, and its arcana. There are 20 different arcanas");
		System.out.println("(ex. Sun, Fool, Emperor, Chariot) and around 10 personas belong to each arcana.");
		System.out.println("Players can fuse together different personas to create a new one. Generally, the results of");
		System.out.println("these fusions are determined by rules that differ based on the arcana and base levels of the");
		System.out.println("ingredient personas. However, some special personas, called “treasure demons,” follow different");
		System.out.println("rules when fused together with different personas. Also, there are usually multiple ways to");
		System.out.println("fuse a specific persona, from two to more than a hundred.\n");
		System.out.println("Even though it is simple to calculate the result of a fusion between two specific personas,");
		System.out.println("knowing all the possible pairs of personas that fuse into a specific persona is difficult");
		System.out.println("without calculating the possible fusions between all personas. This is where the calculator");
		System.out.println("may be useful: if a player wishes to fuse a specific persona p but does not know which pairs");
		System.out.println("of personas will fuse together to create p.");
	}

	/**
	 * print the abbreviation key used when displaying persona information
	 */
	private static void printAbbreviationKey() {
		System.out.println("Abbreviation Key:");
		System.out.println("phys = physical");
		System.out.println("elec = electricity");
		System.out.println("psych = psychic");
		System.out.println("nucl = nuclear");
		System.out.println("- = no weakness or resistance (neutral)");
		System.out.println("wk = weak");
		System.out.println("rs = resist");
		System.out.println("nu = null");
		System.out.println("ab = absorb");
		System.out.println("rp = repel");
	}
}