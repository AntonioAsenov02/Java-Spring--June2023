package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ConstellationSeedDto;
import softuni.exam.models.entity.Constellation;
import softuni.exam.repository.ConstellationRepository;
import softuni.exam.service.ConstellationService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConstellationServiceImpl implements ConstellationService {

    private static String CONSTELLATIONS_FILE_PATH = "src/main/resources/files/json/constellations.json";
    private final ConstellationRepository constellationRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    public ConstellationServiceImpl(ConstellationRepository constellationRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.constellationRepository = constellationRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return constellationRepository.count() > 0;
    }

    @Override
    public String readConstellationsFromFile() throws IOException {
        return Files.readString(Path.of(CONSTELLATIONS_FILE_PATH));
    }

    @Override
    public String importConstellations() throws IOException {

        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readConstellationsFromFile(), ConstellationSeedDto[].class))
                .filter(constellationSeedDto -> {
                    boolean isValid = validationUtil.isValid(constellationSeedDto);

                    Optional<Constellation> constellation =
                            constellationRepository.findByName(constellationSeedDto.getName());

                    if (constellation.isPresent()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format("Successfully imported constellation %s - %s",
                            constellationSeedDto.getName(), constellationSeedDto.getDescription())
                            : "Invalid constellation");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(constellationSeedDto -> modelMapper.map(constellationSeedDto, Constellation.class))
                .forEach(constellationRepository::save);

        return builder.toString().trim();
    }
}
