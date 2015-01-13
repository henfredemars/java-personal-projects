package pack.org;

import java.util.Iterator;
import java.util.NoSuchElementException;

class SquareIndexer extends BoardIndexer implements Iterable<SquareIndexer>,
											Iterator<SquareIndexer> {
	
	protected boolean endOfList = false; //Iterator

	SquareIndexer(int y_literal, int x_literal, byte[][] data) {
		super(y_literal,x_literal,data);
		if (y%2!=1 || x%2!=1) {
			throw new BoardIndexerException("SquareIndexer: Must start on a square!");
		} else if (data.length<5 || data[0].length<5) {
			throw new BoardIndexerException("SquareIndexer: Board must be at least 2x2");
		} else if (y>data.length-1 || x>data[0].length-1) {
			throw new BoardIndexerException("SquareIndexer: Square is outside the board");
		}
	}
	
	SquareIndexer(SquareIndexer si) {
		this(si.y,si.x,si.data);
	}
	
	SquareIndexer deepcopy() {
		return new SquareIndexer(this);
	}

	//Make a new indexer at the square (y,x) (one-based indexing) 
	static SquareIndexer fromSquare(int y, int x, byte[][] data) {
		return new SquareIndexer(y*2-1,x*2-1,data);
	}
	
	BNodeIndexer[] getBNodes() {
		BNodeIndexer[] nodes = new BNodeIndexer[4];
		nodes[0] = new BNodeIndexer(y-1,x-1,data);
		nodes[1] = new BNodeIndexer(y+1,x-1,data);
		nodes[2] = new BNodeIndexer(y-1,x+1,data);
		nodes[3] = new BNodeIndexer(y+1,x+1,data);
		return nodes;
	}
	
	boolean isValid(int y, int x, int cap) {
		if (y<1||y>data.length) return false;
		else if (x<1||x>data[0].length) return false;
		else if (cap<0||cap>4) return false;
		return true;
	}
	
	int getCap() {
		byte pcap = get();
		if (pcap != Board.SQUARE_UNSET)
			return (int)(get()-'0');
		else
			return 4;
	}
	
	boolean capExceeded() {
		return (eval()>getCap());
	}
	
	int eval() {
		int count = 0;
		if (getTopConnector()==Board.CONNECTOR_SET) count++;
		if (getBottomConnector()==Board.CONNECTOR_SET) count++;
		if (getLeftConnector()==Board.CONNECTOR_SET) count++;
		if (getRightConnector()==Board.CONNECTOR_SET) count++;
		return count;
	}

	void set(byte value) {
		if (value=='0'||value=='1'||value=='2'||value=='3'||value==Board.SQUARE_UNSET||
				value=='4') {
			data[y][x]=value;
		} else {
			throw new BoardIndexerException("SquareIndexer: Invalid set value");
		}
	}
	
	void setTopConnector() {
		data[y-1][x] = Board.CONNECTOR_SET;
	}
	
	void setBottomConnector() {
		data[y+1][x] = Board.CONNECTOR_SET;
	}
	
	void setLeftConnector() {
		data[y][x-1] = Board.CONNECTOR_SET;
	}
	
	void setRightConnector() {
		data[y][x+1] = Board.CONNECTOR_SET;
	}
	
	void clearTopConnector() {
		data[y-1][x] = Board.CONNECTOR_UNSET;
	}
	
	void clearBottomConnector() {
		data[y+1][x] = Board.CONNECTOR_UNSET;
	}
	
	void clearLeftConnector() {
		data[y][x-1] = Board.CONNECTOR_UNSET;
	}
	
	void clearRightConnector() {
		data[y][x+1] = Board.CONNECTOR_UNSET;
	}
	
	void unset() {
		data[y][x] = Board.SQUARE_UNSET;
	}
	
	byte getTopConnector() {
		return data[y-1][x];
	}
	
	byte getBottomConnector() {
		return data[y+1][x];
	}
	
	byte getLeftConnector() {
		return data[y][x-1];
	}
	
	byte getRightConnector() {
		return data[y][x+1];
	}
	
	SquareIndexer getTopSquare() {
		return new SquareIndexer(y-2,x,data);
	}
	
	SquareIndexer getBottomSquare() {
		return new SquareIndexer(y+2,x,data);
	}
	
	SquareIndexer getLeftSquare() {
		return new SquareIndexer(y,x-2,data);
	}
	
	SquareIndexer getRightSquare() {
		return new SquareIndexer(y,x+2,data);
	}
	
	int[] getSquareCoordinates() {
		int[] coordinates = {y/2+1,x/2+1};
		return coordinates;
	}

	boolean moveUp() {
		if (y>=3) {
			y -= 2;
			return true;
		} else {
			return false;
		}
	}

	boolean moveLeft() {
		if (x>=3) {
			x -= 2;
			return true;
		} else {
			return false;
		}
	}

	boolean moveRight() {
		if (x<=data[0].length-4) {
			x += 2;
			return true;
		} else {
			return false;
		}
	}

	boolean moveDown() {
		if (y<=data.length-4) {
			y += 2;
			return true;
		} else {
			return false;
		}
	}

	byte lookUp() {
		if (y>=3) {
			return data[y-2][x];
		} else {
			return INVALID_CHAR;
		}
	}

	byte lookDown() {
		if (y<=data.length-4) {
			return data[y+2][x];
		} else {
			return INVALID_CHAR;
		}
	}

	byte lookLeft() {
		if (x>=3) {
			return data[y][x-2];
		} else {
			return INVALID_CHAR;
		}
	}

	byte lookRight() {
		if (x<=data[0].length-4) {
			return data[y][x+2];
		} else {
			return INVALID_CHAR;
		}
	}
	
	public Iterator<SquareIndexer> iterator() {
		return new SquareIndexer(this);
	}

	public boolean hasNext() {
		return !endOfList;
	}

	public SquareIndexer next() {
		if (!hasNext()) throw new NoSuchElementException();
		SquareIndexer pastItem = new SquareIndexer(this);
		if (!moveRight()) {
			x = 1;
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
