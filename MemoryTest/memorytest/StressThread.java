package memorytest;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.math.BigInteger;

public class StressThread implements Runnable {
	boolean go = true;
	JTextArea text = null;
	BigInteger count = new BigInteger("0");
	JFrame frame = null;
	Runtime r = Runtime.getRuntime();
	double usedmem = 0;
	boolean collected = false;

	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			//Do nothing
		}
		for (int i = 1; true ; i++) {
			//System.out.println("Loop... " + i);
			if (go == false) break;
			if (i % 200000 == 0) {
				double newusedmem = (r.totalMemory()-r.freeMemory())/(1024*1024);
				if (newusedmem < usedmem) {
					collected = true;
				} else {
					collected = false;
				}
				usedmem = newusedmem;
			}
			if (i % 7000 == 0) {
				count = count.add(new BigInteger("7000"));
				String s = "\n\n Using " + usedmem + " Mbytes of memory!";
				if (collected) {
					s += "\n\n Collected!";
				}
				text.setText("Created " + count.toString() + " array objects!" + s);
				frame.repaint();
			}
			int[] array = new int[2000];
		}
	}

}
