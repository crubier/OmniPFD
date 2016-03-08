package omnipfd;

import java.awt.geom.Point2D;

import com.sun.javafx.geom.Vec3d;

public class LambertAzimutalEqualAreaProjection  implements Projection{
	
	public Vec3d invert(Point2D pt) {
		double x=pt.getX();
		double y=pt.getY();
				
		return new Vec3d((2 - x*x - y*y)/2.,(x*Math.sqrt(4 - x*x - y*y))/2.,(y*Math.sqrt(4 - x*x - y*y))/2.);
	}

	public Point2D project(Vec3d vec) {
		
		double x=vec.x;
		double y=vec.y;
		double z=vec.z;
		
		double norm = Math.sqrt(x*x +y*y+z*z);
		
		if(Math.abs(norm)<= Double.MIN_VALUE) {
			return new Point2D.Double(0, 0);
		}
		
		x/=norm;
		y/=norm;
		z/=norm;
		
		double n1 = (-1 + x)*(-1 + x) + y*y + z*z;
		if(Math.abs(n1)<= Double.MIN_VALUE) {
			return new Point2D.Double(0, 0);
		}
		
		double n2 = Math.sqrt((y*y + z*z)/n1);
		if(Math.abs(n1)<= Double.MIN_VALUE) {
			return new Point2D.Double(0, 0);
		}
		return new Point2D.Double(y/n2,z/n2);

		
	}
	
}
