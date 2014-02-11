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
import java.awt.geom.Point2D;
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
	GestureVisualiser gestureFrame;
	App currenApp = null;

	private SwingDisplayEngine() {}

	private static SwingDisplayEngine __instance;
	public static SwingDisplayEngine getInstance() {
		if (__instance == null) {
			__instance = new SwingDisplayEngine();
		}
		return __instance;
	}
	
	@Override
	public void start() {
		init();
	}

	private void init() {
		mainFrame = new JFrame("ioArmband");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initAppPanel();
		initGestureVisualiser();
		putFullScreen();
		
		mainFrame.setVisible(true);
		
		setApp(null);
	}
	
	private void initAppPanel() {
		appPanel = (JPanel) mainFrame.getContentPane();
		appPanel.setBackground(Color.BLACK);
	}

	private void initGestureVisualiser(){
		gestureFrame = new GestureVisualiser();
		gestureFrame.setVisible(true);
		mainFrame.setGlassPane(gestureFrame);
		
	}
	private void putFullScreen(){
		GraphicsDevice gd = getGraphicalDevice();
		
		mainFrame.setUndecorated(true);
		gd.setFullScreenWindow(mainFrame);
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);	
	}

	@Override
	public void setPointers(Collection<Pointer> pointers) {
		gestureFrame.setPointers(pointers);
	}

	@Override
	public void setGestures(Collection<Gesture> gestures) {
		gestureFrame.setGestures(gestures);

		for(Gesture gesture : gestures){
			Pointer p = gesture.getPointer();
			if(gesture.getType() == GestureType.TOUCH){
				simulateClick(p);
			}
		}
	}

	@Override
	public void setApp(App app) {
		logger.debug("dispaly app: " + app);
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
		Robot robot;
		try {
			robot = new Robot(getGraphicalDevice());
			image = robot.createScreenCapture(getDisplayRectangle());
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	private Rectangle getDisplayRectangle(){
		DisplayMode dm = getGraphicalDevice().getDisplayMode();
		return new Rectangle(dm.getWidth(), dm.getHeight());
	}
	

	private void simulateClick(Pointer p){
		if(p.getX()>1 || p.getX() <-1 || p.getY() > 1 || p.getY() < -1)
			return;
		Point2D.Double pos = getPosition(p);
		Robot robot;
		try {
			robot = new Robot(getGraphicalDevice());
			robot.mouseMove((int)pos.x, (int)pos.y);
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private GraphicsDevice getGraphicalDevice(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gds = ge.getScreenDevices();
		return gds[gds.length-1];
	}
	
	public Point2D.Double getPosition(Pointer p){
		int center_x = mainFrame.getWidth()/2;
		int center_y = mainFrame.getHeight()/2;
		
		int pos_x = (int) (center_x + p.getX() * center_x);
		int pos_y = (int) (center_y + p.getY() * center_y);
		
		return new Point2D.Double(pos_x, pos_y);
	}
}


class GestureVisualiser extends JComponent{
	
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
		
		repaint();
	}
	
	
	protected void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		JLabel fontLabel = new JLabel();
		g2d.setFont(new Font(fontLabel.getFont().getName(),fontLabel.getFont().getStyle(), 40));
		
		for(Pointer p : pointers){
			drawFinger(g2d, p);
		}
		
		for(Gesture gesture : gestures){
			switch(gesture.getType()){
				case TOUCH:
					drawTouch(g2d, gesture.getPointer());
					break;
				default:
			}
		}
	}
	private void drawFinger(Graphics2D g2d, Pointer p) {	
		Point2D.Double pos = getPosition(p);		
		String chars = p.getId().toString();
		Float size = p.getSize();
		
		g2d.drawChars(chars.toCharArray(), 0, chars.length(), (int)(pos.x+size), (int)(pos.y+size));
		drawCircle(g2d, p, size, new Color(0xff0000));
	};
	private void drawTouch(Graphics2D g2d, Pointer p) {
		Float size = p.getSize();
		fillCircle(g2d, p, size, new Color(0x00ff00));
	};
	
	private void drawCircle(Graphics2D g2d, Pointer p, Float size, Color color){
		Point2D.Double pos = getPosition(p);
		Ellipse2D.Double circle = new Ellipse2D.Double(pos.x-size/2, pos.y-size/2, size,size);
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(10));
		g2d.draw(circle);
	}
	private void fillCircle(Graphics2D g2d, Pointer p, Float size, Color color){
		Point2D.Double pos = getPosition(p);
		Ellipse2D.Double circle = new Ellipse2D.Double(pos.x-size/2, pos.y-size/2, size,size);
		g2d.setColor(color);
		g2d.fill(circle);
	}
	
	private Point2D.Double getPosition(Pointer p){
		int center_x = getWidth()/2;
		int center_y = getHeight()/2;
		
		int pos_x = (int) (center_x + p.getX() * center_x);
		int pos_y = (int) (center_y + p.getY() * center_y);
		
		return new Point2D.Double(pos_x, pos_y);
	}
};
