package in.ranjitkokare.expensetrackerapi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ranjitkokare.expensetrackerapi.entity.AuthModel;
import in.ranjitkokare.expensetrackerapi.entity.JwtResponse;
import in.ranjitkokare.expensetrackerapi.entity.User;
import in.ranjitkokare.expensetrackerapi.entity.UserModel;
import in.ranjitkokare.expensetrackerapi.security.CustomUserDetailsService;
import in.ranjitkokare.expensetrackerapi.service.UserService;
import in.ranjitkokare.expensetrackerapi.util.JwtTokenUtil;

@RestController
public class AuthController {
	//Public APIs--> anyone can access
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	CustomUserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody AuthModel authModel) throws Exception{
		
		authenticate(authModel.getEmail(), authModel.getPassword());
		//once we authenticate the user 
		//we need to generate the JWT token 
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authModel.getEmail());
		
		final String token = jwtTokenUtil.generateToken(userDetails);
		
		//now send this token to client 
		return new ResponseEntity<JwtResponse>(new JwtResponse(token) ,HttpStatus.OK);
	}
	
	private void authenticate(String email, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (DisabledException e) {
			throw new Exception("User disabled");
 		} catch (BadCredentialsException e) {
 			throw new Exception("Bad Credentials");
 		}
	}

	@PostMapping("/register")
	public ResponseEntity<User> save(@Valid @RequestBody UserModel user) {//@RequestBody to request body to Java Bean
		return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
	}
}
