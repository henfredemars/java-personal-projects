package TextCompress;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

//Main application window
public class TextCompress extends JFrame {
	private static final long serialVersionUID = 1L;
	JButton button;
	JButton about;
	JButton settings;
	JTextArea input;
	JTextArea output;
	ListenManager listenManager;
	JScrollPane outputPane, inputPane;
	
	public TextCompress(String title) {
		super(title);
		setLayout(new BorderLayout());
		setSize(new Dimension(750,480));
		button = new JButton("------------------------------Convert------------------------------");
		about = new JButton("About");
		about.setPreferredSize(new Dimension(100,200));
		settings = new JButton("Settings");
		settings.setPreferredSize(new Dimension(100,200));
		input = new JTextArea();
		input.setBorder(new TitledBorder("Source text"));
		input.setText("Enter text to be converted.");
		input.setWrapStyleWord(true);
		input.setLineWrap(true);
		inputPane = new JScrollPane(input);
		inputPane.setPreferredSize(new Dimension(100,200));
		output = new JTextArea();
		output.setBorder(new TitledBorder("Conversion results"));
		output.setEditable(false);
		output.setWrapStyleWord(false);
		output.setLineWrap(true);
		output.setText("Results will be shown here.");
		outputPane = new JScrollPane(output);
		outputPane.setPreferredSize(new Dimension(100,200));
		listenManager = new ListenManager(button, input, output, about, settings);
		button.addActionListener(listenManager);
		settings.addActionListener(listenManager);
		about.addActionListener(listenManager);
		add(inputPane, BorderLayout.NORTH);
		add(button, BorderLayout.CENTER);
		add(outputPane, BorderLayout.SOUTH);
		add(settings, BorderLayout.WEST);
		add(about, BorderLayout.EAST);
	}

	public static void main(String[] args) {
		TextCompress mainApp = new TextCompress("TextCompress: Human-readable SMS text compressor");
		mainApp.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainApp.setVisible(true);
	}

}
