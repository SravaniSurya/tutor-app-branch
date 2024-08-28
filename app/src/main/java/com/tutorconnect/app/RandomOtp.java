package com.tutorconnect.app;
import java.security.SecureRandom;

public class RandomOtp {

    private static final SecureRandom random = new SecureRandom();

    public static String generateOTP(int length) {
        // Define the characters to be used in the OTP
        String characters = "0123456789";
        StringBuilder otp = new StringBuilder(length);

        // Generate random characters for the OTP
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            otp.append(characters.charAt(index));
        }

        return otp.toString();
    }
}
