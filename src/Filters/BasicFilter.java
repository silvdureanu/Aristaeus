package Filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.Main;
import core.Particle;

public class BasicFilter implements Filter {
	
	public void performStep() {
		List<Particle> particles = Particle.getParticles();
		List<Particle> newParticles = new ArrayList<Particle>();
		
		Random randomSeed = new Random();
		double[] movement = Main.inputGenerator.generateStepInput();
		double totalWeight = 0;
		
		for(Particle p: particles) {
			double gauss1 = randomSeed.nextGaussian();
			double gauss2 = randomSeed.nextGaussian();			
			double dx = movement[0]+ gauss1*15;
			double dy = movement[1]+ gauss2*15;
			
			double newX = p.getX() + dx;
			double newY = p.getY() + dy;
			double newP = Main.map.crossesWall(p, newX, newY) ? 0 : 1;
			if(Main.map.outsideBounds(newX,newY))
				newP = 0;
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
		}
		
		Particle.setParticles(newParticles); 		
	}
}