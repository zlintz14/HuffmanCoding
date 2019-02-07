package decode;

public class InputSymbolsDecoder implements Comparable<InputSymbolsDecoder> {

	public int codewordLength;
	public char symbol;
	
	public InputSymbolsDecoder(int codewordLength, char symbol) {
		this.codewordLength = codewordLength;
		this.symbol = symbol;
	}

	@Override
	public int compareTo(InputSymbolsDecoder o) {
		if(codewordLength != o.codewordLength) {
			return codewordLength - o.codewordLength;
		} else {
			return (int) symbol - (int) o.symbol;
		}
	}
	
	
}
