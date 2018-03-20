package assignment4;

public class RonCritter2 extends Critter {
	
	public RonCritter2() {
		// implement the default constructor
	}
	@Override
	public void doTimeStep() {
	}

	@Override
	public boolean fight(String opponent) {
		run(getRandomInt(8));
		return false;
	}

	@Override
	public String toString () {
		return "5";
	}
}
