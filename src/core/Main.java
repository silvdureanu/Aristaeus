package core;

import Filters.*;
import Inputs.Input;
import Inputs.*;
import Maps.*;


public class Main {
	static public WGBParser wgb = new WGBParser();
	static public Map map =  new UltimateMap();
	static public SkeletonMap skeleMap = new FirstSkeleton();
	static public PolarFilter pfilter = new PolarFilter();
	static public ParallelFilter filter = new ParallelFilter();	
	static public BasicSkeletonFilter skeleFilter = new BasicSkeletonFilter();
	static public Input inputGenerator = new StraightInput();

	public static void main(String[] args) {	
		
		double paratot=0;
		int pararuns=0;
		
		double polatot = 0;
		int polaruns = 0;

		//new Visualiser(map).visualise();
		Filter fil;
		for(int i=0; i<20; i++) {
			System.out.println(i);
			fil = new PolarFilter();
			inputGenerator = new StraightInput();
			while(inputGenerator.hasInputs()) {	
				//try{Thread.sleep(1000);}
				//catch(Exception e){};
				long startTime = System.currentTimeMillis();
				fil.performStep();		
				long endTime = System.currentTimeMillis();
				polatot+=(endTime-startTime);
				polaruns++;
				
			}	
			
			
			fil = new ParallelFilter();
			inputGenerator = new StraightInput();
			while(inputGenerator.hasInputs()) {	
				//try{Thread.sleep(1000);}
				//catch(Exception e){};
				long startTime = System.currentTimeMillis();
				fil.performStep();		
				long endTime = System.currentTimeMillis();
				paratot+=(endTime-startTime);
				pararuns++;
				
			}				
			
		}
		System.out.println(polatot/polaruns);
		System.out.println(paratot/pararuns);
	}
}
