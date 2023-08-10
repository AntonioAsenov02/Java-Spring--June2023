import entities.Car;
import entities.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {

    public static void main(String[] args) {


        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("relations");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        Vehicle car = new Car("Ford Focus", "Petrol", 5);

        entityManager.persist(car);

        Car fromDb = entityManager.find(Car.class, 1L);

        System.out.println(fromDb.getModel() + " " + fromDb.getSeats());

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
