package com.swapnil.todo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swapnil.todo.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Boolean existsByEmail(String email);

	Optional<User> findByUsernameOrEmail(String username, String email);

	Boolean existsByUsername(String username);
}
