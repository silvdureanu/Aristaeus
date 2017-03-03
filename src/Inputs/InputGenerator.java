package Inputs;

public class InputGenerator implements Input {
	 private double[][]inputs = {{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},
			{0,100},{0,100},{0,100},{0,100},
			{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},
			{0,-100},{0,-100},{0,-100},{0,-100},{0,-100}};
	
	 private int nr = inputs.length;
	
	public  boolean hasInputs() {
		return (nr>1);
	}
	
	
	public  double[] generateStepInput() {
			nr--;
			return inputs[inputs.length-nr];
	};
	
	public  double[] generateMapInput() {
		return inputs[inputs.length-nr];
	}
	
	public double[] generateRealInput() {
		double[] v = {0,0};
		v[0]+=inputs[inputs.length-nr][0];
		v[1]+=inputs[inputs.length-nr][1];
		return v;
	}	
	
}
