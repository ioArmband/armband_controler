package org.tse.pri.ioarmband.armband;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.tse.pri.ioarmband.armband.input.Pointer;

public class TestSmallScreen {

	public static void main(String[] args) {

		JFrame mainFrame = new JFrame("ioArmband");
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(200,200);
		mainFrame.setAlwaysOnTop(true);
		mainFrame.setVisible(true);
	}
}
