package com.EclecticsInterview.bank1_test;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;

public class JWTSecretMakerTest {
	@Test
	public void generateSecretKey()
	{
		SecretKey key = Jwts.SIG.HS512.key().build();
		String encodedKey = DatatypeConverter.printHexBinary(key.getEncoded());
		System.out.println("\nencodedKey: "+encodedKey+"\n");
	}
}
