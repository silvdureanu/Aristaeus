package core;

import Filters.DirectionalFilter;
import Filters.Filter;
import Inputs.HeadingInput;
import Inputs.Input;
import Maps.Map;
import Maps.SquareDonutMap;

public class Main {
	
	static public Map map =  new SquareDonutMap();
	static public Filter filter = new DirectionalFilter();	
	static public Input inputGenerator = new HeadingInput();

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
