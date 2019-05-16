package com.qhit.tests;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by HP on 2019/5/8.
 */
public class test {




    public static void main(String[] args) {
        String password="567837236t4276///";
        System.out.println("加密前："+password);
        String encrypt = encrypt(password);
        System.out.println("加密后："+encrypt);
        String decrypt = decrypt("Nic49ftqZNsSF1GSbs02WnwryJxhdBoiwtoecbfXjK4=");
        System.out.println("解密后："+decrypt);
    }
    public static String encrypt(String password) {
        String content="1234";
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器

            byte[] byteContent = password.getBytes("utf-8");

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(content));// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密

            return Base64.encodeBase64String(result);//通过Base64转码返回
        } catch (Exception ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private static SecretKeySpec getSecretKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;

        try {
            kg = KeyGenerator.getInstance("AES");
            //AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(password.getBytes()));
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), "AES");// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, e);
        }



        return null;
    }

    public static String decrypt(String password) {
        String content="1234";

        try {
            //实例化
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(content));

            //执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(password));

            return new String(result, "utf-8");
        } catch (Exception ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
