package org.yyy.homek8s.natterexportconfig.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * MD5加密
 */
public class MD5Util {

    /**
     * Encodes a string 2 MD5
     *
     * @param str String to encode
     * @return Encoded String
     * @throws NoSuchAlgorithmException
     */
    public static String crypt(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] hash = md.digest();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

   public static String base64Decode(String str){
       try {
           byte[] decodedBytes = Base64.getDecoder().decode(str);
           String decodedString = new String(decodedBytes);
           System.out.println("解码后的字符串: " + decodedString);
           return decodedString;
       } catch (IllegalArgumentException e) {
           System.out.println("无效的 Base64 编码字符串: " + e.getMessage());
           return "";
       }
   }
}