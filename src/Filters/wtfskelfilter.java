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

public class wtfskelfilter implements Filter {
	
	ParticleSet<SkeletonParticle> particleSet;
	
	double Aalpha = 0;
	double Balpha = 25;
	double Abeta = 25;
	double Bbeta = 80;
	public wtfskelfilter() {
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
		double epiphysize = 0.7; // depending on domain; Should probably be 1-1.5 avg "steps"?
		
		Random randomSeed = new Random();
		double[] movement = Main.inputGenerator.generateStepInput();
		double totalWeight = 0;
		for(SkeletonParticle p: particles) {
			double palpha = p.getAlpha();
			double pbeta = p.getBeta();
			double gauss1 = randomSeed.nextGaussian();	
			double gauss2 = randomSeed.nextGaussian();
			double dm = movement[0]+ gauss1 * 0.02;
			double dh=movement[1] + gauss2 * 2;
			boolean hoisted = false;
			double newP = 0;
			double alphafact = 0;
			
			Bone bone = p.getSeg();
			double dist = p.getDist();
			int dir = p.getDir();
			
			//TODO - hoist probability exponential instead of linear?			
			if(bone.getLen() - p.getDist()*bone.getLen()<= epiphysize && dir==1 &&bone.hasNextSecond()) { // hoist at end of bone
				double pHoistInv = (bone.getLen() - p.getDist()*bone.getLen()) / epiphysize; // inverse, for easy calc 
				if(randomSeed.nextDouble()>=pHoistInv) {
					hoisted = true;
					
					Joint next =  bone.nextSecondBone(dh);
					Bone nextBone= next.getNextBone();
					//since we calc prob based on angle, just return random next spline? (you from the future: yep)
					int prevDir = dir;
					dir = next.getNextDir();
					
					int d = dir ==prevDir ? 1:-1;
					
					Point2D firstVector = new Point2D.Double(bone.getSecondPoint().getX()-bone.getFirstPoint().getX(),
							bone.getSecondPoint().getY()-bone.getFirstPoint().getY());
					
					Point2D secondVector = new Point2D.Double(d*(nextBone.getSecondPoint().getX()-nextBone.getFirstPoint().getX()),
							d*(nextBone.getSecondPoint().getY()-nextBone.getFirstPoint().getY()));
					
					double angle = Math.acos(Math.min(1, dotProd(firstVector,secondVector)/ firstVector.distance(0, 0) / secondVector.distance(0, 0)));
					int direction = crossProd(firstVector,secondVector) > 0 ? -1: 1; // inversion due to Y starting up
					
					angle *= direction; // to have full trig circle	
					double pbdir = pbeta>0? 1:-1; 
					double padir = palpha>0? 1:-1;

					if(pbeta!=0)
						angle += pbdir*Math.toRadians(Abeta* Math.exp(-Bbeta/Math.abs(pbeta))); // plus/minus through pbdir
					
					double angdir = angle>0? 1:-1;
					padir*=angdir; // alpha drift direction same or different to angle direction
					if(palpha!=0) //alpha interacts with beta now
						alphafact = padir*Math.abs(Math.sin(angle)) * Aalpha* Math.exp(-Balpha/Math.abs(palpha));
					palpha=pbeta = 0;
					newP = Math.cos(Math.abs(angle - Math.toRadians(dh) ));
	

					if(newP<0.2)
						newP = 0;
					bone = nextBone;
					
					if(dir==1) {
						dist = (dm *newP / bone.getLen());
						if(alphafact<0)
							dist = Math.max(dist-alphafact/bone.getLen(),0);
						else dist += (alphafact/bone.getLen());
					}
					else if(dir==2) {
						dist = 1 - dm*newP / bone.getLen();
						if (alphafact<0)
							dist = Math.min(1, dist+alphafact/bone.getLen());
						else dist -=(alphafact/bone.getLen());
					}

					
				}
			}
			
			if(bone.getLen() * p.getDist() <=epiphysize && dir==2&&bone.hasNextFirst()) { // hoist at start of bone
				double pHoistInv = (bone.getLen() * p.getDist()) / epiphysize;
				if(randomSeed.nextDouble()>=pHoistInv) {
					hoisted = true;
					Joint next =  bone.nextFirstBone(dh);
					Bone nextBone= next.getNextBone();
					
					int prevDir = dir;
					dir = next.getNextDir();
					
					int d = dir ==prevDir ? 1:-1;
					
					Point2D firstVector = new Point2D.Double(bone.getFirstPoint().getX()-bone.getSecondPoint().getX(),
							bone.getFirstPoint().getY()-bone.getSecondPoint().getY());
					
					Point2D secondVector = new Point2D.Double(d*(nextBone.getFirstPoint().getX()-nextBone.getSecondPoint().getX()),
							d*(nextBone.getFirstPoint().getY()-nextBone.getSecondPoint().getY()));
					
					double angle = Math.acos(Math.min(1, dotProd(firstVector,secondVector)/ firstVector.distance(0, 0) / secondVector.distance(0, 0)));
					
					int direction = crossProd(firstVector,secondVector) > 0 ? -1: 1; // inversion due to Y starting up
					angle *= direction; // to have full trig circle	
					double pbdir = pbeta>0? 1:-1; 
					double padir = palpha>0? 1:-1;

					if(pbeta!=0)
						angle += pbdir* Math.toRadians(Abeta* Math.exp(-Bbeta/Math.abs(pbeta)));
					
					
					
					double angdir = angle>0? 1:-1;
					padir*=angdir; // alpha drift direction same or different to angle direction
					if(palpha!=0) //alpha interacts with beta now
						alphafact = padir*Math.abs(Math.sin(angle)) * Aalpha* Math.exp(-Balpha/Math.abs(palpha));
					palpha=pbeta=0;
					
					newP = Math.cos(Math.abs(angle - Math.toRadians(dh) ));


					if(newP<0.2)
						newP = 0;
					bone = nextBone;
					if(dir==1) {
						dist = (dm *newP / bone.getLen());
						if(alphafact<0)
							dist = Math.max(dist-alphafact/bone.getLen(),0);
						else dist += (alphafact/bone.getLen());
					}
					else if(dir==2) {
						dist = 1 - dm*newP / bone.getLen();
						if (alphafact<0)
							dist = Math.min(1, dist+alphafact/bone.getLen());
						else dist -=(alphafact/bone.getLen());
					}
				}					
			}
			
			if(!hoisted) { // ordinary movement 
				double findist = cosProd(dm,dh) / bone.getLen(); 
				
				newP = Math.min(Math.abs(Math.cos(Math.toRadians(dh))),1); //min there in case cos is changed to something else that overestimates
				if(Math.abs(dh)>6) {//avoid noise
					palpha +=dh;
					pbeta+=dh;
				}
				else {
					palpha /=1.15;
					pbeta /=1.15;
				}
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
			p.setAlpha(palpha);
			p.setBeta(pbeta);
			totalWeight += newP;
		
			
		}
		if(totalWeight==0) {   //fix division by zero......
			particleSet.setParticles(newParticles);
			return;
		}	
		if(movement[1]>10)
			System.out.println("turniin");
		double sumSoFar[] = new double[particles.size()];
		sumSoFar[0] = particles.get(0).getProb() / totalWeight;
		
		for(int i=2; i<particles.size(); i++) 
			sumSoFar[i] = sumSoFar[i-1]+ particles.get(i).getProb()/totalWeight;
		
		for (int i=0; i<particles.size(); i++) {
			double rand = randomSeed.nextDouble();
			int dex = bsearch(rand,sumSoFar);
			SkeletonParticle p = particles.get(dex);
			newParticles.add(new SkeletonParticle(p.getSeg(),p.getDist(),p.getDir(), p.getProb(),p.getAlpha(),p.getBeta()));				
		}
		Main.map.updateRealLocation();
		particleSet.setParticles(newParticles); 		
	}
	public List<SkeletonParticle> getParticles() {
		return particleSet.getParticles();
	}
}