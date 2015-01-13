package pack.org;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

//Utility functions for parsing a game file
final class GameIO {
	
	private GameIO() {} //Static class should not be instantiated
	
	static Board parseFile(String fileName) {
		Board board = null;
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.US_ASCII);
		} catch (IOException e) {
			if (e instanceof NoSuchFileException) {
				System.out.println("GameIO: File not found. Did you include the extension?");
			} else {
				System.out.println("GameIO: The file may exist, but an error occured when trying to open it");
			}
			return null;
		}
		final StringBuilder buffer = new StringBuilder();
		for (int i=0; i<lines.size();i++) {
			String trimmedLine = lines.get(i).trim();
			if (trimmedLine.startsWith("#")) {
				continue;
			} else if (trimmedLine.isEmpty()) {
				continue;
			} else {
				if (trimmedLine.endsWith(",")) {
					buffer.append(trimmedLine);
				} else {
					buffer.append(trimmedLine + ",");
				}
			}
		}
		String csv = buffer.toString();
		csv = csv.substring(0,csv.length()-1); //Trailing comma
		String[] elements = csv.split(",");
		if (elements.length<2 || (elements.length-2)%3!=0) {
			System.out.println("GameIO: The file is incorrectly formatted");
			return null;
		}
		try {
			board = new Board(Integer.valueOf(elements[0].trim()),Integer.valueOf(elements[1].trim()));
			for (int i=2; i<elements.length;i+=3) {
				int row = Integer.valueOf(elements[i].trim());
				int col = Integer.valueOf(elements[i+1].trim());
				int cap = Integer.valueOf(elements[i+2].trim());
				SquareIndexer square = SquareIndexer.fromSquare(row,col,board.data);
				if (cap<0 || cap>4) {
					System.out.println("GameIO: Square value out of range");
					return null;
				}
				square.set((byte)(cap+'0'));
			}
		} catch (NumberFormatException e) {
			System.out.println("GameIO: A number was encouterd that was not formatted correctly");
			return null;
		} catch (Exception e) {
			System.out.println("GameIO: The file is incorrectly formatted");
			return null;
		}
		return board;
	}
}
