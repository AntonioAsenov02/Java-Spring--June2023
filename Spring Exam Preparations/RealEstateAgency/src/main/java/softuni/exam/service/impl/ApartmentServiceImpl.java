package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentSeedRootDto;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.models.entity.enums.ApartmentType;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    private static String APARTMENTS_FILE_PATH = "src/main/resources/files/xml/apartments.xml";
    private final ApartmentRepository apartmentRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final TownRepository townRepository;

    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, TownRepository townRepository) {
        this.apartmentRepository = apartmentRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of(APARTMENTS_FILE_PATH));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {

        StringBuilder builder = new StringBuilder();

        xmlParser.fromFile(APARTMENTS_FILE_PATH, ApartmentSeedRootDto.class)
                .getApartments().stream()
                .filter(apartmentSeedDto -> {
                    boolean isValid = validationUtil.isValid(apartmentSeedDto);

                    Optional<Apartment> apartment =
                            apartmentRepository.findByTown_TownNameAndArea(
                                    apartmentSeedDto.getTown(), apartmentSeedDto.getArea());


                    if (apartment.isPresent()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format("Successfully imported apartment %s - %s",
                            apartmentSeedDto.getApartmentType(), apartmentSeedDto.getArea())
                            : "Invalid apartment");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(apartmentSeedDto -> {
                    Apartment apartment = modelMapper.map(apartmentSeedDto, Apartment.class);

                    Optional<Town> town = townRepository.findByTownName(apartmentSeedDto.getTown());

                    apartment.setApartmentType(ApartmentType.valueOf(apartmentSeedDto.getApartmentType()));
                    apartment.setTown(town.get());

                    return apartment;
                })
                .forEach(apartmentRepository::save);

        return builder.toString().trim();
    }
}
