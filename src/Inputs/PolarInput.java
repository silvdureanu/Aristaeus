package Inputs;

import java.util.Random;

public class PolarInput implements Input {
	 /*private double[][]inputs = {{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},
			{100,270},{100,0},{100,0},{100,0},
			{100,270},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{110,0},{30,180}, {10,180},
			{100,270},{100,0},{100,0},{100,0}};*/
	
	private double[][]inputs = {{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},
			{10,270},{10,0},{10,0},{10,0},
			{10,270},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{10,0},{11,0},{3,180}, {5,180},
			{10,270},{10,0},{10,0},{10,0}};
	private int nr = inputs.length;
	Random randomSeed = new Random();
	double dx;
	double da;
	 
	public  boolean hasInputs() {
		return (nr>0);
	}
	
	
	public  double[] generateStepInput() {
			nr--;
			dx = randomSeed.nextGaussian() * 0.1;
			da = randomSeed.nextGaussian()* 0.1;
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
	
	public double[] generateRealInput() {
		double[] v = {0,0};
		v[0]+=inputs[inputs.length-nr][0];
		v[1]+=inputs[inputs.length-nr][1];
		return v;
	}		
		
	
	
}
