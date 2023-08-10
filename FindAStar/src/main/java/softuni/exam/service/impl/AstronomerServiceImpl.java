package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AstronomerSeedRootDto;
import softuni.exam.models.entity.Astronomer;
import softuni.exam.models.entity.Star;
import softuni.exam.repository.AstronomerRepository;
import softuni.exam.repository.StarRepository;
import softuni.exam.service.AstronomerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;

@Service
public class AstronomerServiceImpl implements AstronomerService {

    private static String ASTRONOMERS_FILE_PATH = "src/main/resources/files/xml/astronomers.xml";
    private final AstronomerRepository astronomerRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final StarRepository starRepository;

    public AstronomerServiceImpl(AstronomerRepository astronomerRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, StarRepository starRepository) {
        this.astronomerRepository = astronomerRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.starRepository = starRepository;
    }

    @Override
    public boolean areImported() {
        return astronomerRepository.count() > 0;
    }

    @Override
    public String readAstronomersFromFile() throws IOException {
        return Files.readString(Path.of(ASTRONOMERS_FILE_PATH));
    }

    @Override
    public String importAstronomers() throws IOException, JAXBException {

        StringBuilder builder = new StringBuilder();

        xmlParser.fromFile(ASTRONOMERS_FILE_PATH, AstronomerSeedRootDto.class)
                .getAstronomers()
                .stream()
                .filter(astronomerSeedDto -> {
                    boolean isValid = validationUtil.isValid(astronomerSeedDto);

                    Optional<Astronomer> astronomer =
                            astronomerRepository.findByFirstNameAndLastName(
                                    astronomerSeedDto.getFirstName(), astronomerSeedDto.getLastName());
                    Optional<Star> star = starRepository.findById(astronomerSeedDto.getObservingStar());

                    if (astronomer.isPresent() || star.isEmpty()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format(Locale.US,"Successfully imported astronomer %s %s - %.2f",
                            astronomerSeedDto.getFirstName(), astronomerSeedDto.getLastName(),
                            astronomerSeedDto.getAverageObservationHours())
                            : "Invalid astronomer");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(astronomerSeedDto -> {
                    Astronomer astronomer = modelMapper.map(astronomerSeedDto, Astronomer.class);

                    Optional<Star> star = starRepository.findById(astronomerSeedDto.getObservingStar());

                    astronomer.setObservingStar(star.get());

                    return astronomer;
                })
                .forEach(astronomerRepository::save);

        return builder.toString().trim();
    }
}
