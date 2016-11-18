package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
	
	static int[][] coords = new int[][]{
		{5,5,5,995},{5,995,495,995},{495,995,495,5},{495,5,5,5},
		{100,100,100,900},{100,900,400,900},{400,900,400,100},{400,100,100,100}
	}; // map
	
	static public Map map =  new Map(coords);
	static public Filter filter = new Filter();	
	static public InputGenerator inputGenerator = new InputGenerator();

	public static void main(String[] args) {
		//generate initial particle list
	
		List<Particle> initialParticles = new ArrayList<Particle>();
		int nrSeeded = 0;
		Random randomSeed = new Random();
		while(nrSeeded<2000) {
			int xCoord = randomSeed.nextInt(1000);
			int yCoord = randomSeed.nextInt(500);
			initialParticles.add(new Particle(xCoord,yCoord));
			nrSeeded++;			
		}
		
		Particle.setParticles(initialParticles);		
		Visualiser visualiser = new Visualiser();		
		visualiser.visualise();
		
		
		
		while(inputGenerator.hasInputs()) {
			try{Thread.sleep(1000);}
			catch(Exception e){};
			filter.performStep();
			
		}
		
	}

}
