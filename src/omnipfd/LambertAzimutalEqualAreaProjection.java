package omnipfd;

import java.awt.geom.Point2D;

import javax.vecmath.Vector3d;

public class LambertAzimutalEqualAreaProjection {

	public Point2D project(Vector3d v,Quaternion q) {
		double x = v.x;
		double y = v.y;
		double z = v.z;

		double a = q.W;
		double b = q.X;
		double c = q.Y;
		double d = q.Z;
		
		
		double denom = Math.sqrt(Math.pow(Math.abs(-(b*(-(c*x) + b*y + a*z)) + a*(d*x + a*y - b*z) + d*(a*x - d*y + c*z) - c*(-(b*x) - c*y - d*z)),2) + Math.pow(Math.abs(a*(-(c*x) + b*y + a*z) + b*(d*x + a*y - b*z) - c*(a*x - d*y + c*z) - d*(-(b*x) - c*y - d*z)),2));
		
		if(Math.abs(denom)<Double.MIN_NORMAL) {
			return new Point2D.Double(0,0);
		}else {
		
		return new Point2D.Double(
				((-(b*(-(c*x) + b*y + a*z)) + a*(d*x + a*y - b*z) + d*(a*x - d*y + c*z) - c*(-(b*x) - c*y - d*z))*Math.sqrt(Math.pow(Math.abs(-1 + c*(-(c*x) + b*y + a*z) - d*(d*x + a*y - b*z) + a*(a*x - d*y + c*z) - b*(-(b*x) - c*y - d*z)),2) + Math.pow(Math.abs(-(b*(-(c*x) + b*y + a*z)) + a*(d*x + a*y - b*z) + d*(a*x - d*y + c*z) - c*(-(b*x) - c*y - d*z)),2) + Math.pow(Math.abs(a*(-(c*x) + b*y + a*z) + b*(d*x + a*y - b*z) - c*(a*x - d*y + c*z) - d*(-(b*x) - c*y - d*z)),2)))/denom
				,
				((a*(-(c*x) + b*y + a*z) + b*(d*x + a*y - b*z) - c*(a*x - d*y + c*z) - d*(-(b*x) - c*y - d*z))   *Math.sqrt(Math.pow(Math.abs(-1 + c*(-(c*x) + b*y + a*z) - d*(d*x + a*y - b*z) + a*(a*x - d*y + c*z) - b*(-(b*x) - c*y - d*z)),2) + Math.pow(Math.abs(-(b*(-(c*x) + b*y + a*z)) + a*(d*x + a*y - b*z) + d*(a*x - d*y + c*z) - c*(-(b*x) - c*y - d*z)),2) + Math.pow(Math.abs(a*(-(c*x) + b*y + a*z) + b*(d*x + a*y - b*z) - c*(a*x - d*y + c*z) - d*(-(b*x) - c*y - d*z)),2)))/denom
				);
		}
		
	}
}
