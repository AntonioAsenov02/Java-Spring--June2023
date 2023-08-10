package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.StarSeedDto;
import softuni.exam.models.entity.Constellation;
import softuni.exam.models.entity.Star;
import softuni.exam.models.entity.enums.StarType;
import softuni.exam.repository.ConstellationRepository;
import softuni.exam.repository.StarRepository;
import softuni.exam.service.StarService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@Service
public class StarServiceImpl implements StarService {

    private static String STARS_FILE_PATH = "src/main/resources/files/json/stars.json";
    private final StarRepository starRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ConstellationRepository constellationRepository;

    public StarServiceImpl(StarRepository starRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, ConstellationRepository constellationRepository) {
        this.starRepository = starRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.constellationRepository = constellationRepository;
    }

    @Override
    public boolean areImported() {
        return starRepository.count() > 0;
    }

    @Override
    public String readStarsFileContent() throws IOException {
        return Files.readString(Path.of(STARS_FILE_PATH));
    }

    @Override
    public String importStars() throws IOException {

        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readStarsFileContent(), StarSeedDto[].class))
                .filter(starSeedDto -> {
                    boolean isValid = validationUtil.isValid(starSeedDto);

                    Optional<Star> star = starRepository.findByName(starSeedDto.getName());

                    if (star.isPresent()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format(Locale.US,"Successfully imported star %s - %.2f light years",
                            starSeedDto.getName(), starSeedDto.getLightYears())
                            : "Invalid star");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(starSeedDto -> modelMapper.map(starSeedDto, Star.class))
                .forEach(starRepository::save);

        return builder.toString().trim();
    }

    @Override
    public String exportStars() {

        StringBuilder builder = new StringBuilder();

        starRepository.findNonObservedStars()
                .forEach(starExportDto -> builder.append(String.format(Locale.US,"Star: %s%n   " +
                                        "*Distance: %.2f light years%n  " +
                        " **Description: %s%n   " +
                                        "***Constellation: %s",
                        starExportDto.getName(), starExportDto.getLightYears(),
                        starExportDto.getDescription(), starExportDto.getConstellationName()))
                        .append(System.lineSeparator()));

        return builder.toString().trim();
    }
}
