package core;

import org.apache.commons.math3.ml.clustering.Clusterable;

import Particles.Particle;

public class ClusterParticleWrapper implements Clusterable {
	 private double[] points;
	    private Particle particle;

	    public ClusterParticleWrapper(Particle particle) {
	        this.particle = particle;
	        this.points = new double[] { particle.getX(), particle.getY()};
	    }

	    public Particle getParticle() {
	        return particle;
	    }

	    public double[] getPoint() {
	        return points;
	    }
	
}
