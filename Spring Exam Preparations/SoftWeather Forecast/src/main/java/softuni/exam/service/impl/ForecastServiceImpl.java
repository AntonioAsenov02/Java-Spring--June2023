package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ForecastSeedRootDto;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class ForecastServiceImpl implements ForecastService {

    private static final String FORECAST_FILE_PATH = "src/main/resources/files/xml/forecasts.xml";
    private final ForecastRepository forecastRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final CityService cityService;

    public ForecastServiceImpl(ForecastRepository forecastRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, CityService cityService) {
        this.forecastRepository = forecastRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.cityService = cityService;
    }

    @Override
    public boolean areImported() {
        return forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(Path.of(FORECAST_FILE_PATH));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {

        StringBuilder builder = new StringBuilder();

        xmlParser.fromFile(FORECAST_FILE_PATH, ForecastSeedRootDto.class)
                .getForecasts()
                .stream()
                .filter(forecastSeedDto -> {
                    boolean isValid = validationUtil.isValid(forecastSeedDto);

                    Optional<Forecast> forecast =
                            forecastRepository.findByCityAndDayOfWeek(forecastSeedDto.getCity().getId(), forecastSeedDto.getDayOfWeek());

                    if (forecast.isPresent()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format("Successfully imported forecast %s - %s",
                            forecastSeedDto.getDayOfWeek(), forecastSeedDto.getMaxTemperature())
                            : "Invalid forecast");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(forecastSeedDto -> {

                    Forecast forecast = modelMapper.map(forecastSeedDto, Forecast.class);

                    Optional<City> cityById = cityService.getCityById(forecastSeedDto.getCity().getId());
                    if (cityById.isEmpty()) {
                        System.out.println("ERROR:  " + forecastSeedDto.getDayOfWeek());
                        return forecast;
                    }

                    forecast.setCity(cityById.get());
                    return forecast;
                })
                .forEach(forecastRepository::save);


        return builder.toString();
    }

    @Override
    public String exportForecasts() {
        return null;
    }
}
