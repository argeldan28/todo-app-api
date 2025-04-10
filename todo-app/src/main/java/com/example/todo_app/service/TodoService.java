package com.example.todo_app.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.todo_app.model.Todo;
import com.example.todo_app.repository.TodoRepository;

@Service
public class TodoService {
    private final TodoRepository todoRepo;

    public TodoService(TodoRepository todoRepo){
        this.todoRepo = todoRepo;
    }
        

    public List<Todo> getAllTodos(){
      return todoRepo.findAll();
    }

    public Todo getTodoById(Integer id){
        return todoRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));
    }

    public Todo createTodo(Todo t){
        return todoRepo.save(t);
    }

    public Todo updateTodo(Integer id, Todo updatedTodo){
        return todoRepo.findById(id).map(t ->{
          if (updatedTodo.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Title cannot be blank");
          }

          t.setTitle(updatedTodo.getTitle());
          t.setCompleted(updatedTodo.isCompleted());

          return todoRepo.save(t);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Todo not found with id " + id));
    }

    public void deleteTodo(Integer id){
        if (!todoRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Todo not found with id " + id);
        }
        todoRepo.deleteById(id);
    }
}
