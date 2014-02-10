package org.tse.pri.ioarmband.armband.apps.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.Timer;

public class JChrono extends JLabel implements ActionListener{
	Timer timer;
	JLabel timeLabel;
	long elapsed;
	long previouslyElapsed;
	long startTime = 0;
	SimpleDateFormat sdf;
	private final static int DELAY = 29; 
	
	public JChrono(){
    	sdf = new SimpleDateFormat();
    	sdf.applyPattern("mm:ss::SSS");
		timer = new Timer(DELAY, this);
		elapsed = 0;
		previouslyElapsed = 0;
		updateText();
	}
	
	public void start(){
		startTime = System.currentTimeMillis();
		timer.start();
	}
	public void stop(){
		timer.stop();
		previouslyElapsed += elapsed;
		elapsed = 0;
		updateText();
	}
	public void clear(){
		timer.stop();
		elapsed = 0;
		previouslyElapsed = 0;
		updateText();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		elapsed = System.currentTimeMillis() - startTime;
		updateText();
	}

	private void updateText() {
    	setText(sdf.format(new Date(elapsed+previouslyElapsed)));
	}

}
