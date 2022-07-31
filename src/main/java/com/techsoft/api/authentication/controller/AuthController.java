package com.techsoft.api.authentication.controller;

import com.techsoft.api.authentication.domain.Role;
import com.techsoft.api.authentication.domain.ApplicationUser;
import com.techsoft.api.authentication.service.RoleService;
import com.techsoft.api.authentication.service.UserService;
import com.techsoft.api.config.jwt.JwtRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserService userService;
	private final RoleService roleService;

	@Autowired
	public AuthController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}

	@PostMapping("/signIn")
	public ResponseEntity<?> authenticateUser(@RequestBody JwtRequest request) {
		try {
			ApplicationUser user = userService.signIn(request);
			List<String> authorities = user.getRoles().stream().map(Role::getAuthority).collect(Collectors.toList());
			HashMap<String, Object> hashMap = new HashMap<>();

			hashMap.put("roles", authorities);
			hashMap.put("username", user.getUsername());
			hashMap.put("token", user.getToken());

			return ResponseEntity.ok(hashMap);
		} catch (Exception e) {
			return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@PostMapping("/create")
	public ResponseEntity<ApplicationUser> create() {
		Role roleAdmin = roleService.list().get(0);

		ApplicationUser user = new ApplicationUser();
		user.setEnabled(true);
		user.setEmail("teste@teste.com");
		user.setFullName("Matheus Brito");
		user.setPassword(new BCryptPasswordEncoder().encode("123456"));
		user.setUsername("karantes");
		user.setRoles(Collections.singletonList(roleAdmin));

		log.info("Save user {}", user.toString());

        userService.saveDomain(user);

        URI uri = URI.create(String.format("/user/%s", user.getId().toString()));

        return ResponseEntity.created(uri).body(user);
	}

}
