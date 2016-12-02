package Filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import SkeletonMaps.Bone;
import core.Main;
import core.SkeletonParticle;

public class BasicSkeletonFilter implements Filter {
	
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
	
	static double dotProd(Bone bone, double dm, double dh) {
		return Math.cos(Math.toRadians(dh)) * dm;
	}
	
	public void performStep() {
		List<SkeletonParticle> particles = SkeletonParticle.getParticles();
		List<SkeletonParticle> newParticles = new ArrayList<SkeletonParticle>();
		
		Random randomSeed = new Random();
		double[] movement = Main.inputGenerator.generateStepInput();
		double totalWeight = 0;
		
		for(SkeletonParticle p: particles) {
			double gauss1 = randomSeed.nextGaussian();	
			double gauss2 = randomSeed.nextGaussian();
			double dm = movement[0]+ gauss1 * 5;
			double dh=movement[1] + gauss2 * 4;
			
			Bone bone = p.getSeg();
			double dist = p.getDist();
			int dir = p.getDir();
			double findist = dotProd(bone,dm,dh);
			
			
			
			//dir 1 is from first point to second, 2 is opposite. If dot product <0, the direction has switched
			if(findist<0) {
				dir = 3-dir;
				findist *= -1;
			}
			
			if(dir==1)
				dist +=findist;
			else
				dist-=findist;
			
			if(dist>=1) {
				bone = bone.nextSecondBone();
				dist = 0;
				dir = 1;
			}
			else if(dist<=0) {
				bone = bone.nextFirstBone();
				dist = 1;
				dir = 2;
			}		
			double newP = 1; //Calculate in terms of angle between heading and actual new bone segment?
			p.setSeg(bone);
			p.setDist(dist);
			p.setDir(dir);
			totalWeight += newP;
		}

		
		double sumSoFar[] = new double[particles.size()];
		sumSoFar[0] = particles.get(0).getP() / totalWeight;
		
		for(int i=2; i<particles.size(); i++) 
			sumSoFar[i] = sumSoFar[i-1]+ particles.get(i).getP()/totalWeight;
		
		for (int i=0; i<particles.size(); i++) {
			double rand = randomSeed.nextDouble();
			int dex = bsearch(rand,sumSoFar);
			SkeletonParticle p = particles.get(dex);
			newParticles.add(new SkeletonParticle(p.getX(),p.getY(),p.getH()));				
		}
		
		SkeletonParticle.setParticles(newParticles); 		
	}
}