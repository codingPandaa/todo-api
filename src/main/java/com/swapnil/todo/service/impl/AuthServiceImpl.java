package com.swapnil.todo.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swapnil.todo.dto.JwtAuthResponse;
import com.swapnil.todo.dto.LoginDto;
import com.swapnil.todo.dto.RegisterDto;
import com.swapnil.todo.entity.Role;
import com.swapnil.todo.entity.User;
import com.swapnil.todo.exception.TodoApiException;
import com.swapnil.todo.repo.RoleRepo;
import com.swapnil.todo.repo.UserRepo;
import com.swapnil.todo.security.JwtTokenProvider;
import com.swapnil.todo.service.AuthService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

	private UserRepo userRepo;
	private RoleRepo roleRepo;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public String register(RegisterDto registerDto) {

		if (userRepo.existsByUsername(registerDto.getUsername())) {
			throw new TodoApiException(HttpStatus.BAD_REQUEST, "Username already exists");
		}

		if (userRepo.existsByEmail(registerDto.getEmail())) {
			throw new TodoApiException(HttpStatus.BAD_REQUEST, "Email already exists");
		}

		User user = new User();

		user.setName(registerDto.getName());
		user.setUsername(registerDto.getUsername());
		user.setEmail(registerDto.getEmail());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		Set<Role> roles = new HashSet<>();

		Role userRole = roleRepo.findByName("ROLE_USER");
		roles.add(userRole);

		user.setRoles(roles);

		userRepo.save(user);

		return "User registered successfully";
	}

	@Override
	public JwtAuthResponse login(LoginDto loginDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtTokenProvider.generateToken(authentication);

		Optional<User> userOptional = userRepo.findByUsernameOrEmail(loginDto.getUsernameOrEmail(),
				loginDto.getUsernameOrEmail());

		// getting role of the user logged in
		String role = null;

		if (userOptional.isPresent()) {
			User loggedInUser = userOptional.get();
			Optional<Role> optionalRole = loggedInUser.getRoles().stream().findFirst();

			if (optionalRole.isPresent()) {
				Role userRole = optionalRole.get();
				role = userRole.getName();
			}
		}

		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
		jwtAuthResponse.setRole(role);
		jwtAuthResponse.setAccessToken(token);

		return jwtAuthResponse;
	}

}
