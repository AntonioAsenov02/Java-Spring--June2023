package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OffersSeedRootDto;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.OfferService;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class OfferServiceImpl implements OfferService {

    private static final String OFFER_FILE_PATH =  "src/main/resources/files/xml/offers.xml";
    private final OfferRepository offerRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CarService carService;
    private final SellerService sellerService;

    public OfferServiceImpl(OfferRepository offerRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil, CarService carService, SellerService sellerService) {
        this.offerRepository = offerRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.carService = carService;
        this.sellerService = sellerService;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFER_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {

        StringBuilder builder = new StringBuilder();

        xmlParser.fromFile(OFFER_FILE_PATH, OffersSeedRootDto.class)
                .getOffers()
                .stream()
                .filter(offersSeedDto -> {
                    boolean isValid = validationUtil.isValid(offersSeedDto);

                    builder.append(isValid ? String.format("Successfully import offer %s - %s",
                            offersSeedDto.getAddedOn(), offersSeedDto.getHasGoldStatus())
                                    : "Invalid offer")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(offersSeedDto -> {
                    Offer offer = modelMapper.map(offersSeedDto, Offer.class);
                    offer.setCar(carService.findById(offersSeedDto.getCar().getId()));
                    offer.setSeller(sellerService.findById(offersSeedDto.getSeller().getId()));

                    return offer;
                })
                .forEach(offerRepository::save);

        return builder.toString();
    }
}
