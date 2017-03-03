package Inputs;

public class HeadingInput implements Input {
	 private double[][]inputs = {{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},
			{100,1},{100,0},{100,0},{100,0},
			{100,1},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{50,0},
			{100,1},{100,0},{100,0},{100,0}};
	
	 private int nr = inputs.length;
	
	public  boolean hasInputs() {
		return (nr>0);
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
