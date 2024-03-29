package Inputs;

import java.util.Random;

public class LeftTurn implements Input {
	 /*private double[][]inputs = {{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,15},{0.5,15},{0.5,15},{0.5,15},{0.5,15},{0.5,15},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0}};*/
	 
	 
	 private double[][] inputs = {{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,15},{0.5,15},{0.5,15},{0.5,15},{0.5,15},{0.5,15},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},{0.5,0},
			 {0.5,30},{0.5,30},{0.5,30},
			 {0.5,0},{0.5,0},{0.5,0},{0.6,270},{0.5,0},{0.5,0},{0.5,0},{0.5,315},{0.5,315},{0.5,0},
			 {0.5,0},{0.5,0},{0.5,0},{0.5,0}
			 
	 };
	
	private int nr = inputs.length;
	Random randomSeed = new Random();
	double dx;
	double da;
	 
	public  boolean hasInputs() {
		return (nr>0);
	}
	
	
	public  double[] generateStepInput() {
			dx = randomSeed.nextGaussian() * 0.1;
			da = randomSeed.nextGaussian()* 0.1;
			double[] v = {dx,da};
			v[0]+=inputs[inputs.length-nr][0];
			v[1]+=inputs[inputs.length-nr][1];
			nr--;
			//v[0]*=10;
			//v[1]*=10;
			return v;
	};
	
	public  double[] generateMapInput() {
		double[] v = {dx,da};
		v[0]+=inputs[inputs.length-nr][0];
		v[1]+=inputs[inputs.length-nr][1];
		//v[0]*=10;
		//v[1]*=10;
		return v;
	}
	
	public double[] generateRealInput() {
		double[] v = {0,0};
		v[0]+=inputs[inputs.length-nr][0];
		v[1]+=inputs[inputs.length-nr][1];
		return v;
	}		
	
}
