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
	
	private static int[] plusRepeats(byte[] codeString) {
		int[] plusRepeats = new int[codeString.length];
		int current = 0;
		for (int i = 0; i < codeString.length; i++) {
			if (codeString[i] == '+') {
				if (codeString[current] != '+') {
					current = i;
				}
				plusRepeats[current]++;
			} else {
				current = i;
			}
		}
		return plusRepeats;
	}
	
	private static int[] minusRepeats(byte[] codeString) {
		int[] minusRepeats = new int[codeString.length];
		int current = 0;
		for (int i = 0; i < codeString.length; i++) {
			if (codeString[i] == '-') {
				if (codeString[current] != '-') {
					current = i;
				}
				minusRepeats[current]++;
			} else {
				current = i;
			}
		}
		return minusRepeats;
	}
	
	private static int[] rightRepeats(byte[] codeString) {
		int[] rightRepeats = new int[codeString.length];
		int current = 0;
		for (int i = 0; i < codeString.length; i++) {
			if (codeString[i] == '>') {
				if (codeString[current] != '>') {
					current = i;
				}
				rightRepeats[current]++;
			} else {
				current = i;
			}
		}
		return rightRepeats;
	}
	
	private static int[] leftRepeats(byte[] codeString) {
		int[] leftRepeats = new int[codeString.length];
		int current = 0;
		for (int i = 0; i < codeString.length; i++) {
			if (codeString[i] == '<') {
				if (codeString[current] != '<') {
					current = i;
				}
				leftRepeats[current]++;
			} else {
				current = i;
			}
		}
		return leftRepeats;
	}
	
	void execute() {
		byte[] tape = new byte[30000];
		int tp = 0;
		int[] brackets = getBrackets(this.codeString);
		int[] plusRepeats = plusRepeats(this.codeString);
		int[] minusRepeats = minusRepeats(this.codeString);
		int[] rightRepeats = rightRepeats(this.codeString);
		int[] leftRepeats = leftRepeats(this.codeString);
		for (int i = 0; i < codeString.length; i++) {
			switch(codeString[i]) {
			case '+':
				tape[tp] += plusRepeats[i];
				i += (plusRepeats[i] - 1);
				break;
			case '-':
				tape[tp] -= minusRepeats[i];
				i += (minusRepeats[i] - 1);
				break;
			case '>':
				tp = ((tp + rightRepeats[i]) % 30000);
				i += (rightRepeats[i] - 1);
				break;
			case '<':
				tp = ((tp - leftRepeats[i]) % 30000);
				i += (leftRepeats[i] - 1);
				break;
			case ',':
				throw new RuntimeException("Reading is for losers");
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
		BfInterpreter bfi = new BfInterpreter(new BufferedInputStream(new FileInputStream("/Users/alexgriffin/eclipse-workspace/Java-bf/src/mandelbrot.bf")));
		long l = System.currentTimeMillis();
		bfi.execute();
		System.out.println("The interpreter took " + (System.currentTimeMillis() - l) + " milliseconds.");
	}

}
