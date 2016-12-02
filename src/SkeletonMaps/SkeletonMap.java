package SkeletonMaps;

import java.awt.Shape;

import core.Particle;

public interface SkeletonMap {	
	
	public void setUpMap();
	public  Shape getWalls();	
	public  Shape getRealLocation();	
	public  Shape getParticles();
	public Bone[] getSkeleton();
	
}
