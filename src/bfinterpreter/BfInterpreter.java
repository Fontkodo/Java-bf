package bfinterpreter;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

public class BfInterpreter {
	
	final byte[] codeString;
	
	BfInterpreter(InputStream is) throws IOException {
		this.codeString = getCodeBytes(is);
	}
	
	private static int[] getBrackets(byte[] codeString) {
		int[] brackets = new int[codeString.length];
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = 0; i < codeString.length; i++) {
			if (codeString[i] == '[') {
				stack.push(i);
			} else if (codeString[i] == ']') {
				int current = stack.pop();
				brackets[current] = i;
				brackets[i] = current;
			}
		}
		return brackets;
	}
	
	private static int[] charRepeats(byte[] codeString) {
		int[] charRepeats = new int[codeString.length];
		for (int i = (codeString.length - 2); i >= 0; i--) {
			if (codeString[i] == codeString[i+1]) {
				charRepeats[i] = (1 + charRepeats[i+1]);
			}
		}
		return charRepeats;
	}
	
	void execute() throws IOException {
		byte[] tape = new byte[30000];
		int tp = 0;
		int[] brackets = getBrackets(this.codeString);
		int[] repeats = charRepeats(this.codeString);
		for (int i = 0; i < codeString.length; i++) {
			switch(codeString[i]) {
			case '+':
				tape[tp] += (repeats[i] + 1);
				i += repeats[i];
				break;
			case '-':
				tape[tp] -= (repeats[i] + 1);
				i += repeats[i];
				break;
			case '>':
				tp = ((tp + (repeats[i] + 1)) % 30000);
				i += repeats[i];
				break;
			case '<':
				tp = ((tp - (repeats[i] + 1)) % 30000);
				i += repeats[i];
				break;
			case ',':
				{
					int c = System.in.read();
					if (c == -1) {
						throw new RuntimeException("Reading past end of file");
					}
					tape[tp] = (byte) c;
				}
				break;
			case '.':
				System.out.write(tape[tp]);
				break;
			case '[':
				if (tape[tp] == 0) {
					i = brackets[i];
				}
				break;
			case ']':
				if (tape[tp] != 0) {
					i = brackets[i];
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
		if (args.length < 1) {
			throw new RuntimeException("No file provided");
		}
		BfInterpreter bfi = new BfInterpreter(new BufferedInputStream(new FileInputStream(args[0])));
		long l = System.currentTimeMillis();
		bfi.execute();
		System.out.println("The interpreter took " + (System.currentTimeMillis() - l) + " milliseconds.");
	}

}
