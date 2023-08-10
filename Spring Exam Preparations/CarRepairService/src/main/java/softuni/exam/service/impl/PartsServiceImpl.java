package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PartSeedDto;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartsRepository;
import softuni.exam.service.PartsService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@Service
public class PartsServiceImpl implements PartsService {

    private static final String PARTS_FILE_PATH = "src/main/resources/files/json/parts.json";
    private final PartsRepository partRepository;

    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public PartsServiceImpl(PartsRepository partRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.partRepository = partRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return partRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        return Files.readString(Path.of(PARTS_FILE_PATH));
    }

    @Override
    public String importParts() throws IOException {

        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readPartsFileContent(), PartSeedDto[].class))
                .filter(partSeedDto -> {
                    boolean isValid = validationUtil.isValid(partSeedDto);

                    Optional<Part> part = partRepository.findByPartName(partSeedDto.getPartName());

                    if (part.isPresent()){
                        isValid = false;
                    }

                    builder.append(isValid ? String.format(Locale.US,"Successfully imported part %s - %s",
                            partSeedDto.getPartName(), partSeedDto.getPrice())
                            : "Invalid part");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(partSeedDto -> modelMapper.map(partSeedDto, Part.class))
                .forEach(partRepository::save);

        return builder.toString();
    }
}
