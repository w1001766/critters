package assignment4;

public class WenCritter1 extends Critter {
	
	public WenCritter1() {
		// implement the default constructor
	}
	
	@Override
	public void doTimeStep() {
	}

	@Override
	public boolean fight(String opponent) {
		return true;
	}

	@Override
	public String toString () {
		return "W";
	}
}
