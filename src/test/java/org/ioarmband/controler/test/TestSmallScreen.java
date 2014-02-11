package org.ioarmband.controler.test;

import javax.swing.JFrame;

public class TestSmallScreen {

	public static void main(String[] args) {

		JFrame mainFrame = new JFrame("ioArmband");
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(200,200);
		mainFrame.setAlwaysOnTop(true);
		mainFrame.setVisible(true);
	}
}
