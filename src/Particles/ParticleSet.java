package Particles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Maps.Bone;
import core.Main;

public class ParticleSet<ParticleType> {
	
	private List<ParticleType> particles;
	
	public  List<ParticleType> getParticles() {
		return particles;
	}
	
	public void setParticles(List<ParticleType> p) {
		particles = p;
	}
	
	public void seedParticles(Class<ParticleType> c, int nr) {
		
		if(c.equals(Particle.class)) {
			Class[] args = new Class[3];		
			args[0]= args[1] = args[2] = double.class;		
			particles = new ArrayList<ParticleType>();
			int nrSeeded = 0;
			Random randomSeed = new Random();
			while(nrSeeded<nr) {
				double xCoord = randomSeed.nextDouble()*20;
				double yCoord = randomSeed.nextDouble()*11;
				/*while((xCoord>100&&xCoord<400)&&(yCoord>100&&yCoord<900)){
					xCoord = randomSeed.nextDouble()*500;
					yCoord = randomSeed.nextDouble()*1000;
				}*/
					
				int hDir = randomSeed.nextInt(361);
				
				try{
				particles.add(c.getDeclaredConstructor(args).newInstance(xCoord,yCoord,hDir));
				nrSeeded++;
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		else if(c.equals(SkeletonParticle.class)) {
			Class[] args = new Class[6];		
			args[0] = Bone.class;
			args[1]=args[3] = args[4]=args[5]=double.class;
			args[2] =int.class;			
			particles = new ArrayList<ParticleType>();
			int nrSeeded = 0;
			Bone[] bones = Main.skeleMap.getSkeleton();
			Random randomSeed = new Random();
			while(nrSeeded<nr) {
				int boneCoord = randomSeed.nextInt(bones.length);
				//int boneCoord = 0;
				double length = randomSeed.nextDouble();
				int dir = randomSeed.nextInt(2)+1;
				double p = 1;
				try{
				particles.add(c.getDeclaredConstructor(args).newInstance(bones[boneCoord],length,dir,p,0.0,0.0));
				nrSeeded++;		
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		
		}
	}
}
