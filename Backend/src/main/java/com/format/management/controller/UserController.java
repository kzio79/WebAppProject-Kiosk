package com.format.management.controller;



import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import com.format.management.dto.ResponseDTO;
import com.format.management.dto.UserDTO;
import com.format.management.entity.UserEntity;
import com.format.management.security.TokenProvider;
import com.format.management.service.UserService;
	

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenProvider tokenProvider;
	
//	static String frontEnd = "http://localhost:8000/startbootstrap-sb-admin-2-gh-pages/";
//	RedirectView redirectview = new RedirectView();

	
//	static final String SecretKey = "fwefwegregre!WEWGWGRERRE##";
	
//	@PostMapping("/signinpass")
//	public ResponseEntity<?> androidsign(@RequestBody String passkey){
//		
//	}
	
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
	
		
		System.out.println(userDTO.getUserid());
		
		UserEntity user = userService.getByCredentials(userDTO.getUserid(), userDTO.getUserpw());

		
		if(user != null) {
			final String token = tokenProvider.create(user);
			final UserDTO responseUserDTO = UserDTO.builder()
					.userid(user.getId())
					.token(token)
					.build();
			return ResponseEntity.created(null).body(responseUserDTO);
			
		}else {
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error("Login failed.")
					.build();
			return ResponseEntity.badRequest().body(responseDTO);	
		
			
		}
	
	}
	
}
