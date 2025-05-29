package com.example.SentinelBE.utils.converter;

import java.util.Base64;

public class ByteStringConverter {
    public static String byteArrayToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] base64ToByteArray(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}