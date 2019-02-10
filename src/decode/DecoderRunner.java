package decode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import encode.EncoderReaderAndWriter;

public class DecoderRunner {

	public static void main(String[] args) throws RuntimeException, FileNotFoundException {
		DecoderReaderAndWriter readWrite = new DecoderReaderAndWriter();
		try {
			readWrite.readAndSortFile();
		} catch(Exception e) {
			System.out.print(e);
		}
		
		CanonicalHuffmanTree tree = new CanonicalHuffmanTree();
		List<InputSymbolsDecoder> list = readWrite.inSymbolsList;
		for(int i = 0; i < readWrite.inSymbolsList.size(); i++) {
			InputSymbolsDecoder currInSym = list.get(i);
			boolean result = tree.insert(tree.root, currInSym.codewordLength, currInSym.symbol);
			if(!result) {
				throw new RuntimeException("Insertion into canonical Huffman Tree failed");
			}
		}
		
		try {
			readWrite.decodeCompressedFileAndWriteOutput(tree.root);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//for calculating Entropy of Source Message
		EncoderReaderAndWriter rW = new EncoderReaderAndWriter();
		try {
			rW.readSymbolsAndDetermineProbabilities();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
