package com.example.line.demo.security;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

import javax.imageio.ImageIO;

import com.example.line.demo.common.ProjectLogging;
import com.google.common.base.Throwables;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * 產生圖形驗證碼，提供SHA方法類別。
 *
 * @author TimChen
 */
public class WaCareSecurity {

  private static final ProjectLogging LOGGER = new ProjectLogging(WaCareSecurity.class);
  private static final SHA3.DigestSHA3 digest512 = new SHA3.Digest512();
  private static final SHA3.DigestSHA3 digest256 = new SHA3.Digest256();

  private static final Random random = new Random();
  private static final Font FONT = new Font("Fixedsys", Font.CENTER_BASELINE, 26);

  /**
   * 取得圖形驗證碼
   *
   * @return 回傳圖形驗證碼index 0 放的是圖形驗證碼的字串，index 1 放的是圖形驗證碼圖片Base64Encode字串。
   */
  public static String[] getSecurityImage() {
    Integer randInt = random.nextInt(9000) + 1000;
    char[] randStr = randInt.toString().toCharArray();
    String[] result = {
      randInt.toString(), imageBase64Encode2String(getImage(randStr, 120, 50, 50), "png")
    };
    return result;
  }

  /**
   * 圖片轉Base64字串
   *
   * @param image
   * @param type
   * @return
   */
  private static String imageBase64Encode2String(BufferedImage image, String type) {
    String imageString = null;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    try {
      ImageIO.write(image, type, bos);
      byte[] imageBytes = bos.toByteArray();

      Base64.Encoder encoder = Base64.getEncoder();
      imageString = encoder.encodeToString(imageBytes);

      bos.close();
    } catch (IOException e) {
      LOGGER.error(Throwables.getStackTraceAsString(e));
    }
    return imageString;
  }

  /**
   * 取得圖形驗證碼圖片
   *
   * @param code 驗證碼數字
   * @param width 圖片寬度
   * @param height 圖片高度
   * @param line 線條數量
   * @return 圖形驗證碼圖片
   */
  private static BufferedImage getImage(char[] code, int width, int height, int line) {
    // BufferedImage類是具有緩衝區的Image類,Image類是用於描述圖像信息的類
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
    Graphics g = image.getGraphics();
    g.fillRect(0, 0, width, height);
    g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 23));
    g.setColor(getRandColor(110, 133));
    // 繪製干擾線
    for (int i = 0; i < line; i++) {
      drowLine(g, width, height);
    }
    // 繪製隨機字元
    for (int i = 0; i < code.length; i++) {
      drowChar(g, width / code.length * i, random.nextInt(height / 3) + height / 3, code[i]);
    }
    g.dispose();
    return image;
  }

  /*
   * 繪製干擾線
   */
  private static void drowLine(Graphics g, int width, int height) {
    int x = random.nextInt(width);
    int y = random.nextInt(height);
    int xl = random.nextInt(width / 2);
    int yl = random.nextInt(height / 2);
    g.drawLine(x, y, x + xl, y + yl);
  }

  /*
   * 繪製字符
   */
  private static char drowChar(Graphics g, int x, int y, char code) {
    g.setFont(FONT);
    g.setColor(getRandColor(10, 200));
    g.translate(random.nextInt(3), random.nextInt(3));
    g.drawString(code + "", x, y);
    return code;
  }

  /*
   * 獲取顏色
   */
  private static Color getRandColor(int fc, int bc) {
    if (fc > 255) {
      fc = 255;
    }
    if (bc > 255) {
      bc = 255;
    }
    int r = fc + random.nextInt(bc - fc - 16);
    int g = fc + random.nextInt(bc - fc - 14);
    int b = fc + random.nextInt(bc - fc - 18);
    return new Color(r, g, b);
  }

  public String getRandomString(int length) {
    String letter = "abcdefghijklmnopqrstuvwxyz1234567890";
    Random ran = new Random();
    String result = "";
    for (int i = 0; i < length; i++) {
      result = result + letter.charAt(ran.nextInt(36));
    }
    return result;
  }

  /**
   * 傳入文本內容，返回SHA-256 串
   *
   * @param strText
   * @return
   */
  public static String SHA256(final String strText) {
    byte[] digest = digest256.digest(strText.getBytes());
    return Hex.toHexString(digest);
  }

  /**
   * 傳入文本內容，返回SHA-512 串
   *
   * @param strText
   * @return
   */
  public static String SHA512(final String strText) {
    byte[] digest = digest512.digest(strText.getBytes());
    return Hex.toHexString(digest);
  }
}
