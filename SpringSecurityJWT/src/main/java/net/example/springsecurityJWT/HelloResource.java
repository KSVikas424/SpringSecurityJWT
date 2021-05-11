package net.example.springsecurityJWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.example.springsecurityJWT.model.AuthenticateRequest;
import net.example.springsecurityJWT.model.AuthenticateResponse;
import net.example.springsecurityJWT.services.MyUserDetailsService;
import net.example.springsecurityJWT.util.JwtUtil;

@RestController
public class HelloResource {
	@Autowired
	JwtUtil jwtTokenUtil;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	MyUserDetailsService myUserDetailsService;
	
	@RequestMapping("/hello")
	public String hello() {
		return "Hello world";
	}
	
	@RequestMapping(value ="/authenticate",method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticateRequest authenticateRequest) throws Exception {
		try {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticateRequest.getUsername(), authenticateRequest.getPassword())); //we'll use it to authenticate for the first time using name and password
		}
		catch (BadCredentialsException e) {
			throw new Exception("incorrect username or password");
		}
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticateRequest.getUsername());
		String jwt = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticateResponse(jwt));
		
	}

}