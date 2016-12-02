package core;

import Filters.Filter;
import Filters.PolarFilter;
import Inputs.Input;
import Inputs.PolarInput;
import Maps.FastCrossMap;
import Maps.Map;

public class Main {
	
	static public Map map =  new FastCrossMap();
	static public Filter filter = new PolarFilter();	
	static public Input inputGenerator = new PolarInput();

	public static void main(String[] args) {
		Particle.seedParticles();		
		new Visualiser().visualise();	
		while(inputGenerator.hasInputs()) {
			//try{Thread.sleep(1000);}
			//catch(Exception e){};
			filter.performStep();			
		}		
	}
}
