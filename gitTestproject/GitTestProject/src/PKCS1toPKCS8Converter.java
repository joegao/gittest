openssl pkcs8 -topk8 -inform PEM -in rsa_private_key_pkcs1.pem -out pkcs8_private_key.pem -nocrypt
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class PKCS1toPKCS8Converter {

    // Load PKCS#1 RSA private key from a PEM file
    public static byte[] readPKCS1PrivateKey(String filename) throws IOException {
        String key = new String(Files.readAllBytes(Paths.get(filename)));
        key = key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s", ""); // Remove line breaks and spaces
        return Base64.getDecoder().decode(key);
    }

    // Convert PKCS#1 to PKCS#8
    public static byte[] convertPKCS1toPKCS8(byte[] pkcs1PrivateKeyBytes) throws Exception {
        // Parse the PKCS#1 private key structure
        RSAPrivateKey rsaPrivateKey = RSAPrivateKey.getInstance(ASN1Primitive.fromByteArray(pkcs1PrivateKeyBytes));

        // Create a PrivateKeyInfo object, which wraps the RSA key in PKCS#8 format
        PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo(
                new org.bouncycastle.asn1.x509.AlgorithmIdentifier(
                        org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers.rsaEncryption), rsaPrivateKey);

        // Convert to PKCS#8 encoded byte array
        return privateKeyInfo.getEncoded();
    }

    public static void main(String[] args) throws Exception {
        // Add Bouncy Castle provider (needed for ASN.1 parsing)
        Security.addProvider(new BouncyCastleProvider());

        // Read the PKCS#1 private key
        byte[] pkcs1Key = readPKCS1PrivateKey("path/to/your/pkcs1_private_key.pem");

        // Convert PKCS#1 to PKCS#8
        byte[] pkcs8Key = convertPKCS1toPKCS8(pkcs1Key);

        // Optionally, you can use Java's KeyFactory to convert the PKCS#8 bytes to a PrivateKey object
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pkcs8Key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(spec);

        // Output the PKCS#8 private key as a PEM string
        String pkcs8Pem = "-----BEGIN PRIVATE KEY-----\n" +
                Base64.getEncoder().encodeToString(pkcs8Key) +
                "\n-----END PRIVATE KEY-----";
        System.out.println(pkcs8Pem);

        // Optionally, write the PKCS#8 private key to a file
        Files.write(Paths.get("path/to/your/pkcs8_private_key.pem"), pkcs8Pem.getBytes());
    }
}


//implementation 'org.bouncycastle:bcprov-jdk15on:1.70' // Use the latest version