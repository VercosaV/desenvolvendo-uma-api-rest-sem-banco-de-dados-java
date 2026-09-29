package dio.taskmanager.application;

import dio.taskmanager.application.input.CreateTaskInput;
import dio.taskmanager.application.output.TaskOutput;
import dio.taskmanager.domain.Task;
import dio.taskmanager.domain.TaskRepository;

public class CreateTaskUseCase {
    private final TaskRepository repository;

    public CreateTaskUseCase(TaskRepository repository){
        this.repository = repository;
    }

    public TaskOutput execute(CreateTaskInput input) {
       var task =  new Task(input.title(), input.description());
       // repository.save()
        var saved = repository.save(task);
        return TaskOutput.from(saved);
    }
}
