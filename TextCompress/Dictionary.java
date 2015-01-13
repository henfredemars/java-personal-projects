package TextCompress;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Dictionary {
	static String fileName = "dictionary";
	
	private Dictionary() {
		System.out.println("Not for instantiation");
	}
	
	public static void writeDictionaryEntry(String one, String two) {
		String superString = one + ":" + two + System.getProperty("line.separator");
		FileWriter fw;
		try {
			fw = new FileWriter(fileName,true);
			fw.write(superString);
			fw.close();
			info("Translation addition complete!");
		} catch (Exception e) {
			error("Something went wrong while writing the new entry.");
		}
	}
	
	public static HashMap<String,String> readDictionary() {
		File file = new File(fileName);
		HashMap<String,String> answer = new HashMap<String,String>();
		if (!file.exists()) {
			System.out.println("Dictionary file does not exist.");
			error("Dictionary file does not exist.");
			System.out.println(file.getAbsolutePath());
			return answer;
		}
		Scanner input = null;
		try{
			input = new Scanner(file);
			while (input.hasNextLine()) {
				String line = input.nextLine();
				line = line.trim();
				if (line.equals("")) continue;
				if (line.charAt(0) == '#') continue;
				String[] entry = line.split(":");
				entry[0] = entry[0].trim();
				entry[0] = entry[0].toLowerCase();
				entry[1] = entry[1].trim();
				//entry[1] = entry[1].toLowerCase(); Allowing dictionary capitals
				answer.put(entry[0], entry[1]);
			}		
		} catch (Exception e) {
			System.out.println("Error reading dictionary!");
			error("Error reading dictionary.");
			e.printStackTrace(System.out);
		} finally {
			if (input != null) input.close();
		}
		return answer;
	}
	
	public static void error(String message) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JOptionPane.showMessageDialog(frame, message,"Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void info(String message) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JOptionPane.showMessageDialog(frame, message,"Info", JOptionPane.INFORMATION_MESSAGE);
	}

}
