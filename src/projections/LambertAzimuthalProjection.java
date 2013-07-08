package projections;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

public class LambertAzimuthalProjection implements Projection {
	
	private double phi0;
	private double lambda0;

	public Point2D project(Point2D coord) {
		double phi = coord.getX();
		double lambda = coord.getY();
		double alpha = Math.sqrt(2. / (1 + Math.cos(phi - this.phi0) + Math
				.cos(this.phi0)
				* Math.cos(phi)
				* (Math.cos(lambda - this.lambda0) - 1)));
		double x = alpha * Math.cos(phi) * Math.sin(lambda - this.lambda0);
		double y = alpha
				* (Math.sin(phi - this.phi0) - Math.sin(this.phi0)
						* Math.cos(phi) * (Math.cos(lambda - this.lambda0) - 1));
		Point2D res = new Point2D.Double(x, y);
		return res;
	}

	public Path2D project(Path2D path) {
		Path2D res = new Path2D.Double();
		PathIterator pathIter = path.getPathIterator(null);

		double[] coords = new double[6];
		pathIter.currentSegment(coords);
		Point2D currPt = new Point2D.Double(coords[0], coords[1]);
		Point2D transfPt = this.project(currPt);
		res.moveTo(transfPt.getX(), transfPt.getY());

		while (!pathIter.isDone()) {
			pathIter.currentSegment(coords);
			currPt = new Point2D.Double(coords[0], coords[1]);
			transfPt = this.project(currPt);
			res.lineTo(transfPt.getX(), transfPt.getY());
			pathIter.next();
		}

//		res.closePath();

		return res;

	}

	public double getPhi() {
		return this.phi0;
	}

	public void setPhi(double phi) {
		this.phi0 = phi;
	}

	public double getLambda() {
		return this.lambda0;
	}

	public void setLambda(double lambda) {
		this.lambda0 = lambda;
	}

	@Override
	public Point2D inverse(Point2D pos) {
		double x=pos.getX();
		double y=pos.getY();

		double rho=Math.sqrt(x*x+y*y);
		double c=2*Math.asin(0.5*rho);
		
		double phi=Math.asin(Math.sin(this.phi0)*Math.cos(c)+y*Math.cos(this.phi0)*Math.sin(c)/rho);
		double lambda=this.lambda0+Math.atan2(x*Math.sin(c), rho*Math.cos(this.phi0)*Math.cos(c)-y*Math.sin(this.phi0)*Math.sin(c));

		Point2D res = new Point2D.Double(((phi+Math.PI/2.)%Math.PI)-Math.PI/2., ((lambda+Math.PI)%(2.*Math.PI))-Math.PI);
		return res;
	}

	@Override
	public Path2D inverse(Path2D path) {
		Path2D res = new Path2D.Double();
		PathIterator pathIter = path.getPathIterator(null);

		double[] coords = new double[6];
		pathIter.currentSegment(coords);
		Point2D currPt = new Point2D.Double(coords[0], coords[1]);
		Point2D transfPt = this.project(currPt);
		res.moveTo(transfPt.getX(), transfPt.getY());

		while (!pathIter.isDone()) {
			pathIter.currentSegment(coords);
			currPt = new Point2D.Double(coords[0], coords[1]);
			transfPt = this.inverse(currPt);
			res.lineTo(transfPt.getX(), transfPt.getY());
			pathIter.next();
		}

//		res.closePath();

		return res;

	}

}
