package omnipfd;


import java.awt.geom.Point2D;

import com.sun.javafx.geom.Vec3d;

public interface Projection {


	public Vec3d invert(Point2D pt) ;

	public Point2D project(Vec3d vec) ;
	
}
