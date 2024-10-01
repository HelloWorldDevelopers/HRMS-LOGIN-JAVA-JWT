package hrms.rnt.login.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import hrms.rnt.login.Inputmodel.Credentials;
import hrms.rnt.login.customeException.CustomeException;
import hrms.rnt.login.repo.AppMasterRepo;
import hrms.rnt.login.repo.EmployeeMasterRepo;
import hrms.rnt.login.security.CustomeUserDetailsService;
import hrms.rnt.login.security.JwtHelper;
import hrms.rnt.login.security.JwtTokenDecoder;
import hrms.rnt.login.security.Sha1Encriptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(allowedHeaders = "*",origins = "*")
@RequestMapping("/api/v1/authentication")
public class LoginController {
	
	@Autowired
	EmployeeMasterRepo employeeMasterRepo;
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	AppMasterRepo appMasterRepo;

	@Autowired
	CustomeUserDetailsService customeUserDetailsService;

	@Autowired
	JwtHelper jwtUtil;

	@PostMapping("/login")
 	public ResponseEntity<Map<String, Object>> authenticateUser( @RequestBody Credentials credentials) throws CustomeException {

		Map<String, Object> map = new HashMap<>();
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUserName(),
					Sha1Encriptor.encryptThisString(credentials.getPassword())));
			UserDetails userDetails = this.customeUserDetailsService.loadUserByUsername(credentials.getUserName());
			String token = this.jwtUtil.generateToken(userDetails);
		 
			map.put("token", token);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			map.put("token", null);
  			log.error("error occor during create token");
 			throw new CustomeException(e);
		}
		return ResponseEntity.ok(map);
	
	}
	
	@PostMapping("/parse")
 	public ResponseEntity<Map<String, Object>> extractDetailFromToken( @RequestBody Credentials credentials) {
		Map<String, Object> map = new HashMap<>();
		try {
		 
			JsonNode json = new ObjectMapper()
					.readTree(new JwtTokenDecoder().testDecodeJWT(new String(credentials.getToken())));
			map.put("staffId", json.get("staffId"));
			map.put("userName", json.get("userName"));
 			map.put("createdTime", json.get("createdTime"));
 			map.put("roles", json.get("roles"));
 			map.put("status", true);
 			map.put("success", true);
		} catch (Exception e) {
			log.error("error occor during parse token");
			throw new CustomeException(e);
		}
		return ResponseEntity.ok(map);
		
 	
	}
	
	@GetMapping("/allapps")
 	public ResponseEntity<Map<String, Object>> getAllApp() {
		Map<String, Object> map = new HashMap<>();
		try {
			map.put("data",appMasterRepo.findAllByDeletedByIsNull());
			map.put("success", true);
		} catch (Exception e) {
			log.error("error occor during get all app ");
			throw new CustomeException(e);
		}
		return ResponseEntity.ok(map);
	
	}
	
	
	 

}
