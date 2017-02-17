package core;

import Filters.BasicSkeletonFilter;
import Filters.PolarFilter;
import Inputs.Input;
import Inputs.PolarInput;
import Maps.FastCrossMap;
import Maps.FirstSkeleton;
import Maps.Map;
import Maps.SkeletonMap;

public class Main {
	
	static public Map map =  new FastCrossMap();
	static public SkeletonMap skeleMap = new FirstSkeleton();
	static public PolarFilter filter = new PolarFilter();	
	static public BasicSkeletonFilter skeleFilter = new BasicSkeletonFilter();
	static public Input inputGenerator = new PolarInput();

	public static void main(String[] args) {			
		new Visualiser(skeleMap).visualise();
		while(inputGenerator.hasInputs()) {
			try{Thread.sleep(2000);}
			catch(Exception e){};
			skeleFilter.performStep();			
		}		
	}
}
