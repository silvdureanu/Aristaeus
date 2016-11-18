package core;

public class InputGenerator {
	static private double[][]inputs = {{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},{100,0},
			{0,100},{0,100},{0,100},{0,100},
			{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},{-100,0},
			{0,-100},{0,-100},{0,-100},{0,-100},{0,-100}};
	
	static private int nr = inputs.length;
	
	public static boolean hasInputs() {
		return (nr>0);
	}
	
	
	public static double[] generateStepInput() {
			nr--;
			return inputs[inputs.length-nr];
	};
	
	public static double[] generateMapInput() {
		return inputs[inputs.length-nr];
	}
	
}
