package exam.service.impl;

import exam.model.dtos.ShopSeedRootDto;
import exam.model.entity.Shop;
import exam.model.entity.Town;
import exam.repository.ShopRepository;
import exam.repository.TownRepository;
import exam.service.ShopService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class ShopServiceImpl implements ShopService {

    private static String SHOPS_FILE_PATH = "src/main/resources/files/xml/shops.xml";
    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final TownRepository townRepository;

    public ShopServiceImpl(ShopRepository shopRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, TownRepository townRepository) {
        this.shopRepository = shopRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(Path.of(SHOPS_FILE_PATH));
    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {

        StringBuilder builder = new StringBuilder();

        xmlParser.fromFile(SHOPS_FILE_PATH, ShopSeedRootDto.class).getShops()
                .stream()
                .filter(shopSeedDto -> {
                    boolean isValid = validationUtil.isValid(shopSeedDto);

                    Optional<Shop> shop = shopRepository.findByName(shopSeedDto.getName());

                    if (shop.isPresent()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format("Successfully imported Shop %s %s",
                            shopSeedDto.getName(), shopSeedDto.getIncome())
                            : "Invalid shop");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(shopSeedDto -> {
                    Shop shop = modelMapper.map(shopSeedDto, Shop.class);

                    Optional<Town> town = townRepository.findByName(shopSeedDto.getTown().getName());

                    shop.setTown(town.get());

                    return shop;
                })
                .forEach(shopRepository::save);


        return builder.toString().trim();
    }
}
