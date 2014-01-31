package org.tse.pri.ioarmband.armband.apps;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.Ellipse2D;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.tse.pri.ioarmband.armband.input.Gesture;
import org.tse.pri.ioarmband.armband.input.Pointer;

public class SwingDisplayEngine implements DisplayEngine{

	JFrame mainFrame;
	CirlclesComponents circleFrame;
	private SwingDisplayEngine() {

	}

	@Override
	public void start() {
		init();
	}

	private void init() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gds = ge.getScreenDevices();
		GraphicsDevice gd = gds[gds.length-1];
		mainFrame = new JFrame("ioArmband");
		mainFrame.getContentPane().setBackground(Color.BLACK);
		mainFrame.setUndecorated(true);
		mainFrame.setVisible(true);
		circleFrame = new CirlclesComponents();
		Vector<Pointer> cs = new Vector<Pointer>();
		Pointer c = new Pointer();
		c.setX(0f);
		c.setY(0f);
		c.setSize(100.0f);
		cs.add(c);
		circleFrame.setPointers(cs);
		mainFrame.setGlassPane(circleFrame);
		mainFrame.add(circleFrame);
		circleFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		gd.setFullScreenWindow(mainFrame);

		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		JLabel label = new JLabel("ioArmband");
		Font font = new Font(label.getFont().getName(), label.getFont().getStyle(), 100);
		label.setForeground(Color.WHITE);
		label.setFont(font);
		mainFrame.getContentPane().add(label);
	}
	private static SwingDisplayEngine __instance;
	public static SwingDisplayEngine getInstance() {
		if (__instance == null) {
			__instance = new SwingDisplayEngine();
		}
		return __instance;
	}

	@Override
	public void setPointers(Collection<Pointer> pointers) {
		circleFrame.setPointers(pointers);
	}

	@Override
	public void setGestures(Collection<Gesture> gestures) {
		circleFrame.setGestures(gestures);
	}

}

class CirlclesComponents extends JComponent{
	private static final long serialVersionUID = -5151649518767032140L;
	Collection<Pointer> pointers = new Vector<Pointer>();
	Collection<Gesture> gestures = new Vector<Gesture>();

	public void setPointers(Collection<Pointer> pointers){
		this.pointers = pointers; 
		repaint();
	}
	public void setGestures(Collection<Gesture> gestures){
		this.gestures = gestures; 
		repaint();
	}
	protected void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		int center_x = getWidth()/2;
		int center_y = getHeight()/2;
		System.out.println(center_x+ "   " +center_y);

		JLabel aa = new JLabel();
		g2d.setFont(new Font(aa.getFont().getName(),aa.getFont().getStyle(), 40));
		g2d.setColor(new Color(0xff0000));
		for(Pointer p : pointers){
			int pos_x = (int) (center_x + p.getX() * center_x);
			int pos_y = (int) (center_y + p.getY() * center_y);
			Float size = p.getDist();
			System.out.println(center_x+ "   " +center_y);

			g2d.setColor(new Color(0xff0000));
			String chars = p.getId().toString();
			g2d.drawChars(chars.toCharArray(), 0, chars.length(), (int)(pos_x+size), (int)(pos_y+p.getDist()));
			Ellipse2D.Double circle;
			Double DZ_RATE = 1.0;
			
				g2d.setColor(new Color(0xff0000));
				circle = new Ellipse2D.Double(pos_x-size/2, pos_y-size/2, size,size);
				g2d.draw(circle);
		}
		for(Gesture gesture : gestures){
			Pointer p = gesture.getPointer();
			int pos_x = (int) (center_x + p.getX() * center_x);
			int pos_y = (int) (center_y + p.getY() * center_y);
			Float size = p.getDist();
			System.out.println(center_x+ "   " +center_y);

			g2d.setColor(new Color(0xff0000));
			String chars = p.getId().toString();
			g2d.drawChars(chars.toCharArray(), 0, chars.length(), (int)(pos_x+size), (int)(pos_y+p.getDist()));
			Ellipse2D.Double circle;
			Double DZ_RATE = 1.0;
			
			g2d.setColor(new Color(0x00ff00));
			circle = new Ellipse2D.Double(pos_x-size/2, pos_y-size/2, size,size);
			g2d.fill(circle);
		}
	};
};
