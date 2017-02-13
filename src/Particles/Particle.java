package Particles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.Main;

public class Particle {
	private double x,y,p,h;
	
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
	
	public boolean crossesWall(Particle p, double newX, double newY) {
		return Main.map.crossesWall(p, newX, newY);
	}
}
