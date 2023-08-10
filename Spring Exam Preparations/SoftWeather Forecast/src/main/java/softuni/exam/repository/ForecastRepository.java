package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Forecast;
import softuni.exam.models.entity.enums.DayOfWeek;

import java.util.Optional;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    @Query("SELECT f FROM Forecast f WHERE f.city = :city AND f.dayOfWeek = :dayOfWeek")
    Optional<Forecast> findByCityAndDayOfWeek(Long city, String dayOfWeek);

    Optional<Forecast> findForecastByDayOfWeek(DayOfWeek dayOfWeek);
}
