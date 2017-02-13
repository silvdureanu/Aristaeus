package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import SkeletonMaps.Bone;

public class SkeletonParticle {
	private Bone seg;
	private double dist;
	private int dir;
	private static List<SkeletonParticle> particles;
	//TODO change with new class, ParticleSet, to allow multiple filters @ same time
	//each filter has a particleset, the map and visualiser may be extended to multiple particle sets

	
	public SkeletonParticle(Bone b, double d, int dr ) {
		seg = b;
		dist = d;
		dir = dr;
	}
	
	public Bone getSeg() {
		return seg;
	}
	
	public double getDist() {
		return dist;
	}
	
	public int getDir(){
		return dir;
	}
	
	
	public void setSeg(Bone b) {
		seg = b;
	}
	
	public void setDist(double n) {
		dist = n;
	}
	
	public void setDir(int d){
		dir = d;
	}
	
	public static List<SkeletonParticle> getParticles() {
		return particles;
	}
	
	public static void setParticles(List<SkeletonParticle> p) {
		particles = p;
	}
	
	public static void seedParticles() {
		particles = new ArrayList<SkeletonParticle>();
		int nrSeeded = 0;
		Bone[] bones = Main.map.getSkeleton();
		Random randomSeed = new Random();
		while(nrSeeded<500000) {
			int boneCoord = randomSeed.nextInt(bones.length);
			double length = randomSeed.nextDouble();
			int dir = randomSeed.nextInt(1)+1;
			particles.add(new SkeletonParticle(bones[boneCoord],length,dir));
			nrSeeded++;			
		}
	}
	
}
