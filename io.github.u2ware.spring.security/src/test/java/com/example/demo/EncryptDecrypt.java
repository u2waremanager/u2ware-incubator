package org.springframework.data.rest.webmvc.multipart;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EncryptDecrypt {
    private static final String UNICODE_FORMAT = "UTF8";
    private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    private byte[] arrayBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    private SecretKey key;
	private ObjectMapper objectMapper; 


    public EncryptDecrypt() {
    	try {
            myEncryptionKey = getClass().getName();
            myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
            arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
            ks = new DESedeKeySpec(arrayBytes);
            skf = SecretKeyFactory.getInstance(myEncryptionScheme);
            cipher = Cipher.getInstance(myEncryptionScheme);
            key = skf.generateSecret(ks);
    		
            objectMapper = new ObjectMapper();
            
    	}catch(Exception e) {
    		throw new RuntimeException();
    	}
    }


    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encodeBase64URLSafe(encryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }


    public String decrypt(String encryptedString) {
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString.getBytes());
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

	public <T> String encryptObject(T unencryptedObject) {
		String encryptedString = null;
		try {
			String unencryptedString = objectMapper.writeValueAsString(unencryptedObject);
			encryptedString = encrypt(unencryptedString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedString;
	}

	public <T> T decryptObject(String encryptedString, Class<T> type) {
		T decryptedObject=null;
		try {
			String decryptedText = decrypt(encryptedString);
			decryptedObject = objectMapper.readValue(decryptedText, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decryptedObject;
	}
    

    public static void main(String args []) throws Exception
    {
        EncryptDecrypt td= new EncryptDecrypt();

        String target="{\"password/@123";
        String encrypted=td.encrypt(target);
        String decrypted=td.decrypt(encrypted);

        System.out.println("String To Encrypt: "+ target);
        System.out.println("Encrypted String: " + encrypted);
        System.out.println("Decrypted String: " + decrypted);

    }
}