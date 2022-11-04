package com.cognixia.jump.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.MyUserDetailsService;
import com.cognixia.jump.service.UserService;
import com.cognixia.jump.util.JwtUtil;
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

	@MockBean
	UserService service;
	
	@MockBean
	UserRepository userRepo;
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	User user;
	
	@MockBean
	AuthenticationManager manager;
	
	@MockBean
	JwtUtil jwt;
	
	@MockBean
	@Autowired
	MyUserDetailsService myUserDetailsService;
	
	@MockBean
	PasswordEncoder encoder;
	
	
	
	@Test
	@AutoConfigureMockMvc
	@WithMockUser(roles = "ADMIN")
	public void testGetAllUsers() throws Exception {
		
		String uri = "/user/all";
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.get(uri)//.with(SecurityMockMvcRequestPostProcessors.jwt())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		
	}
	@Test
	@AutoConfigureMockMvc
	@WithMockUser(roles = "ADMIN")
	public void testGetAllUsersMangas() throws Exception {
		
		String uri = "/user/{username}/manga";
		
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.get(uri,user.getUsername())
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		
	}

}
