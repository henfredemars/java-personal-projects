package pack.org;

import java.util.ArrayList;

//Game state
class SNode implements Comparable<SNode> {

	protected final Board board;
	
	SNode(Board board) {
		this.board = board.deepcopy();
	}
	
	int getScore() {
		return board.getScore();
	}
	
	void printBoard() {
		System.out.println(board);
	}

	void printMoves() {
		board.printMoves();
	}
	
	ArrayList<SNode> explore() {
		final ArrayList<SNode> newNodes = new ArrayList<SNode>(4);
		final BNodeIndexer cursor = (BNodeIndexer) board.getCursor();
		if (cursor==null) {
			return setCursor();
		}
		final ArrayList<BNodeIndexer> manhattanNodes = cursor.getManhattanNodes();
		for (BNodeIndexer node: manhattanNodes) {
			if (node.connectToIfValid(cursor)) {
				board.setCursor(runThroughConnected(node,cursor,null));
				newNodes.add(new SNode(board));
				node.disconnectFrom(cursor);
			}
		}
		board.setCursor(cursor);
		return newNodes;
	}
	
	BNodeIndexer runThroughConnected(BNodeIndexer current, BNodeIndexer previous,
			BNodeIndexer stopCondition) {
		if (stopCondition!=null && stopCondition.equals(current)) return current; //Circuit!
		if (stopCondition==null) stopCondition = current;
		ArrayList<BNodeIndexer> neighbors = current.getManhattanNodes();
		for (int i = 0; i<neighbors.size();i++) {
			BNodeIndexer neighbor = neighbors.get(i);
			if ((previous==null || !neighbor.equals(previous)) && neighbor.connectedTo(current)) {
				neighbors = null; //Limited tail call optimization
				return runThroughConnected(neighbor,current,stopCondition);
			}
		}
		return current;
	}
	
	boolean win() {
		return board.winAndScore();
	}
	
	ArrayList<SNode> setCursor() {
		Rules.applyAllDeductive(board);
		//System.out.println(board);
		ArrayList<SNode> moves = new ArrayList<SNode>(4);
		SquareIndexer squareIndexer = board.makeSquareIndexer();
		SquareIndexer threeSquare = null;
		SquareIndexer twoSquare = null;
		SquareIndexer oneSquare = null;
		SquareIndexer noneSquare = null;
		for (SquareIndexer square: squareIndexer) {
			if (square.getCap()==3) {
				threeSquare = square; 
			} else if (square.getCap()==2) {
				twoSquare = square;
			} else if (square.getCap()==1) {
				oneSquare = square;
			} else {
				noneSquare = square;
			}
		}
		if (threeSquare!=null) {
			BNodeIndexer[] nodes = threeSquare.getBNodes();
			board.setCursor(runThroughConnected(nodes[0],null,null));
			moves.add(new SNode(board));
		} else if (twoSquare!=null) {
			BNodeIndexer[] nodes = twoSquare.getBNodes();
			board.setCursor(runThroughConnected(nodes[0],null,null));
			moves.add(new SNode(board));
		} else if (oneSquare!=null) {
			BNodeIndexer[] nodes = oneSquare.getBNodes();
			board.setCursor(runThroughConnected(nodes[0],null,null));
			moves.add(new SNode(board));
			board.setCursor(runThroughConnected(nodes[3],null,null));
			moves.add(new SNode(board));
		} else { //Must have a trivial solution
			BNodeIndexer[] nodes = noneSquare.getBNodes();
			board.setCursor(runThroughConnected(nodes[0],null,null));
			moves.add(new SNode(board));
		}
		board.setCursor(null);
		return moves;
	}
	
	public String toString() {
		return board.toString();
	}

	public int compareTo(SNode o) {
		int myScore = getScore();
		int otherScore = o.getScore();
		if (myScore<0||otherScore<0) throw new RuntimeException("Score cannot be negative");
		return myScore-otherScore;
	}
}
