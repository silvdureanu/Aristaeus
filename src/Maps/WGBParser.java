package Maps;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WGBParser {
	
	
	static List<double[]> groundFloor = new ArrayList<double[]>();
	static List<double[]> firstFloor = new ArrayList<double[]>();
	static List<double[]> secondFloor = new ArrayList<double[]>();
	
	
	static void addSegment(int h, double x1, double y1, double x2, double y2) {
		List<double[]> floor;
		if(h==0)
			floor=groundFloor;
		else if(h==1)
			floor=firstFloor;
		else
			floor=secondFloor;
		
		x1*=13; //scale for display size and precision when truncating to int
		x2*=13;
		y1*=13;
		y2*=13;
		
		int xx1 = (int)x1; // turn into int for display on pixels
		int xx2=(int)x2;
		int yy1=(int)y1;
		int yy2=(int)y2;
		
		
		floor.add(new double[]{y1,1040-x1,y2,1040-x2}); // swap x with Y, to fit on horizontal screen...
		// TODO change to give PROPER info to map, and map does the adaptation to the visualizer...
		
		
	}
	
	public static void parse() {
		try {			
			Scanner in = new Scanner(WGBParser.class.getResourceAsStream("MapData.wgb"));
			
		int i=-1;
		String s;
		double firstX,firstY,prevX,prevY;
		firstX=firstY=prevX=prevY=0; // pleasing the compiler
		int currentHeight = 0;
		boolean doorway = false;
		while(in.hasNext()) {			
			s = in.next();
			i++;
			if(i>5) {
				if(s.equals("ROOM")) {
					if(i!=6)
						addSegment(currentHeight, prevX,prevY,firstX,firstY);
					if(doorway)
						doorway = false;
					s=in.next(); //roomID
					s=in.next(); //human name;
					s=in.next(); //floor
					currentHeight = Integer.parseInt(s);
					s=in.next(); // first X
					firstX = Double.parseDouble(s);
					s=in.next(); // first Y
					firstY = Double.parseDouble(s);
					prevX=firstX;
					prevY=firstY; // establish invariant
					continue;
				}
				
				if(s.equals("-"))
					continue;
				
				if(s.length()<4) { // this means it's a room ID
					doorway = true;
					continue;
				}
				double xval = Double.parseDouble(s);
				s=in.next();
				double yval = Double.parseDouble(s);
				
				if(!doorway)
				addSegment(currentHeight, prevX,prevY,xval,yval);
				else if(doorway)
					doorway = false;
				
				prevX = xval;
				prevY = yval;			
			}		
		}
		
			addSegment(currentHeight, prevX,prevY,firstX,firstY);

		in.close();
		}
		catch(Exception e) {
			System.out.println("something went wrong");
		}
	}

	public static double[][] getSegs(int level) {
		List<double[]> floor;
		if(level==0)
			floor=groundFloor;
		else if(level==1)
			floor=firstFloor;
		else
			floor=secondFloor;		
		
		double[][] v = new double[floor.size()][4];		
		for(int i=0; i<floor.size(); i++) {
			v[i] = floor.get(i);
		}
		
		return v;
	}
	
	public WGBParser() {
		parse();
	}
	
}
