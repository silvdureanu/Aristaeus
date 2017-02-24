package core;

import Filters.BasicSkeletonFilter;
import Filters.PolarFilter;
import Inputs.Input;
import Inputs.StraightInput;
import Maps.FastCrossMap;
import Maps.FirstSkeleton;
import Maps.Map;
import Maps.SkeletonMap;
import Maps.WGBParser;


public class Main {
	static public WGBParser wgb = new WGBParser();
	static public Map map =  new FastCrossMap();
	static public SkeletonMap skeleMap = new FirstSkeleton();
	static public PolarFilter filter = new PolarFilter();	
	static public BasicSkeletonFilter skeleFilter = new BasicSkeletonFilter();
	static public Input inputGenerator = new StraightInput();

	public static void main(String[] args) {	

		new Visualiser(map).visualise();
		while(inputGenerator.hasInputs()) {
			try{Thread.sleep(2000);}
			catch(Exception e){};
			filter.performStep();			
		}		
	}
}
