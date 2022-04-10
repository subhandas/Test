package com.aro.tokenization.crypto;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.agl.cryptocommon.dom.CryptoGatewayResponse;
import com.agl.cryptogateway.client.CryptoClientApi;
import com.agl.cryptocommon.dom.CryptoGatewayRequest;
import com.agl.cryptogateway.client.ICryptoClient;


public class CryptoTest {
	public static void main(String[] args)
			throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:postgresql://oss-vip-3941.corp.dsarena.com:5433/sparrow",
					"app_account", "rguvhBsWF4");

			
			//Loading Key
			ResultSet rs = connection.createStatement().executeQuery("SELECT data FROM keystore where id=1");
			byte[] key = null;
            if (rs != null) {
                while (rs.next()) {
                    key = rs.getBytes(1);
                }

                ResultSet ps = connection.createStatement().executeQuery("SELECT data FROM keystore where id=3");
                byte[] pass = null;
                while (ps.next()) {
                    pass = ps.getBytes(1);
                }
                
                char[] pwdArray = new char[pass.length];
                for (int i = 0; i < pass.length; i++) {
                    pwdArray[i] = (char) pass[i];
                }
                
                
				InputStream inputStream = new  ByteArrayInputStream(key);
                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(inputStream, pwdArray);
                System.out.println("Key Loaded");
                
              //Loading Trust
                ResultSet rs1 = connection.createStatement().executeQuery("SELECT data FROM keystore where id=1");
    			byte[] trust = null;
                if (rs1 != null) {
                    while (rs1.next()) {
                        trust= rs1.getBytes(1);
                    }
                    InputStream inputStream1 = new  ByteArrayInputStream(trust);
                    KeyStore trustStore = KeyStore.getInstance("JKS");
                    trustStore.load(inputStream1, pwdArray);
                    System.out.println("Trust Loaded");
                    
               //Calling Crypto API
                ICryptoClient api = CryptoClientApi.getInstance(
                        keyStore,
                        trustStore,
                        "https://crypto-digital-uat.aromiddleware.intra.absa.co.za:30037/CryptoGateway",
                        pwdArray,
                        pwdArray
                );

                CryptoGatewayRequest request = new CryptoGatewayRequest();
                try {
                    request.setBusinessId("KEBRB");
                    request.setSubject("1234567891234567");
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
            rs1.close();
            }
            rs.close();
            
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		

    }

}
