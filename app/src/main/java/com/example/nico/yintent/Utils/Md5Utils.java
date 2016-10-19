package com.example.nico.yintent.Utils;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2016/8/30.
 */
public class Md5Utils {
    public final static String MD5(String s){
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9',
        'a','b','c','d','e','f'};
        try {
            byte[] btInput=s.getBytes();
            //获得MD5照耀算法的MessageDigest对象
            MessageDigest mdInst=MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md=mdInst.digest();
            //把密文转成16进制字符串
            int j=md.length;
            char str[]=new char[j * 2];
            int k=0;
            for (int i=0;i<j;i++){
                byte byte0=md[i];
                str[k++]=hexDigits[byte0>>>4&0xf];
                str[k++]=hexDigits[byte0&0xf];
            }
            return new String(str);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
