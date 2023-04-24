package com.lind.common.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.PriorityQueue;

/**************************************************************************************************************
 * Used edu.princeton.cs version of huffman algorithm and made it easily usable outside of
 * example context replaced proprietary priority queue by Java standard one created binary
 * output/input that has minimal features needed
 *
 * If human readable encoding of huffman encoded string is desired then look at HumanByte
 * class that was posted here
 *
 * https://stackoverflow.com/questions/4141317/how-to-convert-byte-array-into-human-readable-format/58332208#58332208
 *
 * @author Stan Sokolov 10/9/19
 **************************************************************************************************************/
public class BetterHuffman {

	private static final int R = 256;

	private static final char PARENT = '\u0000';

	private static final char EMPTY = '\u0001';

	private static final int UNDEFINED = -1;

	/**************************************************************************************************************
	 * Compress string in bytes
	 **************************************************************************************************************/
	public static byte[] compress(final String s) {

		final HuffmanOut binaryOut = new HuffmanOut();
		final char[] input = s.toCharArray();
		final int[] freq = new int[R];

		for (char anInput1 : input) {
			++freq[anInput1];
		}

		final BetterHuffman.Node root = buildTrie(freq);
		final String[] st = new String[R];
		buildCode(st, root, "");

		writeTrie(root, binaryOut);
		binaryOut.write(input.length);

		for (char anInput : input) {
			final String code = st[anInput];
			for (char ch : code.toCharArray()) {
				binaryOut.writeBit(ch == '1');
			}
		}

		return binaryOut.value();
	}

	/**************************************************************************************************************
	 * build huffman tree
	 **************************************************************************************************************/
	private static BetterHuffman.Node buildTrie(int[] freq) {
		final PriorityQueue<BetterHuffman.Node> pq2 = new PriorityQueue<>();

		for (char i = 0; i < R; ++i) {
			if (freq[i] > 0) {
				// pq.insert(new BetterHuffman.Node(i, freq[i], null, null));
				pq2.add(new BetterHuffman.Node(i, freq[i], null, null));
			}
		}

		if (pq2.size() == 1) {// if entire string is just one char repeated
			if (freq[0] == 0) {// empty string
				pq2.add(new BetterHuffman.Node(PARENT, 0, null, null));
			}
			else {
				pq2.add(new BetterHuffman.Node(EMPTY, 0, null, null));
			}
		}
		else
			while (pq2.size() > 1) {
				final BetterHuffman.Node left = pq2.poll();
				final BetterHuffman.Node right = pq2.poll();
				// aggregate two nodes into one by summing frequency
				final BetterHuffman.Node parent = new BetterHuffman.Node(PARENT, left.freq + right.freq, left, right);
				pq2.add(parent);
			}

		// this will be the root node that would have total length of input as frequency
		return pq2.poll();
	}

	/**************************************************************************************************************
	 * write tree into byte output
	 **************************************************************************************************************/
	private static void writeTrie(final BetterHuffman.Node x, final HuffmanOut binaryOut) {
		if (x.isLeaf()) {// if this is a node representing symbol in alphabet
			binaryOut.writeBit(true);
			binaryOut.writeByte((int) x.ch);
		}
		else {
			binaryOut.writeBit(false); // this is an aggregate node used for branching
			writeTrie(x.left, binaryOut);
			writeTrie(x.right, binaryOut);
		}
	}

	/**************************************************************************************************************
	 * make substitutes for incoming words
	 **************************************************************************************************************/
	private static void buildCode(final String[] st, final BetterHuffman.Node x, final String s) {
		if (!x.isLeaf()) {
			buildCode(st, x.left, s + '0');
			buildCode(st, x.right, s + '1');
		}
		else {
			st[x.ch] = s;
		}
	}

	/**************************************************************************************************************
	 * Return uncompressed string
	 **************************************************************************************************************/
	public static String expand(final byte[] value) {

		final HuffmanIn binaryIn = new HuffmanIn(value);
		final StringBuilder out = new StringBuilder();

		final BetterHuffman.Node root = readTrie(binaryIn);
		final int length = binaryIn.readInt();

		for (int i = 0; i < length; ++i) {
			BetterHuffman.Node x = root;

			while (!x.isLeaf()) {
				boolean bit = binaryIn.readBoolean();
				if (bit) {
					x = x.right;
				}
				else {
					x = x.left;
				}
			}

			out.append(x.ch);
		}

		return out.toString();
	}

	/**************************************************************************************************************
	 * get tree from bytes
	 **************************************************************************************************************/
	private static BetterHuffman.Node readTrie(final HuffmanIn binaryIn) {
		boolean isLeaf = binaryIn.readBoolean();
		if (isLeaf) {
			char ch = binaryIn.readChar();
			return new BetterHuffman.Node(ch, UNDEFINED, null, null);
		}
		else {
			return new BetterHuffman.Node(PARENT, UNDEFINED, readTrie(binaryIn), readTrie(binaryIn));
		}
	}

	/**************************************************************************************************************
	 * Simple implementation of node
	 **************************************************************************************************************/
	private static class Node implements Comparable<Node> {

		private final char ch;

		private final int freq;

		private final Node left;

		private final Node right;

		Node(char ch, int freq, Node left, Node right) {
			this.ch = ch;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}

		private boolean isLeaf() {
			return left == null && right == null;
		}

		@Override
		public int compareTo(Node that) {
			return this.freq - that.freq;
		}

	}

	/**************************************************************************************************************
	 * class to read bits from stream
	 **************************************************************************************************************/
	private static class HuffmanIn {

		private final ByteArrayInputStream in;

		private int buffer;

		private byte n;

		HuffmanIn(final byte[] input) {
			in = new ByteArrayInputStream(input);
			fillBuffer();
		}

		private void fillBuffer() {
			buffer = in.read();
			n = 8;
		}

		boolean readBoolean() {
			boolean bit = (buffer >> --n & 1) == 1;
			if (n == 0) {
				fillBuffer();
			}
			return bit;
		}

		char readChar() {
			int x = buffer <<= 8 - n;
			if (n == 8) {
				fillBuffer();
			}
			else {
				byte oldN = n;
				fillBuffer();
				n = oldN;
				x |= buffer >>> n;
			}
			return (char) (x & 255);
		}

		int readInt() {
			int x = 0;
			for (int i = 0; i < 4; ++i) {
				char c = readChar();
				x <<= 8;
				x |= c;
			}
			return x;
		}

	}

	/**************************************************************************************************************
	 * Output
	 **************************************************************************************************************/
	private static class HuffmanOut {

		private ByteArrayOutputStream out = new ByteArrayOutputStream();

		private int buffer;

		private byte n;

		/**************************************************************************************************************
		 * @return what was compressed so far in a human readable (no funny characters)
		 * format
		 **************************************************************************************************************/
		public byte[] value() {
			clearBuffer();
			return out.toByteArray();
		}

		void writeBit(final boolean bit) {
			buffer = (buffer <<= 1) | (bit ? 1 : 0);
			if (++n == 8) {
				clearBuffer();
			}
		}

		void writeByte(final int x) {
			for (int i = 0; i < 8; ++i) {
				writeBit((x >>> 8 - i - 1 & 1) == 1);
			}
		}

		void clearBuffer() {
			if (n != 0) {
				out.write(buffer <<= 8 - n);
				n = 0;
				buffer = 0;
			}
		}

		/**************************************************************************************************************
		 * write all 4 bytes of int
		 **************************************************************************************************************/
		void write(final int x) {
			for (int i = 3; i >= 0; i--)
				writeByte(x >>> (i * 8) & 255);// write 4 bytes of int
		}

	}

}