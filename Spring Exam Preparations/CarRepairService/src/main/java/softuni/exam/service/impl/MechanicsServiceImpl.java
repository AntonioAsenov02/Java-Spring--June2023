package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.MechanicSeedDto;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicsRepository;
import softuni.exam.service.MechanicsService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
public class MechanicsServiceImpl implements MechanicsService {

    private static final String MECHANICS_FILE_PATH = "src/main/resources/files/json/mechanics.json";
    private final MechanicsRepository mechanicRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    public MechanicsServiceImpl(MechanicsRepository mechanicRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.mechanicRepository = mechanicRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }


    @Override
    public boolean areImported() {
        return mechanicRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        return Files.readString(Path.of(MECHANICS_FILE_PATH));
    }

    @Override
    public String importMechanics() throws IOException {

        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readMechanicsFromFile(), MechanicSeedDto[].class))
                .filter(mechanicSeedDto -> {
                    boolean isValid = validationUtil.isValid(mechanicSeedDto);

                    Optional<Mechanic> mechanicByName = mechanicRepository.findByFirstName(mechanicSeedDto.getFirstName());
                    Optional<Mechanic> mechanicByEmail = mechanicRepository.findByEmail(mechanicSeedDto.getEmail());
                    Optional<Mechanic> mechanicByPhone = mechanicRepository.findByPhone(mechanicSeedDto.getPhone());

                    if (mechanicByName.isPresent() || mechanicByEmail.isPresent() || mechanicByPhone.isPresent()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format("Successfully imported mechanic %s %s",
                            mechanicSeedDto.getFirstName(), mechanicSeedDto.getLastName())
                            : "Invalid mechanic");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(mechanicSeedDto -> modelMapper.map(mechanicSeedDto, Mechanic.class))
                .forEach(mechanicRepository::save);


        return builder.toString();
    }
}
