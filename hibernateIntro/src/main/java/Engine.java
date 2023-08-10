import entities.Address;
import entities.Employee;
import entities.Project;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class Engine implements Runnable {
    private final EntityManager entityManager;
    private Scanner scanner;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {

        System.out.println("Select exercise number: ");

        int exerciseNumber = Integer.parseInt(scanner.nextLine());

        switch (exerciseNumber) {
            case 2 -> changeCasing();
            case 3 -> containsEmployee();
            case 4 -> employeesWithSalaryOver50K();
            case 5 -> employeesFromDepartment();
            case 6 -> addingNewAddressToEmployee();
            case 7 -> addressesWithEmployeeCount();
            case 8 -> getEmployeeWithProject();
            case 9 -> findLatest10Projects();
            case 10 -> IncreaseSalary();
            case 11 -> findEmployeesByFirstName();
            case 12 -> employeesMaximumSalaries();
            case 13 -> removeTowns();
        }
    }

    private void removeTowns() {

        System.out.println("Enter town name: ");

        String town = scanner.nextLine();


        int affectedRows = removeAddresses(town);

        Town townToRemove = entityManager.createQuery("SELECT t FROM Town t " +
                        "WHERE t.name = :town_name",Town.class)
                .setParameter("town_name", town).getSingleResult();

        entityManager.getTransaction().begin();

        entityManager.remove(townToRemove);

        entityManager.getTransaction().commit();


        if (affectedRows == 1){

            System.out.printf("%d address in %s deleted",affectedRows,town);
        }else {

            System.out.printf("%d addresses in %s deleted", affectedRows, town);
        }


    }

    private int removeAddresses(String town) {
        List<Address> addresses = entityManager.createQuery("SELECT a FROM Address a " +
                "WHERE a.town.name = :town_name",Address.class)
                .setParameter("town_name", town).getResultList();

        entityManager.getTransaction().begin();

        addresses.forEach(entityManager::remove);

        entityManager.getTransaction().commit();

        return addresses.size();
    }

    private void employeesMaximumSalaries() {

        List<Object[]> resultList = entityManager.createQuery("SELECT e.department.name, MAX(e.salary)  FROM Employee e " +
                "GROUP BY e.department.name " +
                "HAVING MAX(e.salary) NOT BETWEEN 30000 AND 70000")
                .getResultList();

        resultList.forEach(r -> System.out.printf("%s %.2f%n",r[0], r[1]));

    }

    private void findEmployeesByFirstName() {

        System.out.println("Expects input: ");

        String input = scanner.nextLine();

        entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.firstName LIKE :pattern",Employee.class)
                .setParameter("pattern", input.concat("%"))
                .getResultStream()
                .forEach(e -> System.out.printf("%s %s - %s - ($%.2f)%n",
                        e.getFirstName(), e.getLastName(), e.getJobTitle(), e.getSalary()));


    }

    private void IncreaseSalary() {

        entityManager.getTransaction().begin();

        int affectedRows = entityManager.createQuery("UPDATE Employee e " +
                "SET e.salary = e.salary * 1.12 " +
                "WHERE e.department.id IN :ids")
                .setParameter("ids", Set.of(1,2,4,11)).executeUpdate();


        entityManager.getTransaction().commit();

        entityManager.createQuery("SELECT e FROM Employee e " +
                        "WHERE e.department.id IN :ids", Employee.class)
                .setParameter("ids", Set.of(1,2,4,11))
                .getResultStream()
                .forEach(e -> System.out.printf("%s %s ($%.2f)%n",
                        e.getFirstName(), e.getLastName(), e.getSalary()));


    }

    private void findLatest10Projects() {

        entityManager.createQuery("SELECT p FROM Project p " +
                "ORDER BY p.startDate DESC ", Project.class)
                .setMaxResults(10)
                .getResultStream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> System.out.printf("Project name: %s%n\tProject Description: %s%n\t" +
                        "Project Start Date: %s%n\t" +
                        "Project End Date: %s%n",
                        p.getName(),p.getDescription(), p.getStartDate(),p.getEndDate()));


    }

    private void getEmployeeWithProject() {

        System.out.println("Enter employee id: ");

        int employeeId = Integer.parseInt(scanner.nextLine());

        Employee employee = entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.id = :e_id", Employee.class)
                .setParameter("e_id", employeeId)
                .getSingleResult();

        System.out.printf("%s %s - %s%n",employee.getFirstName(),
                employee.getLastName(), employee.getJobTitle());

        employee.getProjects().stream()
                .map(Project::getName)
                .sorted(String::compareTo)
                .forEach(p -> System.out.printf("   %s%n",p));
    }

    private void addressesWithEmployeeCount() {

        entityManager.createQuery("SELECT a FROM Address a " +
                "ORDER BY a.employees.size DESC ", Address.class)
                .setMaxResults(10)
                .getResultStream()
                .forEach(a -> System.out.printf("%s, %s - %d employees%n",
                        a.getText(), a.getTown() == null ? "Unknown" : a.getTown().getName(),
                        a.getEmployees().size()));
    }

    private void addingNewAddressToEmployee() {

        System.out.println("Enter last name: ");

        String lastName = scanner.nextLine();

        Employee employee = entityManager.createQuery("SELECT e  FROM Employee e " +
                "WHERE e.lastName = :l_name", Employee.class)
                .setParameter("l_name", lastName)
                .getSingleResult();

        Address address = createAddress();

        entityManager.getTransaction().begin();

        employee.setAddress(address);

        entityManager.getTransaction().commit();
    }

    private Address createAddress() {

        Address address = new Address();
        address.setText("Vitoshka 15");


        entityManager.getTransaction().begin();

        entityManager.persist(address);

        entityManager.getTransaction().commit();

        return address;
    }

    private void employeesFromDepartment() {

        entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.department.name = :dprt_name " +
                "ORDER BY e.salary, e.id",Employee.class)
                .setParameter("dprt_name", "Research And Development")
                .getResultStream()
                .forEach(e ->
                        System.out.printf("%s %s from %s - $%.2f%n",
                                e.getFirstName(), e.getLastName(),e.getDepartment().getName(), e.getSalary()));


    }

    private void employeesWithSalaryOver50K() {

        entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.salary > 50000",Employee.class)
                .getResultStream()
                .forEach(e -> System.out.println(e.getFirstName()));


    }

    private void containsEmployee() {

        System.out.println("Enter employee full name: ");

        entityManager.getTransaction().begin();

        String [] fullName = scanner.nextLine().split("\\s+");

        String firstName = fullName[0];
        String lastName = fullName[1];

        Long result = entityManager.createQuery("SELECT count(e) FROM Employee e " +
                "WHERE e.firstName = :f_name AND e.lastName = :l_name", Long.class)
                        .setParameter("f_name", firstName)
                                .setParameter("l_name", lastName)
                                        .getSingleResult();

        System.out.println(result == 0 ? "No" : "Yes");

        entityManager.getTransaction().commit();
    }

    private void changeCasing() {

        entityManager.getTransaction().begin();

        Query query = entityManager.createQuery("UPDATE Town t " +
                "SET t.name = UPPER(t.name)" +
                " WHERE length(t.name) <= 5 ");

        System.out.println(query.executeUpdate());

        entityManager.getTransaction().commit();
    }
}
