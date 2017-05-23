package Particles;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Maps.Bone;
import core.Main;

public class SkeletonParticle {
	private Bone seg;
	private double dist,prob;
	private int dir;
	private double alphaval, betaval;

	
	public SkeletonParticle(Bone b, double d, int dr,double p, double ap, double bt ) {
		seg = b;
		dist = d;
		dir = dr;
		prob = p;
		alphaval = ap;
		betaval = bt;
	}
	
	public double getX() {
		Point2D p1 = seg.getFirstPoint();
		Point2D p2 = seg.getSecondPoint();	
		return p1.getX() + dist* (p2.getX()-p1.getX());
	}
	
	public double getY() {
		Point2D p1 = seg.getFirstPoint();
		Point2D p2 = seg.getSecondPoint();		
		return p1.getY() + dist* (p2.getY()-p1.getY());
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
	
	public double getProb() {
		return prob;
	}
	
	public double getAlpha() {
		return alphaval;
	}
	
	public double getBeta() {
		return betaval;
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
	
	public void setProb(double p) {
		prob = p;
	}
	
	public void setAlpha(double a) {
		alphaval = a;
	}
	
	public void setBeta(double b) {
		betaval = b;
	}
	

	

	
}
