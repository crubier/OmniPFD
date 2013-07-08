package projections;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

public interface Projection {

	public Point2D project(Point2D coord);
	public Path2D project(Path2D path) ;
	
	public Point2D inverse(Point2D pos);
	public Path2D inverse(Path2D path);
	
	public double getPhi() ;

	public void setPhi(double phi);

	public double getLambda() ;

	public void setLambda(double lambda) ;
	
}
