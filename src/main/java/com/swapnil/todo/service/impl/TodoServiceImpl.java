package com.swapnil.todo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.swapnil.todo.dto.TodoDto;
import com.swapnil.todo.entity.Todo;
import com.swapnil.todo.exception.ResourceNotFoundException;
import com.swapnil.todo.repo.TodoRepo;
import com.swapnil.todo.service.TodoService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {

	private TodoRepo todoRepo;

	private ModelMapper modelMapper;

	@Override
	public TodoDto addTodo(TodoDto todoDto) {

		Todo todo = modelMapper.map(todoDto, Todo.class);
		Todo savedTodo = todoRepo.save(todo);
		TodoDto savedTodoDto = modelMapper.map(savedTodo, TodoDto.class);
		return savedTodoDto;
	}

	@Override
	public TodoDto getTodo(Long id) {
		Todo todo = todoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Todo not found with id:" + id));
		return modelMapper.map(todo, TodoDto.class);
	}

	@Override
	public List<TodoDto> getAllTodos() {
		List<Todo> todos = todoRepo.findAll();
		return todos.stream().map((todo) -> modelMapper.map(todo, TodoDto.class)).collect(Collectors.toList());
	}

	@Override
	public TodoDto updateTodo(TodoDto todoDto, Long id) {
		Todo todo = todoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Todo not found with id:" + id));
		todo.setTitle(todoDto.getTitle());
		todo.setDescription(todoDto.getDescription());
		todo.setCompleted(todoDto.isCompleted());
		Todo updatedTodo = todoRepo.save(todo);
		return modelMapper.map(updatedTodo, TodoDto.class);
	}

	@Override
	public void deleteTodo(Long id) {

		Todo todo = todoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Todo not found with id:" + id));

		todoRepo.deleteById(id);
	}

	@Override
	public TodoDto completeTodo(Long id) {

		Todo todo = todoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Todo not found with id:" + id));
		todo.setCompleted(Boolean.TRUE);
		Todo updatedTodo = todoRepo.save(todo);
		return modelMapper.map(updatedTodo, TodoDto.class);
	}

	@Override
	public TodoDto inCompleteTodo(Long id) {
		Todo todo = todoRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Todo not found with id:" + id));
		todo.setCompleted(Boolean.FALSE);
		Todo updatedTodo = todoRepo.save(todo);
		return modelMapper.map(updatedTodo, TodoDto.class);
	}

}
