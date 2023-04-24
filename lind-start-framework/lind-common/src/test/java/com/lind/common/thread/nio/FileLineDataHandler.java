package com.lind.common.thread.nio;

import java.io.UnsupportedEncodingException;

public class FileLineDataHandler implements DataProcessHandler {

	private String encode = "UTF-8";

	@Override
	public void process(byte[] data) {
		try {
			System.out.println(new String(data, encode).toString());
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
