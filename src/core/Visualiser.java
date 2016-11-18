package core;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;




public class Visualiser {	
	
    public static class Image extends JPanel{
        private Shape walls;
        private Shape realLocation; 
        private Shape particles;
        
        public Image() {
        	walls = Map.getWalls();
        	realLocation = Map.getRealLocation();  
        	particles = Map.getParticles();
        }
       
        @Override
        public void paint(Graphics g) {
        	super.paint(g);
            Graphics2D drawer = (Graphics2D)g;

            drawer.draw(walls);
            drawer.setColor(Color.RED);
            drawer.draw(realLocation);
            drawer.setColor(Color.BLUE);
            drawer.draw(particles);
        }        

        public void updateRealLocation() {
        	realLocation = Map.getRealLocation();
        }
        
        public void updateParticles() {
        	particles = Map.getParticles();
        }
    }
    static Image thingsToDraw = new Image();    
    
    public static void animate(){
        JFrame frame = new JFrame();
        frame.add(thingsToDraw);        
        Timer timer = new Timer(1000/60,new Redraw());
        timer.start();
        frame.setSize(1280, 1024);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static class Redraw implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent arg0) {
        	thingsToDraw.updateRealLocation();
        	thingsToDraw.updateParticles();
        	thingsToDraw.repaint();
        }
    }
	
	public static void visualise() {
	      javax.swing.SwingUtilities.invokeLater(new Runnable(){
	            @Override
	            public void run(){
	                animate();
	           }
	        });		
	}
	
}
