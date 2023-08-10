package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TaskExportDto;
import softuni.exam.models.dto.TasksSeedRootDto;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.models.entity.Part;
import softuni.exam.models.entity.Task;
import softuni.exam.models.entity.enums.CarType;
import softuni.exam.repository.CarsRepository;
import softuni.exam.repository.MechanicsRepository;
import softuni.exam.repository.PartsRepository;
import softuni.exam.repository.TasksRepository;
import softuni.exam.service.TasksService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;

@Service
public class TasksServiceImpl implements TasksService {

    private static String TASKS_FILE_PATH = "src/main/resources/files/xml/tasks.xml";
    private final TasksRepository taskRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final MechanicsRepository mechanicsRepository;
    private final CarsRepository carsRepository;
    private final PartsRepository partsRepository;

    public TasksServiceImpl(TasksRepository taskRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil, MechanicsRepository mechanicsRepository, CarsRepository carsRepository, PartsRepository partsRepository) {
        this.taskRepository = taskRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.mechanicsRepository = mechanicsRepository;
        this.carsRepository = carsRepository;
        this.partsRepository = partsRepository;
    }

    @Override
    public boolean areImported() {
        return taskRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return Files.readString(Path.of(TASKS_FILE_PATH));
    }

    @Override
    public String importTasks() throws IOException, JAXBException {

        StringBuilder builder = new StringBuilder();

        xmlParser.fromFile(TASKS_FILE_PATH, TasksSeedRootDto.class)
                .getTasks()
                .stream()
                .filter(taskSeedDto -> {
                    boolean isValid = validationUtil.isValid(taskSeedDto);

                    Optional<Mechanic> mechanic = mechanicsRepository.
                            findByFirstName(taskSeedDto.getMechanic().getFirstName());
                    Optional<Car> car = carsRepository.
                            findById(taskSeedDto.getCar().getId());

                    if (mechanic.isEmpty() || car.isEmpty()){
                        isValid = false;
                    }

                    builder.append(isValid ? String.format(Locale.US,"Successfully imported task %.2f",
                            taskSeedDto.getPrice()) : "Invalid task");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(taskSeedDto -> {
                    Task task = modelMapper.map(taskSeedDto, Task.class);

                    Optional<Car> car = carsRepository.findById(taskSeedDto.getCar().getId());
                    Optional<Part> part = partsRepository.findById(taskSeedDto.getPart().getId());
                    Optional<Mechanic> mechanic = mechanicsRepository
                            .findByFirstName(taskSeedDto.getMechanic().getFirstName());


                    task.setCar(car.get());
                    task.setPart(part.get());
                    task.setMechanic(mechanic.get());

                    return task;
                })
                .forEach(taskRepository::save);


        return builder.toString();
    }

    @Override
    public String getCoupeCarTasksOrderByPrice() {

        StringBuilder builder = new StringBuilder();

        taskRepository.findAllByCar_CarTypeOrderByPriceDesc(CarType.coupe)
                .stream()
                .map(task -> modelMapper.map(task, TaskExportDto.class))
                .forEach(taskExportDto -> {
                    builder.append(String.format("Car %s %s with %dkm%n-Mechanic: %s %s - task №%d:%n " +
                            "--Engine: %s%n---Price: %s$",
                            taskExportDto.getCar().getCarMake(), taskExportDto.getCar().getCarModel(),
                            taskExportDto.getCar().getKilometers(), taskExportDto.getMechanic().getFirstName(),
                            taskExportDto.getMechanic().getLastName(), taskExportDto.getId(),
                            taskExportDto.getCar().getEngine(), taskExportDto.getPrice()));
                    builder.append(System.lineSeparator());
                });

        return builder.toString().trim();
    }
}
