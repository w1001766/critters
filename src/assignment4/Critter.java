package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Ronghao Zhang
 * rz4453
 * 15505
 * Wenxuan Wu
 * ww6726
 * 15505
 * Slip days used: <0>
 * Spring 2018
 */

import java.lang.reflect.Constructor;
import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	

	/**
	 * Create the 2D torus of the game map (wrap to the other side at the edge)
	 * @param distanceToTravel
	 * @return correct x coordinate to go
	 */
	private final int travelX(int distanceToTravel) {
		if(x_coord + distanceToTravel > Params.world_width - 1) {
			return Params.world_width - 1;
		}
		else if(x_coord + distanceToTravel < 0) {
			return 1;
		}
		else {
			return x_coord + distanceToTravel;
		}
	}
	
	/**
	 * Create the 2D torus of the game map (wrap to the other side at the edge)
	 * @param distanceToTravel
	 * @return correct y coordinate to go
	 */
	private final int travelY(int distanceToTravel) {
		if(this.y_coord + distanceToTravel > Params.world_height - 1) {
			return Params.world_height - 1;
		}
		else if(this.y_coord+distanceToTravel < 0) {
			return 1;
		}
		else {
			return this.y_coord + distanceToTravel;
		}	
	}
	
	
	protected final void walk(int direction) {
		switch (direction) {
		case 0: 
			x_coord = travelX(1);
			break;	// if this case is true, then don't check the rest of the cases
		case 1:
			x_coord = travelX(1);
			y_coord = travelY(-1);
			break;
		case 2:
			y_coord = travelY(-1);
			break;
		case 3:
			x_coord = travelX(-1);
			y_coord = travelY(-1);
			break;
		case 4:
			x_coord = travelX(-1);
			break;
		case 5:
			x_coord = travelX(-1);
			y_coord = travelY(1);
			break;
		case 6:
			y_coord = travelY(1);
			break;
		default:
			x_coord = travelX(1);
			y_coord = travelY(1);
			break;
		}
	}
	
	protected final void run(int direction) {
		switch (direction) {
		case 0: 
			x_coord = travelX(2);
			break;	// if this case is true, then don't check the rest of the cases
		case 1:
			x_coord = travelX(2);
			y_coord = travelY(-2);
			break;
		case 2:
			y_coord = travelY(-2);
			break;
		case 3:
			x_coord = travelX(-2);
			y_coord = travelY(-2);
			break;
		case 4:
			x_coord = travelX(-2);
			break;
		case 5:
			x_coord = travelX(-2);
			y_coord = travelY(2);
			break;
		case 6:
			y_coord = travelY(2);
			break;
		default:
			x_coord = travelX(2);
			y_coord = travelY(2);
			break;
		}
	}
	
	/** The reproduce method will initialize the offspring critter
	 * The offspring has been created in the doTimeStep function and is passed as a parameter
	 * The offspring has 1/2 of their parent's energy (round factors down)
	 * Reduce the parent energy to 1/2 (round factors up)
	 * @param offspring
	 * @param direction
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if(this.energy<Params.min_reproduce_energy) {	// only reproduce if parent has enough energy
			return;
		}
		offspring.energy = (this.energy)/2;	// round down
		this.energy = (int) Math.ceil((this.energy)/2);	// round up
		offspring.x_coord = this.x_coord;	// initialize the offspring position
		offspring.y_coord = this.y_coord;
		switch (direction) {					// move the offspring
		case 0: 
			offspring.x_coord = offspring.travelX(1);
			break;
		case 1:
			offspring.x_coord = offspring.travelX(1);
			offspring.y_coord = offspring.travelY(-1);
			break;
		case 2:
			offspring.y_coord = offspring.travelY(-1);
			break;
		case 3:
			offspring.x_coord = offspring.travelX(-1);
			offspring.y_coord = offspring.travelY(-1);
			break;
		case 4:
			offspring.x_coord = offspring.travelX(-1);
			break;
		case 5:
			offspring.x_coord = offspring.travelX(-1);
			offspring.y_coord = offspring.travelY(1);
			break;
		case 6:
			offspring.y_coord = offspring.travelY(1);
			break;
		default:
			offspring.x_coord = offspring.travelX(1);
			offspring.y_coord = offspring.travelY(1);
			break;
		}
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try {
			Class<?> c = Class.forName(critter_class_name);
			Constructor<?> cons = c.getConstructor();
			// create a critter based on the given critter_class_name
			Critter cr = (Critter) cons.newInstance();
			// initialize the position and energy for the critters
			cr.x_coord = Critter.getRandomInt(Params.world_width);
			cr.y_coord = Critter.getRandomInt(Params.world_height);
			cr.energy = Params.start_energy;
			population.add(cr);
		}
		catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		catch (IllegalAccessException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		catch (InstantiationException e) {
			throw new InvalidCritterException(critter_class_name);
		} 
		catch (IllegalArgumentException e) {
			throw new InvalidCritterException(critter_class_name);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		Class<?> c = null;
		try {
			c = Class.forName(critter_class_name);
		} 
		catch(ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		catch (IllegalArgumentException e) {
			throw new InvalidCritterException(critter_class_name);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		List<Critter> result = new java.util.ArrayList<Critter>();
		for(Critter crit : population) {
			if(c.isInstance(crit)) {
				result.add(crit);
			}
		}
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		// implement this method
		population.clear();
	}
	
	/**
	 * This method worldTimeStep will proceed one time-step
	 * Update the position of all the critters
	 * If two or more critters occupy the same location, then make them fight
	 * After the conflicts are resolved, critters will reproduce  
	 * Remove all the critters with energy <= zero
	 * New Algae are added to the world map 
	 */
	public static void worldTimeStep() {
		for(Critter c: population) {		// invoke doTimeStep method on every living critter in the critter collection
			c.doTimeStep();
		}
		for(Critter i: population) {
			for(Critter j:population) {
				if(!i.equals(j)) {
					//encounter
					if(!babies.contains(i) && !babies.contains(j)) {//new born babies cannot fight
						if(i.x_coord == j.x_coord && i.y_coord == j.y_coord) {
							if(i.isAlive()==true && j.isAlive() ==true) {
								int iPower = 0; int jPower =0;		//the amount of energy each critter will use to battle (random)
								if(i.fight(j.toString())==true) {	//if i wants to fight
									iPower = getRandomInt(i.energy);
								}
								if(j.fight(i.toString())==true) {	// if j wants to fight as well
									jPower = getRandomInt(j.energy);
								}
								
								if(iPower>jPower) {
									i.energy += 1/2 *(j.energy); 
									j.energy = 0;	// this critter is dead
								}
								else if(jPower> iPower) {
									j.energy += 1/2 *(i.energy);
									i.energy = 0; 	// this critter is dead
								}
								else {
									//decide the winner using flipping a coin. 1 means i win, 0 means j win
									int coinflip = getRandomInt(2);
									if(coinflip==1) {
										i.energy += 1/2 *(j.energy); 
										j.energy = 0;	// this critter is dead
									}
									else {
										j.energy += 1/2 *(j.energy); 
										i.energy = 0;	// this critter is dead
									}
								}
							}
						}
					}
					
				}
				
			}
		}
		// add all the babies in
		for(Critter b: babies) {
			if(b.energy>0) {
				population.add(b);
			}
		}
		// all critters
		for(Critter c: population) {
			c.energy -= Params.rest_energy_cost;
		}
		// clear all the dead critters
		List<Critter> deadCritters = new java.util.ArrayList<Critter>();
		for(Critter c: population) {
			if(c.energy<=0) {
				deadCritters.add(c);
			}
		}
		for(Critter c: deadCritters) {
			population.remove(c);
		}
		//create algae
		for(int i=0;i<Params.refresh_algae_count;i++) {
				try {
					Critter.makeCritter("assignment4.Algae");		// call the default constructor of algea 
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidCritterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/**
	 * This method displayWorld prints out a 2D grid to System.out
	 * If a position is occupied, call the toString method on the occupant critter
	 * If a position is not occupied, then print a single space
	 * This method also prints the border using "|" "-" "+"
	 */
	public static void displayWorld() {
		String[][] world = new String[Params.world_height][Params.world_width];
		// all the corners
		world[0][0] = "+";
		world[0][Params.world_width+1] = "+";
		world[Params.world_height+1][0] = "+";
		world[Params.world_height+1][Params.world_width+1] = "+";
		//all the "-"
		for(int i=1;i<Params.world_width+1;i++) {
			world[0][i] = "-";
			world[Params.world_height+1][i] = "-";
		}
		
		//all the "|"
		for(int i = 1;i<Params.world_height+1;i++) {
			world[i][0] = "|";
			world[i][Params.world_width+1] = "|";
		}
		//all the Critters
		for(Critter c: population) {
			world[c.x_coord+1][c.y_coord+1] = c.toString();
			
		}
		for (int i = 0; i < Params.world_height+2; i += 1) {
			for (int j = 0; j < Params.world_width+2; j += 1) {
				System.out.print(world[i][j]);
			}
			System.out.println();
		}
	}
	
	//----------------Helper Functions---------------------
	public boolean isAlive() {
		if(this.energy>0) {
			return true;
		}
		return false;
	}
	
}
