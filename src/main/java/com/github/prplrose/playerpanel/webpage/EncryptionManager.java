package com.github.prplrose.playerpanel.webpage;

import net.minecraft.network.encryption.NetworkEncryptionException;
import net.minecraft.network.encryption.NetworkEncryptionUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class EncryptionManager {

    static KeyPair keyPair;

    private static String readKey(File file) throws IOException {
        FileReader reader = new FileReader(file);
        StringBuilder builder = new StringBuilder();
        int c;
        while ((c=reader.read()) != -1){
            builder.append((char)c);
        }
        reader.close();
        return builder.toString();
    }

    private static void saveKey(File file, String string) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(string);
        writer.close();
    }

    private static KeyPair generateKeys(Path configDirectory) throws NetworkEncryptionException {
        KeyPair keyPair = NetworkEncryptionUtils.generateServerKeyPair();
        try {
            saveKey(
                    configDirectory.resolve("public_key").toFile(),
                    NetworkEncryptionUtils.encodeRsaPublicKey(keyPair.getPublic())
            );
            saveKey(
                    configDirectory.resolve("private_key").toFile(),
                    NetworkEncryptionUtils.encodeRsaPrivateKey(keyPair.getPrivate())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return keyPair;
    }

    public static void init(Path configDirectory) {
        try{
            String key;
            try {
                key = readKey(configDirectory.resolve("public_key").toFile());
            } catch (IOException e) {
                keyPair = generateKeys(configDirectory);
                return;
            }
            PublicKey publicKey = NetworkEncryptionUtils.decodeRsaPublicKeyPem(key);
            try {
                key = readKey(configDirectory.resolve("private_key").toFile());
            } catch (IOException e) {
                keyPair = generateKeys(configDirectory);
            }
            PrivateKey privateKey = NetworkEncryptionUtils.decodeRsaPrivateKeyPem(key);

            keyPair = new KeyPair(publicKey, privateKey);
        } catch (NetworkEncryptionException e) {
            throw new RuntimeException(e);
        }
    }

    public static KeyPair getKeyPair(){
        return keyPair;
    }
}
