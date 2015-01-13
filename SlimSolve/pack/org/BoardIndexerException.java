package pack.org;

//Thrown by BoardIndexers
class BoardIndexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	BoardIndexerException(String msg) {
		super(msg);
	}

}
