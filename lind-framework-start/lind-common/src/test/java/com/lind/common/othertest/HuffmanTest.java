package com.lind.common.othertest;

import com.lind.common.encrypt.HashUtils;
import com.lind.common.zip.BetterHuffman;
import com.lind.common.zip.HumanByte;
import lombok.SneakyThrows;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class HuffmanTest {

	@Test
	public void huffmanByte() {
		HumanByte humanByte = new HumanByte();
		String code = "DBDCDDDEDFDGDHDIFPGGFPDCDADCDADADGDBDCFPEEFPDBDADAFPFGDG";
		String huffmanCode = humanByte.encode(code.getBytes());
		System.out.println("huffman:" + huffmanCode);
		System.out.println("code:" + new String(humanByte.decode(huffmanCode)));

	}

	@SneakyThrows
	@Test
	public void betterhuffman() {
		String code = "DBDCDDDEDFDGDHDIFPGGFPDCDADCDADADGDBDCFPEEFPDBDADAFPFGDG";
		String huffman16 = new String(BetterHuffman.compress(code), StandardCharsets.UTF_8);
		System.out.println("huffman:" + huffman16);
		// String huff = HashUtils.encryptBASE62(BetterHuffman.compress(huffman16));
		// System.out.println("huffman:" + huff);
		// System.out.println("code:" +
		// BetterHuffman.expand(HashUtils.decryptBASE62(huff)));
	}

	@Test
	public void base62() {
		System.out.println("base62:" + HashUtils.encryptBASE62("v6_12345678_D_ff_20201231_999".getBytes()));
		System.out.println("base64:" + HashUtils.encryptBASE64("v6_12345678_D_ff_20201231_999".getBytes()));
	}

}
