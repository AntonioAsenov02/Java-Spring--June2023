package exam.repository;

import exam.model.dtos.LaptopExtractDto;
import exam.model.entity.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Long> {


    Optional<Laptop> findByMacAddress(String macAddress);

    @Query("SELECT new exam.model.dtos.LaptopExtractDto(" +
            "l.macAddress, l.cpuSpeed, " +
            "l.ram, l.storage, l.price, s.name, t.name)" +
            "FROM Laptop AS l " +
            "JOIN l.shop AS s " +
            "JOIN  s.town AS t " +
            "ORDER BY l.cpuSpeed desc, l.ram desc, l.storage desc, l.macAddress")
    List<LaptopExtractDto> findAllOrdered();
}
