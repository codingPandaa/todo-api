package com.swapnil.todo.service;

import com.swapnil.todo.dto.JwtAuthResponse;
import com.swapnil.todo.dto.LoginDto;
import com.swapnil.todo.dto.RegisterDto;

public interface AuthService {

	String register(RegisterDto registerDto);

	JwtAuthResponse login(LoginDto loginDto);
}
