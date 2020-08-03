package bfinterpreter;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BfInterpreter {
	
	final byte[] codeString;
	
	BfInterpreter(InputStream is) throws IOException {
		this.codeString = getCodeBytes(is);
	}
	
	void execute() {
		byte[] tape = new byte[30000];
		int tp = 0;
		for (int i = 0; i < codeString.length; i++) {
			switch(codeString[i]) {
			case '+':
				tape[tp]++;
				break;
			case '-':
				tape[tp]--;
				break;
			case '>':
				tp = ((tp + 1) % 30000);
				break;
			case '<':
				tp = ((tp - 1) % 30000);
				break;
			case ',':
				throw new RuntimeException("Reading is for losers");
			case '.':
				System.out.write(tape[tp]);
				break;
			case '[':
				if (tape[tp] == 0) {
					int bracketCount = 1;
					while (bracketCount != 0) {
						i++;
						if (codeString[i] == '[') {
							bracketCount++;
						} else if (codeString[i] == ']') {
							bracketCount--;
						}
					}
				}
				break;
			case ']':
				if (tape[tp] != 0) {
					int bracketCount = -1;
					while (bracketCount != 0) {
						i--;
						if (codeString[i] == '[') {
							bracketCount++;
						} else if (codeString[i] == ']') {
							bracketCount--;
						}
					}
				}
				break;
			default:
				throw new RuntimeException("Nonstandard character encountered");
			}
		}
	}
	
	static private byte[] getCodeBytes(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (int c = is.read(); c != -1; c = is.read()) {
			switch(c) {
			case '+':
			case '-':
			case '>':
			case '<':
			case ',':
			case '.':
			case '[':
			case ']':
				sb.append((char) c);
				break;
			default:
				break;
			}
		}
		return sb.toString().getBytes();
	}
	
	public static void main(String[] args) throws IOException {
		BfInterpreter bfi = new BfInterpreter(new BufferedInputStream(new FileInputStream("/Users/alexgriffin/eclipse-workspace/Java-bf/src/mandelbrot.bf")));
		bfi.execute();
	}

}
