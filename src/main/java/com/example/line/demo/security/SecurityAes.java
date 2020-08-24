package com.example.line.demo.security;

import com.example.line.demo.common.ProjectLogging;
/** @author USER */
import com.google.common.base.Throwables;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecurityAes {

  private static final ProjectLogging LOGGER = new ProjectLogging(SecurityAes.class);
  static final Base64.Decoder decoder = Base64.getDecoder();
  static final Base64.Encoder encoder = Base64.getEncoder();

  public static String byte2hex(byte[] b) {
    String hs = "";
    String stmp = "";
    for (int n = 0; n < b.length; n++) {
      stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
      if (stmp.length() == 1) {
        hs = hs + "0" + stmp;
      } else {
        hs = hs + stmp;
      }
    }
    return hs.toUpperCase();
  }

  public static byte[] hex2byte(byte[] b) {
    if ((b.length % 2) != 0) {
      throw new IllegalArgumentException("長度不是偶數");
    }
    byte[] b2 = new byte[b.length / 2];
    for (int n = 0; n < b.length; n += 2) {
      String item = new String(b, n, 2);
      b2[n / 2] = (byte) Integer.parseInt(item, 16);
    }
    return b2;
  }

  public static String Encrypt(String sSrc, String sKey, String ivStr) throws Exception {
    if (sKey == null) {
      System.out.print("key為空null");
      return null;
    }
    // if (sKey.length() != 16) { // 判斷Key是否為16位
    //   System.out.print("Key長度不是16位");
    //   return null;
    // }
    byte[] raw = sKey.getBytes();
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // "算法/模式/補碼方式"
    IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes()); // 使用CBC模式，需要一個向量iv，可增加加密算法的強度
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
    byte[] encrypted = cipher.doFinal(sSrc.getBytes());
    return encoder.encodeToString(encrypted); // 此處使用BASE64做轉碼功能，同時能起到2次加密的作用。
  }

  public static String Decrypt(String sSrc, String sKey, String ivStr) throws Exception {
    try {
      if (sKey == null) {
        System.out.print("key為空null");
        return null;
      }
      // if (sKey.length() != 16) {
      //   System.out.print("Key長度不是16位");
      //   return null;
      // }
      byte[] raw = sKey.getBytes("ASCII");
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
      cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
      // byte[] encrypted1 = sSrc.getBytes();
      byte[] encrypted1 = decoder.decode(sSrc); // 先用base64解密
      byte[] original = cipher.doFinal(encrypted1);
      String originalString = new String(original);
      return originalString;
    } catch (Exception ex) {
      LOGGER.error(Throwables.getStackTraceAsString(ex));
      return null;
    }
  }
}
