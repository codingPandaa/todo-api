package com.swapnil.todo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swapnil.todo.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {

	Role findByName(String name);
}
