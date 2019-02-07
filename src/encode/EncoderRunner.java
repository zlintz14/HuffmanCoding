package encode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import decode.CanonicalHuffmanTree;

public class EncoderRunner {
	
	public static void main(String[] args) throws RuntimeException, IOException {
		EncoderReaderAndWriter readWrite = new EncoderReaderAndWriter();
		readWrite.readSymbolsAndDetermineProbabilities();
				
		MinVarianceHuffmanTree minVarianceTree = new MinVarianceHuffmanTree(readWrite.symsToProbability);
		minVarianceTree.makeMinVarianceTree(null);
		
		CanonicalHuffmanTree canonicalTree = new CanonicalHuffmanTree();
		List<MinVarTreeSymbols> listOutEncode = minVarianceTree.symToLength;
		int size = minVarianceTree.symToLength.size();
		int treeHeight = minVarianceTree.root.treeHeight;
		minVarianceTree = null;
		for(int i = 0; i < size; i++) {
			char currSym = listOutEncode.get(i).symbol;
			int currSymLength = listOutEncode.get(i).codewordLength;
			boolean result = canonicalTree.insert(canonicalTree.root, currSymLength, currSym);
			if(!result) {
				throw new RuntimeException("Insertion into canonical Huffman Tree failed");
			}
		}
		
		canonicalTree.bfsCanonicalTreeAndGetEncodings(treeHeight);
		List<OutputSymbolsEncoder> outSymsList = changeMinVarTreeSymsToOutputSyms(listOutEncode);
		
		try {
			readWrite.encodeTextFileAndWriteOutput(canonicalTree.root, outSymsList, canonicalTree.symsEncodings);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<OutputSymbolsEncoder> changeMinVarTreeSymsToOutputSyms(List<MinVarTreeSymbols> list) {
		List<OutputSymbolsEncoder> newList = new ArrayList<OutputSymbolsEncoder>();
		for(MinVarTreeSymbols obj: list) {
			newList.add(new OutputSymbolsEncoder(obj.symbol, obj.codewordLength));
		}
		return newList;
	}

}
