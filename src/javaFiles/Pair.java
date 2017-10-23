package javaFiles;

public class Pair {
	
	private Persona p1;
	private Persona p2;
	
	public Pair(Persona p1, Persona p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Persona getP1() {
		return p1;
	}
	
	public Persona getP2() {
		return p2;
	}
	
	public String toString() {
		return (p1.getName() + ", " + p2.getName());
	}
	
	public int hashCode() {
		return p1.hashCode() + p2.hashCode();
	}
}
