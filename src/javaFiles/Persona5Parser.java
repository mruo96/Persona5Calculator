package javaFiles;
import java.io.*;
import java.util.*;

/**
 * Parser utility to load the personaData.txt file
 */
public class Persona5Parser {
	/**
	 * A checked exception class for bad data files
	 */
	@SuppressWarnings("serial")
	public static class MalformedDataException extends Exception {
		public MalformedDataException() { }

		public MalformedDataException(String message) {
			super(message);
		}

		public MalformedDataException(Throwable cause) {
			super(cause);
		}

		public MalformedDataException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	/**
	 * Reads the personaData.txt file
	 * 
	 * Each line of the input is either an arcana or a persona. Each arcana
	 * is followed by a list of all the personas in that arcana. Each line
	 * representing a persona is in the following format, separated by tabs:
	 * level	name	strength 	magic	endurance	agility	luck	physical	gun	fire	ice	electric
	 * wind	psychic	nuclear	bless	curse
	 * If the persona is a treasure demon, there will be a "t" following "curse" separated by a tab.
	 * If the persona is a special persona only available through guillotine fusion, all the personas
	 * required to fuse it will be listed after "curse" also separated by tabs
	 * 
	 * @requires filename is a valid file path
	 * @param filename the file that will be read
	 * @param arcana A map from arcana names to a list of the personas in that arcana
	 * @param personas A map from persona names to the actual persona objects
	 * @param highestLevels A map from arcanas to the level of the highest-level persona in that arcana
	 * @modifies arcana, personas, highestLevels
	 * @effects fills arcana with a mappings from arcanas to a list of personas, fills personas with
	 *          mappings from persona names to the actual persona objects, fills highestLevels with 
	 *          mappings from arcanas to the level of the highest-level persona in that arcana
	 * @throws MalformedDataException if the file is not well-formed
	 */
	public static void parsePersonas(String filename, HashMap<String, List<Persona>> arcana, 
			HashMap<String, Persona> personas, HashMap<String, Integer> highestLevels) {

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));

			String currentArcana = "";
			String inputLine;
			int highLev = 0;
			while ((inputLine = reader.readLine()) != null) {

				// add the highest level to highestLevels for the previous arcana
				if (inputLine.equals("")) {
					int size = arcana.get(currentArcana).size();
					highestLevels.put(currentArcana, highLev);
					continue;
				}
				
				String[] tokens = inputLine.split("\t");

				if (tokens.length == 1) { // this is an arcana line
					// add the new arcana to arcana
					arcana.put(tokens[0], new ArrayList<Persona>());
					
					// set the current arcana
					currentArcana = tokens[0];
				} else {
					Persona pers;
					
					Integer level = Integer.parseInt(tokens[0]);
					String name = tokens[1];
					
					List<Integer> stats = new ArrayList<Integer>();
					for (int i = 0; i < 5; i++) {
						stats.add(Integer.parseInt(tokens[i + 2]));
					}
					
					List<String> wr = new ArrayList<String>();
					for (int i = 0; i < 10; i++) {
						wr.add(tokens[i + 7]);
					}
					
					// the persona is either a treasure demon, a dlc persona, a guillotine
					// fusion only persona, or a regular persona
					if (tokens.length > 17) {
						if (tokens[17].equals("y")) { // dlc persona
							pers = new Persona(name, currentArcana, level, stats, wr, "dlc", null);
						} else if (tokens[17].equals("t")) { // treasure demon
							pers = new Persona(name, currentArcana, level, stats, wr, "treasure", null);
						} else { // guillotine fusion
							List<String> special = new ArrayList<String>();
							for (int i = 17; i < tokens.length; i++) {
								special.add(tokens[i]);
							}
							
							pers = new Persona(name, currentArcana, level, stats, wr, "guillotine", special);
						}
					} else { // regular persona
						pers = new Persona(name, currentArcana, level, stats, wr, null, null);
						highLev = level;
					}
					
					arcana.get(currentArcana).add(pers);
					personas.put(name, pers);
				}
			}
		} catch (IOException e) {
			System.err.println(e.toString());
			e.printStackTrace(System.err);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println(e.toString());
					e.printStackTrace(System.err);
				}
			}
		}
	}
}
