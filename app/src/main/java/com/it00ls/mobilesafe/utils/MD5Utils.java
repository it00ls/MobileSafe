package com.it00ls.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author it00ls
 */
public class MD5Utils {

    /**
     * 使用MD5加密明文
     *
     * @param password 明文
     * @return 密文
     */
    public static String encode(String password) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5"); // 创建MD5加密对象
            byte[] digest = instance.digest(password.getBytes()); // 加密明文
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String HexString = Integer.toHexString(b & 0xff); // 获取后八个有效位
                if (HexString.length() < 2) {
                    HexString += "0";
                }
                sb.append(HexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
