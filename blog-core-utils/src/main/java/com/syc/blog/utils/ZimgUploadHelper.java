package com.syc.blog.utils;

import com.alibaba.fastjson.JSON;
import com.syc.blog.model.ZimgResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * 上传文件到Zimg
 * */
public class ZimgUploadHelper {
    public static String uploadImageToZimg(MultipartFile file,String upload,String address,boolean needWarterMark) {
        StringBuilder respXML = new StringBuilder();
        try {
            URL url = new URL(upload);
            URLConnection connection = url.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(2500);
            HttpURLConnection uc = (HttpURLConnection) connection;
            // 设置HTTP协议的消息头
            uc.setRequestMethod("POST");
            uc.setRequestProperty("Connection", "Keep-Alive");
            uc.setRequestProperty("Cache-Control", "no-cache");
            uc.setRequestProperty("Content-Type", "png");// "jpeg");//
            uc.setRequestProperty("COOKIE", "william");
            uc.setDoOutput(true);
            uc.setDoInput(true);

            uc.connect();
            // 设置传输模式为二进制
            OutputStream om = uc.getOutputStream();
            // 循环读取图片，发送到zimg服务器
            InputStream in = file.getInputStream();
            if(needWarterMark){ //加水印
                Image srcImg = ImageIO.read(in);
                BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
                // 2、得到画笔对象
                Graphics2D g = buffImg.createGraphics();
                g.drawImage(srcImg, 0, 0,buffImg.getWidth(),buffImg.getHeight(), null);
                int mode = Font.BOLD;
                g.setFont(new Font("楷体", mode, 28));
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1));

                //计算文字的坐标位置，根据基线、高度来计算
                int logoX =buffImg.getWidth() - 150;
                int logoH = buffImg.getHeight() - g.getFont().getSize();

                //设置抗锯齿，并且先用阴影画一遍，不然字体会模糊
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);//设置抗锯齿
                g.setPaint(new Color(0, 0, 0));//阴影颜色
                g.drawString("", logoX, logoH);//先绘制阴影

                //画水印
                g.setColor(new Color(0,0,0));
                g.drawString("暮色妖娆丶", logoX, logoH);
                g.dispose();
                ImageIO.write(buffImg,"png",om);
            }
            else{
                byte[] buf = new byte[8192];
                while (true) {
                    int len = in.read(buf);
                    if (len <= 0)
                        break;
                    om.write(buf, 0, len);
                }
            }
            // 打开输入（返回信息）流
            InputStreamReader im = new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8);
            // 循环读取，结束，获取返回信息
            char[] bb = new char[8192];
            while (true) {
                int length = im.read(bb);
                if (length == -1)
                    break;
                char[] bc = new char[length];
                System.arraycopy(bb, 0, bc, 0, length);
                respXML.append(new String(bc));
            }
            // 关闭上下行
            im.close();
            om.close();
            uc.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ZimgResponse zimgResponse = JSON.parseObject(respXML.toString(), ZimgResponse.class);
        String md5 = zimgResponse.getInfo().getMd5();
        return address+md5;
    }

    /**
     * 加上后缀p=0,提高清晰度，
     * */
    public static String uploadImageToZimgResource(MultipartFile file,String upload,String address,boolean needWarterMark){
        String s = uploadImageToZimg(file, upload, address,needWarterMark);
        return s+"?p=0";
    }

    public static boolean deleteImageFromZimg(String url,String md5){
        String address = url+"?md5="+md5+"&t=1";
        String s = HttpClientHelper.doGet(address);
        System.out.println(s);
        return s.contains("Successful");
    }
}
