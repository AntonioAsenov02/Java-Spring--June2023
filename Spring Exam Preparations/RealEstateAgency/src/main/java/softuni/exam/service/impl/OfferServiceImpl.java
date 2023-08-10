package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferExportDto;
import softuni.exam.models.dto.OfferSeedRootDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Offer;
import softuni.exam.models.entity.enums.ApartmentType;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {

    private static String OFFERS_FILE_PATH = "src/main/resources/files/xml/offers.xml";
    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final AgentRepository agentRepository;
    private final ApartmentRepository apartmentRepository;

    public OfferServiceImpl(OfferRepository offerRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, AgentRepository agentRepository, ApartmentRepository apartmentRepository) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.agentRepository = agentRepository;
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {

        StringBuilder builder = new StringBuilder();

        xmlParser.fromFile(OFFERS_FILE_PATH, OfferSeedRootDto.class)
                .getOffers()
                .stream()
                .filter(offerSeedDto -> {
                    boolean isValid = validationUtil.isValid(offerSeedDto);

                    Optional<Agent> agent = agentRepository.findByFirstName(offerSeedDto.getAgent().getFirstName());

                    if (agent.isEmpty()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format("Successfully imported offer %s",
                            offerSeedDto.getPrice()) : "Invalid offer");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(offerSeedDto -> {
                    Offer offer = modelMapper.map(offerSeedDto, Offer.class);

                    Optional<Agent> agent = agentRepository.findByFirstName(offerSeedDto.getAgent().getFirstName());
                    Optional<Apartment> apartment = apartmentRepository.findById(offerSeedDto.getApartment().getId());

                    offer.setAgent(agent.get());
                    offer.setApartment(apartment.get());

                    return offer;
                })
                .forEach(offerRepository::save);


        return builder.toString().trim();
    }

    @Override
    public String exportOffers() {

        StringBuilder builder = new StringBuilder();

        offerRepository.findByApartment_ApartmentTypeOrderByApartment_AreaDescPrice(ApartmentType.three_rooms)
                .stream()
                .map(offer -> modelMapper.map(offer, OfferExportDto.class))
                .forEach(offerExportDto ->  {
                    builder.append(String.format("Agent %s %s with offer №%d:%n\t" +
                                    "-Apartment area: %s%n\t--Town: %s%n\t---Price: %s$",
                            offerExportDto.getAgent().getFirstName(), offerExportDto.getAgent().getLastName(),
                            offerExportDto.getId(), offerExportDto.getApartment().getArea(),
                            offerExportDto.getApartment().getTown().getTownName(), offerExportDto.getPrice()));
                    builder.append(System.lineSeparator());
                });

        return builder.toString().trim();
    }
}
