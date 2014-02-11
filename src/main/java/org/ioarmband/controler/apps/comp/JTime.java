package org.ioarmband.controler.apps.comp;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JLabel;

public class JTime extends JPanel implements ActionListener{
	private static final long serialVersionUID = 8709555383022904507L;
	Timer timer;
	JLabel hourLabel;
	JLabel minLabel;
	JLabel secLabel;
	private static JTime __instance;

	public static JTime getInstance() {
		if (__instance == null) {
			__instance = new JTime();
		}
		return __instance;
	}
	
	private JTime() {
		super();
		JLabel separator = new JLabel(":");
		hourLabel = new JLabel();
		minLabel = new JLabel();
		secLabel = new JLabel();
		
		/*hourLabel.setHorizontalAlignment(JLabel.CENTER);
		minLabel.setHorizontalAlignment(JLabel.CENTER);
		secLabel.setHorizontalAlignment(JLabel.CENTER);
		*/
		
		Font font;
		font = new Font(getFont().getName(), getFont().getStyle(), 180);
		hourLabel.setFont(font);
		minLabel.setFont(font);
		font = new Font(getFont().getName(), getFont().getStyle(), 100);
		separator.setFont(font);
		secLabel.setFont(font);
		
		hourLabel.setForeground(Color.LIGHT_GRAY);
		separator.setForeground(Color.LIGHT_GRAY);
		minLabel.setForeground(Color.LIGHT_GRAY);
		secLabel.setForeground(Color.LIGHT_GRAY);
		
		add(hourLabel);
		add(separator);
		add(minLabel);
		add(secLabel);
		
		updateDate();
		setFont(font);
		setBackground(Color.DARK_GRAY);
		timer = new Timer(1000, this);
		timer.setInitialDelay(1000);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		updateDate();
	}
	
	private void updateDate(){

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat();
    	
    	sdf.applyPattern("HH");
    	hourLabel.setText(sdf.format(calendar.getTime()));
    	sdf.applyPattern("mm");
    	minLabel.setText(sdf.format(calendar.getTime()));
    	sdf.applyPattern("ss");
    	secLabel.setText(sdf.format(calendar.getTime()));
	}

}
