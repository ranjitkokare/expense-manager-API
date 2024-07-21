package in.ranjitkokare.expensetrackerapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
//this model class holds jwt token

@Getter
@AllArgsConstructor
public class JwtResponse {

	private final String jwtToken;
}
