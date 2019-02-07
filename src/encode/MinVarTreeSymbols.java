package encode;

public class MinVarTreeSymbols implements Comparable<MinVarTreeSymbols> {

	protected int codewordLength;
	protected char symbol;
	protected double prob;
		
	public MinVarTreeSymbols(int codewordLength, char symbol, double prob) {
		this.codewordLength = codewordLength;
		this.symbol = symbol;
		this.prob = prob;
	}

	@Override
	public int compareTo(MinVarTreeSymbols o) {
		if(codewordLength != o.codewordLength) {
			return codewordLength - o.codewordLength;
		} else {
			return (int) symbol - (int) o.symbol;
		}
	}
				
}
