package com.lind.common.zip;

import org.slf4j.Logger;

/**************************************************************************************************************
 * Convert bytes into human readable string using provided alphabet. If alphabet size 62
 * chars and byte chunk is 5 bytes then we need 7 symbols to encode it. So input size will
 * be increased by 40% plus 7 symbols to encode length of input
 *
 * Basically we do conversion from base255 (byte can has 255 different symbols) to base of
 * a a size of the given alphabet
 *
 * @author Stan Sokolov 10/9/19
 **************************************************************************************************************/
public class HumanByte {

	final static Logger logger = org.slf4j.LoggerFactory.getLogger(HumanByte.class);

	// those are chars we use for encoding
	private final static String DEFAULT_ALPHABET = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

	private char[] ALPHABET;

	private int BASE;

	private int[] POSITIONS;

	private int CHUNK;

	private int PW;

	private long[] POWERS; // {916132832, 14776336, 238328, 3844, 62, 1};

	private long[] MASKS; // (0xFF000000L & (input[0] << 24)) | (0xFF0000L & input[1] <<
							// 16) | (0xFF00L & input[2] << 8) | (0xFFL & input[3]);

	/**************************************************************************************************************
	 * Default constructor, with default alphabet and default chunk
	 **************************************************************************************************************/
	public HumanByte() {
		this(DEFAULT_ALPHABET, 5);
	}

	/**************************************************************************************************************
	 * Setup encoding using provided alphabet and chunk size
	 **************************************************************************************************************/
	public HumanByte(final String alphabet, int chunk) {
		if (chunk > 7) {
			chunk = 7;
		}
		if (chunk < 1) {
			chunk = 1;
		}
		this.ALPHABET = alphabet.toCharArray();
		BASE = alphabet.length();

		CHUNK = chunk;
		long MX = (long) Math.pow(255, CHUNK);
		PW = logBase(MX);
		int max = 0;
		for (int i = 0; i < ALPHABET.length; i++) {
			if (max < ALPHABET[i])
				max = ALPHABET[i];
		}
		POSITIONS = new int[max + 1];
		logger.debug("BASE={}, MX={}, PW={}", BASE, MX, PW);
		for (int i = 0; i < ALPHABET.length; i++) {
			POSITIONS[ALPHABET[i]] = i;
		}
		POWERS = new long[PW]; // these are the powers of base to split input number into
								// digits of its base
		for (int i = 0; i < PW; i++) {
			POWERS[i] = (long) Math.pow(BASE, PW - i - 1);
		}
		MASKS = new long[CHUNK];
		for (int i = 0; i < CHUNK; i++) { // this is how we are going to extract
											// individual bytes from chunk
			MASKS[i] = (0xFFL << ((CHUNK - i - 1) * 8));
		}
	}

	/**************************************************************************************************************
	 * take bytes, split them in group by CHUNK, encode each group in PW number of
	 * alphabet symbols.
	 **************************************************************************************************************/
	public String encode(final byte[] input) {
		final StringBuilder output = new StringBuilder(); // will save output string here
		output.append(word(input.length)); // first write length of input into output to
											// know exact size

		byte[] byte_word;
		for (int i = 0; i < input.length;) {
			byte_word = new byte[CHUNK];
			for (int j = 0; j < CHUNK; j++) {
				if (i < input.length) {
					byte_word[j] = input[i++]; // remove negatives
				}
			}
			final long n = bytes2long(byte_word); // 1099659687880

			final char[] w = word(n);
			output.append(w);

		}
		return output.toString();
	}

	/**************************************************************************************************************
	 * decode input
	 **************************************************************************************************************/
	public byte[] decode(final String in) {
		final int size = (int) number(in.substring(0, PW).toCharArray());
		final byte[] output = new byte[size];

		int j = 0, k = in.length();
		for (int i = PW; i < k; i += PW) {
			final String w = in.substring(i, i + PW);
			final long n = number(w.toCharArray());
			for (byte b : long2bytes(n)) {
				if (j < size) {
					output[j++] = b;
				}
			}
		}
		return output;
	}

	/**************************************************************************************************************
	 * @return take 4 numbers from 0 to 255 and convert them in long
	 **************************************************************************************************************/
	private long bytes2long(byte[] input) {
		long v = 0;
		for (int i = 0; i < CHUNK; i++) {
			v |= ((long) (input[i] + 128) << (8 * (CHUNK - i - 1)) & MASKS[i]); // +128 to
																				// remove
																				// negatives
		}
		return v;
	}

	/**************************************************************************************************************
	 * @return take 4 numbers from 0 to 255 and convert them in long
	 **************************************************************************************************************/
	private byte[] long2bytes(long input) {
		final byte[] bytes = new byte[CHUNK];
		for (int i = 0; i < CHUNK; i++) {
			long x = MASKS[i] & input;
			long y = 8 * (CHUNK - i - 1);
			long z = (x >> y) - 128;
			bytes[i] = (byte) z;
		}
		return bytes;
	}

	/**************************************************************************************************************
	 * create word using given alphabet to represent given number built out of CHUNK bytes
	 **************************************************************************************************************/
	private char[] word(final long n) {
		final char[] output = new char[PW];
		long v = n;
		for (int i = 0; i < PW; i++) {
			final long pn = v / POWERS[i];
			output[i] = ALPHABET[(int) pn];
			long x = pn * POWERS[i];// 900798402816 196327857024 2368963584 16134768
									// 267696 1716 52
			v -= x;
		}
		return output;
	}

	/**************************************************************************************************************
	 * take string that contain number encoded in alphabet and return
	 **************************************************************************************************************/
	private long number(final char[] s) {
		long number = 0;
		for (int i = 0; i < PW; i++) {
			long x = (long) POSITIONS[s[i]];
			long y = POWERS[i];
			long z = x * y;
			number += z;
		}
		return number;
	}

	private int logBase(long num) {
		return (int) Math.ceil(Math.log(num) / Math.log(BASE));
	}

}