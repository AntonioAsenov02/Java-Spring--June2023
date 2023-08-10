package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CitySeedDto;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CityRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private static final String CITY_FILE_PATH = "src/main/resources/files/json/cities.json";
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final CountryService countryService;

    public CityServiceImpl(CityRepository cityRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, CountryService countryService) {
        this.cityRepository = cityRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.countryService = countryService;
    }

    @Override
    public boolean areImported() {
        return cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(Path.of(CITY_FILE_PATH));
    }

    @Override
    public String importCities() throws IOException {

        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readCitiesFileContent(), CitySeedDto[].class))
                .filter(citySeedDto -> {
                    boolean isValid = validationUtil.isValid(citySeedDto);

                    Optional<City> city = cityRepository.findByCityName(citySeedDto.getCityName());

                    if (city.isPresent()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format("Successfully imported city %s - %d",
                            citySeedDto.getCityName(), citySeedDto.getPopulation())
                            : "Invalid city");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(citySeedDto -> modelMapper.map(citySeedDto, City.class))
//                .map(citySeedDto -> {
//                    City city = modelMapper.map(citySeedDto, City.class);
//
//                    Optional<Country> countryById = countryService.getCountryById(citySeedDto.getCountry().getId());
//                    if (countryById.isEmpty()) {
//                        System.out.println("ERROR:  " + citySeedDto.getCityName());
//                        return city;
//                    }
//
//                    city.setCountry(countryById.get());
//                    return city;
//                })
                .forEach(cityRepository::save);


        return builder.toString();
    }

    @Override
    public Optional<City> getCityById(Long city) {
        return cityRepository.findById(city);
    }


}
