package javaFiles;

/**
 * This class stores a pair of persona that represent the ingredient personas in a fusion.
 */
public class Pair {

	private Persona p1; // the first persona in the pair
	private Persona p2; // the second persona in the pair

	/**
	 * @effects Constructs a Pair object with the specified persona 1 p1 and persona 2 p2
	 */
	public Pair(Persona p1, Persona p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	/**
	 * @return The first persona included in this Pair
	 */
	public Persona getP1() {
		return p1;
	}

	/**
	 * @return The second persona included in this Pair
	 */
	public Persona getP2() {
		return p2;
	}

	/**
	 * @return A String representation of this Pair in the format:
	 *         name of persona 1, name of persona 2
	 */
	public String toString() {
		return (p1.getName() + ", " + p2.getName());
	}

	/**
	 * @return A unique hashcode for this Pair
	 */
	public int hashCode() {
		return p1.hashCode() + p2.hashCode();
	}

	/**
	 * Standard equality operation.
	 *
	 * @param o The object to be compared for equality.
	 * @return true if and only if o is an instance of Pair 
	 * 		   and p1 and p2 in both o and this have the same name
	 */ 
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pair)) {
			return false;
		}
		Pair p = (Pair) o;
		return this.p1.getName().equals(p.p1.getName()) &&
				this.p2.getName().equals(p.p2.getName());
	}
}
