import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the task number: ");

        int taskNumber = Integer.parseInt(scanner.nextLine());

        EntityManager entityManager;
        String persistenceUnitName = "";
        switch (taskNumber) {
            case 1 -> persistenceUnitName = "test";
            case 2 -> persistenceUnitName = "sales";
            case 3 -> persistenceUnitName = "university";
            case 4 -> persistenceUnitName = "hospital";
            case 5 -> persistenceUnitName = "bank";
        }
        entityManager = getEntityManager(persistenceUnitName);

        entityManager.getTransaction().begin();



        entityManager.getTransaction().commit();
    }

    private static EntityManager getEntityManager(String persistenceName) {

        return Persistence.createEntityManagerFactory(persistenceName).createEntityManager();
    }
}
