package in.ranjitkokare.expensetrackerapi.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component //so that spring will recognize this when application loads
public class JwtTokenUtil {

	private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; //constant field
	
	@Value("${jwt.secret}") //get from application.poperties file
	private String secret;

	//logic to generate the JWT token
	public String generateToken(UserDetails userDetails) {
		Map< String, Object> claims = new HashMap<>();
		
		return Jwts.builder()
			.setClaims(claims)
			.setSubject(userDetails.getUsername())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis()+ JWT_TOKEN_VALIDITY * 1000))
			.signWith(SignatureAlgorithm.HS512, secret)
			.compact();
	}
	
	
	public String getUsernameFromToken(String jwtToken) {
		return getClaimFromToken(jwtToken, Claims::getSubject);
	}
	
	//here we use java 8 functional programming
	//pass Function as method parameter and it get return function back
	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver ) {
		final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return claimsResolver.apply(claims);
	}

	//logic to validate token 
	public boolean validateToken(String jwtToken, UserDetails userDetails) {
		final String username = getUsernameFromToken(jwtToken);
		
		return username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
	}


	private boolean isTokenExpired(String jwtToken) {
		final Date expiration = getExpirationDateFromToken(jwtToken);
		return expiration.before(new Date());
	}


	private Date getExpirationDateFromToken(String jwtToken) {
		return getClaimFromToken(jwtToken, Claims::getExpiration);
	}
	
	
}
