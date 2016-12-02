package Filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.Main;
import core.Particle;

public class PolarFilter implements Filter {
	
	static int bsearch(double value, double[] v) {  // returns min(i) s.t. v[i]>=value
		int hi,lo,mid;
		lo = 0; 
		hi = v.length-1;
		
		while(hi-lo >1) {
			mid = lo + (hi-lo)/2; //avoid overflow in case of billions of particles
			if (v[mid]>=value)
				hi=mid;
			else
				lo = mid+1;
			
		}
		
		if(hi==lo)
			return hi;
		else if(v[lo]<value)
			return hi;
		return lo;
	}
	
	public void performStep() {
		List<Particle> particles = Particle.getParticles();
		List<Particle> newParticles = new ArrayList<Particle>();
		
		Random randomSeed = new Random();
		double[] movement = Main.inputGenerator.generateStepInput();
		double totalWeight = 0;
		
		for(Particle p: particles) {
			double gauss1 = randomSeed.nextGaussian();	
			double gauss2 = randomSeed.nextGaussian();
			double dm = movement[0]+ gauss1 * 5;
			double dh=movement[1] + gauss2 * 4;
			
			double newH = ((int)p.getH()+dh)%360;			
			double newX = p.getX() + dm * Math.cos(Math.toRadians(newH));
			double newY = p.getY()- dm * Math.sin(Math.toRadians(newH)); // Y coordinates start at the top, so need to invert
			double newP;
			if(Main.map.outsideBounds(newX,newY))
				newP = 0;
			
			else newP = Main.map.crossesWall(p, newX, newY) ? 0 : 1;

			p.setX(newX);
			p.setY(newY);
			p.setP(newP);	
			p.setH(newH);
			totalWeight += newP;
		}

		
		double sumSoFar[] = new double[particles.size()];
		sumSoFar[0] = particles.get(0).getP() / totalWeight;
		
		for(int i=2; i<particles.size(); i++) 
			sumSoFar[i] = sumSoFar[i-1]+ particles.get(i).getP()/totalWeight;
		
		for (int i=0; i<particles.size(); i++) {
			double rand = randomSeed.nextDouble();
			int dex = bsearch(rand,sumSoFar);
			Particle p = particles.get(dex);
			newParticles.add(new Particle(p.getX(),p.getY(),p.getH()));				
		}
		
		Particle.setParticles(newParticles); 		
	}
}