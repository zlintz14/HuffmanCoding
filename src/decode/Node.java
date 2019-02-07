package decode;

public class Node implements Comparable<Node> {

	public Node leftChild, rightChild, parent;
	public double symProbability;
	public boolean isFull, isLeaf;
	public char symbol;
	public int treeHeight;
	
	public Node(char symbol) {
		leftChild = null;
		rightChild = null;
		parent = null;
		isFull = false;
		isLeaf = false;
		this.symbol = symbol;
	}
		
	public Node(char symbol, double symProbability) {
		leftChild = null;
		rightChild = null;
		parent = null;
		isFull = false;
		isLeaf = false;
		treeHeight = 0;
		this.symbol = symbol;
		this.symProbability = symProbability;
	}
	
	public void setLeafLeftChild(Node leftChild) {
		this.leftChild = leftChild;
		this.leftChild.parent = this;
		this.leftChild.isLeaf = true;
		checkIsFull();
	}
	
	public void setLeafRightChild(Node rightChild) {
		this.rightChild = rightChild;
		this.rightChild.parent = this;
		this.rightChild.isLeaf = true;
		checkIsFull();
	}
	
	private void checkIsFull() {
		if((this.leftChild != null && this.rightChild != null) &&
				((this.leftChild.isLeaf && this.rightChild.isLeaf) || 
				(this.leftChild.isFull && this.rightChild.isFull) ||
				(this.leftChild.isLeaf && this.rightChild.isFull) ||
				(this.leftChild.isFull && this.rightChild.isLeaf))) {
			isFull = true;
			if(this.parent != null) {
				this.parent.checkIsFull();
			}
		}
	}
	
	public void incrementTreeHeights() {
		if(this.leftChild.treeHeight > this.rightChild.treeHeight) {
			this.treeHeight = this.leftChild.treeHeight + 1;
		} else {
			this.treeHeight = this.rightChild.treeHeight + 1;
		}
	}
	
	@Override
	public int compareTo(Node o) {
		if(Math.abs(symProbability - o.symProbability) <= 0.000001) {
			return treeHeight - o.treeHeight;
		} else {
			return symProbability > o.symProbability ? 1: -1;
		}
	}
	
}
