package com.swapnil.todo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swapnil.todo.entity.Todo;

public interface TodoRepo extends JpaRepository<Todo, Long> {

}
