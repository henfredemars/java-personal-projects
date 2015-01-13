package pack.org;

import java.util.InputMismatchException;
import java.util.Scanner;

final class SlimSolve {

	private Board board;

	SlimSolve() {
		board = null;
	}

	void runGame(Scanner scanner) {
		try { 
			while (true) {
				System.out.println(board);
				System.out.print("[M]ake connection, [C]lear connection, [R]estart, [I]inspect for win, [Q]uit: ");
				String command = scanner.next();
				scanner.nextLine(); //Consume other input
				if (command.equalsIgnoreCase("M")) {
					System.out.print("Enter square ROW COL SIDE: ");
					String line = scanner.nextLine();
					String[] lines = line.split(" ");
					if (lines.length!=3) {
						System.out.println("Incorrect number of terms or bad spacing (use single space).");
						continue;
					}
					int srow = Integer.valueOf(lines[0].trim());
					int scol = Integer.valueOf(lines[1].trim());
					SquareIndexer square = SquareIndexer.fromSquare(srow,scol,board.data);
					String command_s = lines[2].trim();
					if (square==null) {
						System.out.println("Invalid square.");
						continue;
					}
					if (command_s.equalsIgnoreCase("T")) {
						square.setTopConnector();
						System.out.println("Top connector has been set.");
					} else if (command_s.equalsIgnoreCase("B")) {
						square.setBottomConnector();
						System.out.println("Bottom connector has been set.");
					} else if (command_s.equalsIgnoreCase("L")) {
						square.setLeftConnector();
						System.out.println("Left connector has been set.");
					} else if (command_s.equalsIgnoreCase("R")) {
						square.setRightConnector();
						System.out.println("Right connector has been set.");
					} else {
						System.out.println("Failed to interpret command.");
					}
				} else if (command.equalsIgnoreCase("C")) {
					System.out.print("Enter square ROW COL SIDE: ");
					String line = scanner.nextLine();
					String[] lines = line.split(" ");
					if (lines.length!=3) {
						System.out.println("Incorrect number of terms or bad spacing (use single space).");
						continue;
					}
					int srow = Integer.valueOf(lines[0].trim());
					int scol = Integer.valueOf(lines[1].trim());
					SquareIndexer square = SquareIndexer.fromSquare(srow,scol,board.data);
					String command_s = lines[2].trim();
					if (square==null) continue;
					if (command_s.equalsIgnoreCase("T")) {
						square.clearTopConnector();
						System.out.println("Top connector has been cleared.");
					} else if (command_s.equalsIgnoreCase("B")) {
						square.clearBottomConnector();
						System.out.println("Bottom connector has been cleared.");
					} else if (command_s.equalsIgnoreCase("L")) {
						square.clearLeftConnector();
						System.out.println("Left connector has been cleared.");
					} else if (command_s.equalsIgnoreCase("R")) {
						square.clearRightConnector();
						System.out.println("Right connector has been cleared.");
					} else {
						System.out.println("Failed to interpret command.");
					}
				} else if (command.equalsIgnoreCase("R")) {
					board.clear();
					System.out.println("The board has been cleared and is ready for re-use.");
				} else if (command.equalsIgnoreCase("Q")) {
					System.out.println("Thanks for using this program! Exiting...");
					System.exit(0);
				} else if (command.equalsIgnoreCase("I")) {
					if (board.win()) {
						System.out.println("You win!");
						board.printMoves();
						return;
					} else {
						System.out.println("Sorry, but this board isn't solved... yet.");
					}
				} else {
					System.out.println("Failed to interpret command.");
				}
			}
		} catch (InputMismatchException e) {
			System.out.println("The input is not of the expected type.");
			runGame(scanner);
		} catch (NumberFormatException e) {
			System.out.println("The input is not of the expected format or type.");
			runGame(scanner);
		} catch (BoardIndexerException e) {
			System.out.println("Invalid location or set value is not allowed.");
			runGame(scanner);
		}
	}

