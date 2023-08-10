package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountrySeedDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    private static final String COUNTRY_FILE_PATH = "src/main/resources/files/json/countries.json";
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(Path.of(COUNTRY_FILE_PATH));
    }

    @Override
    public String importCountries() throws IOException {

        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readCountriesFromFile(), CountrySeedDto[].class))
                .filter(countrySeedDto -> {
                    boolean isValid = validationUtil.isValid(countrySeedDto);

                    Optional<Country> country = countryRepository.findByCountryName(countrySeedDto.getCountryName());

                    if (country.isPresent()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format("Successfully imported country %s - %s",
                            countrySeedDto.getCountryName(), countrySeedDto.getCurrency())
                            : "Invalid country");
                    builder.append(System.lineSeparator());


                    return isValid;
                })
                .map(countrySeedDto -> modelMapper.map(countrySeedDto, Country.class))
                .forEach(countryRepository::save);


        return builder.toString();
    }

    @Override
    public Optional<Country> getCountryById(Long countryId) {
        return countryRepository.findById(countryId);
    }
}
