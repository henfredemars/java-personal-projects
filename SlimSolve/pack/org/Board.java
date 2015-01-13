package pack.org;

//The game board, using an array-based representation
class Board {

	static final byte BNODE = 'b';
	static final byte CONNECTOR_UNSET = 'c';
	static final byte CONNECTOR_SET = 'C';
	static final byte SQUARE_UNSET = 'U';
	static final short MAX_SIZE = Short.MAX_VALUE;
	final byte[][] data;
	protected BoardIndexer cursor;
	protected int score;

	Board(int y, int x) {
		cursor = null;
		score = -1;
		int sy = y*2+1;
		int sx = x*2+1;
		data = new byte[sy][sx];
		for (int i=0;i<sx;i++) {
			for (int j=0;j<sy;j++) {
				if (j%2==0) {
					if (i%2==0) {
						data[j][i]=BNODE;
					} else {
						data[j][i]=CONNECTOR_UNSET;
					}
				} else {
					if (i%2==0) {
						data[j][i]=CONNECTOR_UNSET;
					} else {
						data[j][i]=SQUARE_UNSET;
					}
				}
			}
		}
	}

	Board(byte[][] data, BoardIndexer cursor) {
		this.data = data;
		this.cursor = cursor;
		this.score = -1;
	}

	Board deepcopy() {
		byte[][] dataCopy = new byte[data.length][data[0].length];
		for (int i = 0; i<data.length; i++) {
			for (int j = 0; j<data[0].length; j++) {
				dataCopy[i][j] = data[i][j];
			}
		}
		BoardIndexer newCursor = null;
		if (cursor != null) {
			newCursor = cursor.deepcopy();
			newCursor.setData(dataCopy);
		}
		return new Board(dataCopy,newCursor);
	}

	SquareIndexer makeSquareIndexer() {
		return new SquareIndexer(1,1,data);
	}

	BNodeIndexer makeBNodeIndexer() {
		return new BNodeIndexer(0,0,data);
	}

	BoardIndexer getCursor() {
		return cursor;
	}

	static boolean inBoard(int y, int x, byte[][] data) {
		return (y<=data.length-1 && x<=data[0].length-1 && y>0 && x>0);
	}

	void setCursor(BoardIndexer cursor) {
		this.cursor = cursor;
	}

	int getX() {
		return (data[0].length-1)/2;
	}

	int getY() {
		return (data.length-1)/2;
	}
	
	int getScore() {
		return score;
	}
	
	boolean winAndScore() {
		SquareIndexer squareIndexer = makeSquareIndexer();
		int score = 0;
		for (SquareIndexer square: squareIndexer) {
			byte sval = square.get();
			if (sval!=Board.SQUARE_UNSET) {
				if (sval=='3') {
					score += ((int)(sval-'0')-square.eval())*3;
				} else {
					score += (int)(sval-'0')-square.eval();
				}
			}
		}
		this.score = score;
		if (score!=0) return false;
		BNodeIndexer nodeIndexer = makeBNodeIndexer();
		for (BNodeIndexer node: nodeIndexer) {
			int num = node.numConnected();
			if (num != 0 && num != 2) {
				return false;
			}
		}
		return true;
	}

	boolean win() {
		SquareIndexer squareIndexer = makeSquareIndexer();
		for (SquareIndexer square: squareIndexer) {
			byte sval = square.get();
			if (square.eval()!=(int)(sval-'0') && sval!=Board.SQUARE_UNSET) {
				return false;
			}
		}
		BNodeIndexer nodeIndexer = makeBNodeIndexer();
		for (BNodeIndexer node: nodeIndexer) {
			int num = node.numConnected();
			if (num != 0 && num != 2) {
				return false;
			}
		}
		return true;
	}

	void clear() {
		SquareIndexer squareIndexer = makeSquareIndexer();
		for (SquareIndexer square: squareIndexer) {
			square.clearTopConnector();
			square.clearBottomConnector();
			square.clearLeftConnector();
			square.clearRightConnector();
		}
	}

	void printMoves() {
		SquareIndexer squareIndexer = makeSquareIndexer();
		for (SquareIndexer square: squareIndexer) {
			int[] coordinates = square.getSquareCoordinates();
			String coordString = "(" + coordinates[0] + "," + coordinates[1] + ")";
			if (square.getTopConnector()==Board.CONNECTOR_SET)
				System.out.println("Set top of square " + coordString);
			if (square.getBottomConnector()==Board.CONNECTOR_SET)
				System.out.println("Set bottom of square " + coordString);
			if (square.getLeftConnector()==Board.CONNECTOR_SET)
				System.out.println("Set left of square " + coordString);
			if (square.getRightConnector()==Board.CONNECTOR_SET)
				System.out.println("Set right of square " + coordString);
		}
	}

	public String toString() {
		StringBuilder buf = new StringBuilder(1600);
		buf.append("Board:\n\n   ");
		for (int j=0;j<data[0].length;j++)
			if (j%2==1) buf.append(String.valueOf((j+1)/2) + ' ');
		buf.append('\n');
		for (int i=0;i<data.length;i++) {
			if (i%2==0) buf.append("  ");
			else buf.append(" ");
			for (int j=0;j<data[0].length;j++) {
				if (j==0 && i%2==1) buf.append(String.valueOf((i+1)/2));
				if (data[i][j]==BNODE) {
					buf.append("*");
				} else if (data[i][j]==CONNECTOR_UNSET) {
					buf.append(" ");
				} else if (data[i][j]==CONNECTOR_SET) {
					if (i%2==0) {
						buf.append("-");
					} else {
						buf.append("|");
					}
				} else if (data[i][j]==SQUARE_UNSET) {
					buf.append(" ");
				} else {
					buf.append((char)data[i][j]);
				}
			}
			buf.append("\n");
		}
		buf.append("\n");
		return buf.toString();
	}

}
