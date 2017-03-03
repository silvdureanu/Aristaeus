package core;

import Filters.BasicSkeletonFilter;
import Filters.PolarFilter;
import Inputs.Input;
import Inputs.StraightInput;
import Maps.Map;
import Maps.SkeletonMap;
import Maps.UltimateMap;
import Maps.UltimateSkeleton;
import Maps.WGBParser;


public class Main {
	static public WGBParser wgb = new WGBParser();
	static public Map map =  new UltimateMap(10,50);
	static public SkeletonMap skeleMap = new UltimateSkeleton(10,10);
	static public PolarFilter filter = new PolarFilter();
	static public BasicSkeletonFilter skeleFilter = new BasicSkeletonFilter();
	static public Input inputGenerator = new StraightInput();

	public static void main(String[] args) {	
		
		new Visualiser(skeleMap).visualise();
			while(inputGenerator.hasInputs()) {	
				try{Thread.sleep(100);}
				catch(Exception e){};
				skeleFilter.performStep();		
			}	
			
			
	}
	
}