package omnipfd;

import java.awt.geom.Point2D;

import javax.vecmath.Vector3d;

public class LambertAzimutalEqualAreaProjection {
	
	public Vector3d invert(Point2D pt) {
		double x0=pt.getX();
		double y0=pt.getY();
				
		return new Vector3d((2 - x0*x0 - y0*y0)/2.,(x0*Math.sqrt(4 - x0*x0 - y0*y0))/2.,
				   (y0*Math.sqrt(4 - x0*x0 - y0*y0))/2.);
	}

	public Point2D project(Vector3d vec,Quaternion quat) {
	

		Vector3d v = new Vector3d(vec);
		v.normalize();
		
		Quaternion q = new Quaternion(quat.W,quat.X,quat.Y,quat.Z);
		q=q.normalize();
		
		int I0_0 =  2;
		int I0_2 =  -1;
		int I0_1 =  -2;
		
		double R0_0;
		double R0_1;
		double R0_2;
		double R0_3;
		double R0_4;
		double R0_5;
		double R0_6;
		double R0_7;
		double R0_8;
		double R0_9;
		double R0_10;
		double R0_11;
		double R0_12;
		double R0_13;
		double R0_14;
		double R0_15;
		double R0_16;
		double R0_17;
		double R0_18;
		double R0_19;
		double R0_20;
		double R0_21;
		double R0_22;
		double R0_23;
		double R0_24;
		double R0_25;
		double R0_26;
		double R0_27;
		double R0_28;
		double R0_29;
		double R0_30;
		double R0_31;
		double R0_32;
		double R0_33;
		double R0_34;
		double R0_35;
		double R0_36;
		double R0_37;
		double R0_38;
		double R0_39;
		double R0_40;
		double R0_41;
		double R0_42;
		double R0_43;
		double R0_44;
		double R0_45;
		double R0_46;
		double R0_47;
		double R0_48;
		double R0_49;
		double R0_50;
		double R0_51;
		double R0_52;

		R0_0 = v.x;
		R0_1 = v.y;
		R0_2 = v.z;

		R0_3 = q.W;
		R0_4 = q.X;
		R0_5 = q.Y;
		R0_6 = q.Z;
		
		R0_7 = R0_3 * R0_3;
		R0_8 = R0_4 * R0_4;
		R0_9 = R0_5 * R0_5;
		R0_10 = R0_6 * R0_6;
		R0_11 = (double) I0_0;
		R0_11 = R0_11 * R0_4 * R0_5 * R0_0;
		R0_12 = (double) I0_0;
		R0_12 = R0_12 * R0_3 * R0_6 * R0_0;
		R0_13 = R0_7 * R0_1;
		R0_14 = R0_8 * R0_1;
		R0_15 = -R0_14;
		R0_16 = R0_9 * R0_1;
		R0_17 = R0_10 * R0_1;
		R0_18 = -R0_17;
		R0_19 = (double) I0_0;
		R0_19 = R0_19 * R0_3 * R0_4 * R0_2;
		R0_20 = -R0_19;
		R0_21 = (double) I0_0;
		R0_21 = R0_21 * R0_5 * R0_6 * R0_2;
		R0_22 = R0_11 + R0_12 + R0_13 + R0_15 + R0_16 + R0_18 + R0_20 + R0_21;
		R0_23 = R0_22 < 0 ? -R0_22 : R0_22;
		R0_24 = R0_23 * R0_23;
		R0_25 = (double) I0_1;
		R0_25 = R0_25 * R0_3 * R0_5 * R0_0;
		R0_26 = (double) I0_0;
		R0_26 = R0_26 * R0_4 * R0_6 * R0_0;
		R0_27 = (double) I0_0;
		R0_27 = R0_27 * R0_3 * R0_4 * R0_1;
		R0_28 = (double) I0_0;
		R0_28 = R0_28 * R0_5 * R0_6 * R0_1;
		R0_29 = R0_7 * R0_2;
		R0_30 = R0_8 * R0_2;
		R0_31 = -R0_30;
		R0_32 = R0_9 * R0_2;
		R0_33 = -R0_32;
		R0_34 = R0_10 * R0_2;
		R0_35 = R0_25 + R0_26 + R0_27 + R0_28 + R0_29 + R0_31 + R0_33 + R0_34;
		R0_36 = R0_35 < 0 ? -R0_35 : R0_35;
		R0_37 = R0_36 * R0_36;
		R0_38 = R0_24 + R0_37;
		R0_39 = R0_7 * R0_0;
		R0_40 = R0_8 * R0_0;
		R0_41 = R0_9 * R0_0;
		R0_42 = -R0_41;
		R0_41 = R0_10 * R0_0;
		R0_43 = -R0_41;
		R0_41 = (double) I0_0;
		R0_41 = R0_41 * R0_4 * R0_5 * R0_1;
		R0_44 = (double) I0_0;
		R0_44 = R0_44 * R0_3 * R0_6 * R0_1;
		R0_45 = -R0_44;
		R0_44 = (double) I0_0;
		R0_44 = R0_44 * R0_3 * R0_5 * R0_2;
		R0_46 = (double) I0_0;
		R0_46 = R0_46 * R0_4 * R0_6 * R0_2;
		R0_47 = (double) I0_2;
		R0_47 = R0_47 + R0_39 + R0_40 + R0_42 + R0_43 + R0_41 + R0_45 + R0_44 + R0_46;
		R0_39 = R0_47 < 0 ? -R0_47 : R0_47;
		R0_47 = R0_39 * R0_39;
		R0_47 = R0_47 + R0_24 + R0_37;
		if(Math.abs(R0_47)<=Double.MIN_VALUE) return new Point2D.Double(0.,0.);
		R0_39 = 1 / R0_47;
		R0_38 = R0_38 * R0_39;
		R0_39 = Math.sqrt(R0_38);
		R0_38 = R0_3 * R0_3;
		R0_47 = R0_4 * R0_4;
		R0_40 = R0_5 * R0_5;
		R0_42 = R0_6 * R0_6;
		if(Math.abs(R0_39)<=Double.MIN_VALUE) return new Point2D.Double(0.,0.);
		R0_43 = 1 / R0_39;
		R0_41 = (double) I0_0;
		R0_41 = R0_41 * R0_4 * R0_5 * R0_0;
		R0_45 = (double) I0_0;
		R0_45 = R0_45 * R0_3 * R0_6 * R0_0;
		R0_44 = R0_38 * R0_1;
		R0_46 = R0_47 * R0_1;
		R0_48 = -R0_46;
		R0_46 = R0_40 * R0_1;
		R0_49 = R0_42 * R0_1;
		R0_50 = -R0_49;
		R0_49 = (double) I0_0;
		R0_49 = R0_49 * R0_3 * R0_4 * R0_2;
		R0_51 = -R0_49;
		R0_49 = (double) I0_0;
		R0_49 = R0_49 * R0_5 * R0_6 * R0_2;
		R0_41 = R0_41 + R0_45 + R0_44 + R0_48 + R0_46 + R0_50 + R0_51 + R0_49;
		R0_41 = R0_41 * R0_43;
		R0_45 = (double) I0_1;
		R0_45 = R0_45 * R0_3 * R0_5 * R0_0;
		R0_44 = (double) I0_0;
		R0_44 = R0_44 * R0_4 * R0_6 * R0_0;
		R0_48 = (double) I0_0;
		R0_48 = R0_48 * R0_3 * R0_4 * R0_1;
		R0_46 = (double) I0_0;
		R0_46 = R0_46 * R0_5 * R0_6 * R0_1;
		R0_50 = R0_38 * R0_2;
		R0_51 = R0_47 * R0_2;
		R0_49 = -R0_51;
		R0_51 = R0_40 * R0_2;
		R0_52 = -R0_51;
		R0_51 = R0_42 * R0_2;
		R0_45 = R0_45 + R0_44 + R0_48 + R0_46 + R0_50 + R0_49 + R0_52 + R0_51;
		R0_45 = R0_45 * R0_43;

		return new Point2D.Double(R0_41,R0_45);
		

		
	}
}
