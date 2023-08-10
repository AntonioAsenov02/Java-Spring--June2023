package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dtos.LaptopSeedDto;
import exam.model.entity.Laptop;
import exam.model.entity.Shop;
import exam.repository.LaptopRepository;
import exam.repository.ShopRepository;
import exam.service.LaptopService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
public class LaptopServiceImpl implements LaptopService {

    private static String LAPTOPS_FILES_PATH = "src/main/resources/files/json/laptops.json";
    private final LaptopRepository laptopRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ShopRepository shopRepository;

    public LaptopServiceImpl(LaptopRepository laptopRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, ShopRepository shopRepository) {
        this.laptopRepository = laptopRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.shopRepository = shopRepository;
    }

    @Override
    public boolean areImported() {
        return laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files.readString(Path.of(LAPTOPS_FILES_PATH));
    }

    @Override
    public String importLaptops() throws IOException {

        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readLaptopsFileContent(), LaptopSeedDto[].class))
                .filter(laptopSeedDto -> {
                    boolean isValid = validationUtil.isValid(laptopSeedDto);

                    Optional<Laptop> laptop = laptopRepository.findByMacAddress(laptopSeedDto.getMacAddress());

                    if (laptop.isPresent()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format("Successfully imported Laptop %s - %s - %d - %d",
                            laptopSeedDto.getMacAddress(), laptopSeedDto.getCpuSpeed(),
                            laptopSeedDto.getRam(), laptopSeedDto.getStorage()) : "Invalid Laptop");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(laptopSeedDto -> {
                    Laptop laptop = modelMapper.map(laptopSeedDto, Laptop.class);

                    Optional<Shop> shop = shopRepository.findByName(laptopSeedDto.getShop().getName());

                    laptop.setWarrantyType(laptopSeedDto.getWarrantyType());
                    laptop.setShop(shop.get());

                    return laptop;
                })
                .forEach(laptopRepository::save);

        return builder.toString().trim();
    }

    @Override
    public String exportBestLaptops() {

        StringBuilder builder = new StringBuilder();

        laptopRepository.findAllOrdered()
                .forEach(laptopExtractDto ->
                        builder.append(String.format("Laptop - %s%n" +
                        "*Cpu speed - %s%n**Ram - %d%n***Storage - %d%n" +
                        "****Price - %s%n#Shop name - %s%n##Town - %s%n",
                        laptopExtractDto.getMacAddress(), laptopExtractDto.getCpuSpeed(),
                        laptopExtractDto.getRam(), laptopExtractDto.getStorage(),
                        laptopExtractDto.getPrice(), laptopExtractDto.getShop(),
                        laptopExtractDto.getTown())).append(System.lineSeparator()));

        return builder.toString().trim();
    }
}
