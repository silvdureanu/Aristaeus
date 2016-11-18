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
	
    public  class Image extends JPanel{
        private Shape walls;
        private Shape realLocation; 
        private Shape particles;
        
        public Image() {
        	walls = Main.map.getWalls();
        	realLocation = Main.map.getRealLocation();  
        	particles = Main.map.getParticles();
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
        	realLocation = Main.map.getRealLocation();
        }
        
        public void updateParticles() {
        	particles = Main.map.getParticles();
        }
    }
     Image thingsToDraw = new Image();    
    
    public  void animate(){
        JFrame frame = new JFrame();
        frame.add(thingsToDraw);        
        Timer timer = new Timer(1000/60,new Redraw());
        timer.start();
        frame.setSize(1280, 1024);
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
