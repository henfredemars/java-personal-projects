package memorytest;
import java.util.Scanner;

public class MemoryTest {
	static Scanner wait = new Scanner(System.in);

	public static void main(String[] args) {
		String s = "This program will run the following line of code:\n\n";
		s += "int[] a = new int[2000];\n\n";
		s += "Forever until you press \"STOP!\"\n\n\n";
		s += "The purpose is to create lots of objects to stress the \n";
		s += "Java garbage collector.";
		CommandWindow cw = new CommandWindow();
		cw.setText(s);
		
		
	}

}
