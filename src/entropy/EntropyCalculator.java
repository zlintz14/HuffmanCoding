package entropy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import decode.Node;

public class EntropyCalculator {

	
	private List<Node> symsToProbability;
	
	public EntropyCalculator(List<Node> symsToProbability) {
		/* make new List of symsToProbability to ensure entropy is calculated correctly in case symsToProbability is modified later,
		*  specifically in encoding, when making MinVarTree symsToProbability will be modified
		*/
		this.symsToProbability = new ArrayList<Node>(symsToProbability);
		Collections.sort(this.symsToProbability);
	}
	
	public void calculateTheoreticalEntropy() {
		double theoreticalEntropy = 0.0;
		for(Node n: symsToProbability) {
			double probability = n.symProbability;
			if(probability != 0.0) {
				//Note this calculation uses Shannon's Law and this expression is equivalent to: -probability * log_2(probability)
				theoreticalEntropy += probability * (log2(1 / probability));
			}
		}
		System.out.println("The Theoretical Entropy of the source message based off Shannon's Law is: " + theoreticalEntropy + " bits per symbol");
	}
	
	public void calculateCompressedFileEntropy(Map<Character, String> symsToEncoding) {
		double compressedEntropy = 0.0;
		for(Node n: symsToProbability) {
			char sym = n.symbol;
			double probability = n.symProbability;
			int bitLen = symsToEncoding.get(sym).length();
			compressedEntropy += probability * bitLen;
		}
		double compressionAsPercentage =  (1 - (compressedEntropy / 8.0)) * 100;
		System.out.println("The Entropy of the compressed file is: " + compressedEntropy + " bits per symbol or " + compressionAsPercentage + "% compression of the original file");
	}
	
	private double log2(double d) {
	      return Math.log(d)/Math.log(2.0);
	}
	
}
