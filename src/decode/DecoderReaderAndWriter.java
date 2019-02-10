package decode;

import java.io.IOException;

import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import io.InputStreamBitSource;
import io.InsufficientBitsLeftException;

public class DecoderReaderAndWriter {
	
	protected List<InputSymbolsDecoder> inSymbolsList;
	private int asciiDecimalVal;
	private InputStream in;
	private InputStreamBitSource inBitSource;
	
	
	public DecoderReaderAndWriter() throws FileNotFoundException {
		inSymbolsList = new ArrayList<InputSymbolsDecoder>();
		asciiDecimalVal = 0;
		in = new FileInputStream(System.getProperty("user.dir") + "/Data/encoded.dat");
		inBitSource = new InputStreamBitSource(in);
	}
	
	public void readAndSortFile() {
		while(asciiDecimalVal < 256) {	
			try {
				inSymbolsList.add(new InputSymbolsDecoder(inBitSource.next(8), (char) asciiDecimalVal));
				asciiDecimalVal++;
			} catch(InsufficientBitsLeftException e) { 
				e.printStackTrace();
				break;
			} catch(Exception e) {
				e.printStackTrace();
				break;
			}
		}	
		Collections.sort(inSymbolsList);
	}
	
	public void decodeCompressedFileAndWriteOutput(Node root) throws IOException {
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Paths.get("").toAbsolutePath().toString() + "/Data/decoded.txt"), "utf-8"));
		int remainingSyms = read4BytesAfterInital256();
		for(int i = 0; i < remainingSyms; i++) {
			Node n = root;
			while(!n.isLeaf) {
				try {
					int bit = inBitSource.next(1);
					if(bit == 0) {
						n = n.leftChild;
					} else {
						n = n.rightChild;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			writer.write(n.symbol);
		}
		in.close();
		writer.close();
	}
	
	private int read4BytesAfterInital256() {
		String totalInBinary = "";
		for(int i = 0; i < 4; i++) {
			try {
				totalInBinary += String.format("%8s", Integer.toBinaryString(inBitSource.next(8) & 0xFF)).replace(' ', '0');
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Integer.parseInt(totalInBinary, 2);
	}
	
	
}
