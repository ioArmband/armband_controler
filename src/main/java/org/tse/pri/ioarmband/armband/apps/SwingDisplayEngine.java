package org.tse.pri.ioarmband.armband.apps;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class SwingDisplayEngine implements DisplayEngine{

	JFrame mainFrame;
	

	private SwingDisplayEngine() {

	}
	
	@Override
	public void start() {
		init();
	}

	private void init() {
		mainFrame = new JFrame("ioArmband");
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setUndecorated(true);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Hello World");
        mainFrame.getContentPane().add(label);
	}

	
	
	private static SwingDisplayEngine __instance;
	public static SwingDisplayEngine getInstance() {
		if (__instance == null) {
			__instance = new SwingDisplayEngine();
		}
		return __instance;
	}
	
}
