package pack.org;

import java.util.ArrayList;
import java.util.PriorityQueue;

class Solver {

	final protected PriorityQueue<SNode> frontier;
	protected long nodeCount;
	
	Solver(SNode root) {
		frontier = new PriorityQueue<SNode>(20000);
		frontier.add(root);
		nodeCount = 0;
	}
	
	long getNodeCount() {
		return nodeCount;
	}
	
	void clearCounter() {
		nodeCount = 0L;
	}
	
	SNode solve() {
		while (true) {
			nodeCount++;
			SNode interRoot = frontier.poll();
			//System.out.println(interRoot);
			if (interRoot==null) {
				System.out.println("Reached end of frontier. Ending search...");
				return null;
			}
			ArrayList<SNode> newFrontierNodes = interRoot.explore();
			for (SNode newNode: newFrontierNodes) {
				if (newNode.win()) {
					return newNode;
				}
			}
			frontier.addAll(newFrontierNodes);
		}
	}
}
