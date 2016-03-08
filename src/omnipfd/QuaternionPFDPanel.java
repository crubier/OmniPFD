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
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import com.sun.javafx.geom.Vec3d;

import omnipfd.QuaternionPFDPanel.HudMotionListener;

public class QuaternionPFDPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8675890055834516646L;

	private Quaternion orientation; 
	private Quaternion momentum;
	
	private Projection projection;
	

	public Quaternion getOrientation() {
		return orientation;
	}

	public void setOrientation(Quaternion orientation) {
		this.orientation = orientation;
	}

	public Projection getProjection() {
		return projection;
	}

	public void setProjection(Projection projection) {
		this.projection = projection;
	}

	public List<QuaternionPFDObject> getObjects() {
		return objects;
	}

	public void setObjects(List<QuaternionPFDObject> objects) {
		this.objects = objects;
	}

	private List<QuaternionPFDObject> objects;

	private static Color GROUND_COLOR = new Color(50, 120, 30);
	private static Color SKY_COLOR = new Color(50, 150, 255);

	public QuaternionPFDPanel() {
		super();

		projection = new LambertAzimutalEqualAreaProjection();
		// projection = PerspectiveProjection.GNOMONIC_PROJECTION;

		orientation = new Quaternion().fromAxis(0, 0, 0, 1);
		orientation.normalize();
		
		momentum = new Quaternion(0.01, new Vec3d(0, 1, 0));

		objects = new ArrayList<QuaternionPFDObject>(10);

		List<Vec3d> equator, north, south, m45, m90, m135, l45, lm45, sun;

		double inc = Math.PI / 720;

		// EQUATOR
		equator = new ArrayList<Vec3d>();
		for (double i = 0.; i < 2 * Math.PI; i += inc) {
			Vec3d q = new Vec3d(Math.cos(i), Math.sin(i), 0);
			equator.add(q);
		}
		objects.add(new QuaternionPFDObject(QuaternionPFDObject.Type.PATH_FILL, new Vec3d(0, 0, -1), equator,
				GROUND_COLOR, new BasicStroke(), Color.WHITE));
		objects.add(new QuaternionPFDObject(QuaternionPFDObject.Type.PATH_FILL, new Vec3d(0, 0, 1), equator, SKY_COLOR,
				new BasicStroke(), Color.WHITE));
		objects.add(new QuaternionPFDObject(QuaternionPFDObject.Type.PATH_STROKE, new Vec3d(0, 0, 1), equator, null,
				new BasicStroke(2.0f), Color.WHITE));

		// NORTH
		north = new ArrayList<Vec3d>();
		for (double i = -Math.PI / 2.; i <= Math.PI / 2.; i += inc) {
			Vec3d q = new Vec3d(Math.cos(i), 0, Math.sin(i));
			north.add(q);
		}
		objects.add(new QuaternionPFDObject(QuaternionPFDObject.Type.PATH_STROKE, new Vec3d(0, -1, 0), north, null,
				new BasicStroke(4.0f), Color.WHITE));
		objects.add(new QuaternionPFDObject(QuaternionPFDObject.Type.PATH_STROKE, new Vec3d(0, -1, 0), north, null,
				new BasicStroke(2.0f), Color.RED.brighter()));

		// SOUTH
		south = new ArrayList<Vec3d>();
		for (double i = -Math.PI / 2.; i <= Math.PI / 2.; i += inc) {
			Vec3d q = new Vec3d(-Math.cos(i), 0, Math.sin(i));
			south.add(q);
		}
		objects.add(new QuaternionPFDObject(QuaternionPFDObject.Type.PATH_STROKE, new Vec3d(0, -1, 0), south, null,
				new BasicStroke(2.0f), new Color(1.f, 1.f, 1.f, 1.f)));

		m45 = new ArrayList<Vec3d>();
		for (double i = 0.; i < 2 * Math.PI; i += inc) {
			Vec3d q = new Vec3d(Math.cos(Math.PI / 2.) * Math.cos(i), Math.sin(Math.PI / 2.) * Math.cos(i),
					Math.sin(i));
			m45.add(q);
		}
		objects.add(new QuaternionPFDObject(QuaternionPFDObject.Type.PATH_STROKE, new Vec3d(0, -1, 0), m45, null,
				new BasicStroke(), new Color(1.f, 1.f, 1.f, 1.f)));

		m45 = new ArrayList<Vec3d>();
		for (double i = 0.; i < 2 * Math.PI; i += inc) {
			Vec3d q = new Vec3d(Math.cos(Math.PI / 4.) * Math.cos(i), Math.sin(Math.PI / 4.) * Math.cos(i),
					Math.sin(i));
			m45.add(q);
		}
		objects.add(new QuaternionPFDObject(QuaternionPFDObject.Type.PATH_STROKE, new Vec3d(0, -1, 0), m45, null,
				new BasicStroke(), new Color(1.f, 1.f, 1.f, 0.15f)));

		m45 = new ArrayList<Vec3d>();
		for (double i = 0.; i < 2 * Math.PI; i += inc) {
			Vec3d q = new Vec3d(Math.cos(3 * Math.PI / 4.) * Math.cos(i), Math.sin(3 * Math.PI / 4.) * Math.cos(i),
					Math.sin(i));
			m45.add(q);
		}
		objects.add(new QuaternionPFDObject(QuaternionPFDObject.Type.PATH_STROKE, new Vec3d(0, -1, 0), m45, null,
				new BasicStroke(), new Color(1.f, 1.f, 1.f, 0.15f)));

		equator = new ArrayList<Vec3d>();
		for (double i = 0.; i < 2 * Math.PI; i += inc) {
			Vec3d q = new Vec3d(Math.cos(i) * Math.cos(Math.PI / 4.), Math.sin(i) * Math.cos(Math.PI / 4.),
					Math.sin(Math.PI / 4.));
			equator.add(q);
		}
		objects.add(new QuaternionPFDObject(QuaternionPFDObject.Type.PATH_STROKE, new Vec3d(0, -1, 0), equator, null,
				new BasicStroke(), new Color(1.f, 1.f, 1.f, 0.15f)));

		equator = new ArrayList<Vec3d>();
		for (double i = 0.; i < 2 * Math.PI; i += inc) {
			Vec3d q = new Vec3d(Math.cos(i) * Math.cos(-Math.PI / 4.), Math.sin(i) * Math.cos(-Math.PI / 4.),
					Math.sin(-Math.PI / 4.));
			equator.add(q);
		}
		objects.add(new QuaternionPFDObject(QuaternionPFDObject.Type.PATH_STROKE, new Vec3d(0, -1, 0), equator, null,
				new BasicStroke(), new Color(1.f, 1.f, 1.f, 0.15f)));

		// SUN
		sun = new ArrayList<Vec3d>();
		for (double i = 0.; i < 2 * Math.PI; i += inc) {
			Vec3d q = new Vec3d(0.1 * Math.cos(i), 0.1 * Math.sin(i), 1);
			sun.add(q);
		}
		objects.add(new QuaternionPFDObject(QuaternionPFDObject.Type.PATH_FILL, new Vec3d(0, 0, 1), sun, Color.WHITE,
				new BasicStroke(), Color.WHITE));

		this.setMinimumSize(new Dimension(320, 240));
		this.setPreferredSize(new Dimension(800, 600));

		HudMotionListener listener = new HudMotionListener(this);

		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		this.addKeyListener(listener);
		this.setFocusable(true);


		QuaternionPFDPanel that = this;

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        
		scheduler.scheduleAtFixedRate( () -> {
            that.orientation.leftmult(momentum);
            that.momentum.slerp(that.momentum, new Quaternion().fromAxis(0,1,0,0), 0.01);
            that.repaint();
        }, 200, 16, TimeUnit.MILLISECONDS);

	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double size = Math.min(this.getBounds().getWidth(), this.getBounds().getHeight());
		double scale = size / 4.;
		double x0 = this.getBounds().getCenterX();
		double y0 = this.getBounds().getCenterY();

		// BG
		g2.setBackground(Color.BLACK);
		g2.clearRect(0, 0, this.getWidth(), this.getHeight());

		// System.out.println("paint");
		// PATH
		for (QuaternionPFDObject p : objects) {
			// System.out.println(p);

			if (p.getType() == QuaternionPFDObject.Type.PATH_FILL) {

				Path2D path = new Path2D.Double();

				Vec3d qs = p.getPath().get(0);
				Point2D pt = projection.project(orientation.rotate(qs));

				path.moveTo(pt.getX() * scale + x0 - 1, pt.getY() * scale + y0 - 1);
				for (Vec3d q : p.getPath()) {

					pt = projection.project(orientation.rotate(q));
					path.lineTo(pt.getX() * scale + x0 - 1, pt.getY() * scale + y0 - 1);
				}

				path.closePath();
				Area area = new Area(path);
				pt = projection.project(orientation.rotate(p.getPoint()));

				if (area.contains(new Point2D.Double(pt.getX() * scale + x0 - 1, pt.getY() * scale + y0 - 1))) {
					// normal paint
					g2.setPaint(p.getPaint());
					g2.fill(area);
				} else {
					// inverse paint
					Shape border = new Ellipse2D.Double(x0 - 2. * scale, y0 - 2. * scale, 4. * scale, 4. * scale);
					Area area2 = new Area(border);
					area2.subtract(area);
					g2.setPaint(p.getPaint());
					g2.fill(area2);
				}

			}

			else if (p.getType() == QuaternionPFDObject.Type.PATH_STROKE) {

				Path2D path = new Path2D.Double();

				Vec3d qs = p.getPath().get(0);
				Point2D pt = projection.project(orientation.rotate(qs));

				path.moveTo(pt.getX() * scale + x0 - 1, pt.getY() * scale + y0 - 1);
				for (Vec3d q : p.getPath()) {

					pt = projection.project(orientation.rotate(q));
					path.lineTo(pt.getX() * scale + x0 - 1, pt.getY() * scale + y0 - 1);
				}

				g2.setColor(p.getColor());
				g2.setStroke(p.getStroke());
				g2.draw(path);
			}

			else if (p.getType() == QuaternionPFDObject.Type.POINT) {

				Point2D pt = projection.project(orientation.rotate(p.getPoint()));
				g2.setColor(p.getColor());
				g2.drawOval((int) (pt.getX() * scale + x0 - 3), (int) (pt.getY() * scale + y0 - 3), 6, 6);
			}

		}

		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(2.0f));
		g2.drawOval((int) (x0 - 2 * scale), (int) (y0 - 2 * scale), (int) (4 * scale), (int) (4 * scale));
		g2.drawOval((int) (x0 - 10), (int) (y0 - 10), (int) (18), (int) (18));
		g2.drawLine((int) (x0 - 1), (int) (y0 - 10), (int) (x0 - 1), (int) (y0 + 8));
		g2.drawLine((int) (x0 - 10), (int) (y0 - 1), (int) (x0 + 8), (int) (y0 - 1));
		g2.dispose();
	}

	public class HudMotionListener implements MouseListener, MouseMotionListener, KeyListener {

		private QuaternionPFDPanel hud;

		private Vec3d mousePoint;

		public HudMotionListener(QuaternionPFDPanel hud) {
			super();
			this.hud = hud;
		}

		public Point2D getRelativePosition(Point2D position) {
			double size = Math.min(hud.getBounds().getWidth(), hud.getBounds().getHeight());

			double x = (position.getX() - (1. * hud.getBounds().getWidth() - size) / 2.) / size;
			double y = (position.getY() - (1. * hud.getBounds().getHeight() - size) / 2.) / size;

			x = x * 4. - 2.;
			y = y * 4. - 2.;

			return new Point2D.Double(x, y);

		}

		@Override
		public void mouseClicked(MouseEvent arg0) {

			Point2D relativePos = this.getRelativePosition(arg0.getPoint());
			if (arg0.getButton() == MouseEvent.BUTTON1) {
				if (relativePos.distance(0., 0.) < 2) {
					this.hud.getObjects()
							.add(new QuaternionPFDObject(QuaternionPFDObject.Type.POINT,
									this.hud.orientation.reciprical().rotate(this.hud.projection.invert(relativePos)),
									null, null, null, Color.WHITE));
				}
			} else {
				this.hud.setOrientation(new Quaternion()
						.rotationFromTo(new Vec3d(1., 0., 0.),
								this.hud.orientation.reciprical().rotate(this.hud.projection.invert(relativePos)))
						.reciprical());
			}

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
			Point2D relativePos = this.getRelativePosition(arg0.getPoint());
			if (relativePos.distance(0., 0.) < 2) {
				// Store the orientation to which we pointed
				this.mousePoint = this.hud.orientation.reciprical().rotate(this.hud.projection.invert(relativePos));
			}

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {

		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			Point2D relativePos = this.getRelativePosition(arg0.getPoint());
			this.hud.setOrientation(new Quaternion()
					.rotationFromTo(this.hud.orientation.reciprical().rotate(this.hud.projection.invert(relativePos)),
							this.mousePoint)
					.reciprical());
			this.hud.repaint();

		}

		@Override
		public void mouseMoved(MouseEvent arg0) {

		}

		@Override
		public void keyPressed(KeyEvent arg0) {

			double angle = Math.PI / 36. * 16./1000.;
			Quaternion mod;
			switch (arg0.getKeyCode()) {
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_Q:
				mod = new Quaternion(Math.cos(angle), Math.sin(angle), 0, 0);
				this.hud.momentum.leftmult(mod).normalize();
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				mod = new Quaternion(Math.cos(-angle), Math.sin(-angle), 0, 0);
				this.hud.momentum.leftmult(mod).normalize();
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_Z:
				mod = new Quaternion(Math.cos(angle), 0, Math.sin(angle), 0);
				this.hud.momentum.leftmult(mod).normalize();
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				mod = new Quaternion(Math.cos(-angle), 0, Math.sin(-angle), 0);
				this.hud.momentum.leftmult(mod).normalize();
				break;
			case KeyEvent.VK_A:
				mod = new Quaternion(Math.cos(angle), 0, 0, Math.sin(angle));
				this.hud.momentum.leftmult(mod).normalize();
				break;

			case KeyEvent.VK_E:
				mod = new Quaternion(Math.cos(-angle), 0, 0, Math.sin(-angle));
				this.hud.momentum.leftmult(mod).normalize();
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
