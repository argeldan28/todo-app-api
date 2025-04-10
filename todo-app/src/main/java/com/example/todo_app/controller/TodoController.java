package com.example.todo_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.todo_app.model.Todo;
import com.example.todo_app.repository.TodoRepository;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/todos")
public class TodoController {

    //@Autowired
    private final TodoRepository repository;

    public TodoController(TodoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Todo> getAllTodos(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable Integer id) {
       return repository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Todo not found"));
    }
    
    @PostMapping
    public Todo createTodo(@RequestBody Todo todo){
        return repository.save(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Integer id, @RequestBody Todo updatedTodo) {
        return repository.findById(id).map(t -> {

            if (updatedTodo.getTitle().isBlank()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be blank");
            }

            t.setTitle(updatedTodo.getTitle());
            t.setCompleted(updatedTodo.isCompleted());

            return repository.save(t);  

        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id " + id));
        
    }

    public void deleteTodo(@PathVariable Integer id){
        repository.deleteById(id);
    }
}
