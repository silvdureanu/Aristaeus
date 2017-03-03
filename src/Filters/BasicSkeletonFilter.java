package Filters;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Maps.Bone;
import Maps.Joint;
import Particles.ParticleSet;
import Particles.SkeletonParticle;
import core.Main;

public class BasicSkeletonFilter implements Filter {
	
	ParticleSet<SkeletonParticle> particleSet;
	
	public BasicSkeletonFilter() {
		particleSet = new ParticleSet<SkeletonParticle>();
		particleSet.seedParticles((Class)SkeletonParticle.class,10000);		
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
	
	static double cosProd(double dm, double dh) {
		return Math.cos(Math.toRadians(dh)) * dm;
	}
	
	static double dotProd(Point2D a, Point2D b) {
		return a.getX()*b.getX() + a.getY() * b.getY();
	}
	
	static double crossProd(Point2D a, Point2D b) {
		double x1 = a.getX();
		double x2 = b.getX();
		double y1 = a.getY();
		double y2 = b.getY();
		
		return x1*y2 - y1*x2; 
	}
	
	public void performStep() {
		List<SkeletonParticle> particles = particleSet.getParticles();
		List<SkeletonParticle> newParticles = new ArrayList<SkeletonParticle>();
		double epiphysize = 7; // depending on domain; Should probably be 1-1.5 avg "steps"?
		
		Random randomSeed = new Random();
		double[] movement = Main.inputGenerator.generateStepInput();
		double totalWeight = 0;
		for(SkeletonParticle p: particles) {
			double gauss1 = randomSeed.nextGaussian();	
			double gauss2 = randomSeed.nextGaussian();
			double dm = movement[0]+ gauss1 * 0.25;
			double dh=movement[1] + gauss2 * 2;
			boolean hoisted = false;
			double newP = 0;
			
			Bone bone = p.getSeg();
			double dist = p.getDist();
			int dir = p.getDir();
			
			//TODO - hoist probability exponential instead of linear?			
			if(bone.getLen() - p.getDist()*bone.getLen()<= epiphysize && dir==1) { // hoist at end of bone
				double pHoistInv = (bone.getLen() - p.getDist()*bone.getLen()) / epiphysize; // inverse, for easy calc 
				if(randomSeed.nextDouble()>=pHoistInv) {
					hoisted = true;
					
					Joint next =  bone.nextSecondBone(dh);
					Bone nextBone= next.getNextBone();
					
					int prevDir = dir;
					dir = next.getNextDir();
					
					int d = dir ==prevDir ? 1:-1;
					
					Point2D firstVector = new Point2D.Double(bone.getSecondPoint().getX()-bone.getFirstPoint().getX(),
							bone.getSecondPoint().getY()-bone.getFirstPoint().getY());
					
					Point2D secondVector = new Point2D.Double(d*(nextBone.getSecondPoint().getX()-nextBone.getFirstPoint().getX()),
							d*(nextBone.getSecondPoint().getY()-nextBone.getFirstPoint().getY()));
					
					double angle = Math.acos(dotProd(firstVector,secondVector)/ firstVector.distance(0, 0) / firstVector.distance(0, 0));
					int direction = crossProd(firstVector,secondVector) > 0 ? -1: 1; // inversion due to Y starting up
					
					//System.out.print(firstVector);
					//System.out.print(' ');
					//System.out.println(secondVector);
					angle *= direction; // to have full trig circle			
					newP = Math.cos(Math.abs(angle - Math.toRadians(dh) ));
					
					//System.out.println(newP);
					if(newP<0.05)
						newP = 0;
					
					bone = nextBone;
					
					if(dir==1)
						dist = (dm *newP / bone.getLen());
					else if(dir==2)
						dist = 1 - dm*newP / bone.getLen();					
					
				}
			}
			
			if(bone.getLen() * p.getDist() <=epiphysize && dir==2) { // hoist at start of bone
				double pHoistInv = (bone.getLen() * p.getDist()) / epiphysize;
				if(randomSeed.nextDouble()>=pHoistInv) {
					hoisted = true;
					Joint next =  bone.nextFirstBone(dh);
					Bone nextBone= next.getNextBone();
					
					int prevDir = dir;
					dir = next.getNextDir();
					
					int d = dir ==prevDir ? 1:-1;
					
					Point2D secondVector = new Point2D.Double(d*(bone.getSecondPoint().getX()-bone.getFirstPoint().getX()),
							d*(bone.getSecondPoint().getY()-bone.getFirstPoint().getY()));
					
					Point2D firstVector = new Point2D.Double(nextBone.getSecondPoint().getX()-nextBone.getFirstPoint().getX(),
							nextBone.getSecondPoint().getY()-nextBone.getFirstPoint().getY());
					
					double angle = Math.acos(dotProd(firstVector,secondVector)/ firstVector.distance(0, 0) / firstVector.distance(0, 0));
					
					// must do cross prod in terms of directions, not absolute vals
					int direction = crossProd(firstVector,secondVector) > 0 ? -1: 1; // inversion due to Y starting up
					angle *= direction; // to have full trig circle			
					
					newP = -1*Math.cos(Math.abs(angle - Math.toRadians(dh) ));		//NO idea why this works	
					
					if(newP<0.05)
						newP = 0;
					bone = nextBone;
					if(dir==1)
						dist = (dm *newP / bone.getLen());
					else if(dir==2)
						dist = 1 - dm*newP / bone.getLen();		
				}					
			}
			
			if(!hoisted) { // ordinary movement 
				double findist = cosProd(dm,dh) / bone.getLen(); 
				
				newP = Math.min(Math.abs(Math.cos(Math.toRadians(dh))),1); //min there in case cos is changed to something else that overestimates
				
				//dir 1 is from first point to second, 2 is opposite. If dot product <0, the direction has switched
				if(findist<0) {
					dir = 3-dir;
					findist *= -1;
				}
				
				if(dir==1)
					dist +=findist;
				else
					dist-=findist;
				
				if(dist>1 || dist<0) {
					newP = 0;
					dist =0; // cosmetic, prevents "tails" appearing at animation step
				}
				
				if(newP<0.2)
					newP = 0;
				
				
			}				
			p.setSeg(bone);
			p.setDist(dist);
			p.setDir(dir);
			p.setProb(newP);
			totalWeight += newP;
			
		}
		if(totalWeight==0) {   //fix division by zero......
			particleSet.setParticles(newParticles);
			return;
		}	
		
		double sumSoFar[] = new double[particles.size()];
		sumSoFar[0] = particles.get(0).getProb() / totalWeight;
		
		for(int i=2; i<particles.size(); i++) 
			sumSoFar[i] = sumSoFar[i-1]+ particles.get(i).getProb()/totalWeight;
		
		for (int i=0; i<particles.size(); i++) {
			double rand = randomSeed.nextDouble();
			int dex = bsearch(rand,sumSoFar);
			SkeletonParticle p = particles.get(dex);
			newParticles.add(new SkeletonParticle(p.getSeg(),p.getDist(),p.getDir(), p.getProb()));				
		}
		Main.map.updateRealLocation();
		particleSet.setParticles(newParticles); 		
	}
	public List<SkeletonParticle> getParticles() {
		return particleSet.getParticles();
	}
}