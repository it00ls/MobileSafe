package com.it00ls.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author it00ls
 */
public class StreamUtils {

    /**
     * 读取流中的数据
     * @param is 字节输入流
     * @return 返回流中的数据
     * @throws IOException
     */
    public static String readFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        int len;
        byte[] b = new byte[1024];
        while ((len = is.read(b)) != -1) {
            bao.write(b, 0, len);
        }
        is.close();
        bao.close();
        return bao.toString();
    }
}
