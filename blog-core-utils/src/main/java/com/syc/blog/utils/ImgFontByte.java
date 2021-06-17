package com.syc.blog.utils;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @description 验证码图片生成
 * 
 * @Author SYC
 *
 */
public class ImgFontByte {
	public Font getFont(int fontHeight) {
		try {
			Font baseFont = Font.createFont(Font.TRUETYPE_FONT,new ByteArrayInputStream(hex2Byte(getFontByteStr())));
			return baseFont.deriveFont(Font.PLAIN, fontHeight);
		} catch (Exception e) {
			return new Font("Arial", Font.PLAIN, fontHeight);
		}
	}

	private byte[] hex2Byte(String str) {
		if (str == null)
			return null;
		str = str.trim();
		int len = str.length();
		if (len == 0 || len % 2 == 1)
			return null;
		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
				b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
			}
			return b;
		} catch (Exception e) {
			return null;
		}
	}

	private String getFontByteStr() {
		String path = ImgFontByte.class.getResource("/").getPath().substring(1);
		String result = null;
		try {
			File file = new File(path + "font64.txt");
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
			StringBuilder sb = new StringBuilder();
			BufferedReader bufferReader = new BufferedReader(reader);
			String lineText = null;
			while ((lineText = bufferReader.readLine())!=null){
				sb.append(lineText);
			}
			bufferReader.close();
			reader.close();
			result = sb.toString();
		}catch (Exception ex){
			System.out.println("验证码生成失败:"+ex);
		}
		return result;
	}

}