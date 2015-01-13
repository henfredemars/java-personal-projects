package TextCompress;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

class SettingsWindow extends JFrame {
	JLabel maxCharacters, cutOffWindow, newTranslation;
	JTextField maxCharactersField, cutOffWindowField, sourceTrans, destTrans;
	JButton okayButton, define;
	ListenManager listenManager;
	SettingsWindow frame = this;

	private static final long serialVersionUID = 1L;

	SettingsWindow(ListenManager listenManager){
		super("Settings");
		this.listenManager = listenManager;
		setLayout(new BorderLayout());
		maxCharacters = new JLabel("Max Characters Per Message: ");
		cutOffWindow = new JLabel("Phrase Cutoff Window in Characters: ");
		newTranslation = new JLabel("New Translation: ");
		maxCharactersField = new JTextField(String.valueOf(listenManager.getMaxChars()));
		cutOffWindowField = new JTextField(String.valueOf(listenManager.getCutOff()));
		sourceTrans = new JTextField("#Caution!");
		destTrans = new JTextField("No undo!");
		maxCharactersField.setPreferredSize(new Dimension(50,20));
		cutOffWindowField.setPreferredSize(new Dimension(50,20));
		okayButton = new JButton("Save and Close");
		define = new JButton("Add Translation");
		SettingsListener sl = new SettingsListener();
		define.addActionListener(sl);
		okayButton.addActionListener(sl);
		JPanel p0 = new JPanel();
		p0.setBorder(new TitledBorder("Settings"));
		p0.add(maxCharacters);
		p0.add(maxCharactersField);
		p0.add(cutOffWindow);
		p0.add(cutOffWindowField);
		p0.add(newTranslation);
		p0.add(sourceTrans);
		p0.add(new JLabel("-->"));
		p0.add(destTrans);
		p0.add(define);
		p0.add(okayButton);
		add(p0, BorderLayout.CENTER);
		setSize(new Dimension(320,180));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	class SettingsListener implements ActionListener {
		
		//Assume only gets the save event
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okayButton) {
				int maxCharactersInt = Integer.valueOf(maxCharactersField.getText());
				int cutOffWindowInt = Integer.valueOf(cutOffWindowField.getText());
				listenManager.setCutOff(cutOffWindowInt);
				listenManager.setMaxChars(maxCharactersInt);
				frame.setVisible(false);
				frame.dispose();
			} else if (e.getSource() == define) {
				String one = sourceTrans.getText();
				String two = destTrans.getText();
				Dictionary.writeDictionaryEntry(one, two);
				listenManager.reDefine();
			}
		}
		
	}
	
	
}
