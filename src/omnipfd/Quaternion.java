package omnipfd;
import javax.vecmath.Vector3d;


/***************************************************************************
 * Quaternion class written by BlackAxe / Kolor aka Laurent Schmalen in 1997
 * Translated to Java(with ProcesMath.sing) by RangerMauve in 2012
 * this class is freeware. you are fully allowed to use this class in non-
 * commercial products. Use in commercial environment is strictly prohibited
 */

public class Quaternion {
	public  double W, X, Y, Z;      // components of a quaternion

	// default constructor
	public Quaternion() {
		W = 1.0;
		X = 0.0;
		Y = 0.0;
		Z = 0.0;
	}

	// initialized constructor

	public Quaternion(double w, double x, double y, double z) {
		W = w;
		X = x;
		Y = y;
		Z = z;
	}

	// quaternion multiplication
	public Quaternion mult (Quaternion q) {
		double w = W*q.W - (X*q.X + Y*q.Y + Z*q.Z);

		double x = W*q.X + q.W*X + Y*q.Z - Z*q.Y;
		double y = W*q.Y + q.W*Y + Z*q.X - X*q.Z;
		double z = W*q.Z + q.W*Z + X*q.Y - Y*q.X;

		W = w;
		X = x;
		Y = y;
		Z = z;
		return this;
	}
	
	
	
	// quaternion multiplication
	public Quaternion leftmult (Quaternion q) {
		double w = q.W*W - (q.X*X + q.Y*Y + q.Z*Z);

		double x = q.W*X + W*q.X + q.Y*Z - q.Z*Y;
		double y = q.W*Y + W*q.Y + q.Z*X - q.X*Z;
		double z = q.W*Z + W*q.Z + q.X*Y - q.Y*X;

		W = w;
		X = x;
		Y = y;
		Z = z;
		return this;
	}

	// conjugates the quaternion
	public Quaternion conjugate () {
		X = -X;
		Y = -Y;
		Z = -Z;
		return this;
	}

	// inverts the quaternion
	public Quaternion reciprical () {
		double norme = Math.sqrt(W*W + X*X + Y*Y + Z*Z);
		if (norme == 0.0)
			norme = 1.0;

		double recip = 1.0 / norme;

		W =  W * recip;
		X = -X * recip;
		Y = -Y * recip;
		Z = -Z * recip;

		return this;
	}

	// sets to unit quaternion
	public Quaternion normalize() {
		double norme = Math.sqrt(W*W + X*X + Y*Y + Z*Z);
		if (norme == 0.0)
		{
			W = 1.0; 
			X = Y = Z = 0.0;
		}
		else
		{
			double recip = 1.0/norme;

			W *= recip;
			X *= recip;
			Y *= recip;
			Z *= recip;
		}
		return this;
	}

	// Makes quaternion from axis
	public Quaternion fromAxis(double Angle, double x, double y, double z) { 
		double omega, s, c;

		s = Math.sqrt(x*x + y*y + z*z);

		if (Math.abs(s) > Float.MIN_VALUE)
		{
			c = 1.0/s;

			x *= c;
			y *= c;
			z *= c;

			omega = -0.5f * Angle;
			s = (float)Math.sin(omega);

			X = s*x;
			Y = s*y;
			Z = s*z;
			W = (float)Math.cos(omega);
		}
		else
		{
			X = Y = 0.0f;
			Z = 0.0f;
			W = 1.0f;
		}
		normalize();
		return this;
	}

	public Quaternion fromAxis(double Angle, Vector3d axis) {
		return this.fromAxis(Angle, axis.x, axis.y, axis.z);
	}

	// Rotates towards other quaternion
	public void slerp(Quaternion a, Quaternion b, double t)
	{
		double omega, cosom, sinom, sclp, sclq;



		cosom = a.X*b.X + a.Y*b.Y + a.Z*b.Z + a.W*b.W;


		if ((1.0f+cosom) > Float.MIN_VALUE)
		{
			if ((1.0f-cosom) > Float.MIN_VALUE)
			{
				omega = Math.acos(cosom);
				sinom = Math.sin(omega);
				sclp = Math.sin((1.0f-t)*omega) / sinom;
				sclq = Math.sin(t*omega) / sinom;
			}
			else
			{
				sclp = 1.0f - t;
				sclq = t;
			}

			X = sclp*a.X + sclq*b.X;
			Y = sclp*a.Y + sclq*b.Y;
			Z = sclp*a.Z + sclq*b.Z;
			W = sclp*a.W + sclq*b.W;
		}
		else
		{
			X =-a.Y;
			Y = a.X;
			Z =-a.W;
			W = a.Z;

			sclp = Math.sin((1.0f-t) * Math.PI * 0.5);
			sclq = Math.sin(t * Math.PI * 0.5);

			X = sclp*a.X + sclq*b.X;
			Y = sclp*a.Y + sclq*b.Y;
			Z = sclp*a.Z + sclq*b.Z;
		}
	}

