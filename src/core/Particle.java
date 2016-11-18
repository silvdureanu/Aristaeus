package core;

import java.util.List;

public class Particle {
	private double x,y,p;
	private static List<Particle> particles;
	//TODO change with new class, ParticleSet, to allow multiple filters @ same time
	//each filter has a particleset, the map and visualiser may be extended to multiple particle sets
	
	public Particle(double xloc, double yloc, double prob) {
		x=xloc;
		y=yloc;
		p=prob;
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
	
	public void setX(double nx) {
		x = nx;
	}
	
	public void setY(double ny) {
		y = ny;
	}
	
	public void setP(double np){
		p = np;
	}
	
	
	public static List<Particle> getParticles() {
		return particles;
	}
	
	public static void setParticles(List<Particle> p) {
		particles = p;
	}
	
	public boolean crossesWall(Particle p, double newX, double newY) {
		return Main.map.crossesWall(p, newX, newY);
	}
}
