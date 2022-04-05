package com.aro.tokenization.crypto;

import com.agl.cryptocommon.dom.CryptoGatewayResponse;
import com.agl.cryptogateway.client.CryptoClientApi;
import com.agl.cryptocommon.dom.CryptoGatewayRequest;
import com.agl.cryptogateway.client.ICryptoClient;

import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.IOException;
//import java.net.InetAddress;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;


public class CryptoTest {

	public static void main(String[] args) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        System.out.println("Welocme to Crypto");

//        InetAddress address = InetAddress.getByName("22.147.149.155");
//        boolean reachable = address.isReachable(10000);
//        System.out.println(reachable);
        File file = new File("C:/Users/ABSD491/Desktop/Projects/Card Tokenization/Keys/arocvv-client-keystore.jks");
        KeyStore keyStore = KeyStore.getInstance("JKS");
        File file2 = new File("C:/Users/ABSD491/Desktop/Projects/Card Tokenization/Keys/client-truststore.jks");
        KeyStore trustStore = KeyStore.getInstance("JKS");
        if (file.exists()) {
            // if exists, load
            keyStore.load(new FileInputStream(file), "password@123".toCharArray());
            trustStore.load(new FileInputStream(file2), "password@123".toCharArray());
        }
        else
        {
            System.out.println("Keystore did not load");
        }

        ICryptoClient api = CryptoClientApi.getInstance(
                keyStore,
                trustStore,
                "https://crypto-digital-uat.aromiddleware.intra.absa.co.za:30037/CryptoGateway",
                "password@123".toCharArray(),
                "password@123".toCharArray()
        );

        CryptoGatewayRequest request = new CryptoGatewayRequest();
        try {
            request.setBusinessId("KEBRB");
            request.setSubject("1234567891234567");
            //request.setSubject("48656C6C6F576F72");
            request.setSystemId("AROCVV");
            request.setUrn("123496");
            request.setUserId("ABSD491");
            CryptoGatewayResponse responseEnc = api.encrypt(request);
            System.out.println("Encrypted: " + responseEnc.getSubject());
            request.setSubject(responseEnc.getSubject());
            responseEnc = api.decrypt(request);
            System.out.println("Result: " + responseEnc.getSubject());
        }catch (Exception ex){
            System.out.println("Exception}" + ex.getMessage());
        }
    }
	

}
