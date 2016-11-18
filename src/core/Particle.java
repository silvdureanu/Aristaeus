package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Particle {
	private double x,y,p,h;
	private static List<Particle> particles;
	//TODO change with new class, ParticleSet, to allow multiple filters @ same time
	//each filter has a particleset, the map and visualiser may be extended to multiple particle sets

	
	public Particle(double xloc, double yloc, double hloc) {
		x=xloc;
		y=yloc;
		h=hloc;
	}
	
	public Particle(double xloc, double yloc) {
		x=xloc;
		y=yloc;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getP(){
		return p;
	}
	
	public double getH(){
		return h;
	}
	
	public void setX(double nx) {
		x = nx;
	}
	
	public void setY(double ny) {
		y = ny;
	}
	
	public void setP(double np){
		p = np;
	}
	
	public void setH(double nh) {
		h=nh;
	}
	
	
	public static List<Particle> getParticles() {
		return particles;
	}
	
	public static void setParticles(List<Particle> p) {
		particles = p;
	}
	
	public static void seedParticles() {
		particles = new ArrayList<Particle>();
		int nrSeeded = 0;
		Random randomSeed = new Random();
		while(nrSeeded<2000) {
			int xCoord = randomSeed.nextInt(1000);
			int yCoord = randomSeed.nextInt(500);
			int hDir = randomSeed.nextInt(4);
			particles.add(new Particle(xCoord,yCoord,hDir));
			nrSeeded++;			
		}
	}
	
	public boolean crossesWall(Particle p, double newX, double newY) {
		return Main.map.crossesWall(p, newX, newY);
	}
}
