package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TownSeedDto;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
public class TownServiceImpl implements TownService {

    private static final String TOWNS_FILE_PATH = "src/main/resources/files/json/towns.json";
    private final TownRepository townRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public TownServiceImpl(TownRepository townRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(TOWNS_FILE_PATH));
    }

    @Override
    public String importTowns() throws IOException {

        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readTownsFileContent(), TownSeedDto[].class))
                .filter(townSeedDto -> {
                    boolean isValid = validationUtil.isValid(townSeedDto);

                    Optional<Town> town = townRepository.findByTownName(townSeedDto.getTownName());

                    if (town.isPresent()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format("Successfully imported town %s - %d",
                            townSeedDto.getTownName(), townSeedDto.getPopulation())
                            : "Invalid town");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(townSeedDto -> modelMapper.map(townSeedDto, Town.class))
                .forEach(townRepository::save);

        return builder.toString().trim();
    }
}