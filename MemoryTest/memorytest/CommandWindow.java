package memorytest;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CommandWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextArea text;
	private JButton stop, run;
	StressThread st;
	Thread thread;

	CommandWindow() {
		super("Java GC Evaluator by James");
		text = new JTextArea("Text will appear here.");
		text.setEditable(false);
		setLayout(new BorderLayout());
		add(text,BorderLayout.CENTER);
		
		stop = new JButton("STOP!");
		stop.setPreferredSize(new Dimension(100,30));
		stop.addActionListener(new CloseListener());
		
		run = new JButton("RUN!");
		run.setPreferredSize(new Dimension(100,30));
		run.addActionListener(new CloseListener());
		
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		p1.add(stop);
		p1.add(run);
		add(p1,BorderLayout.SOUTH);
		p1.setPreferredSize(new Dimension(100,40));
		
		setSize(350,250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
		st = new StressThread();
		st.text = this.text;
		st.frame = this;
		thread = new Thread(st);
	}
	
	void setText(String s) {
		text.setText(s);
		repaint();
	}
	
	void destroy() {
		setVisible(false);
		dispose();
	}

	class CloseListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == stop) {
				st.go = false;
				setVisible(false);
				dispose();
			}
			if (arg0.getSource() == run) {
				text.setText("Spawning thread...");
				repaint();
				thread.start();
			}
		}
	
	}

}
