package core;

public class InputGenerator {
	 private double[][]inputs = {{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},
			{0,100},{0,100},{0,100},{0,100},
			{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},
			{0,-100},{0,-100},{0,-100},{0,-100},{0,-100}};
	
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
	
}
