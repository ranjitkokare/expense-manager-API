package in.ranjitkokare.expensetrackerapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
//this model class holds jwt token

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {

	private final String jwtToken;
}
