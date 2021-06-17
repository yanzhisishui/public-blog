package com.syc.blog.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.syc.blog.constants.GlobalConstant;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具
 * */
public class QRCodeHelper {

    public static void createQRCode(int width, int height, String content,InputStream logoInput, HttpServletResponse response){
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //设置纠错级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 1);//二维码空白区域,最小为0也有白边,只是很小,最小是6像素左右
        BitMatrix bitMatrix = null;// 生成矩阵
        try {
            if(content == null || content.length() == 0){
                content= GlobalConstant.HOST_ADDRESS;
            }
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            //定义一张空白缓冲流图片
            BufferedImage image = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);

            //循环二维码字节转化对象中所有点  矩阵转image
            for (int x = 0; x < bitMatrix.getWidth(); x++){
                for (int y = 0; y < bitMatrix.getHeight(); y++){
                    // 获取其中一点，判断它是黑色还是白色   true:黑色   false:白色
                    int rgb = bitMatrix.get(x, y) ? 0x000000 : 0xffffff;
                    //  在空白图片绘制一点
                    image.setRGB(x, y, rgb);
                }
            }
            Graphics2D g2 = image.createGraphics();
            image.flush() ;
            g2.dispose();
            //加载logo
            if(logoInput != null){
                BufferedImage logo = ImageIO.read(logoInput);
                //嵌套logo
                image = LogoConfig.LogoMatrix(image, logo);
            }
            ImageIO.write(image,"png",response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static class LogoConfig{
        /**
         * 设置 logo
         * @param matrixImage 源二维码图片
         * @return 返回带有logo的二维码图片
         * @throws IOException
         * @author Administrator sangwenhao
         */
        public static BufferedImage LogoMatrix(BufferedImage matrixImage, BufferedImage logo) {
            /**
             * 读取二维码图片，并构建绘图对象
             */
            Graphics2D g2 = matrixImage.createGraphics();

            int matrixWidth = matrixImage.getWidth();
            int matrixHeigh = matrixImage.getHeight();

            //开始绘制图片
            g2.drawImage(logo,matrixWidth/5*2,matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5, null);//绘制
            BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
            g2.setStroke(stroke);// 设置笔画对象
            //指定弧度的圆角矩形
            RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth / 5 << 1, matrixHeigh / 5 << 1, matrixWidth/5, matrixHeigh/5,20,20);
            g2.setColor(Color.white);
            g2.draw(round);// 绘制圆弧矩形

            //设置logo 有一道灰色边框
            BasicStroke stroke2 = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
            g2.setStroke(stroke2);// 设置笔画对象
            RoundRectangle2D.Float round2 = new RoundRectangle2D.Float((matrixWidth / 5 << 1) +2, (matrixHeigh / 5 << 1) +2, matrixWidth/5-4, matrixHeigh/5-4,20,20);
            g2.setColor(new Color(128,128,128));
            g2.draw(round2);// 绘制圆弧矩形*/
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            matrixImage.flush() ;
            g2.dispose();
            return matrixImage ;
        }

    }


}
