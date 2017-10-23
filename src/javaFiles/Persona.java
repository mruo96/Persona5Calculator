package javaFiles;
import java.util.List;

public class Persona implements Comparable<Persona> {
	
	private String name;
	private String arcana;
	private final int baseLevel;
	
	// the base stats of the persona, in the order: strength, magic, endurance, agility, luck
	private final List<Integer> stats;
	
	// the weaknesses/resistance of the persona, in the order: physical, gun, fire, ice, electric, 
	// wind, psychic, nuclear, bless, curse
	// Abbreviation key:
	// nu = null
	// rp = repel
	// wk = weak
	// rs = resist
	// ab = absorb
	private final List<String> wr;
	
	// either null (regular persona), "treasure" (treasure demon), "dlc" (dlc persona) or
	// "guillotine" (guillotine fusion only persona)
	private final String specialCase;
	
	// a list of the personas that fuse together to make this persona if this persona can only
	// be made via guillotine fusion
	private final List<String> specialFusion;
	
    /**
     * @effects Constructs a Persona object with the specified name, arcana, base level,
     * 	        stats, weaknesses/resistances, status (regular persona, treasure demon, dlc 
     * 			persona) and list of ingredient personas if applicable
     */
	public Persona(String name, String arcana, int baseLevel, List<Integer> stats, List<String> wr,
			String specialCase, List<String> specialFusion) {
		this.name = name;
		this.arcana = arcana;
		this.baseLevel = baseLevel;
		this.stats = stats;
		this.wr = wr;
		this.specialCase = specialCase;
		this.specialFusion = specialFusion;
	}
	
    /**
     * @returns a String that represents the name of this persona
     */
	public String getName() {
		return name;
	}
	
    /**
     * @returns a String that represents the arcana of this persona
     */
	public String getArcana() {
		return arcana;
	}
	
    /**
     * @returns an int that is the base level of this persona
     */
	public int getBaseLevel() {
		return baseLevel;
	}
	
    /**
     * @returns a list of Integers representing the base stats of this persona
     */
	public List<Integer> getStats() {
		return stats;
	}
	
    /**
     * @returns a list of Strings representing the weaknesses/resistance of this persona
     */
	public List<String> getWR() {
		return wr;
	}
	
    /**
     * @returns a String indicating whether or not this persona is a special persona (dlc
     * 			persona, treasure demon, or guillotine fusion only persona)
     */
	public String getSpecialCase() {
		return specialCase;
	}
	
    /**
     * @returns a list of Strings representing the personas that fuse together to make
     * 	        this persona. If this persona is not a guillotine-only persona, returns null
     */
	public List<String> getSpecialFusion() {
		return specialFusion;
	}
	
    /**
     * Standard compareTo method
     * 
     * @param other The Persona object to compare this persona to
     * @returns 1 if the base level of this persona is greater than the base level of the other
     * 			persona
     *          0 if the base level of this persona is the same as the base level of the other
     *          persona
     *          -1 if the base level of this persona is less than the base level of the other
     *          persona
     */
	public int compareTo(Persona other) {
		if (this.baseLevel > other.baseLevel) {
			return 1;
		} else if (this.baseLevel < other.baseLevel) {
			return -1;
		} else {
			return 0;
		}
	}
	
    /**
     * @returns a unique hashcode for this persona
     */
	public int hashCode() {
		return name.hashCode();
	}
	
    /**
     * @returns a String representation of this persona in the form of its name
     */
	public String toString() {
		return name;
	}
}
