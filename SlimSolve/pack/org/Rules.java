package pack.org;

//Rules for deductive and inductive subsystems

final class Rules {
	
	private Rules() {}
	
	static void applyAllDeductive(Board board) {
		cornerRules(board);
		threeAndZeroRules(board);
		parallelThrees(board);
	}
	
	//Rules of deduction -- do not depend on previous moves
	//Rules of induction -- WILL depend on previous moves
	static void cornerRules(Board board) { //Pure deductive
		int x = board.getX();
		int y = board.getY();
		SquareIndexer topLeft = board.makeSquareIndexer();
		SquareIndexer bottomRight = SquareIndexer.fromSquare(y,x,board.data);
		SquareIndexer bottomLeft = SquareIndexer.fromSquare(y,1,board.data);
		SquareIndexer topRight = SquareIndexer.fromSquare(1,x,board.data);
		//Corner is a 3
		if (topLeft.getCap()==3) {
			topLeft.setTopConnector();
			topLeft.setLeftConnector();
		}
		if (bottomRight.getCap()==3) {
			bottomRight.setBottomConnector();
			bottomRight.setRightConnector();
		}
		if (bottomLeft.getCap()==3) {
			bottomLeft.setBottomConnector();
			bottomLeft.setLeftConnector();
		} 
		if (topRight.getCap()==3) {
			topRight.setTopConnector();
			topRight.setRightConnector();
		}
		//Corner is a 2
		if (topLeft.getCap()==2) {
			SquareIndexer.fromSquare(1,2,board.data).setTopConnector();
			SquareIndexer.fromSquare(2,1,board.data).setLeftConnector();
		} 
		if (bottomRight.getCap()==2) {
			SquareIndexer.fromSquare(y-1,x,board.data).setRightConnector();
			SquareIndexer.fromSquare(y,x-1,board.data).setBottomConnector();
		} 
		if (bottomLeft.getCap()==2) {
			SquareIndexer.fromSquare(y-1,1,board.data).setLeftConnector();
			SquareIndexer.fromSquare(y,2,board.data).setBottomConnector();
		} 
		if (topRight.getCap()==2) {
			SquareIndexer.fromSquare(2,x,board.data).setRightConnector();
			SquareIndexer.fromSquare(1,x-1,board.data).setTopConnector();
		}
	}
	
	static void threeAndZeroRules(Board board) { //Pure deductive
		for (SquareIndexer sq: board.makeSquareIndexer()) {
			try {
				if (sq.getCap()!=3) continue;
				//Square and a zero
				if (sq.lookDown()==0) {
					sq.setTopConnector();
					sq.setLeftConnector();
					sq.setRightConnector();
					SquareIndexer leftSquare = sq.getLeftSquare();
					SquareIndexer rightSquare = sq.getRightSquare();
					leftSquare.setBottomConnector();
					rightSquare.setBottomConnector();
				} else if (sq.lookUp()==0) {
					sq.setBottomConnector();
					sq.setLeftConnector();
					sq.setRightConnector();
					SquareIndexer leftSquare = sq.getLeftSquare();
					SquareIndexer rightSquare = sq.getRightSquare();
					leftSquare.setTopConnector();
					rightSquare.setTopConnector();
				} else if (sq.lookLeft()==0) {
					sq.setTopConnector();
					sq.setBottomConnector();
					sq.setRightConnector();
					SquareIndexer topSquare = sq.getTopSquare();
					SquareIndexer bottomSquare = sq.getBottomSquare();
					topSquare.setLeftConnector();
					bottomSquare.setLeftConnector();
				} else if (sq.lookRight()==0) {
					sq.setLeftConnector();
					sq.setTopConnector();
					sq.setBottomConnector();
					SquareIndexer topSquare = sq.getTopSquare();
					SquareIndexer bottomSquare = sq.getBottomSquare();
					topSquare.setRightConnector();
					bottomSquare.setRightConnector();
				}
				//Square and zero on diagonal
				SquareIndexer topLeft = sq.getLeftSquare().getTopSquare();
				SquareIndexer bottomLeft = sq.getLeftSquare().getBottomSquare();
				SquareIndexer topRight = sq.getRightSquare().getTopSquare();
				SquareIndexer bottomRight = sq.getRightSquare().getBottomSquare();
				if (topLeft.getCap()==0) {
					sq.setLeftConnector();
					sq.setTopConnector();
				} else if (bottomLeft.getCap()==0) {
					sq.setBottomConnector();
					sq.setLeftConnector();
				} else if (topRight.getCap()==0) {
					sq.setTopConnector();
					sq.setRightConnector();
				} else if (bottomRight.getCap()==0) {
					sq.setBottomConnector();
					sq.setRightConnector();
				}
			} catch (BoardIndexerException e) {}
		}
	}
	
	static void parallelThrees(Board board) { //Deductive
		for (SquareIndexer sq: board.makeSquareIndexer()) {
			try {
				if (sq.getCap()!=3) continue;
				SquareIndexer leftSquare = sq.getLeftSquare();
				SquareIndexer rightSquare = sq.getRightSquare();
				SquareIndexer topSquare = sq.getTopSquare();
				SquareIndexer bottomSquare = sq.getBottomSquare();
				if (leftSquare.getCap()==3) {
					sq.setLeftConnector();
					sq.setRightConnector();
					leftSquare.setLeftConnector();
				} else if (rightSquare.getCap()==3) {
					sq.setLeftConnector();
					sq.setRightConnector();
					rightSquare.setRightConnector();
				} else if (topSquare.getCap()==3) {
					sq.setTopConnector();
					sq.setBottomConnector();
					topSquare.setTopConnector();
				} else if (bottomSquare.getCap()==3) {
					sq.setTopConnector();
					sq.setBottomConnector();
					bottomSquare.setBottomConnector();
				}
			} catch (BoardIndexerException e) {}
		}
	}

}
