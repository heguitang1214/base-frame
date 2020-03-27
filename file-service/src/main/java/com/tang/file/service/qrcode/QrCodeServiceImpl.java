package com.tang.file.service.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.wangchi.uploaddemo.service.httpclient.ImgIOJsonUtil;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;

public class QrCodeServiceImpl {

//    private Logger logger = LoggerFactory.getLogger(QrCodeServiceImpl.class);

    public static final Integer QR_CODE_WIDTH = 204; //二维码宽度
    public static final Integer QR_CODE_HEIGHT = 204;// 二维码高度
    public static final Integer OR_YELLOW_COLOR = 0xFF6600;// 黄色
    public static final Integer OR_WHITE_COLOR = 0xFFFFFFFF;// 白色

    public static void main(String[] args) throws IOException {
        QrCodeServiceImpl qrCodeService = new QrCodeServiceImpl();
        String str = qrCodeService.createBase64Image("这是一个二维码", "D:\\image\\timg.jpg");


        System.out.println(str);

        // 字符串解码为byte数组
        byte[] decode = ImgIOJsonUtil.decode(str);
        FileOutputStream fos = new FileOutputStream("D:\\image\\aaaaaa.jpg");
        fos.write(decode);
        fos.close();

        System.out.println("ok。。。。。");

    }

    /**
     * 将二维码和背景图片拼装成新的分享图片
     *
     * @param shareContent  生成二维码内容
     * @param bgPicturePath 背景图片地址
     * @return base64 String 的图片
     */
    public String createBase64Image(String shareContent, String bgPicturePath) {
        try {

            // 1.1 生成二维码图片
            BufferedImage qrCodeImage = createQrImage(shareContent, QR_CODE_WIDTH, QR_CODE_HEIGHT);

            // 1.2 读取并生成背景图片
            Image bgImage = ImageIO.read(new FileInputStream(new File(bgPicturePath)));
            BufferedImage bgPicture = new BufferedImage(bgImage.getWidth(null), bgImage.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 1.3 将二维码拼接背景图片
            Graphics2D g = bgPicture.createGraphics();
            g.drawImage(bgImage, 0, 0, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1));
//            int starX = bgImage.getWidth(null) * 275 / 750;// 二维码距背景图片X轴距离
//            int startY = bgImage.getHeight(null) * 990 / 1405;// 二维码距背景图片Y轴距离
            int starX = bgImage.getWidth(null) * 275 / 750;// 二维码距背景图片X轴距离
            int startY = bgImage.getHeight(null) * 990 / 1405;// 二维码距背景图片Y轴距离
            System.out.println(starX + ">>>" + startY);


            g.drawImage(qrCodeImage, starX, startY, qrCodeImage.getWidth(null), qrCodeImage.getHeight(null), null);
            g.dispose();

            // 1.4 将合成的图片转为base64String
            ByteArrayOutputStream compositeImageByte = new ByteArrayOutputStream();
            ImageIO.write(bgPicture, "png", compositeImageByte);
            String pngBase64 = new BASE64Encoder().encodeBuffer(compositeImageByte.toByteArray()).trim();//转换成base64串
//            pngBase64 = "data:image/png;base64," + pngBase64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n

            return pngBase64;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成二维码图片
     *
     * @param shareContent 生成二维码分享内容
     * @param qrCodeWidth  维码宽度
     * @param qrCodeHeight 二维码高度
     * @return 二维码图片【ImageIO.write方法可将图片保存到本地】
     */
    public BufferedImage createQrImage(String shareContent, int qrCodeWidth, int qrCodeHeight) {

        // 用于设置QR二维码参数
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        // 设置QR二维码的纠错级别——这里选择最高H级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置编码方式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 使用EncodeHintType.MARGIN设置二维码空白区域大小
        hints.put(EncodeHintType.MARGIN, 1);
        // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(shareContent, BarcodeFormat.QR_CODE, qrCodeWidth, qrCodeHeight, hints);
        } catch (WriterException e) {
            return null;
        }
        // 创建一个不带透明色的BufferedImage对象
        BufferedImage image = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < bitMatrix.getWidth(); x++) {
            for (int y = 0; y < bitMatrix.getHeight(); y++) {
                // 开始利用二维码数据，分别设为黄0xFF6600和白0xFFFFFFFF两色
                image.setRGB(x, y, bitMatrix.get(x, y) ? OR_YELLOW_COLOR : OR_WHITE_COLOR);
            }
        }
        return image;
    }
}
