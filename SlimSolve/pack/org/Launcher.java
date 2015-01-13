package pack.org;

public class Launcher {

	public static void main(String[] args) {
		boolean noerror = true;
		try {
			String versionString = System.getProperty("java.specification.version");
			double version = Double.valueOf(versionString);
			if (version<1.7) {
				System.out.println("WARNING! Old (unsupported) Java version detected!");
				System.out.println("Please use Oracle Java 7 or later.");
			}
		} catch (Exception e) {
			noerror = false;
			System.out.println("Unable to determine Java version.");
		}
		if (noerror) {
			System.out.println("Java version detected.");
		}
		try {
			System.out.println("Processor identifier: " + System.getenv("PROCESSOR_IDENTIFIER"));
		} catch (Exception e) {
			//Do nothing
		}
		SlimSolve ss = new SlimSolve();
		ss.run();
	}

}
