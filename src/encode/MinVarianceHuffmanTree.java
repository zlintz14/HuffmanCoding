package encode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import decode.Node;

public class MinVarianceHuffmanTree {

	protected Node root;
	private boolean treeIsEmpty;
	protected List<MinVarTreeSymbols> symToLength;
	private List<Node> listNodes;
	private final static double EPSILON = 0.000001;
	
	public MinVarianceHuffmanTree(List<Node> listNodes) {
		root = null;
		treeIsEmpty = true;
		symToLength = new ArrayList<MinVarTreeSymbols>();
		this.listNodes = listNodes;
		
	}
	
	public void makeMinVarianceTree(Node parent) {
		double nodesProbability;
		Node par;
		Node lowestProbNode = listNodes.get(0);
			
		if(parent != null && 1 - (lowestProbNode.symProbability + parent.symProbability) <= EPSILON) {
			nodesProbability = lowestProbNode.symProbability + parent.symProbability; 
			Node secondLowestProbNode = listNodes.get(1);
			if(lowestProbNode.symbol != Character.MIN_VALUE) {	
				lowestProbNode.isLeaf = true;
			}
			if(secondLowestProbNode.symbol != Character.MIN_VALUE) {
				secondLowestProbNode.isLeaf = true;
			}
			
			par = new Node(Character.MIN_VALUE, nodesProbability);
			setParent(par, lowestProbNode, secondLowestProbNode);

			root = par;
			mapSymsToCodeWordLength();
			Collections.sort(symToLength);
		} else if(treeIsEmpty) {
			Node nodeLeft = lowestProbNode;
			nodeLeft.isLeaf = true;
			Node nodeRight = listNodes.get(1);
			nodeRight.isLeaf = true;
			
			nodesProbability = nodeRight.symProbability + nodeLeft.symProbability;
			par = new Node(Character.MIN_VALUE, nodesProbability);
			setParent(par, nodeLeft, nodeRight);

			treeIsEmpty = false;
			makeMinVarianceTree(par);
		} else {
			Node secondLowestProbNode = listNodes.get(1);
			nodesProbability = lowestProbNode.symProbability + secondLowestProbNode.symProbability; 
			if(lowestProbNode.symbol != Character.MIN_VALUE) {	
				lowestProbNode.isLeaf = true;
			}
			if(secondLowestProbNode.symbol != Character.MIN_VALUE) {
				secondLowestProbNode.isLeaf = true;
			}
			par = new Node(Character.MIN_VALUE, nodesProbability);
			setParent(par, lowestProbNode, secondLowestProbNode);
			makeMinVarianceTree(par); 
		}

	}
	
	private void setParent(Node par, Node nL, Node nR) {
		par.leftChild = nL;
		par.rightChild = nR;
		par.incrementTreeHeights();
		
		listNodes.remove(nL);
		listNodes.remove(nR);
		listNodes.add(par);
		Collections.sort(listNodes);
	}
	
	private void mapSymsToCodeWordLength() {
		int height = root.treeHeight;
		
		for(int i = 1; i <= height; i++) {
			setSymToCodeWordLength(root, i, 0);
		}
	}
	
	private void setSymToCodeWordLength(Node root, int level, int lengthToWordCount) {
		if(root == null) {
			return;
		} else if(level == 0 && root.isLeaf) {
			symToLength.add(new MinVarTreeSymbols(lengthToWordCount, root.symbol, root.symProbability));
		} else if(level > 0){
			int lengthToWordLeft = lengthToWordCount, lengthToWordRight = lengthToWordCount;
			int levelLeft = level, levelRight = level;
			setSymToCodeWordLength(root.leftChild, --levelLeft, ++lengthToWordLeft);
			setSymToCodeWordLength(root.rightChild, --levelRight, ++lengthToWordRight);
		}
	}
	
}
