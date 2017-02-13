package Filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Particles.Particle;
import Particles.ParticleSet;
import core.Main;

public class DirectionalFilter implements Filter {
	
	
	private ParticleSet<Particle> particleSet ;
	
	public DirectionalFilter() {
		particleSet = new ParticleSet<Particle>();
		particleSet.seedParticles((Class)Particle.class, 500000);
	}
	
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
		List<Particle> particles = particleSet.getParticles();
		List<Particle> newParticles = new ArrayList<Particle>();
		
		Random randomSeed = new Random();
		double[] movement = Main.inputGenerator.generateStepInput();
		double totalWeight = 0;
		
		for(Particle p: particles) {
			double gauss1 = randomSeed.nextGaussian();				
			double dm = movement[0]+ gauss1*5;
			int dh=(int)movement[1];
			
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
		
		particleSet.setParticles(newParticles); 		
	}
	public List<Particle> getParticles() {
		return particleSet.getParticles();
	}
}