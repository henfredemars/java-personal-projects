import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Vending extends JFrame {
	String imagePath = "C:\\Users\\theuser\\Desktop\\";
	String[] imageNames = {"WaterBottle.jpg", "Soda.jpg", "Pretzel.jpg", "ChocolateBar.jpg"};
	String[] buttonLabels = {"W","S","P","C"};
	String[] toolTips = {"Water Bottle", "Soda", "Pretzel", "Chocolate Bar"};
	double[] prices = {1.5, 500, 73, 0.05};
	String[] pricesStrings = {"$1.50", "$500.00", "$73.00" ,"$0.05"};
	String dollarImageName = "Dollar.jpg";
	String dollarToolTip = "Too expensive...";
	JButton[] foodButtons;
	JButton[] selectionButtons;
	JTextField PriceAmount;
	JTextField MoneyEntry; 
	JTextField whereGetItem; 
	JTextField returnAmount; 
	MyEventListener eventListener = new MyEventListener(); 
	MyKeyListener keyListener = new MyKeyListener();
	MyMouseListener mouseListener = new MyMouseListener();
	static JPanel selections;
	JFrame ref = this; //could pass instead of increasing the scope
	
	public Vending(String title) {
		super(title);
		
		//Food display panel
		this.setFocusable(true);
		JPanel foods = new JPanel();
		foods.setBorder(new TitledBorder("Goodies"));
		foods.setLayout(new GridLayout(4,1));
		foodButtons = new JButton[imageNames.length];
		for (int i = 0; i < foodButtons.length; i++) {
			ImageIcon image = new ImageIcon(imagePath + imageNames[i]);
			image = new ImageIcon(image.getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH));
			foodButtons[i] = new JButton(image);
			foodButtons[i].setContentAreaFilled(false);
			foodButtons[i].setBorderPainted(false);
			foodButtons[i].setToolTipText(toolTips[i]);
			foodButtons[i].addActionListener(eventListener); 
			foods.add(foodButtons[i]); 
		}
		
		//Dollar
		ImageIcon image = new ImageIcon(imagePath + dollarImageName);
		image = new ImageIcon(image.getImage().getScaledInstance(200,80, java.awt.Image.SCALE_SMOOTH));
		JButton dollarButton = new JButton(image);
		dollarButton.setContentAreaFilled(false);
		dollarButton.setBorderPainted(false);
		dollarButton.setToolTipText(dollarToolTip);
		
		//Money handler
		JPanel dealerContainer = new JPanel();
		dealerContainer.setLayout(new BorderLayout());
		JPanel MoneyDealer = new JPanel();
		((FlowLayout)MoneyDealer.getLayout()).setAlignment(FlowLayout.LEFT);		
		MoneyDealer.setBorder(new TitledBorder(""));
		JLabel Price = new JLabel("Price: ");
		PriceAmount = new JTextField("Make a Selection");
		PriceAmount.setEditable(false);
		MoneyEntry = new JTextField("Enter Money Here");
		MoneyDealer.add(Price);
		MoneyDealer.add(PriceAmount);
		dealerContainer.add(MoneyDealer,BorderLayout.CENTER);
		dealerContainer.add(MoneyEntry,BorderLayout.SOUTH);
		dealerContainer.setBorder(new TitledBorder(""));
		
		//Selections panel
		selections = new JPanel();
		selections.setCursor(new Cursor(Cursor.HAND_CURSOR));
		selections.setBorder(new TitledBorder("Choose One"));
		selections.setLayout(new GridLayout(4,1));
		//selections.setFocusable(true); 
		ref.addKeyListener(keyListener);
		selectionButtons = new JButton[buttonLabels.length];
		for (int i = 0; i < selectionButtons.length; i++){
			selectionButtons[i] = new JButton(buttonLabels[i]);
			selectionButtons[i].setContentAreaFilled(false);
			selectionButtons[i].setToolTipText(toolTips[i]);
			selectionButtons[i].addActionListener(eventListener); 
			selectionButtons[i].setMnemonic(buttonLabels[i].toLowerCase().charAt(0));
			//selectionButtons[i].addMouseListener(mouseListener);
			selections.add(selectionButtons[i]);
		}
		
		ref.addMouseListener(mouseListener);
		
		returnAmount = new JTextField("Return Amount");
		returnAmount.setEditable(false);
		whereGetItem = new JTextField("This is where you get your item...");
		whereGetItem.setEditable(false); 
		whereGetItem.setPreferredSize(new Dimension(400,70));
		
		//Build Window
		add(whereGetItem, BorderLayout.SOUTH);
		add(foods, BorderLayout.WEST);
		JPanel system = new JPanel();
		system.setLayout(new BorderLayout());
		system.add(dollarButton, BorderLayout.NORTH);
		JPanel subSystem = new JPanel();
		subSystem.setLayout(new BorderLayout());
		subSystem.add(dealerContainer, BorderLayout.NORTH);
		subSystem.add(selections, BorderLayout.CENTER);
		subSystem.add(returnAmount, BorderLayout.SOUTH);
		system.add(subSystem, BorderLayout.CENTER);
		add(system, BorderLayout.EAST); 
	}
	
	public static void main(String[] args) {
		String titleString = "Front View of a Vending Machine";
		Vending machine = new Vending(titleString);
		machine.setSize(400,800);
		machine.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		machine.setVisible(true);
		//ref.requestFocusInWindow();
	}
	
	class MyMouseListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e) {
			System.out.println("Click detected in JFrame. Requesting focus in JFrame...");
			ref.requestFocusInWindow();
			//selections.requestFocusInWindow();
		}
	}
	
	class MyKeyListener implements KeyListener
	{
		public void keyPressed(KeyEvent e) {
			//Placeholder
		}

		public void keyReleased(KeyEvent e) {
			//Placeholder
		}

		public void keyTyped(KeyEvent e) {
			String inputKey = Character.toString(e.getKeyChar());
			System.out.println("Got KeyEvent: " + inputKey);
			if (inputKey.equalsIgnoreCase("W"))
				selectionButtons[0].doClick();
			else if (inputKey.equalsIgnoreCase("S"))
				selectionButtons[1].doClick();
			else if (inputKey.equalsIgnoreCase("P"))
				selectionButtons[2].doClick();
			else if (inputKey.equalsIgnoreCase("C"))
				selectionButtons[3].doClick();
		}
	}
	
	class MyEventListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent event) {
			Object source = event.getSource();
			int index = findIndex(source, foodButtons);
			if (index != -1) //Use HashMap next time
				PriceAmount.setText(pricesStrings[index]);
			else
				handlePurchase(source);
		}
	
		public void handlePurchase(Object source) {
			double money = readMoney();
			int index = findIndex(source, selectionButtons);
			if (money >= prices[index]) {
				returnAmount.setText("$" + String.format("%.2f", money - prices[index]));
				whereGetItem.setText("Here is your " + toolTips[index]);
			} else {
				whereGetItem.setText("Not enough cash...");
			}
		}
	
		private int findIndex(Object source, JButton[] array) {
			int index = -1;
			for (int i = 0; i < array.length; i++)
				if (source == array[i])
					index = i;
			return index;
		}
	
		private double readMoney() {
			double money = 0;
			if (MoneyEntry.getText().equals("Enter Money Here")) {
				money = 0;
			} else {
				try {
					money = Double.parseDouble(MoneyEntry.getText());
					MoneyEntry.setText("Enter Money Here");
				} catch (NumberFormatException E1) {
					try {
						money = Double.parseDouble(MoneyEntry.getText().substring(1));  
						MoneyEntry.setText("Enter Money Here");
					} catch (NumberFormatException E2) {
						MoneyEntry.setText("Error reading money");
						money = 0;
					}
				}		
			}
			return money;
		}
	}
}
