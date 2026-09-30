package dio.taskmanager.application;

import dio.taskmanager.application.output.TaskOutput;
import dio.taskmanager.domain.TaskId;
import dio.taskmanager.domain.TaskRepository;
import dio.taskmanager.domain.TaskNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetByIdTasksUseCase {
    private final TaskRepository repository;

    public GetByIdTasksUseCase(TaskRepository repository){
        this.repository = repository;
    }

    public TaskOutput execute(TaskId id) {
        return repository.findById(id).map(TaskOutput::from).orElseThrow(() -> new TaskNotFoundException(id));
    }



}
