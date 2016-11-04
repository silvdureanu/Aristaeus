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
			double dx = movement[0]+ gauss1*0.2;
			double dy = movement[1]+ gauss2*0.2;
			
			double newX = p.getX() + dx;
			double newY = p.getY() + dy;
			double newP = Map.crossesWall(p, newX, newY) ? 0 : 1;
			p.setX(newX);
			p.setY(newY);
			p.setP(newP);			
			totalWeight += newP;
		}
		
		
		
		
		for (int i=0; i<particles.size(); i++) {
			double rand = randomSeed.nextDouble();
			double sumSoFar = 0;
			int dex = 0;
			while(((particles.get(dex).getP()/totalWeight) + sumSoFar)< rand && (dex<particles.size()-1)) {
				sumSoFar += particles.get(dex).getP()/totalWeight;
				dex++; // multinomial sampling
			}
			
			Particle p = particles.get(dex);
			newParticles.add(new Particle(p.getX(),p.getY()));
			//TODO: separate functions for each filter step, binary search			
		}
		
		particles = newParticles;		
	}

}