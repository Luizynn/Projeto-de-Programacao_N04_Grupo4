package com.pagamentos.projeto_programacao.users;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.security.MessageDigest;

public class HelpersUser {
    public static boolean checkCpf  (String cpf) {
        if(cpf.length() != 11) {
            return false;
        }

        int total_1 = 0;
        int index = 10;

        for (String num : cpf.substring(0,9).split("")) {
            total_1 += Integer.parseInt(num) * index;
            System.out.println(Integer.parseInt(num));
            index--;
        }

        System.out.println(total_1);

        int rest_1 = total_1 % 11;

        rest_1 = 11 - rest_1;

        rest_1 = rest_1 > 9 ? 0 : rest_1;

        if(Character.getNumericValue(cpf.charAt(9)) != rest_1) {
            return false;
        }

        index = 11;
        total_1 = 0;

        for (String num : cpf.substring(0,10).split("")) {
            total_1 += Integer.parseInt(num) * index;
            System.out.println(Integer.parseInt(num));
            index--;
        }

        rest_1 = total_1 % 11;

        rest_1 = 11 - rest_1;

        rest_1 = rest_1 > 9 ? 0 : rest_1;

        if(Character.getNumericValue(cpf.charAt(10)) != rest_1) {
            return false;
        }

        return true;
    }

    public static boolean checkEmail (String email) {
        Pattern pattern = Pattern.compile("\\S+@\\S+\\.\\S+",Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }

    public static String createPasswordHash(String senha)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        int iteracoes = 65536;
        int keyLength = 256;

        KeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, iteracoes, keyLength);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hash = factory.generateSecret(spec).getEncoded();

        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        String hashBase64 = Base64.getEncoder().encodeToString(hash);

        return iteracoes + ":" + saltBase64 + ":" + hashBase64;
    }

    public static boolean verifyPassword(String senhaDigitada, String hashArmazenado)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        String[] partes = hashArmazenado.split(":");
        if (partes.length != 3) {
            return false;
        }

        int iteracoes = Integer.parseInt(partes[0]);
        byte[] salt = Base64.getDecoder().decode(partes[1]);
        byte[] hashOriginal = Base64.getDecoder().decode(partes[2]);

        int keyLength = hashOriginal.length * 8;

        KeySpec spec = new PBEKeySpec(senhaDigitada.toCharArray(), salt, iteracoes, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hashDigitado = factory.generateSecret(spec).getEncoded();

        return MessageDigest.isEqual(hashOriginal, hashDigitado);
    }

}
