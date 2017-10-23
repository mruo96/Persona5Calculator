package javaFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Tester {
	
	public static void main(String[] args) {
		String a = "abcba";
		String b = "aabcaa";
		String c = "aaa";
		String d = "aaaa";
		String e = "aa";
		String f = "abccba";
		String g = "";
		System.out.println(a + " " + isPalindrome(a));
		System.out.println(b + " " + isPalindrome(b));
		System.out.println(c + " " + isPalindrome(c));
		System.out.println(d + " " + isPalindrome(d));
		System.out.println(e + " " + isPalindrome(e));
		System.out.println(f + " " + isPalindrome(f));
		System.out.println(g + " " + isPalindrome(g));

		
		/*
		FusionDataGraph fd = new FusionDataGraph();
		HashMap<String, HashMap<String, Integer>> tf = fd.treasureFusions;
		HashMap<String, List<Persona>> arcanas = fd.arcanas;
		HashMap<String, HashMap<String, Integer>> treasureFusions = fd.treasureFusions;
		Graph<String, String> fusions = fd.fusions;
		HashMap<String, List<Pair>> fusionResults = fd.fusionResults;


		Set<String> treasures = treasureFusions.keySet();
		Iterator<String> it = treasures.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
		
		Persona regularPersona = fd.getPersona("Lachesis");
		Persona treasureDemon = fd.getPersona("Stone of Scone");
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
			fusions.insertEdge(treasureDemon.getName(), regularPersona.getName(), finalPersona.getName());
			fusions.insertEdge(regularPersona.getName(), treasureDemon.getName(), finalPersona.getName());
			System.out.println(finalPersona.getName());
			
			//updateFusionResults(finalPersona.getName(), regularPersona, treasureDemon);
			if (fusionResults.containsKey(finalPersona.getName())) {
				fusionResults.get(finalPersona.getName()).add(new Pair(regularPersona, treasureDemon));
			} else {
				List<Pair> temp = new ArrayList<Pair>();
				temp.add(new Pair(regularPersona, treasureDemon));
				fusionResults.put(finalPersona.getName(), temp);
			}
		}
		
		System.out.println(fd.troubleshooting);
		
		
		Persona scone = fd.getPersona("Stone of Scone");
		if (scone.getSpecialCase().equals("treasure")) {
			System.out.println("yes it worked");
		}
		
		Set<String> keyset = tf.keySet();
		Iterator<String> itr = keyset.iterator();
		System.out.println(keyset);
		String s = itr.next();
		Set<String> ks2 = tf.get(s).keySet();
		itr = ks2.iterator();
		while (itr.hasNext()) {
			String str = itr.next();
			System.out.println(str + " " + tf.get(s).get(str));
		}
		
		System.out.print(Integer.parseInt("-4"));

		
		Set<String> keyset = hm.keySet();
		System.out.println(keyset.toString());
		
		Iterator<String> itr = keyset.iterator();
		
		for (int i = 0; i < keyset.size(); i++) {
			String arc = itr.next();
			if (hm.get(arc) != null) {
				System.out.println("not null for arcana: " + arc);
			}
		}*/
		/*
		List<Pair> pairs = fd.getFusions("Sandman");
		for (int i = 0; i < pairs.size(); i++) {
			Pair temp = pairs.get(i);
			System.out.println(temp.toString());
		}
		System.out.println(pairs.size());
		/*
		List<String> temp = fd.getGuillotineFusion("Satanael");
		for (int i = 0; i < temp.size(); i++) {
			System.out.println(temp.get(i));
		}*/
	}
	// abcba
	public static boolean isPalindrome(String s) {
		if (s.equals("")) {
			return false;
		} else if (s.length() == 1) {
			return true;
		} else {
			for (int i = 0; i < s.length(); i++) {
				if (s.charAt(i) != s.charAt(s.length() - 1 - i)) {
					return false;
				}
			}
			return true;
		}
	}

}
