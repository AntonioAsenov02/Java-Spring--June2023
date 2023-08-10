package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AgentSeedDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.AgentService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
public class AgentServiceImpl implements AgentService {

    private static final String AGENTS_FILE_PATH = "src/main/resources/files/json/agents.json";
    private final AgentRepository agentRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final TownRepository townRepository;

    public AgentServiceImpl(AgentRepository agentRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil, TownRepository townRepository) {
        this.agentRepository = agentRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(Path.of(AGENTS_FILE_PATH));
    }

    @Override
    public String importAgents() throws IOException {

        StringBuilder builder = new StringBuilder();

        Arrays.stream(gson.fromJson(readAgentsFromFile(), AgentSeedDto[].class))
                .filter(agentSeedDto -> {
                    boolean isValid = validationUtil.isValid(agentSeedDto);

                    Optional<Agent> agent = agentRepository.findByFirstName(agentSeedDto.getFirstName());

                    if (agent.isPresent()) {
                        isValid = false;
                    }

                    builder.append(isValid ? String.format("Successfully imported agent - %s %s",
                            agentSeedDto.getFirstName(), agentSeedDto.getLastName())
                            : "Invalid agent");
                    builder.append(System.lineSeparator());

                    return isValid;
                })
                .map(agentSeedDto -> {
                    Agent agent = modelMapper.map(agentSeedDto, Agent.class);

                    Optional<Town> town = townRepository.findByTownName(agentSeedDto.getTown());

                    agent.setTown(town.get());

                    return agent;
                })
                .forEach(agentRepository::save);

        return builder.toString().trim();
    }
}
