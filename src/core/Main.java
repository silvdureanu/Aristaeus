package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Filters.BasicFilter;
import Filters.Filter;
import Inputs.Input;
import Inputs.InputGenerator;
import Maps.Map;
import Maps.SquareDonutMap;

public class Main {
	
	static public Map map =  new SquareDonutMap();
	static public Filter filter = new BasicFilter();	
	static public Input inputGenerator = new InputGenerator();

	public static void main(String[] args) {
		Particle.seedParticles();		
		Visualiser visualiser = new Visualiser();		
		visualiser.visualise();		
		while(inputGenerator.hasInputs()) {
			try{Thread.sleep(1000);}
			catch(Exception e){};
			filter.performStep();			
		}		
	}
}
