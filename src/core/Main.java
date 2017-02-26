package core;

import Filters.*;
import Inputs.Input;
import Inputs.*;
import Maps.*;


public class Main {
	static public WGBParser wgb = new WGBParser();
	static public Map map =  new UltimateMap();
	static public SkeletonMap skeleMap = new FirstSkeleton();
	static public PolarFilter filter = new PolarFilter();	
	static public BasicSkeletonFilter skeleFilter = new BasicSkeletonFilter();
	static public Input inputGenerator = new StraightInput();

	public static void main(String[] args) {	

		new Visualiser(map).visualise();
		while(inputGenerator.hasInputs()) {
			//try{Thread.sleep(1000);}
			//catch(Exception e){};
			filter.performStep();			
		}		
	}
}
