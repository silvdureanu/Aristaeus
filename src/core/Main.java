package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

import Filters.PolarFilter;
import Filters.wtfskelfilter;
import Inputs.Input;
import Inputs.LeftTurn;
import Maps.Map;
import Maps.SkeletonMap;
import Maps.UltimateMap;
import Maps.UltimateSkeleton;
import Maps.WGBParser;
import Particles.Particle;
import Particles.SkeletonParticle;


public class Main {
	static public WGBParser wgb = new WGBParser();
	static public Map map =  new UltimateMap(5.5,5.5,270);
	static public SkeletonMap skeleMap = new UltimateSkeleton(5.5,5.5,270);
	static public PolarFilter filter = new PolarFilter();
	static public wtfskelfilter skeleFilter = new wtfskelfilter();
	static public Input inputGenerator = new LeftTurn();

	public static void main(String[] args) {
		BufferedWriter bw = null;

		new Visualiser(skeleMap).visualise();
		
		
		
		try {
			 File file = new File("2d.txt");
	    	  FileWriter fw = new FileWriter(file);
	    	  bw = new BufferedWriter(fw);
	    	  
	    	  double nr = 0;
	    	  double tot=0;
	    	for (int q=0; q<1; q++) { 
	    		inputGenerator = new LeftTurn();
	    		skeleFilter = new wtfskelfilter();
			while(inputGenerator.hasInputs()) {	
				//try{Thread.sleep(100);}
				//catch(Exception e){};
				long startTime = System.nanoTime();
				skeleFilter.performStep();		
				long endTime = System.nanoTime();
				List<SkeletonParticle> l = skeleFilter.getParticles();
				List<ClusterSkeletonParticleWrapper> clusterInput = new ArrayList<ClusterSkeletonParticleWrapper>(l.size());
				for(SkeletonParticle particle: l)
					clusterInput.add(new ClusterSkeletonParticleWrapper(particle));
				
				DBSCANClusterer<ClusterSkeletonParticleWrapper> clusterer = new DBSCANClusterer<ClusterSkeletonParticleWrapper>(0.2,300);
				List<Cluster<ClusterSkeletonParticleWrapper>> clusterResults = clusterer.cluster(clusterInput);
				double min = 20;
				for(Cluster c:clusterResults) {
					double acc=((UltimateMap)map).getAccuracy(c);
					if(acc<min)
						min=acc;
				}
				
				tot++;
				bw.write(Double.toString(tot));
				bw.write(' ');
				bw.write(Double.toString(min));
				bw.write('\n');
				System.out.println(min);
				//System.out.println(clusterResults.size());
				
				
				long duration = (endTime - startTime)/1000000;
				
			
				//System.out.println(Math.log(wallchecks)/Math.log(duration));
				
			}
	    	}
		
         } catch (IOException ioe) {
   	   ioe.printStackTrace();
   	}
   	finally
   	{ 
   	   try{
   	      if(bw!=null)
   		 bw.close();
   	   }catch(Exception ex){
   	       System.out.println("Error in closing the BufferedWriter"+ex);
   	    }
   	}

	}
	
}