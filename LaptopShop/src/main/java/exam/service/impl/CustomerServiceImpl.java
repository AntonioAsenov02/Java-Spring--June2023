package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dtos.CustomerSeedDto;
import exam.model.entity.Customer;
import exam.model.entity.Town;
import exam.repository.CustomerRepository;
import exam.repository.TownRepository;
import exam.service.CustomerService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static String CUSTOMERS_FILE_PATH = "src/main/resources/files/json/customers.json";
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final TownRepository townRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, TownRepository townRepository) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Files.readString(Path.of(CUSTOMERS_FILE_PATH));
    }

    @Override
    public String importCustomers() throws IOException {

        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readCustomersFileContent(), CustomerSeedDto[].class))
                .filter(customerSeedDto -> {
                    boolean isValid = validationUtil.isValid(customerSeedDto);

                    Optional<Customer> customer = customerRepository.findByEmail(customerSeedDto.getEmail());

                    if (customer.isPresent()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format("Successfully imported Customer %s %s - %s",
                            customerSeedDto.getFirstName(), customerSeedDto.getLastName(),
                            customerSeedDto.getEmail()) : "Invalid Customer");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(customerSeedDto -> {
                    Customer customer = modelMapper.map(customerSeedDto, Customer.class);

                    Optional<Town> town = townRepository.findByName(customerSeedDto.getTown().getName());

                    customer.setTown(town.get());

                    return customer;
                })
                .forEach(customerRepository::save);


        return builder.toString().trim();
    }
}
