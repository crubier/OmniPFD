package projections;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

public class PerspectiveProjection implements Projection {

	private double phi0;
	private double lambda0;
	private double d;
	
	public static final PerspectiveProjection STEREOGRAPHIC_PROJECTION = new PerspectiveProjection(-1.);
	public static final PerspectiveProjection GNOMONIC_PROJECTION = new PerspectiveProjection(0.);
	public static final PerspectiveProjection ORTHOGRAPHIC_PROJECTION = new PerspectiveProjection(Double.MAX_VALUE);
	

	public PerspectiveProjection(double d) {
		super();
		this.d = d;
	}

	public Point2D project(Point2D coord) {
		double phi = coord.getX();
		double lambda = coord.getY();
		double k = Math.cos(phi - this.phi0) + Math.cos(this.phi0)* Math.cos(phi)*(Math.cos(lambda - this.lambda0) - 1);
		double alpha = (d-1.)/(d-k);
		double x = alpha * Math.cos(phi) * Math.sin(lambda - this.lambda0);
		double y = alpha
				* (Math.sin(phi - this.phi0) - Math.sin(this.phi0)
						* Math.cos(phi) * (Math.cos(lambda - this.lambda0) - 1));
		Point2D res = new Point2D.Double(x, y);
		return res;
	}
	
	@Override
	public Point2D inverse(Point2D pos) {
		// TODO Auto-generated method stub
		return null;
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

	public Path2D project(Path2D path) {
		Path2D res = new Path2D.Double();
		PathIterator pathIter = path.getPathIterator(null);

		double[] coords = new double[6];
	

		Point2D currentP1;
		Point2D currentP2;
		Point2D currentP3;
		Point2D currentP1Projected;
		Point2D currentP2Projected;
		Point2D currentP3Projected;
		
		
		while (!pathIter.isDone()) {
			
			
			switch(pathIter.currentSegment(coords)) {
			case PathIterator.SEG_CUBICTO :
				currentP1 = new Point2D.Double(coords[0], coords[1]);
				currentP1Projected = this.project(currentP1);
				currentP2 = new Point2D.Double(coords[2], coords[3]);
				currentP2Projected = this.project(currentP2);
				currentP3 = new Point2D.Double(coords[4], coords[5]);
				currentP3Projected = this.project(currentP3);
				res.curveTo(currentP1Projected.getX(),currentP1Projected.getY(),currentP2Projected.getX(),currentP2Projected.getY(),currentP3Projected.getX(),currentP3Projected.getY());
				break;
			case PathIterator.SEG_LINETO :
				
				currentP1 = new Point2D.Double(coords[0], coords[1]);
				currentP1Projected = this.project(currentP1);
				
				res.lineTo(currentP1Projected.getX(),currentP1Projected.getY());
				
				break;
			case PathIterator.SEG_MOVETO :
				currentP1 = new Point2D.Double(coords[0], coords[1]);
				currentP1Projected = this.project(currentP1);
				
				res.moveTo(currentP1Projected.getX(),currentP1Projected.getY());
				break;
				
			case PathIterator.SEG_QUADTO :
				currentP1 = new Point2D.Double(coords[0], coords[1]);
				currentP1Projected = this.project(currentP1);
				currentP2 = new Point2D.Double(coords[2], coords[3]);
				currentP2Projected = this.project(currentP2);

				res.quadTo(currentP1Projected.getX(),currentP1Projected.getY(),currentP2Projected.getX(),currentP2Projected.getY());
				break;
			case PathIterator.SEG_CLOSE :
				res.closePath();
				break;
			default:
				break;
			}
			
			
			pathIter.next();
		}

//		res.closePath();

		return res;

	}

	@Override
	public Path2D inverse(Path2D path) {
		Path2D res = new Path2D.Double();
		PathIterator pathIter = path.getPathIterator(null);

		double[] coords = new double[6];
	

		Point2D currentP1;
		Point2D currentP2;
		Point2D currentP3;
		Point2D currentP1Projected;
		Point2D currentP2Projected;
		Point2D currentP3Projected;
		
		
		while (!pathIter.isDone()) {
			
			
			switch(pathIter.currentSegment(coords)) {
			case PathIterator.SEG_CUBICTO :
				currentP1 = new Point2D.Double(coords[0], coords[1]);
				currentP1Projected = this.inverse(currentP1);
				currentP2 = new Point2D.Double(coords[2], coords[3]);
				currentP2Projected = this.inverse(currentP2);
				currentP3 = new Point2D.Double(coords[4], coords[5]);
				currentP3Projected = this.inverse(currentP3);
				res.curveTo(currentP1Projected.getX(),currentP1Projected.getY(),currentP2Projected.getX(),currentP2Projected.getY(),currentP3Projected.getX(),currentP3Projected.getY());
				break;
			case PathIterator.SEG_LINETO :
				
				currentP1 = new Point2D.Double(coords[0], coords[1]);
				currentP1Projected = this.inverse(currentP1);
				
				res.lineTo(currentP1Projected.getX(),currentP1Projected.getY());
				
				break;
			case PathIterator.SEG_MOVETO :
				currentP1 = new Point2D.Double(coords[0], coords[1]);
				currentP1Projected = this.inverse(currentP1);
				
				res.moveTo(currentP1Projected.getX(),currentP1Projected.getY());
				break;
				
			case PathIterator.SEG_QUADTO :
				currentP1 = new Point2D.Double(coords[0], coords[1]);
				currentP1Projected = this.inverse(currentP1);
				currentP2 = new Point2D.Double(coords[2], coords[3]);
				currentP2Projected = this.inverse(currentP2);

				res.quadTo(currentP1Projected.getX(),currentP1Projected.getY(),currentP2Projected.getX(),currentP2Projected.getY());
				break;
			case PathIterator.SEG_CLOSE :
				res.closePath();
				break;
			default:
				break;
			}
			
			
			pathIter.next();
		}

//		res.closePath();

		return res;
	}

}
