package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Filter {
	
	public static void performStep() {
		List<Particle> particles = Particle.getParticles();
		List<Particle> newParticles = new ArrayList<Particle>();
		
		Random randomSeed = new Random();
		double[] movement = InputGenerator.generateInput();
		double totalWeight = 0;
		
		for(Particle p: particles) {
			double gauss1 = randomSeed.nextGaussian();
			double gauss2 = randomSeed.nextGaussian();			
			double dx = movement[0]+ gauss1*0.5;
			double dy = movement[1]+= gauss2*0.5;
			
			double newX = p.getX() + dx;
			double newY = p.getY() + dy;
			double newP = Map.crossesWall(p,newX, newY) ? 0 : gauss1 * gauss2;
			p = new Particle(newX, newY, newP);			
			
			totalWeight += newP;
		}
		
		
		
		
		for (int i=0; i<particles.size(); i++) {
			double rand = randomSeed.nextDouble();
			double sumSoFar = 0;
			int dex = 0;
			while((particles.get(dex).getP()/totalWeight) + sumSoFar < rand && dex<particles.size())
				dex++; // multinomial sampling
			
			newParticles.add(particles.get(dex));
			//TODO: separate functions for each filter step, binary search			
		}
		
		particles = newParticles;		
	}

}
