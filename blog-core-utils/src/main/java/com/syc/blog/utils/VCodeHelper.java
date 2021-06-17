package com.syc.blog.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * 验证码生成器
 * */
public class VCodeHelper {

	private int width = 160;
	private int height = 40;
	private int codeCount = 4;
	private int lineCount = 10;
	private String code = null;
	private BufferedImage buffImg = null;

	private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
			'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public VCodeHelper(int codeCount) {
		this.codeCount=codeCount;
	}

	/*生成非图片验证码*/
	public String createNumCode() {
		Random random = new Random();
		StringBuilder code=new StringBuilder();
		for(int i=0;i<codeCount;i++) {
			String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			code.append(strRand);
		}
		return code.toString();
	}

	/**
	 * 
	 * @param width
	 *  
	 * @param height
	 * 
	 */
	public VCodeHelper(int width, int height) {
		this.width = width;
		this.height = height;
		this.createCode();
	}

	/**
	 * 
	 * @param width
	 * 
	 * @param height
	 *  
	 * @param codeCount
	 *   
	 * @param lineCount
	 *  
	 */
	public VCodeHelper(int width, int height, int codeCount, int lineCount) {
		this.width = width;
		this.height = height;
		this.codeCount = codeCount;
		this.lineCount = lineCount;
		this.createCode();
	}

	private void createCode() {
		int x = 0, fontHeight = 0, codeY = 0;
		int red = 0, green = 0, blue = 0;

		x = width / (codeCount + 2);
		fontHeight = height - 2;
		codeY = height - 4;


		buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();

		Random random = new Random();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		ImgFontByte imgFont = new ImgFontByte();
		Font font = imgFont.getFont(fontHeight);
		g.setFont(font);
		for (int i = 0; i < lineCount; i++) {
			int xs = random.nextInt(width);
			int ys = random.nextInt(height);
			int xe = xs + random.nextInt(width / 8);
			int ye = ys + random.nextInt(height / 8);
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			g.setColor(new Color(red, green, blue));
			g.drawLine(xs, ys, xe, ye);
		}
		StringBuffer randomCode = new StringBuffer();
		for (int i = 0; i < codeCount; i++) {
			String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			g.setColor(new Color(red, green, blue));
			g.drawString(strRand, (i + 1) * x, codeY);
			randomCode.append(strRand);
		}
		code = randomCode.toString();
	}

	public void write(String path) throws IOException {
		OutputStream sos = new FileOutputStream(path);
		this.write(sos);
	}

	public void write(OutputStream os) throws IOException {
		ImageIO.write(buffImg, "png", os);
		os.close();
	}

	public BufferedImage getBuffImg() {
		return buffImg;
	}
	public String getCode() {
		return code;
	}
}
