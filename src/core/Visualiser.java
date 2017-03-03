package core;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import Maps.PrimitiveMap;

public class Visualiser {	
	PrimitiveMap map;
	Image thingsToDraw;
	
	public Visualiser(PrimitiveMap mp) {
		map = mp;
	    thingsToDraw = new Image();   
	}
	
    public  class Image extends JPanel{
        private Shape walls;
        private Shape realLocation; 
        private Shape particles;
        
        public Image() {
        	walls = map.getWalls();
        	realLocation = map.getRealLocation();  
        	particles = map.getParticles();
        }
       
        @Override
        public void paint(Graphics g) {
        	super.paint(g);
            Graphics2D drawer = (Graphics2D)g;
            drawer.draw(walls);
            drawer.setColor(Color.BLUE);
            drawer.draw(particles);
            drawer.setColor(Color.RED);
            drawer.draw(realLocation);
        }        

        public void updateRealLocation() {
        	realLocation = map.getRealLocation();
        }
        
        public void updateParticles() {
        	particles = map.getParticles();
        }
    }
 
    
    public  void animate(){
        JFrame frame = new JFrame();
        frame.add(thingsToDraw);        
        Timer timer = new Timer(1000/600,new Redraw());
        timer.start();
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public  class Redraw implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
        	thingsToDraw.updateRealLocation();
        	thingsToDraw.updateParticles();
        	thingsToDraw.repaint();
        }
    }
	
	public  void visualise() {
	      javax.swing.SwingUtilities.invokeLater(new Runnable(){
	            @Override
	            public void run(){
	                animate();
	           }
	        });		
	}
	
}
