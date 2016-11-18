package Filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.Main;
import core.Particle;

public class DirectionalFilter implements Filter {
	
	public void performStep() {
		List<Particle> particles = Particle.getParticles();
		List<Particle> newParticles = new ArrayList<Particle>();
		
		Random randomSeed = new Random();
		double[] movement = Main.inputGenerator.generateStepInput();
		double totalWeight = 0;
		
		for(Particle p: particles) {
			double gauss1 = randomSeed.nextGaussian();				
			double dm = movement[0]+ gauss1*5;
			int dh=(int)movement[1];
			System.out.println(dh);
			
			int newH = ((int)p.getH()+dh)%4;			
			double newX = p.getX();
			double newY = p.getY();
			
			switch(newH) {
			case 0: newY -= dm;
					break;
			case 1: newX += dm;
					break;
			case 2: newY += dm;
					break;
			case 3: newX -= dm;
					break;			
			}
			double newP = Main.map.crossesWall(p, newX, newY) ? 0 : 1;
			if(Main.map.outsideBounds(newX,newY))
				newP = 0;
			p.setX(newX);
			p.setY(newY);
			p.setP(newP);	
			p.setH(newH);
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
			newParticles.add(new Particle(p.getX(),p.getY(),p.getH()));				
		}
		
		Particle.setParticles(newParticles); 		
	}
}