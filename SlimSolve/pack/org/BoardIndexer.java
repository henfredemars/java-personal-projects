package pack.org;

//Superclass of Square and BNode, indexes into the board
abstract class BoardIndexer {
	
	static final byte INVALID_CHAR = '~';

	protected int y,x;
	protected byte[][] data;
	
	BoardIndexer(int y_literal, int x_literal, byte[][] data) {
		y = y_literal;
		x = x_literal;
		this.data = data;
	}
	
	void setData(byte[][] data) {
		this.data = data;
	}
	
	byte get() {
		return data[y][x];
	}
	
	abstract void set(byte value);
	
	abstract boolean moveUp();
	abstract boolean moveLeft();
	abstract boolean moveRight();
	abstract boolean moveDown();
	
	abstract byte lookUp();
	abstract byte lookDown();
	abstract byte lookLeft();
	abstract byte lookRight();

	abstract BoardIndexer deepcopy();
	
	public boolean equals(BoardIndexer other) {
		return (y==other.y && x==other.x);
	}
	
}
