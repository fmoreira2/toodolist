package br.com.fms.toodolist.task;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TaskModel task) {

        var taskSaved = this.taskRepository.save(task); 
        return ResponseEntity.created(null).body(taskSaved);
    }
}
