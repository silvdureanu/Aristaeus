package Inputs;

import java.util.Random;

public class StraightInput implements Input {
	 private double[][]inputs = {{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},
			 {100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},
			 {100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},
			 {100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},
			 {100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0}};
	
	private int nr = inputs.length;
	Random randomSeed = new Random();
	double dx;
	double da;
	 
	public  boolean hasInputs() {
		return (nr>0);
	}
	
	
	public  double[] generateStepInput() {
			nr--;
			dx = randomSeed.nextGaussian() * 4;
			da = randomSeed.nextGaussian()* 5;
			double[] v = {dx,da};
			v[0]+=inputs[inputs.length-nr][0];
			v[1]+=inputs[inputs.length-nr][1];
			return v;
	};
	
	public  double[] generateMapInput() {
		double[] v = {dx,da};
		v[0]+=inputs[inputs.length-nr][0];
		v[1]+=inputs[inputs.length-nr][1];
		return v;
	}
	
}