	Board defineBoard(Scanner scanner) {
		Board resultBoard = null;
		System.out.println("Now defining the game board!");
		try {
			boolean haveHeight = false;
			int y = -1;
			while (!haveHeight) {
				System.out.print("Enter board height: ");
				y = scanner.nextInt();
				if (y < 2) {
					System.out.println("The board must be at least a 2x2");
				} else if (y > Board.MAX_SIZE) {
					System.out.println("Height is too large for the solver!");
				} else {
					haveHeight = true;
				}
			}
			System.out.println("Board will be of height " + y);
			boolean haveWidth = false;
			int x = -1;
			while (!haveWidth) {
				System.out.print("Enter board width: ");
				x = scanner.nextInt();
				if (x < 2) {
					System.out.println("The board must be at least a 2x2");
				} else if (x > Board.MAX_SIZE) {
					System.out.println("Width is too large for the solver!");
				} else {
					haveWidth = true;
				}
			}
			System.out.println("Board will be of width " + x);
			resultBoard = new Board(y,x);
			while (true) {
				System.out.println(resultBoard);
				System.out.print("[S]et square value, [F]inish: ");
				String command = scanner.next();
				if (command.equalsIgnoreCase("S")) {
					System.out.print("Enter square row: ");
					int row = scanner.nextInt();
					System.out.print("Enter square column: ");
					int col = scanner.nextInt();
					System.out.print("Enter square value (-1 for unset): ");
					int cap = scanner.nextInt();
					SquareIndexer square = SquareIndexer.fromSquare(row,col,resultBoard.data);
					byte capArg;
					if (cap<0) {
						capArg = Board.SQUARE_UNSET;
					} else {
						capArg = (byte) (cap+'0');
					}
					square.set(capArg);
				} else if (command.equalsIgnoreCase("F")) {
					System.out.println("Finalizing board...");
					return resultBoard;
				} else {
					System.out.println("Unrecognized command.");
				}
			}
		} catch (InputMismatchException e) {
			System.out.println("The input is not of the expected type.");
		} catch (BoardIndexerException e) {
			System.out.println("Invalid location or set value is not allowed.");
		}
		return null; //Shouldn't happen
	}

	void beginInteraction() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("[D]efine board, [L]oad board (recommended), [S]tart Game, [A]uto Solve, [Q]uit: ");
			String command = scanner.next();
			if (command.equalsIgnoreCase("D")) {
				board = defineBoard(scanner);
			} else if (command.equalsIgnoreCase("L")) {
				System.out.print("Enter a file name in the current directory: ");
				String fileName = scanner.next();
				board = GameIO.parseFile(fileName); //Messages will be printed within
				if (board!=null) System.out.println(board);
			} else if (command.equalsIgnoreCase("A")) {
				if (board==null) {
					System.out.println("The board is not defined. Define or load a board first.");
					continue;
				} else {
					Solver ohMyHeart = new Solver(new SNode(board));
					long clock = System.currentTimeMillis();
					SNode solution = ohMyHeart.solve(); //SPARTA!!!
					double totalTime = (System.currentTimeMillis()-clock)/1000.0;
					if (solution!=null) {
						System.out.println(solution);
						solution.printMoves();
						System.out.println("Total runtime: " + totalTime + " seconds");
					} else {
						System.out.println("No solution was found.");
					}
					System.out.println("Explored " + ohMyHeart.getNodeCount() + " nodes.");
				}
			} else if (command.equalsIgnoreCase("S")) {
				if (board != null) {
					runGame(scanner);
					board.clear();
				} else {
					System.out.println("The board is not defined. Define or load a board first.");
				}
			} else if (command.equalsIgnoreCase("Q")) {
				System.out.println("Thanks for using this program! Exiting...");
				System.exit(0);
			} else {
				System.out.println("The command was not recognized.");
			}
		}
	}

	void run() {
		String msg = "\nWelcome to SlitherSolve by James Birdsong!\n" +
				"This program was designed to play and, at the conclusion of " +
				"\n\tthe AI undergraduate class, solve effectively games of " +
				"\n\tany size.\n\nPlease wait while the virtual machine initializes...";
		System.out.println(msg);
		beginInteraction();
	}

}
