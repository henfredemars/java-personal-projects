package pack.org;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

class BNodeIndexer extends BoardIndexer implements Iterable<BNodeIndexer>,
											Iterator<BNodeIndexer> {

	protected boolean endOfList = false; //Iterator
	
	BNodeIndexer(int y_literal, int x_literal, byte[][] data) {
		super(y_literal,x_literal,data);
		if (y%2!=0||x%2!=0) {
			throw new BoardIndexerException("BNodeIndexer: Must start on a BNode!");
		} else if (data.length<5 || data[0].length<5) {
			throw new BoardIndexerException("BNodeIndexer: Board must be at least 2x2");
		} else if (y>data.length-1 || x>data[0].length-1) {
			throw new BoardIndexerException("BNodeIndexer: BNode is outside the board");
		}
	}
	
	BNodeIndexer(BNodeIndexer bi) {
		this(bi.y,bi.x,bi.data);
	}
	
	BNodeIndexer deepcopy() {
		return new BNodeIndexer(this);
	}
	
	void set(byte value) {
		throw new BoardIndexerException("BNodeIndexer: BNode value is a constant");
	}
	
	byte get() {
		return (byte)numConnected();
	}
	
	int getXLiteral() {
		return x;
	}
	
	int getYLiteral() {
		return y;
	}
	
	ArrayList<BNodeIndexer> getManhattanNodes() {
		ArrayList<BNodeIndexer> nodes = new ArrayList<BNodeIndexer>(4);
		if (y>0) nodes.add(getTopBNode());
		if (y<data.length-1) nodes.add(getBottomBNode());
		if (x>0) nodes.add(getLeftBNode());
		if (x<data[0].length-1) nodes.add(getRightBNode());
		return nodes;
	}
	
	int numConnected() {
		int count = 0;
		if (getTopConnector()==Board.CONNECTOR_SET) count++;
		if (getBottomConnector()==Board.CONNECTOR_SET) count++;
		if (getLeftConnector()==Board.CONNECTOR_SET) count++;
		if (getRightConnector()==Board.CONNECTOR_SET) count++;
		return count;
	}
	
	ArrayList<SquareIndexer> getAdjacentSquares() {
		ArrayList<SquareIndexer> adjacent = new ArrayList<SquareIndexer>(4);
		if (Board.inBoard(y-1,x-1,data))
			adjacent.add(new SquareIndexer(y-1,x-1,data));
		if (Board.inBoard(y+1,x-1,data))
			adjacent.add(new SquareIndexer(y+1,x-1,data));
		if (Board.inBoard(y-1,x+1,data))
			adjacent.add(new SquareIndexer(y-1,x+1,data));
		if (Board.inBoard(y+1,x+1,data))
			adjacent.add(new SquareIndexer(y+1,x+1,data));
		return adjacent;
	}
	
	void disconnectFrom(BNodeIndexer other) {
		if (lookUp()==Board.CONNECTOR_SET && getTopBNode().equals(other)) {
			clearTopConnector(); return;
		}
		if (lookDown()==Board.CONNECTOR_SET && getBottomBNode().equals(other)) {
			clearBottomConnector(); return;
		}
		if (lookLeft()==Board.CONNECTOR_SET && getLeftBNode().equals(other)) {
			clearLeftConnector(); return;
		}
		if (lookRight()==Board.CONNECTOR_SET && getRightBNode().equals(other)) {
			clearRightConnector(); return;
		}
	}
	
	boolean connectedTo(BNodeIndexer other) {
		if (lookUp()==Board.CONNECTOR_SET && getTopBNode().equals(other)) return true;
		if (lookDown()==Board.CONNECTOR_SET && getBottomBNode().equals(other)) return true;
		if (lookLeft()==Board.CONNECTOR_SET && getLeftBNode().equals(other)) return true;
		if (lookRight()==Board.CONNECTOR_SET && getRightBNode().equals(other)) return true;
		return false;
	}
	
	boolean connectToIfValid(BNodeIndexer other) {
		if (other.numConnected()>1) return false;
		if (numConnected()>1) return false;
		if (lookUp()==Board.CONNECTOR_UNSET && getTopBNode().equals(other)) {
			ArrayList<SquareIndexer> adjacent = getAdjacentSquares();
			setTopConnector();
			for (SquareIndexer square: adjacent) {
				if (square.capExceeded()) {
					clearTopConnector();
					return false;
				}
			}
			return true;
		}
		if (lookDown()==Board.CONNECTOR_UNSET && getBottomBNode().equals(other)) {
			ArrayList<SquareIndexer> adjacent = getAdjacentSquares();
			setBottomConnector();
			for (SquareIndexer square: adjacent) {
				if (square.capExceeded()) {
					clearBottomConnector();
					return false;
				}
			}
			return true;
		}
		if (lookLeft()==Board.CONNECTOR_UNSET && getLeftBNode().equals(other)) {
			ArrayList<SquareIndexer> adjacent = getAdjacentSquares();
			setLeftConnector();
			for (SquareIndexer square: adjacent) {
				if (square.capExceeded()) {
					clearLeftConnector();
					return false;
				}
			}
			return true;
		}
		if (lookRight()==Board.CONNECTOR_UNSET && getRightBNode().equals(other)) {
			ArrayList<SquareIndexer> adjacent = getAdjacentSquares();
			setRightConnector();
			for (SquareIndexer square: adjacent) {
				if (square.capExceeded()) {
					clearRightConnector();
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	void setTopConnector() {
		if (y>0) {
			data[y-1][x] = Board.CONNECTOR_SET;
		}
	}
	
	void setBottomConnector() {
		if (y<data.length-1) {
			data[y+1][x] = Board.CONNECTOR_SET;
		}
	}
	
	void setLeftConnector() {
		if (x>0) {
			data[y][x-1] = Board.CONNECTOR_SET;
		}
	}
	
	void setRightConnector() {
		if (x<data[0].length-1) {
			data[y][x+1] = Board.CONNECTOR_SET;
		}
	}
	
	void clearTopConnector() {
		if (y>0) {
			data[y-1][x] = Board.CONNECTOR_UNSET;
		}
	}
	
	void clearBottomConnector() {
		if (y<data.length-1) {
			data[y+1][x] = Board.CONNECTOR_UNSET;
		}
	}
	
	void clearLeftConnector() {
		if (x>0) {
			data[y][x-1] = Board.CONNECTOR_UNSET;
		}
	}
	
	void clearRightConnector() {
		if (x<data[0].length-1) {
			data[y][x+1] = Board.CONNECTOR_UNSET;
		}
	}
	
	byte getTopConnector() {
		if (y>0) {
			return data[y-1][x];
		} else {
			return INVALID_CHAR;
		}
	}
	
	byte getBottomConnector() {
		if (y<data.length-1) {
			return data[y+1][x];
		} else {
			return INVALID_CHAR;
		}
	}
	
	byte getLeftConnector() {
		if (x>0) {
			return data[y][x-1];
		} else {
			return INVALID_CHAR;
		}
	}
	
	byte getRightConnector() {
		if (x<data[0].length-1) {
			return data[y][x+1];
		} else {
			return INVALID_CHAR;
		}
	}
	
	BNodeIndexer getTopBNode() {
		return new BNodeIndexer(y-2,x,data);
	}
	
	BNodeIndexer getBottomBNode() {
		return new BNodeIndexer(y+2,x,data);
	}
	
	BNodeIndexer getLeftBNode() {
		return new BNodeIndexer(y,x-2,data);
	}
	
	BNodeIndexer getRightBNode() {
		return new BNodeIndexer(y,x+2,data);
	}
	
	int[] getBNodeCoordinates() {
		int[] coordinates = {y/2+1,x/2+1};
		return coordinates;
	}

	boolean moveUp() {
		if (y>0) {
			y -= 2;
			return true;
		} else {
			return false;
		}
	}

	boolean moveLeft() {
		if (x>0) {
			x -= 2;
			return true;
		} else {
			return false;
		}
	}

	boolean moveRight() {
		if (x<data[0].length-1) {
			x += 2;
			return true;
		} else {
			return false;
		}
	}

	boolean moveDown() {
		if (y<data.length-1) {
			y += 2;
			return true;
		} else {
			return false;
		}
	}

	byte lookUp() {
		if (y>0) {
			return data[y-1][x];
		} else {
			return INVALID_CHAR;
		}
	}

	byte lookDown() {
		if (y<data.length-1) {
			return data[y+1][x];
		} else {
			return INVALID_CHAR;
		}
	}

	byte lookLeft() {
		if (x>0) {
			return data[y][x-1];
		} else {
			return INVALID_CHAR;
		}
	}

	byte lookRight() {
		if (x<data[0].length-1) {
			return data[y][x+1];
		} else {
			return INVALID_CHAR;
		}
	}
	
	public Iterator<BNodeIndexer> iterator() {
		return new BNodeIndexer(this);
	}

	public boolean hasNext() {
		return !endOfList;
	}

	public BNodeIndexer next() {
		if (!hasNext()) throw new NoSuchElementException();
		BNodeIndexer pastItem = new BNodeIndexer(this);
		if (!moveRight()) {
			x = 0;
			if (!moveDown()) {
				endOfList = true;
			}
		}
		return pastItem;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
