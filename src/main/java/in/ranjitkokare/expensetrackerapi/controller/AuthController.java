package in.ranjitkokare.expensetrackerapi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ranjitkokare.expensetrackerapi.entity.AuthModel;
import in.ranjitkokare.expensetrackerapi.entity.User;
import in.ranjitkokare.expensetrackerapi.entity.UserModel;
import in.ranjitkokare.expensetrackerapi.service.UserService;

@RestController
public class AuthController {
	//Public APIs--> anyone can access
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<HttpStatus> login(@RequestBody AuthModel authModel){
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authModel.getEmail(), authModel.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		//here authentication obj stores loggedIn user and it is hold by context holder
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<User> save(@Valid @RequestBody UserModel user) {//@RequestBody to request body to Java Bean
		return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
	}
}
