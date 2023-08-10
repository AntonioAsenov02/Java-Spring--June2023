package com.example.modelmapper;

import com.example.modelmapper.entities.Address;
import com.example.modelmapper.entities.Employee;
import com.example.modelmapper.entities.dtos.EmployeeDTO;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;

import java.math.BigDecimal;

public class Main implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {

        ModelMapper modelMapper = new ModelMapper();

        Address address = new Address("Bulgaria", "Sofia");
        Employee employee = new Employee("First", BigDecimal.TEN, address);

        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);

        System.out.println(employeeDTO);
    }
}
