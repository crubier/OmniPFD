package omnipfd;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.List;

import javax.vecmath.Vector3d;

public class QuaternionPFDObject {
	public static enum Type {POINT, PATH_STROKE, PATH_FILL, PATH_STROKE_FILL};
	
	private Type type;
	private Vector3d point;
	private List<Vector3d> path;
	private Paint paint;
	private Stroke stroke;
	private Color color;
	
	
	public QuaternionPFDObject() {
		super();
	}



	public QuaternionPFDObject(Type type, Vector3d point, List<Vector3d> path,
			Paint paint, Stroke stroke, Color color) {
		super();
		this.type = type;
		this.point = point;
		this.path = path;
		this.paint = paint;
		this.stroke = stroke;
		this.color = color;
	}



	public QuaternionPFDObject(Vector3d point, Paint paint) {
		super();
		this.point = point;
		this.paint = paint;
		this.type = Type.POINT;
	}
	
	
	
	public QuaternionPFDObject(List<Vector3d> path, Stroke stroke) {
		super();
		this.path = path;
		this.stroke = stroke;
		this.type = Type.PATH_STROKE;
	}


	public QuaternionPFDObject(Vector3d point, List<Vector3d> path, Paint paint) {
		super();
		this.point = point;
		this.path = path;
		this.paint = paint;
		this.type = Type.PATH_FILL;
	}



	public QuaternionPFDObject(Vector3d point, List<Vector3d> path,
			Paint paint, Stroke stroke) {
		super();
		this.point = point;
		this.path = path;
		this.paint = paint;
		this.stroke = stroke;
		this.type = Type.PATH_STROKE_FILL;
	}
	
	
	
	public QuaternionPFDObject(Vector3d point) {
		super();
		this.point = point;
		this.paint = Color.MAGENTA;
		this.type = Type.POINT;
	}
	
	
	
	public QuaternionPFDObject(List<Vector3d> path) {
		super();
		this.path = path;
		this.stroke = new BasicStroke();
		this.type = Type.PATH_STROKE;
	}


	public QuaternionPFDObject(Vector3d point, List<Vector3d> path) {
		super();
		this.point = point;
		this.path = path;
		this.paint = Color.MAGENTA;
		this.type = Type.PATH_FILL;
	}

	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Vector3d getPoint() {
		return point;
	}
	public void setPoint(Vector3d point) {
		this.point = point;
	}
	public List<Vector3d> getPath() {
		return path;
	}
	public void setPath(List<Vector3d> path) {
		this.path = path;
	}
	public Paint getPaint() {
		return paint;
	}
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	public Stroke getStroke() {
		return stroke;
	}
	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}	
}
