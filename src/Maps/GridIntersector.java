package Maps;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class GridIntersector {

	private List<Line2D> gridSegments[][];
	private Rectangle2D grid[][];
	private int nrx,nry,mx,my;
	private double cellwidth, cellheight;
	
	public GridIntersector(int x, int y,int maxx,int maxy,int[][] segs) {
		nrx = x;
		nry = y;
		mx = maxx;
		my = maxy;
		cellwidth = (double)mx / nrx;
		cellheight = (double)my / nry;
		
		gridSegments = new ArrayList[nrx][nry];
		grid = new Rectangle2D[nrx][nry];		

		//build set of lines intersecting each cell
		for(int i = 0; i<nrx; i++)
			for(int j=0; j<nry; j++) {
				grid[i][j] = new Rectangle2D.Double(i*cellwidth,j*cellheight,cellwidth,cellheight);
				gridSegments[i][j] = new ArrayList<Line2D>();
				
				for(int l=0; l<segs.length; l++) {
					Line2D cl = new Line2D.Double(segs[l][1],segs[l][0],segs[l][3],segs[l][2]);
					if(grid[i][j].intersectsLine(cl)) {
							gridSegments[i][j].add(cl);
					}
				}
				
			}	
	}
	
	
	
	
	public GridIntersector(int x, int y,int maxx,int maxy,double[][] segs) {
		nrx = x;
		nry = y;
		mx = maxx;
		my = maxy;
		cellwidth = (double)mx / nrx;
		cellheight = (double)my / nry;
		
		gridSegments = new ArrayList[nrx][nry];
		grid = new Rectangle2D[nrx][nry];		

		//build set of lines intersecting each cell
		for(int i = 0; i<nrx; i++)
			for(int j=0; j<nry; j++) {
				grid[i][j] = new Rectangle2D.Double(i*cellwidth,j*cellheight,cellwidth,cellheight);
				gridSegments[i][j] = new ArrayList<Line2D>();
				
				for(int l=0; l<segs.length; l++) {
					Line2D cl = new Line2D.Double(segs[l][0],segs[l][1],segs[l][2],segs[l][3]);
					if(grid[i][j].intersectsLine(cl)) {
							gridSegments[i][j].add(cl);
					}
				}
				
			}	
	}
	
	public boolean crossesWall(double x1, double y1, double x2, double y2) {
		Line2D line = new Line2D.Double(x1,y1,x2,y2);
		
		int i1 = (int)(x1/cellwidth);
		int j1 = (int)(y1/cellheight);
		
		int i2 = (int)(x2/cellwidth);
		int j2 = (int)(y2/cellheight);
		
		//pretty much guaranteed not to differ by >1
		//check up to all 4 possible boxes, just in case  the 3-square corner case happens
		for (int i=Math.min(i1,i2); i<=Math.max(i1,i2); i++)
			for (int j=Math.min(j1,j2); j<=Math.max(j1,j2); j++) {
				for (Line2D l : gridSegments[i][j])
					if(l.intersectsLine(line))
						return true;
			}
		return false;
	}
}
