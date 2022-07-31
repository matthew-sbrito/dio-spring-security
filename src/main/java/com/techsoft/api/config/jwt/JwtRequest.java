package com.techsoft.api.config.jwt;

import lombok.Data;

@Data
public class JwtRequest {
	private String username;
	private String password;
}
