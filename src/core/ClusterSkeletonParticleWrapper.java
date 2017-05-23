package core;

import org.apache.commons.math3.ml.clustering.Clusterable;

import Particles.Particle;
import Particles.SkeletonParticle;

public class ClusterSkeletonParticleWrapper implements Clusterable {
	 private double[] points;
	    private SkeletonParticle particle;

	    public ClusterSkeletonParticleWrapper(SkeletonParticle particle) {
	        this.particle = particle;
	        this.points = new double[] { particle.getX(), particle.getY()};
	    }

	    public SkeletonParticle getParticle() {
	        return particle;
	    }

	    public double[] getPoint() {
	        return points;
	    }
	
}
