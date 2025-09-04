package com.ntp.be.auth.helper;

import java.util.Random;

public class VerificationCodeGenerator {
    public static String generateCode() {
        Random rand = new Random();
        int code = 100000 + rand.nextInt(900000);
        return String.valueOf(code);
    }
}
