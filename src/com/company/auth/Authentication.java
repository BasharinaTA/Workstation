package com.company.auth;

import com.company.clinic.Manager;
import com.company.utils.File;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class Authentication {

    private static final String PATH = "system/admin.txt";
    private String login;
    private String passwordHash;

    public Authentication() {
    }

    public Authentication(String[] arr) {
        this.login = arr[0];
        this.passwordHash = arr[1];
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Manager authenticate(Scanner sc) {
        List<Authentication> authentications = File.readFile(PATH, Authentication::new);
        while (true) {
            System.out.println("Введите логин:");
            this.setLogin(sc.nextLine());
            System.out.println("Введите пароль:");
            String password = sc.nextLine();
            for (Authentication auth : authentications) {
                if (this.login.equals(auth.getLogin())) {
                    this.setPasswordHash(generatePasswordHash(password));
                    if (this.passwordHash.equals(auth.getPasswordHash())) {
                        return new Manager();
                    }
                }
            }
            System.out.println("Логин / пароль введён неверно. Попробуйте ещё раз.");
        }
    }

    public static String generatePasswordHash(String password) {
        byte[] salt = {-11, 93, -31, -22, 0, 56, -79, -54, -105, 83, 1, 69, 2, 75, -109, -96};
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return Base64.getEncoder().encodeToString(factory.generateSecret(spec).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
