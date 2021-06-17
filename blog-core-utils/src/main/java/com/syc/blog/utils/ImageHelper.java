package com.syc.blog.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class ImageHelper {

    /**
     * 压缩过程
     * @param f 图片对象
     * */
    public static File press(File f,Integer targetWidth) throws IOException {
        String suffix = f.getName().substring(f.getName().lastIndexOf(".") + 1);//拿到原始文件格式
        BufferedImage image = ImageIO.read(f);
        //获取图片原来的宽和高
        int width = image.getWidth();
        int height = image.getHeight();
        if(targetWidth == null) {  //如果没有目标宽度，那就用原来图片的宽度
            targetWidth = width;
        }
        //暂时把计算出来的高四舍五入
        double temp=height*1.0*targetWidth/width;
        BigDecimal decimal = new BigDecimal(String.valueOf(temp)).setScale(0, BigDecimal.ROUND_HALF_UP);
        int TARGET_HEIGHT = decimal.intValue();//计算出目标高度
        Image scaledInstance = image.getScaledInstance(targetWidth, TARGET_HEIGHT, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(targetWidth, TARGET_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(scaledInstance, 0, 0, null); // 绘制处理后的图
        g.dispose();
        ImageIO.write(tag,suffix,f);//覆盖源文件
        return f;
    }

}
