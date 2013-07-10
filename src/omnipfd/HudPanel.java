package omnipfd;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
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

import javax.swing.JPanel;

import omnipfd.HudPanel.HudMotionListener;

import projections.LambertAzimuthalProjection;
import projections.Projection;
import projections.PerspectiveProjection;

public class HudPanel extends JPanel {

	private double pitch; // pitch
	private double yaw; // yaw
	private double roll; // roll

	private Projection projection;

	private static Color GROUND_COLOR = new Color(255, 150, 55);
	private static Color SKY_COLOR = new Color(0, 102, 255);

	public HudPanel() {
		super();
		this.pitch = Math.toRadians(0);
		this.yaw = Math.toRadians(0);
		this.roll = Math.toRadians(0);

		this.projection = PerspectiveProjection.STEREOGRAPHIC_PROJECTION;

		this.setMinimumSize(new Dimension(320, 240));
		this.setPreferredSize(new Dimension(640, 480));

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
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		this.projection.setLambda(this.yaw);
		this.projection.setPhi(this.pitch);

		Point2D North = new Point2D.Double(0, 0);
		Point2D South = new Point2D.Double(0, Math.PI);
		Point2D East = new Point2D.Double(0, Math.PI / 2.);
		Point2D West = new Point2D.Double(0, -1. * Math.PI / 2.);
		Point2D Up = new Point2D.Double(Math.PI / 2., 0.);
		Point2D Down = new Point2D.Double(-1. * Math.PI / 2., 0.);

		double scale = 0.125 * Math.min(this.getSize().getWidth(), this.getSize().getHeight());
		AffineTransform transformer = new AffineTransform();
		transformer.concatenate(AffineTransform.getRotateInstance(this.roll, 0.5 * this.getSize().getWidth(),
				0.5 * this.getSize().getHeight()));
		transformer.concatenate(AffineTransform.getTranslateInstance(0.5 * this.getSize().getWidth(), 0.5 * this
				.getSize().getHeight()));
		transformer.concatenate(AffineTransform.getScaleInstance(scale, scale));

		Point2D NorthT = new Point2D.Double();
		Point2D SouthT = new Point2D.Double();
		Point2D EastT = new Point2D.Double();
		Point2D WestT = new Point2D.Double();
		Point2D UpT = new Point2D.Double();
		Point2D DownT = new Point2D.Double();

		transformer.transform(this.projection.project(North), NorthT);
		transformer.transform(this.projection.project(South), SouthT);
		transformer.transform(this.projection.project(East), EastT);
		transformer.transform(this.projection.project(West), WestT);
		transformer.transform(this.projection.project(Up), UpT);
		transformer.transform(this.projection.project(Down), DownT);

		g2.setBackground(Color.BLACK);
		g2.clearRect(0, 0, this.getWidth(), this.getHeight());
		drawPoint(g2, NorthT, Color.RED);
		drawPoint(g2, SouthT, Color.YELLOW);
		drawPoint(g2, EastT, Color.GREEN);
		drawPoint(g2, WestT, Color.CYAN);
		drawPoint(g2, UpT, Color.BLUE);
		drawPoint(g2, DownT, Color.MAGENTA);

		// Horizon
		if (Math.sin(this.pitch) >= 0) {
			g2.setBackground(SKY_COLOR);
			g2.clearRect(0, 0, this.getWidth(), this.getHeight());
			g2.setPaint(GROUND_COLOR);
			g2.fill(circle(EastT, NorthT, WestT, SouthT, UpT));
			g2.setPaint(Color.WHITE);
			g2.setStroke(new BasicStroke(2));
			g2.draw(circle(EastT, NorthT, WestT, SouthT, UpT));

		} else {
			g2.setBackground(GROUND_COLOR);
			g2.clearRect(0, 0, this.getWidth(), this.getHeight());
			g2.setPaint(SKY_COLOR);
			g2.fill(circle(EastT, NorthT, WestT, SouthT, DownT));
			g2.setPaint(Color.WHITE);
			g2.setStroke(new BasicStroke(2));
			g2.draw(circle(EastT, NorthT, WestT, SouthT, DownT));

		}

		// North-South
		{
			g2.setPaint(Color.WHITE);
			g2.setStroke(new BasicStroke(2));
			g2.draw(circle(SouthT, UpT, NorthT, DownT, WestT));
		}

		// East-West
		{
			g2.setPaint(Color.WHITE);
			float[] dashPattern = { 30, 10, 10, 10 };
			g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dashPattern, 0));
			g2.draw(circle(EastT, UpT, WestT, DownT, NorthT));
		}

		drawMask(g2, Color.WHITE);

		g2.setPaint(Color.WHITE);
		g2.drawString(Double.toString(Math.round(Math.toDegrees(this.pitch))), 5, 10);
		g2.drawString(Double.toString(Math.round(Math.toDegrees(this.yaw))), 5, 20);
		g2.drawString(Double.toString(Math.round(Math.toDegrees(this.roll))), 5, 30);

		g2.dispose();

	}

	/**
	 * Only feed with valid points, i.e. not Nan or Infinite coordinates
	 * 
	 * @param a
	 *            a point on the circle
	 * @param b
	 *            a point on the circle
	 * @param c
	 *            a point on the circle
	 * @param e
	 *            a point inside the circle
	 * @return the circle
	 */
	private Shape circle(Point2D a, Point2D b, Point2D c, Point2D e) {
		Shape res;

		double d = 2 * (a.getX() * (b.getY() - c.getY()) + b.getX() * (c.getY() - a.getY()) + c.getX()
				* (a.getY() - b.getY()));
		
		if (Double.isNaN(d) || Double.isInfinite(d)) {

			double cx = e.getX();
			double cy = e.getY();
			double radius = 0.;
			res = new Ellipse2D.Double(cx - radius, cy - radius, 2 * radius, 2 * radius);

		} else {

			double posmax = 10000;
			double posmin = -1.*posmax;
			double cx = ((a.getY() * a.getY() + a.getX() * a.getX()) * (b.getY() - c.getY())
					+ (b.getY() * b.getY() + b.getX() * b.getX()) * (c.getY() - a.getY()) + (c.getY() * c.getY() + c
					.getX() * c.getX())
					* (a.getY() - b.getY()))
					/ d;
			double cy = ((a.getY() * a.getY() + a.getX() * a.getX()) * (c.getX() - b.getX())
					+ (b.getY() * b.getY() + b.getX() * b.getX()) * (a.getX() - c.getX()) + (c.getY() * c.getY() + c
					.getX() * c.getX())
					* (b.getX() - a.getX()))
					/ d;

			double radius = Point2D.distance(cx, cy, a.getX(), a.getY());
			
			System.out.println(cx+ " " + cy);
			
			if(Math.abs(cx) < posmax && Math.abs(cy) < posmax) {
				res = new Ellipse2D.Double(cx - radius, cy - radius, 2 * radius, 2 * radius);
			} else {
				System.out.println("Aww");
				res = new Path2D.Double();
				
				double x1 = a.getX() + 100*(b.getX()-a.getX());
				double y1 = a.getY() + 100*(b.getY()-a.getY());
				
				double x2 = b.getX() + 100*(a.getX()-b.getX());
				double y2 = b.getY() + 100*(a.getY()-b.getY());
				
				double x3 = a.getX() + 100*(e.getX()-a.getX());
				double y3 = a.getY() + 100*(e.getY()-a.getY());
				
				double x4 = b.getX() + 100*(e.getX()-b.getX());
				double y4 = b.getY() + 100*(e.getY()-b.getY());
					

				((Path2D) res).moveTo(x1,y1);
				((Path2D) res).lineTo(x2,y2);
				((Path2D) res).lineTo(x3,y3);
				((Path2D) res).lineTo(x4,y4);
				
				((Path2D) res).closePath();
			}
			
		}

		return res;
	}

	/**
	 * @param a
	 *            a point on the circle
	 * @param b
	 *            a point on the circle
	 * @param c
	 *            a point on the circle
	 * @param d
	 *            a point on the circle
	 * @param e
	 *            a point inside the circle
	 * @return the circle
	 */
	private Shape circle(Point2D a, Point2D b, Point2D c, Point2D d, Point2D e) {

		Point2D aa, bb, cc;

		if (Double.isInfinite(a.getX()) || Double.isNaN(a.getX()) || Double.isInfinite(a.getY())
				|| Double.isNaN(a.getY())) {
			aa = b;
			bb = c;
			cc = d;
		} else if (Double.isInfinite(b.getX()) || Double.isNaN(b.getX()) || Double.isInfinite(b.getY())
				|| Double.isNaN(b.getY())) {
			aa = a;
			bb = c;
			cc = d;
		} else if (Double.isInfinite(c.getX()) || Double.isNaN(c.getX()) || Double.isInfinite(c.getY())
				|| Double.isNaN(c.getY())) {
			aa = a;
			bb = b;
			cc = d;
		} else {
			aa = a;
			bb = b;
			cc = c;
		}

		return circle(aa, bb, cc, e);
	}

	private void drawPoint(Graphics2D g2, Point2D pt, Color col) {
		Paint temp = g2.getPaint();
		g2.setPaint(col);
		Shape shape = new Ellipse2D.Double(pt.getX() - 6, pt.getY() - 6, 12, 12);
		g2.fill(shape);
		g2.setPaint(temp);
	}

	private void drawMask(Graphics2D g2, Color col) {
		Shape bound = new Ellipse2D.Double(0.5 * this.getBounds().getWidth() - 0.5 * 0.7 * this.getBounds().getWidth(),
				0.5 * this.getBounds().getHeight() - 0.5 * 0.7 * this.getBounds().getWidth(), 0.7 * this.getBounds()
						.getWidth(), 0.7 * this.getBounds().getWidth());
		bound = new Rectangle2D.Double(0.5 * this.getBounds().getWidth() - 0.5 * 0.7 * this.getBounds().getWidth(), 0.5
				* this.getBounds().getHeight() - 0.5 * 0.7 * this.getBounds().getWidth(), 0.7 * this.getBounds()
				.getWidth(), 0.7 * this.getBounds().getWidth());
		Path2D mask = new Path2D.Double(this.getBounds());
		mask.append(bound, false);
		mask.setWindingRule(Path2D.WIND_EVEN_ODD);

		g2.setStroke(new BasicStroke(2));
		g2.setPaint(Color.BLACK);
		g2.fill(mask);
		g2.setPaint(col);
		g2.draw(bound);

		Shape center = new Ellipse2D.Double(0.5 * this.getBounds().getWidth() - 6,
				0.5 * this.getBounds().getHeight() - 6, 12, 12);

		g2.setPaint(col);
		g2.draw(center);
	}

	public double getLambda() {
		return yaw;
	}

	public void setLambda(double lambda) {
		this.yaw = lambda;
	}

	public double getPhi() {
		return pitch;
	}

	public void setPhi(double phi) {
		this.pitch = phi;
	}

	public double getPsi() {
		return roll;
	}

	public void setPsi(double psi) {
		this.roll = psi;
	}

	public class HudMotionListener implements MouseListener, MouseMotionListener, KeyListener {

		private HudPanel hud;

		private double x0, y0;

		public HudMotionListener(HudPanel hud) {
			super();
			this.hud = hud;
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			this.hud.setPhi(0.);
			this.hud.setLambda(0.);
			this.hud.setPsi(0.);
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
			this.x0 = arg0.getX();
			this.y0 = arg0.getY();

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {

		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			double x = arg0.getX() - this.x0;
			double y = arg0.getY() - this.y0;
			double coef = -1. * Math.PI / this.hud.getSize().getWidth();
			this.hud.setPhi(this.hud.getPhi() + y * coef);
			this.hud.setLambda(this.hud.getLambda() + x * coef);
			this.x0 = arg0.getX();
			this.y0 = arg0.getY();
			this.hud.repaint();

		}

		@Override
		public void mouseMoved(MouseEvent arg0) {

		}

		@Override
		public void keyPressed(KeyEvent arg0) {

			switch (arg0.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				this.hud.setPsi(this.hud.getPsi() + Math.toRadians(1));
				break;
			case KeyEvent.VK_RIGHT:
				this.hud.setPsi(this.hud.getPsi() + Math.toRadians(-1));
				break;
			case KeyEvent.VK_UP:
				this.hud.setPhi(this.hud.getPhi() + Math.toRadians(1) * Math.cos(this.hud.getPsi()));
				this.hud.setLambda(this.hud.getLambda() + Math.toRadians(1) * Math.sin(this.hud.getPsi()));
				break;
			case KeyEvent.VK_DOWN:
				this.hud.setPhi(this.hud.getPhi() + Math.toRadians(-1) * Math.cos(this.hud.getPsi()));
				this.hud.setLambda(this.hud.getLambda() + Math.toRadians(-1) * Math.sin(this.hud.getPsi()));
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
