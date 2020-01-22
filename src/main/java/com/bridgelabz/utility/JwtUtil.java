/******************************************************************************
 *  Compilation:  javac -d bin ElasticSearchConfig.java
 *  Execution:    java -cp bin com.bridgelabz.config;
 *  						  
 *  
 *  Purpose:      ElasticSearch configuration class
 *  @author  Shubham Chavan
 *  @version 1.0
 *  @since   11-12-2019
 *
 ******************************************************************************/
package com.bridgelabz.utility;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.bridgelabz.exception.custome.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	UserDetails userDetails;
	private String SECRET_KEY = "secret";
	
	public String extractUsername(String token)
	{
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractEpiration(String token)
	{
		return extractClaim(token, Claims::getExpiration);
	}
	
	public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		try {
			return extractEpiration(token).before(new Date());	
		}catch (ExpiredJwtException e) {
			throw new CustomException.ExpiredJwtTokenExeception("Token is Expired you neet to login");
		}
	}
//	public String generateToken(UserDetails userDetails)
//	{
//		Map<String, Object> claims = new HashMap<>();
//		return createToken(claims,userDetails.getUsername());
//	}

	public String generateToken(String username)
	{
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims,username);
	}
	
	private String createToken(Map<String, Object> claims, String username) {
		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	public Boolean validateToken(String token,UserDetails userDetails)
	{
		final String username = extractUsername(token);
		//System.out.println(username);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	public String validateToken(String token)
	{
		final String username = extractUsername(token);
		return (username);
	}
}
