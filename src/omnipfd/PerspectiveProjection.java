package omnipfd;

import java.awt.geom.Point2D;

import com.sun.javafx.geom.Vec3d;

public class PerspectiveProjection implements Projection {

	private double d;

	public static final PerspectiveProjection STEREOGRAPHIC_PROJECTION = new PerspectiveProjection(0.);
	public static final PerspectiveProjection GNOMONIC_PROJECTION = new PerspectiveProjection(1.);
	public static final PerspectiveProjection ORTHOGRAPHIC_PROJECTION = new PerspectiveProjection(Double.MAX_VALUE);

	public PerspectiveProjection() {
		this.d = 0;
	}

	public PerspectiveProjection(double d) {
		super();
		this.d = d;
	}

	public Vec3d invert(Point2D pt) {

		double px = pt.getX();
		double py = pt.getY();

		double n = Math.sqrt(px * px + py * py);
		double a = Math.atan2(py,px);

		if (n <= 0) {
			return new Vec3d(-1, 0, 0);
		} else {

			double r1 = -((1 + d) * n * n * (-1 - n * n + d * (-1 + n * n)));

			if (r1 <= 0) {
				return null;
			} else {
				double s1 = Math.sqrt(r1);

				double den1 = (1 + 2 * d + d * d + n * n);

				if (den1 == 0) {
					return null;
				} else {

					double x1 = (s1 + d * (n * n * n + s1)) / (n * den1);
					double y1 = (d * n + d * d * n - s1) / den1;
	
					double x2 = (-s1 + d * (n * n * n - s1)) / (n * den1);
					double y2 = (d * n + d * d * n + s1) / den1;
					
					if(x1<x2) {
						return new Vec3d(x1, y1*Math.cos(a), y1*Math.sin(a));
					} else {
						return new Vec3d(x2, y2*Math.cos(a), y2*Math.sin(a));
					}
				
				}
			}

		}

		// return new Vec3d(x,y,z);
	}

	public Point2D project(Vec3d vec) {

		double x = vec.x;
		double y = vec.y;
		double z = vec.z;

		double norm = Math.sqrt(x * x + y * y + z * z);

		if (Math.abs(norm) <= Double.MIN_VALUE) {
			return new Point2D.Double(0, 0);
		}

		x /= norm;
		y /= norm;
		z /= norm;

		return new Point2D.Double((1 + d - x) * 2 * y, (1 + d - x) * 2 * z);

	}

}
