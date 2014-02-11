package org.ioarmband.controler.apps;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.ioarmband.controler.input.Gesture;
import org.ioarmband.controler.input.Pointer;
import org.ioarmband.net.message.enums.GestureType;

public class SwingDisplayEngine implements DisplayEngine, GestureListener{
	private static final Logger logger = Logger.getLogger(SwingDisplayEngine.class);
	
	JFrame mainFrame;
	JPanel appPanel;
	CirlclesComponents circleFrame;
	App currenApp = null;
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
		appPanel = (JPanel) mainFrame.getContentPane();
		mainFrame.getContentPane().setBackground(Color.BLACK);
		mainFrame.setUndecorated(true);
		mainFrame.setVisible(true);
		appPanel.setLayout(new GridLayout(1,1));
		
		circleFrame = new CirlclesComponents();
		Vector<Pointer> cs = new Vector<Pointer>();
		Pointer c = new Pointer();
		c.setX(0f);
		c.setY(0f);
		c.setSize(100.0f);
		cs.add(c);
		mainFrame.setGlassPane(circleFrame);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gd.setFullScreenWindow(mainFrame);
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		circleFrame.setVisible(true);
		//circleFrame.setPointers(cs);
		setApp(null);
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

	@Override
	public void setApp(App app) {
		logger.info("dispaly app: " + app);
		appPanel.removeAll();
		appPanel.setLayout(new GridLayout(1,1));
		if(currenApp != null){
			currenApp.hide();
		}
		if(app != null){
			Container panel = app.getPanel();
			appPanel.add(panel);
		}
		else{
			buildGenericFrame();
		}
		currenApp = app;
		appPanel.updateUI();
	}
	
	private void buildGenericFrame(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,1));
		panel.setBackground(Color.BLACK);
		
		JLabel label = new JLabel("ioArmband", JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		Font font = new Font(label.getFont().getName(), label.getFont().getStyle(), 100);
		label.setForeground(Color.WHITE);
		label.setFont(font);
		label.setSize(panel.getSize());
		panel.add(label);

		panel.setPreferredSize(appPanel.getSize());
		panel.setVisible(true);
		appPanel.add(panel);
	}
	@Override
	public void onGesture(App app, GestureType gestureType, String source) {
	}

	@Override
	public BufferedImage getScreenCapture() {
		BufferedImage image = null;
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gds = ge.getScreenDevices();
		GraphicsDevice gd = gds[gds.length-1];
		DisplayMode dm = gd.getDisplayMode();
		Robot robot;
		try {
			robot = new Robot(gd);
			image = robot.createScreenCapture(new Rectangle(dm.getWidth(), dm.getHeight()));
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		return image;
	}
	
	

}


class CirlclesComponents extends JComponent{
	
	private static final long serialVersionUID = -5151649518767032140L;
	Collection<Pointer> pointers = new Vector<Pointer>();
	Collection<Gesture> gestures = new Vector<Gesture>();
	int i;
	public void setPointers(Collection<Pointer> pointers){
		this.pointers = pointers; 
		repaint();
	}
	
	public void setGestures(Collection<Gesture> gestures){
		this.gestures = gestures; 

		for(Gesture gesture : gestures){
			Pointer p = gesture.getPointer();
			if(gesture.getType() == GestureType.TOUCH){
				simulateClick(p);
			}
		}
		
		repaint();
	}
	
	
	protected void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		int center_x = getWidth()/2;
		int center_y = getHeight()/2;
		
		JLabel aa = new JLabel();
		g2d.setFont(new Font(aa.getFont().getName(),aa.getFont().getStyle(), 40));
		g2d.setColor(new Color(0xff0000));
		for(Pointer p : pointers){
			int pos_x = (int) (center_x + p.getX() * center_x);
			int pos_y = (int) (center_y + p.getY() * center_y);
			Float size = p.getDist();
			//System.out.println(center_x+ "   " +center_y);

			g2d.setColor(new Color(0xff0000));
			String chars = p.getId().toString();
			g2d.drawChars(chars.toCharArray(), 0, chars.length(), (int)(pos_x+size), (int)(pos_y+p.getDist()));
			Ellipse2D.Double circle;
			
			g2d.setColor(new Color(0xff0000));
			g2d.setStroke(new BasicStroke(10));
			circle = new Ellipse2D.Double(pos_x-size/2, pos_y-size/2, size,size);
			
			g2d.draw(circle);
		}
		
		for(Gesture gesture : gestures){
			Pointer p = gesture.getPointer();
			
			int pos_x = (int) (center_x + p.getX() * center_x);
			int pos_y = (int) (center_y + p.getY() * center_y);
			Float size = p.getSize();

			g2d.setColor(new Color(0xff0000));
			String chars = p.getId().toString();
			g2d.drawChars(chars.toCharArray(), 0, chars.length(), (int)(pos_x+size), (int)(pos_y+p.getDist()));
			Ellipse2D.Double circle;
			
			g2d.setColor(new Color(0x00ff00));
			circle = new Ellipse2D.Double(pos_x-size/2, pos_y-size/2, size,size);
			g2d.fill(circle);
		}
	};
	private void simulateClick(Pointer p){
		if(p.getX()>1 || p.getX() <-1 || p.getY() > 1 || p.getY() < -1)
			return;
		Robot robot;
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] gds = ge.getScreenDevices();
			GraphicsDevice gd = gds[gds.length-1];
			robot = new Robot(gd);
			int center_x = getWidth()/2;
			int center_y = getHeight()/2;
			int pos_x = (int) (center_x + p.getX() * center_x);
			int pos_y = (int) (center_y + p.getY() * center_y);
			robot.mouseMove(pos_x, pos_y);
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
};
