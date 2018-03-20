package assignment4;

public class RonCritter1 extends Critter {

	public RonCritter1() {
		// implement the default constructor
	}
	@Override
	public void doTimeStep() {
		walk(0);
	}

	@Override
	public boolean fight(String opponent) {
		if (getEnergy() > 10) return true;
		return false;
	}
	
	public String toString() {
		return "R";
	}
	
}
