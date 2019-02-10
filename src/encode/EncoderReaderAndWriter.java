package encode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import decode.Node;
import io.OutputStreamBitSink;

public class EncoderReaderAndWriter {

	private InputStream in;
	private Map<Character, Integer> symsToOccurences;
	protected List<Node> symsToProbability;
	private List<Character> allSymsInFile;
	private int totalNumSymsInFile;
	
	public EncoderReaderAndWriter() {
		in = null;
		symsToOccurences = new HashMap<Character, Integer>();
		symsToProbability = new ArrayList<Node>();
		allSymsInFile = new ArrayList<Character>();
		totalNumSymsInFile = 0;
	}
	
	public void readSymbolsAndDetermineProbabilities() throws IOException {
		try {
			in = new FileInputStream(Paths.get("").toAbsolutePath().toString() + "/Data/decoded.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		int symByte;
		while((symByte = in.read()) != -1) {
			if(!allSymsInFile.contains((char) symByte)) {
				allSymsInFile.add((char) symByte);
			}
			if(symsToOccurences.get((char) symByte) == null) {
				symsToOccurences.put((char) symByte, 1);
			} else {
				int symOccurence = symsToOccurences.get((char) symByte);
				symsToOccurences.put((char) symByte, ++symOccurence);
			}
			totalNumSymsInFile++;
		}
		
		symbolsToProbabilities();
		in.close();
	}
	
	public void encodeTextFileAndWriteOutput(Node root, List<OutputSymbolsEncoder> syms, Map<Character, String> symsToEncoding) throws IOException {
		OutputStream out = new FileOutputStream(Paths.get("").toAbsolutePath().toString() + "/Data/encoded.dat");
		OutputStreamBitSink outBitSink = new OutputStreamBitSink(out);
		Collections.sort(syms);
		for(int i = 0; i < 256; i++) {
			outBitSink.write(syms.get(i).codewordLength, 8);
		}
		outBitSink.write(totalNumSymsInFile, 32);
		
		try {
			in = new FileInputStream(Paths.get("").toAbsolutePath().toString() + "/Data/decoded.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int symByte;
		while((symByte = in.read()) != -1) {
			outBitSink.write(symsToEncoding.get((char) symByte));
		}
		outBitSink.padToWord();
		
		in.close();
		out.close();
	}
	
	private void symbolsToProbabilities() {
		int symsCount = 0;
		Collections.sort(allSymsInFile);
		for(int i = 0; i < 256; i++) {
			if(i == allSymsInFile.get(symsCount)) {
				char sym = allSymsInFile.get(symsCount);
				int occurencesOfSym = symsToOccurences.get(sym);
				double symProbability = (double) occurencesOfSym / totalNumSymsInFile;
				symsToProbability.add(new Node(sym, symProbability));
				if(symsCount != allSymsInFile.size() - 1) {
					symsCount++;
				}
			} else {
				symsToProbability.add(new Node((char) i, 0.0));
			}
			
		}
		
		Collections.sort(symsToProbability);
		calculateTheoreticalEntropyOfSourceMessage();
	}
	
	
	private void calculateTheoreticalEntropyOfSourceMessage() {
		double entropy = 0.0;
		for(Node n: symsToProbability) {
			double probability = n.symProbability;
			if(probability != 0.0) {
				entropy += probability * (log2(1 / probability));
			}
		}
		System.out.println("The Entropy of the Source Message is: " + entropy + " bits per symbol");
	}
	
	private double log2(double d) {
	      return Math.log(d)/Math.log(2.0);
	}
}
