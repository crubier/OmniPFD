package omnipfd;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.vecmath.Vector3d;

import omnipfd.QuaternionPFDPanel.HudMotionListener;

import projections.LambertAzimuthalProjection;
import projections.Projection;
import projections.PerspectiveProjection;

public class QuaternionPFDPanel extends JPanel {

	private Quaternion orientation; // pitch
	private LambertAzimutalEqualAreaProjection projection;

	private List<List<Quaternion>> areas;
	private List<List<Quaternion>> paths;
	private List<Quaternion> points;

	private static Color GROUND_COLOR = new Color(200, 100, 50);
	private static Color SKY_COLOR = new Color(50, 150, 255);

	public QuaternionPFDPanel() {
		super();
		
		projection = new LambertAzimutalEqualAreaProjection();
		
		orientation = new Quaternion().fromAxis(1,0, 0, 0);
		orientation.normalize();
		
		points=new ArrayList<Quaternion>();
		paths=new ArrayList<List<Quaternion>>();
		areas=new ArrayList<List<Quaternion>>();
		
		
		for(double i=0.;i<2*Math.PI;i+=Math.PI/360.) {
			Quaternion q = new Quaternion().fromAxis(i, 0, 0, 1);
			q.normalize();
			points.add(q);
			
		}

		this.setMinimumSize(new Dimension(320, 240));
		this.setPreferredSize(new Dimension(800	, 600));

		HudMotionListener listener = new HudMotionListener(this);

		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		this.addKeyListener(listener);
		this.setFocusable(true);
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Vector3d ref = new Vector3d(1.,0.,0.);
		
		double size=Math.min(this.getBounds().getWidth(),this.getBounds().getHeight());
		double scale=size/4.;
		double x0=this.getBounds().getCenterX();
		double y0=this.getBounds().getCenterY();

		System.out.println("DRAW");
		g2.setBackground(Color.BLACK);
		g2.clearRect(0, 0, this.getWidth(), this.getHeight());
		for(Quaternion q : points) {
			q.mult(orientation);
			
			Point2D pt = projection.project(ref, q);
			System.out.println(pt);
			g2.setColor(Color.WHITE);
			g2.drawOval((int)(pt.getX()*scale+x0-3),(int)( pt.getY()*scale+y0-3), 6, 6);
		}
		
		
		g2.dispose();

	}

	

	public class HudMotionListener implements MouseListener,
	MouseMotionListener, KeyListener {

		private QuaternionPFDPanel hud;

		private Point2D lastPoint;
		private Point2D lastPos;

		public HudMotionListener(QuaternionPFDPanel hud) {
			super();
			this.hud = hud;
		}

		public Point2D getRelativePosition(Point2D position) {
			double size=Math.min(hud.getBounds().getWidth(),hud.getBounds().getHeight());

			double x=(position.getX()-(1.*hud.getBounds().getWidth()-size)/2.)/size;
			double y=(position.getY()-(1.*hud.getBounds().getHeight()-size)/2.)/size;

			x=x*4.-2.;
			y=y*4.-2.;

			return new Point2D.Double(x,y);

		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
//			if(arg0.getButton()==MouseEvent.BUTTON1) {
//				Point2D relativePos=this.getRelativePosition(arg0.getPoint());
//				if(relativePos.distance(0., 0.)<2) {
//					this.hud.setTarget(this.hud.projection.inverse(relativePos));
//				}
//			}
//			else {
//				Point2D relativePos=this.getRelativePosition(arg0.getPoint());
//				if(relativePos.distance(0., 0.)<2) {
//					this.hud.setPhi(this.hud.projection.inverse(relativePos).getX());
//					this.hud.setLambda(this.hud.projection.inverse(relativePos).getY());
//					this.hud.setPsi(0.);
//				}
//
//			}

			this.hud.repaint();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {

		}

		@Override
		public void mouseExited(MouseEvent arg0) {

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
//			Point2D relativePos=this.getRelativePosition(arg0.getPoint());
//			if(relativePos.distance(0., 0.)<2) {
//				this.lastPoint=this.hud.projection.inverse(relativePos);
//				this.lastPos=new Point2D.Double(this.hud.getPhi(),this.hud.getLambda());
//			}

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {

		}

		@Override
		public void mouseDragged(MouseEvent arg0) {

//			Point2D relativePos=this.getRelativePosition(arg0.getPoint());
//			if(relativePos.distance(0., 0.)<2) {
//				Point2D arg=this.hud.projection.inverse(relativePos);
//
//				double x = arg.getX() - this.lastPoint.getX();
//				double y = arg.getY() - this.lastPoint.getY();
//
//				this.hud.setPhi(this.lastPos.getX() -x);
//				this.hud.setLambda( this.lastPos.getY() -y );
//
//				//this.lastPoint=this.hud.projection.inverse(relativePos);
//				this.lastPos=new Point2D.Double(this.hud.getPhi(),this.hud.getLambda());
//				
//				System.out.println("---------------------------\n" + this.lastPos);
//				
//				this.hud.repaint();
//			}

		}

		@Override
		public void mouseMoved(MouseEvent arg0) {

		}

		@Override
		public void keyPressed(KeyEvent arg0) {

			Quaternion mod;
			switch (arg0.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				mod=new Quaternion(Math.cos(Math.PI/360.),Math.sin(Math.PI/360.),0,0);
				this.hud.orientation.leftmult(mod).normalize();
				break;
			case KeyEvent.VK_RIGHT:
				mod=new Quaternion(Math.cos(-Math.PI/360.),Math.sin(-Math.PI/360.),0,0);
				this.hud.orientation.leftmult(mod).normalize();
				break;
			case KeyEvent.VK_UP:
				mod=new Quaternion(Math.cos(Math.PI/360.),0,Math.sin(Math.PI/360.),0);
				this.hud.orientation.leftmult(mod).normalize();
				break;
			case KeyEvent.VK_DOWN:
				mod=new Quaternion(Math.cos(-Math.PI/360.),0,Math.sin(-Math.PI/360.),0);
				this.hud.orientation.leftmult(mod).normalize();
				break;
			default:
				break;
			}
			this.hud.repaint();

		}

		@Override
		public void keyReleased(KeyEvent arg0) {

		}

		@Override
		public void keyTyped(KeyEvent arg0) {

		}
	}
}
