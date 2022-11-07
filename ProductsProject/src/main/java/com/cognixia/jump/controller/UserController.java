package com.cognixia.jump.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.DuplicateMangaException;
import com.cognixia.jump.exception.InvalidMangaException;
import com.cognixia.jump.exception.InvalidUserException;
import com.cognixia.jump.exception.OutOfOrderException;
import com.cognixia.jump.model.AuthenticationRequest;
import com.cognixia.jump.model.AuthenticationResponse;
import com.cognixia.jump.model.Manga;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.requestmodels.MangaAndUserReqModel;
import com.cognixia.jump.service.MyUserDetailsService;
import com.cognixia.jump.service.UserService;
import com.cognixia.jump.util.JwtUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	MyUserDetailsService myUserDetailsService;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PasswordEncoder encoder;

	@GetMapping("/all")
	public ResponseEntity<?> getAllUsers(){
		List<User> users = userService.getAllUsers();
		
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@JsonIgnoreProperties(value = "qty")
	@GetMapping("/all/{username}/manga")
	public ResponseEntity<?> getAllUsersMangas(@PathVariable String username){
		Set<Manga> users = userService.getAllUserManga(username);
		
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> createUser(@RequestBody @Valid User user){
		
		if(userService.createUser(user)) {
			User created = user;
			return ResponseEntity.status(201).body(created);
		}
		
		return new ResponseEntity<>("Failed to create user: " + user, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@PostMapping("add/auth")
	public ResponseEntity<?> createJwtToken(@RequestBody AuthenticationRequest request) throws Exception {
		
		// try to catch the exception for bad credentials, just so we can set our own
		// message when this doesn't work
		try {
			// make sure we have a valid user by checking their username and password
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		} catch (BadCredentialsException e) {
			// provide our own message on why login didn't work
			throw new Exception("Incorrect username or password");
		}

		// as long as no exception was thrown, user is valid

		// load in the user details for that user
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(request.getUsername());

		// generate the token for that user
		final String jwt = jwtUtil.generateTokens(userDetails);

		// return the token
		return ResponseEntity.status(201).body( new AuthenticationResponse(jwt) );

	}
	
	@PutMapping("/manga")
	public ResponseEntity<?> addMangaToUser(HttpServletRequest request, @RequestBody MangaAndUserReqModel model) throws OutOfOrderException, DuplicateMangaException{
		User user = userService.loadUserById(model.getUserId());
		
		if(user != null) {
			//returns the token in the header as a string to use to authenticate current user.
			String jwt = jwtUtil.returnToken(request);
			final UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getUsername());
			
			//checks to see if the token provided is legitimate and connected to the current user
			if(jwtUtil.validateToken(jwt, userDetails))	{
			if(userService.addMangaToUser(model.getMangaId(), model.getUserId())) {
				return new ResponseEntity<>("added Manga to user.", HttpStatus.CREATED);
			}else{
				return new ResponseEntity<>("Cannot add manga to this user, please check for valid token for user", HttpStatus.FORBIDDEN);
			}
		
		}
	}
		return new ResponseEntity<>("Failed to update user. Wrong User logged in.", HttpStatus.NOT_ACCEPTABLE);
	}
	
	
	@DeleteMapping("/manga/delete")
	public ResponseEntity<?> deleteMangaFromUser(HttpServletRequest request, @RequestBody MangaAndUserReqModel model) throws InvalidMangaException{
		User user = userService.loadUserById(model.getUserId());
		
		if(user != null) {
			//returns the token in the header as a string to use to authenticate current user.
			String jwt = jwtUtil.returnToken(request);
			final UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getUsername());
			
			//checks to see if the token provided is legitimate and connected to the current user
			if(jwtUtil.validateToken(jwt, userDetails))	{
			if(userService.deleteMangafromUser(model.getMangaId(), model.getUserId())) {
				return new ResponseEntity<>("deleted Manga from user.", HttpStatus.CREATED);
			}else{
				return new ResponseEntity<>("Cannot delete manga to this user, please check for valid token for user", HttpStatus.FORBIDDEN);
			}
		
		}
	}
		return new ResponseEntity<>("Failed to update user.", HttpStatus.NOT_ACCEPTABLE);
	}
	
	@PutMapping("/edituser")
	public ResponseEntity<?> editUsername(@RequestBody User user, HttpServletRequest request) throws InvalidUserException{

		//fix this, just use example in product api smh

			// returns the token in the header as a string to use to authenticate current
			// user.
			User curr_user = userService.loadUserById(user.getId());
			final UserDetails userDetails = myUserDetailsService.loadUserByUsername(curr_user.getUsername());
			//Long id = user.getId();
			//final UserDetails userDetails = myUserDetailsService.loadUserById(id);
			String jwt = jwtUtil.returnToken(request);
			String username = user.getUsername();
			
			
			// checks to see if the token provided is legitimate and connected to the
			// current user
			if (jwtUtil.validateToken(jwt, userDetails)) {
				userService.updateUsername(user,username);
				return new ResponseEntity<>("Changed username to " + user.getUsername() + ".", HttpStatus.CREATED);
			}
				
			
		
		return new ResponseEntity<>("Wrong user logged in.", HttpStatus.CREATED);
	}
	@PutMapping("/editpass")
	public ResponseEntity<?> editPassword(@RequestBody User user, HttpServletRequest request) throws InvalidUserException{

		//fix this, just use example in product api smh

			// returns the token in the header as a string to use to authenticate current
			// user.
			User curr_user = userService.loadUserById(user.getId());
			final UserDetails userDetails = myUserDetailsService.loadUserByUsername(curr_user.getUsername());
			//Long id = user.getId();
			//final UserDetails userDetails = myUserDetailsService.loadUserById(id);
			String jwt = jwtUtil.returnToken(request);
			String password = user.getPassword();
		
			
			// checks to see if the token provided is legitimate and connected to the
			// current user
			if (jwtUtil.validateToken(jwt, userDetails)) {
				userService.updatePassword(user,password);
				return new ResponseEntity<>("Changed password.", HttpStatus.CREATED);
			}
		
		return new ResponseEntity<>("Dint't change password.", HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) throws InvalidUserException{
		if(userService.deleteUser(id)) {
			return new ResponseEntity<>("Deleted user with id: " + id + " from list ", HttpStatus.OK);
		}
		
		return new ResponseEntity<>("Failed to delete user.", HttpStatus.NOT_ACCEPTABLE);
		
	}
	
}
