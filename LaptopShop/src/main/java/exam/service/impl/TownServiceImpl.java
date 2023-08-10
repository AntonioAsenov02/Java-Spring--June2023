package exam.service.impl;


import exam.model.dtos.TownSeedRootDto;
import exam.model.entity.Town;
import exam.repository.TownRepository;
import exam.service.TownService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class TownServiceImpl implements TownService {

    private static String TOWNS_FILE_PATH = "src/main/resources/files/xml/towns.xml";
    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;

    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
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
    public String importTowns() throws JAXBException, IOException {

        StringBuilder builder = new StringBuilder();

        xmlParser.fromFile(TOWNS_FILE_PATH, TownSeedRootDto.class)
                .getTowns()
                .stream()
                .filter(townSeedDto -> {
                    boolean isValid = validationUtil.isValid(townSeedDto);

                    Optional<Town> town = townRepository.findByName(townSeedDto.getName());

                    if (town.isPresent()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format("Successfully imported Town %s",
                            townSeedDto.getName()) : "Invalid town");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(townSeedDto -> modelMapper.map(townSeedDto, Town.class))
                .forEach(townRepository::save);


        return builder.toString().trim();
    }
}
