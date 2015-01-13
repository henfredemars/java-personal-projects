package TextCompress;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

//Handle actions
class ListenManager implements ActionListener {
	static String endl;
	int charactersPerMessage = 160, cutOffWindow = 10;
	JButton client;
	JTextArea input;
	JTextArea output;
	JButton about;
	JButton settings;
	ArrayList<String> symbolStore;
	HashMap<String,String> dictionary;
	
	ListenManager(JButton button, JTextArea input, JTextArea output, 
			JButton about, JButton settings) {
		super();
		client = button;
		this.input = input;
		this.output = output;
		this.about = about;
		this.settings = settings;
		endl = System.getProperty("line.separator");
		symbolStore = new ArrayList<String>(5);
		dictionary = Dictionary.readDictionary();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == client)
			processText(input, output);
		else if (e.getSource() == about)
			displayAbout();
		else if (e.getSource() == settings) {
			displaySettings();
		} else
			System.out.println("Event recieved, but nothing to do.");
	}
	
	void setMaxChars(int chars) {
		charactersPerMessage = chars;
	}
	
	int getMaxChars() {
		return charactersPerMessage;
	}
	
	void setCutOff(int cut) {
		cutOffWindow = cut;
	}
	
	int getCutOff() {
		return cutOffWindow;
	}
	
	void processText(JTextArea input, JTextArea output) {
		String source = input.getText();
		
		//Pre-processing
		source = deleteApostropheAndComma(source);
		source = filterNewlines(source);
		source = cleanSpaces(source);
		
		//Break text into phrases
		ArrayList<String> phrases = new ArrayList<String>();
		int phraseStart = 0;
		for (int i = 0; i < source.length(); i++) {
			int n = i + 1;
			boolean foundPhrase = false;
			if (isSymbol(source.charAt(i))) {
				foundPhrase = true;
				while (n < source.length() && isSymbolWithSpaces(source.charAt(n)))
					n++;
			} //n minus i Symbols exist between i and n
			if (foundPhrase) {
				phrases.add(source.substring(phraseStart, n));
				phraseStart = n;
				i += n - i - 1;
			} else if (i == source.length()-1) {
				phrases.add(source.substring(phraseStart, i + 1));
			}
		}
		
		//Process phrases
		for (int i = 0; i < phrases.size(); i++) {
			phrases.add(i, processPhrase(phrases.remove(i)));
		}
		
		//Build messages
		String written = "";
		int charsLeft = charactersPerMessage;
		while (phrases.size() > 0) {
			if ((phrases.get(0)).length() <= charsLeft) {
				String phrase = phrases.remove(0);
				charsLeft -= phrase.length(); 
				written += phrase;
				continue;
			} else {
				if (charsLeft <= cutOffWindow && 
						(phrases.get(0)).length() <= charactersPerMessage) {
					written += endl + endl;
					String phrase = phrases.remove(0);
					written += phrase;
					charsLeft = charactersPerMessage - phrase.length();
					continue;
				}
				//Force the phrase to fit
				String[] phraseChunks = splitPhrase(phrases.remove(0));
				for (int i = 0; i < phraseChunks.length; i++) {
					if (charsLeft - phraseChunks[i].length() >= 0 ) {
						written += phraseChunks[i];
						charsLeft -= phraseChunks[i].length();
					} else {
						if (phraseChunks[i].length() <= charactersPerMessage) {
							written += endl + endl;
							written += phraseChunks[i];
							charsLeft = charactersPerMessage - phraseChunks[i].length();
						} else {
							char[] toBeForced = phraseChunks[i].toCharArray();
							for (int j = 0; j < toBeForced.length; j++) {
								if (charsLeft > 0) {
									written += toBeForced[j];
									charsLeft--;
								} 
								else {
									written += endl + endl;
									charsLeft = charactersPerMessage-1;
									written += toBeForced[j];
								}
							}
						}
					}
				}
			}
			
		}
		
		output.setText(written);
		
	}
	
	//Strip symbols to be placed in the store for a single word
	public String stripSymbols(String in) {
		char[] chars = in.toCharArray();
		int count = 0;
		while (isSymbol(chars[in.length()-1]) && count <= chars.length-1 && in.length() > 1) {
			count++; //Restrict the possibility of infinite loops (for bug hunting)
			symbolStore.add(String.valueOf(chars[chars.length-1]));
			in = in.substring(0,chars.length-1);
			chars = in.toCharArray();
		}
		return in;
	}
	
	public String cleanSpaces(String in) {
		char[] chars = in.toCharArray();
		StringBuilder result = new StringBuilder(200);
		for (int i = 0; i < chars.length-1 ; i++) {
			if (!((chars[i] == ' ') && (chars[i+1] == ' '))) {
				result.append(chars[i]);
			}
		}
		result.append(chars[chars.length-1]);
		return result.toString();
	}
	
	//Get symbols from store for a single word
	public String restoreSymbols(String in) {
		for (int i = symbolStore.size()-1; i >= 0; i--) {
			String toAdd = symbolStore.remove(symbolStore.size()-1);
			in += toAdd;
		}
		return in;
	}
	
	String[] splitPhrase(String in) {
		char[] p = in.toCharArray();
		ArrayList<String> phrase = new ArrayList<String>();
		int wordStart = 0;
		for (int i = 0; i<p.length-1; i++) {
			if ((String.valueOf(p[i+1]).toUpperCase().equals(String.valueOf(p[i+1])) &&
					!isSymbol(p[i+1])) || i == p.length-2) {
				String pr;
				if (i == p.length-2)
					pr = in.substring(wordStart,i+2);
				else
					pr = in.substring(wordStart,i+1); 
				phrase.add(pr);
				wordStart = i+1;
			}
		}
		String[] answer = new String[phrase.size()];
		answer = phrase.toArray(answer);
		return answer;
	}
	
	String processPhrase(String in) {
		String p = in.trim();
		String[] ps = p.split(" ");
		for (int i = 0; i < ps.length; i++) {
			ps[i] = ps[i].toLowerCase();
			ps[i] = stripSymbols(ps[i]);
			String candidate = "";
			try {
				candidate = dictionary.get(ps[i]);
			} catch (Exception e) {
				//Do nothing--just keep s
			} finally {
				if (candidate == null) {
					ps[i] = restoreSymbols(ps[i]);
					continue;
				} else {
					ps[i] = restoreSymbols(candidate);
				}
			}
		}
		String rejoined = "";
		for (String s : ps)
			rejoined += s + " ";
		char[] chars = rejoined.toCharArray();
		for (int i = 0; i < chars.length-1; i++) {
			if (chars[i] == ' ') {
				char[] d = (String.valueOf(chars[i+1]).toUpperCase()).toCharArray();
				chars[i+1] = d[0];
			} else if (i == 0) {
				char[] d = (String.valueOf(chars[i]).toUpperCase()).toCharArray();
				chars[0] = d[0];
			}
		}
		String[] ps2 = (String.valueOf(chars)).split(" ");
		String answer = "";
		for (String l : ps2)
			answer += l;
		return answer;
	}
	
	boolean isSymbol(char c) {
		char[] symbols = {'!','@','#','$','%','^','&','*','(',')','-','+','=',
				'{','}','[',']',':',';','\'','\"','<','>','?',',','.','?','/',
				'\\','|','`','~','1','2','3','4','5','6','7','8','9','0'
		};
		for (int i = 0; i < symbols.length; i++)
			if (c == symbols[i])
				return true;
		return false;
	}
	
	boolean isSymbolWithSpaces(char c) {
		char[] symbols = {'!','@','#','$','%','^','&','*','(',')','-','+','=',
				'{','}','[',']',':',';','\'','\"','<','>','?',',','.','?','/',
				'\\','|','`','~',' ','1','2','3','4','5','6','7','8','9','0'
		};
		for (int i = 0; i < symbols.length; i++)
			if (c == symbols[i])
				return true;
		return false;
	}
	
	//Handle removal of grammatical apostrophe to prevent premature phrase end
	String deleteApostropheAndComma(String input) {
		StringBuilder answer = new StringBuilder(200);
		for (int i = 0; i < input.length(); i++){
			if (i == 0) {
				answer.append(input.charAt(0));
				continue;
			}
			else if (i == input.length()) {
				answer.append(input.length());
				continue;
			} else {
				if (!((input.charAt(i) == '\'' || input.charAt(i) == ',') && !isSymbol(input.charAt(i-1))
						&& !isSymbol(input.charAt(i+1))))
					answer.append(input.charAt(i));
			}
		}
		return answer.toString();
	}
	
	String filterNewlines(String input) {
		StringBuilder answer = new StringBuilder(200);
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '\n' || input.charAt(i) == '\r')
				continue;
			answer.append(input.charAt(i));
		}
		return answer.toString();
	}
	
	void reDefine() {
		dictionary = Dictionary.readDictionary();
	}
	
	void displayAbout() {
		String message = "Press \"Convert\" to change input text into short text." + endl +
				"Press \"Settings\" to change the message length and cuttoff window." + endl +
				"Press \"About\" to return to this dialog." + endl + endl +
				"A phrase for the cutoff is marked by symbols (Ex: Period, Comma, Brace...)" + endl +
				"Apostrophes and commas are handled specially when used gramatically.";
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JOptionPane.showMessageDialog(frame, message);
	}
	
	void displaySettings() {
		new SettingsWindow(this);
	}
	
}