	public Quaternion exp()
	{                               
		double Mul;
		double Length = Math.sqrt(X*X + Y*Y + Z*Z);

		if (Length > 1.0e-4)
			Mul = Math.sin(Length)/Length;
		else
			Mul = 1.0;

		W = Math.cos(Length);

		X *= Mul;
		Y *= Mul;
		Z *= Mul; 

		return this;
	}

	public Quaternion log()
	{
		double Length;

		Length = Math.sqrt(X*X + Y*Y + Z*Z);
		Length = Math.atan(Length/W);

		W = 0.0;

		X *= Length;
		Y *= Length;
		Z *= Length;

		return this;
	}
	
public Quaternion rotationFromTo( Vector3d from, Vector3d to)
	 {
	     // Based on Stan Melax's article in Game Programming Gems
	     // Copy, since cannot modify local
			Vector3d v0 = from;
			Vector3d v1 = to;
	     v0.normalize();
	     v1.normalize();
	 
	     double d = v0.dot(v1);
	     if (d >= 1.0) // If dot == 1, vectors are the same
	     {
	         return new Quaternion(1.,0.,0.,0.);
	     }
	     else if (d <= -1.0) // exactly opposite
	     {
	    	 Vector3d axis = new Vector3d(1.0, 0., 0.);
	         axis.cross(axis, v0);
	         if (axis.length()==0)
	         {
	             axis.set(0.,1.,0.);
	             axis.cross(axis,v0);
	         }
	         // same as fromAngleAxis(core::PI, axis).normalize();
	         Quaternion res = new Quaternion(0,axis.x, axis.y, axis.z);
	         res.normalize();
	         return res;
	     }
	     
	     double s = Math.sqrt( (1+d)*2 ); // optimize inv_sqrt
	     double invs = 1. / s;
	    
	     Vector3d c = new Vector3d();
	     c.cross(v0, v1);
	     c.scale(invs);
	     Quaternion res = new Quaternion(s * 0.5,c.x, c.y, c.z );
	     res.normalize();
	     return res;
	 }

@Override
public String toString() {
	return "Quaternion [" + W + ", " + X + ", " + Y + ", " + Z + "]";
}



//inline void quaternion::toEuler(vector3df& euler) const
//00597 {
//00598     const f64 sqw = W*W;
//00599     const f64 sqx = X*X;
//00600     const f64 sqy = Y*Y;
//00601     const f64 sqz = Z*Z;
//00602     const f64 test = 2.0 * (Y*W - X*Z);
//00603 
//00604     if (core::equals(test, 1.0, 0.000001))
//00605     {
//00606         // heading = rotation about z-axis
//00607         euler.Z = (f32) (-2.0*atan2(X, W));
//00608         // bank = rotation about x-axis
//00609         euler.X = 0;
//00610         // attitude = rotation about y-axis
//00611         euler.Y = (f32) (core::PI64/2.0);
//00612     }
//00613     else if (core::equals(test, -1.0, 0.000001))
//00614     {
//00615         // heading = rotation about z-axis
//00616         euler.Z = (f32) (2.0*atan2(X, W));
//00617         // bank = rotation about x-axis
//00618         euler.X = 0;
//00619         // attitude = rotation about y-axis
//00620         euler.Y = (f32) (core::PI64/-2.0);
//00621     }
//00622     else
//00623     {
//00624         // heading = rotation about z-axis
//00625         euler.Z = (f32) atan2(2.0 * (X*Y +Z*W),(sqx - sqy - sqz + sqw));
//00626         // bank = rotation about x-axis
//00627         euler.X = (f32) atan2(2.0 * (Y*Z +X*W),(-sqx - sqy + sqz + sqw));
//00628         // attitude = rotation about y-axis
//00629         euler.Y = (f32) asin( clamp(test, -1.0, 1.0) );
//00630     }
//00631 }
	 
};
