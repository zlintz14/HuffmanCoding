package decode;

import java.util.HashMap;
import java.util.Map;


public class CanonicalHuffmanTree {

	public Node root;
	public Map<Character, String> symsEncodings;
	
	public CanonicalHuffmanTree() {
		root = new Node(Character.MIN_VALUE);
		symsEncodings = new HashMap<Character, String>();
	}
	
	public boolean insert(Node n, int codewordLength, char symbol) {
		Node temp;
		if(codewordLength == 1 && n.leftChild == null) {
			temp = new Node(symbol);
			n.setLeafLeftChild(temp);
			return true;
		} else if(codewordLength == 1 && n.rightChild == null) {
			temp = new Node(symbol);
			n.setLeafRightChild(temp);
			return true;
		} else if(n.leftChild == null) {
			temp = new Node(Character.MIN_VALUE);
			n.leftChild = temp;
			n.leftChild.parent = n;
			return insert(n.leftChild, --codewordLength, symbol);
		} else if(!n.leftChild.isFull && !n.leftChild.isLeaf) {
			return insert(n.leftChild, --codewordLength, symbol);
		} else if(n.rightChild == null) {
			temp = new Node(Character.MIN_VALUE);
			n.rightChild = temp;
			n.rightChild.parent = n;
			return insert(n.rightChild, --codewordLength, symbol);
		} else if(!n.rightChild.isFull && !n.rightChild.isLeaf) {
			return insert(n.rightChild, --codewordLength, symbol);
		}
		return false;
	}
	
	public void bfsCanonicalTreeAndGetEncodings(int treeHeight) {
		
		for(int i = 1; i <= treeHeight; i++) {
			getSymbolEncodings(root, i, "");
		}
	}
	
	/* method is just for Decoder since it does not know its tree height like encoder does, 
	 * but tree height is necessary for setting up variables for Entropy Calculations
	 */
	protected int findHeight(Node node)  
    { 
        if (node == null) 
            return 0; 
        else 
        { 
            int lDepth = findHeight(node.leftChild); 
            int rDepth = findHeight(node.rightChild); 
   
            if (lDepth > rDepth) 
                return (lDepth + 1); 
             else 
                return (rDepth + 1); 
        } 
    } 
	
	private void getSymbolEncodings(Node root, int level, String symEncoding) {
		if(root == null) {
			return;
		} else if(level == 0 && root.isLeaf) {
			symsEncodings.put(root.symbol, symEncoding);
		} else if(level > 0){
			String symEncodingLeft = symEncoding + "0", symEncodingRight = symEncoding + "1";
			int levelLeft = level, levelRight = level;
			getSymbolEncodings(root.leftChild, --levelLeft, symEncodingLeft);
			getSymbolEncodings(root.rightChild, --levelRight, symEncodingRight);
		}
	}
	
	
}
