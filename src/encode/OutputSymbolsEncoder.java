package encode;

public class OutputSymbolsEncoder implements Comparable<OutputSymbolsEncoder>{

	protected char sym;
	protected int codewordLength;
	
	public OutputSymbolsEncoder(char sym, int codewordLength) {
		this.sym = sym;
		this.codewordLength = codewordLength;
	}

	@Override
	public int compareTo(OutputSymbolsEncoder o) {
		return (int) sym - (int) o.sym;
	}
	
}
