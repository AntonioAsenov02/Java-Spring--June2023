package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.dto.StarExportDto;
import softuni.exam.models.entity.Star;
import softuni.exam.models.entity.enums.StarType;

import java.util.List;
import java.util.Optional;

@Repository
public interface StarRepository extends JpaRepository<Star, Long> {


    Optional<Star> findByName(String name);

    @Query("SELECT new softuni.exam.models.dto.StarExportDto(" +
            "s.name, s.lightYears, s.description, c.name) " +
            "FROM Star AS s " +
            "LEFT JOIN Astronomer AS a ON s.id = a.observingStar.id " +
            "JOIN Constellation AS c ON c.id = s.constellation.id " +
            "WHERE s.starType = 'RED_GIANT' AND a.observingStar.id = null " +
            "ORDER BY s.lightYears")
    List<StarExportDto> findNonObservedStars();
}
