package com.example;
import java.security.MessageDigest;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    private static final String SALT = "1#2$3%4(5)6@7!poeeww$3%4(5)djjkkldss";

    public static String encodePwd(String password) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] digest = md.digest(password.getBytes("UTF-8"));

        StringBuilder hex = new StringBuilder();

        for (byte b : digest) {
            String s = Integer.toHexString(0xff & b);
            if (s.length() == 1) hex.append("0");
            hex.append(s);
        }

        return hex.toString();
    }


    public static String encodePwd2(String password) {

        String input = password + "{" + SALT + "}";

        return DigestUtils.md5Hex(input);
    }

//    public static void main(String[] args) throws Exception {
//
//        String rawPwd = "123456a?";
//
//        String encPwd = encodePwd2(rawPwd);
//
//        System.out.println(encPwd);
//    }
}

