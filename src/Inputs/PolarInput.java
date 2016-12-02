package Inputs;

public class PolarInput implements Input {
	 private double[][]inputs = {{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},
			{100,270},{100,0},{100,0},{100,0},
			{100,270},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{110,0},{30,180}, {10,180},
			{100,270},{100,0},{100,0},{100,0}};
	
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
