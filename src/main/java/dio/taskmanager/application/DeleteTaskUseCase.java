package dio.taskmanager.application;

import dio.taskmanager.domain.TaskId;
import dio.taskmanager.domain.TaskNotFoundException;
import dio.taskmanager.domain.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteTaskUseCase {
    private final TaskRepository repository;

    public DeleteTaskUseCase(TaskRepository repository){
        this.repository = repository;
    }

    public void execute(TaskId taskid) {
       if( repository.findById(taskid).isEmpty()) {
           throw new TaskNotFoundException(taskid);
       }

       repository.delete(taskid);
    }
}
