import java.sql.*;
import java.util.*;


public class Main {

    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "minions_db";
    private static final Scanner scanner = new Scanner(System.in);
    private static Connection connection;

    public static void main(String[] args) throws SQLException {

        connection = getConnection();

        System.out.println("Enter exercise number:");

        int exNum = Integer.parseInt(scanner.nextLine());

        switch (exNum) {
            case 2 : exTwo();
            break;
            case 3 : exThree();
            break;
            case 5 : exFive();
            break;
            case 6 : exSix();
            break;
            case 7 : exSeven();
            break;
            case 8 : exEight();
            break;
            case 9 : exNine();
        }
    }

    private static void exTwo() throws SQLException {

        PreparedStatement statement = connection.prepareStatement(
                "SELECT v.name, COUNT(DISTINCT mv.minion_id) AS 'count' " +
                        "FROM  villains AS v " +
                        "JOIN minions_villains mv on v.id = mv.villain_id " +
                        "GROUP BY v.name " +
                        "HAVING `count` > ?;");


        statement.setInt(1, 15);

        ResultSet result = statement.executeQuery();

        while (result.next()) {

            System.out.printf("%s %d %n", result.getString("name"), result.getInt("count"));
        }
    }
    private static void exThree() throws SQLException {

        System.out.println("Enter villain id:");

        int villainId = Integer.parseInt(scanner.nextLine());

        String villainName = findVillainNameById(villainId);

        System.out.println(villainName);
        PreparedStatement statement = connection.prepareStatement(
                "SELECT m.name, m.age " +
                        "FROM minions AS m " +
                        "JOIN minions_villains mv on m.id = mv.minion_id " +
                        "WHERE villain_id = ?;");

        statement.setInt(1, villainId);
        ResultSet resultSet = statement.executeQuery();

        int counter = 1;

        while (resultSet.next()){

            System.out.printf("%d %s %d %n",counter++, resultSet.getString("name"),
                    resultSet.getInt("age"));
        }
    }
    private static void exFive() throws SQLException {

        System.out.println("Enter country:");

        String country = scanner.nextLine();

        PreparedStatement statement = connection.prepareStatement(
                "UPDATE towns SET name = UPPER(name) WHERE country = ?");

        statement.setString(1, country);

        int affectedRows = statement.executeUpdate();

        if (affectedRows == 0){

            System.out.println("No town names were affected.");
            return;
        }

        System.out.printf("%d town names were affected.%n",affectedRows);

        PreparedStatement statement1 = connection.prepareStatement(
                "SELECT name FROM towns WHERE country = ?");

        statement1.setString(1, country);

        ResultSet result = statement1.executeQuery();

        Set<String> towns = new LinkedHashSet<>();

        while (result.next()){

            towns.add(result.getString("name"));
        }

        System.out.println(towns);
    }
    private static void exSix() throws SQLException {

        System.out.println("Villain id: ");

        int villainId = Integer.parseInt(scanner.nextLine());

        System.out.println(findVillainById2(villainId));

        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM minions_villains WHERE villain_id = ?");

        statement.setInt(1, villainId);

        int minionsReleased = statement.executeUpdate();

        if (minionsReleased > 0){

            System.out.printf("%d minions released",minionsReleased);
        }
    }
    private static void exSeven() throws SQLException {

        PreparedStatement statement = connection.prepareStatement(
                "SELECT name FROM minions");

        ResultSet result = statement.executeQuery();

        List<String> allMinionsNames = new ArrayList<>();

        while (result.next()) {

            allMinionsNames.add(result.getString("name"));
        }

        int start = 0;
        int end = allMinionsNames.size() - 1;

        for (int id = 0; id < allMinionsNames.size() - 1; id++) {
            System.out.println(id % 2 == 0 ? allMinionsNames.get(start++)
                    : allMinionsNames.get(end--));

        }
    }
    private static void exEight() throws SQLException {

        System.out.println("Minions IDs: ");

        int[] minionsIds = Arrays.stream(scanner.nextLine().split(" "))
                .mapToInt(e -> Integer.parseInt(e))
                .toArray();

        PreparedStatement statement = connection.prepareStatement(
                "UPDATE minions SET age = age + 1, name = LOWER(name) WHERE id = ?");

        for (int i = 0; i < minionsIds.length; i++) {
            statement.setInt(1, minionsIds[i]);
            statement.executeUpdate();
        }

        PreparedStatement statement1 = connection.prepareStatement(
                "SELECT name, age FROM minions");

        ResultSet result = statement1.executeQuery();
        while (result.next()){

            System.out.printf("%s %d%n", result.getString("name"),
                    result.getInt("age"));
        }
    }
    private static void exNine() throws SQLException {

        System.out.println("Minion id: ");

        int minion_id = Integer.parseInt(scanner.nextLine());

        CallableStatement statement = connection.prepareCall("CALL usp_get_older(?)");

        statement.setInt(1,minion_id);

        statement.executeUpdate();

        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT name, age FROM minions");

        ResultSet result = preparedStatement.executeQuery();

        while (result.next()){

            System.out.printf("%s %d%n", result.getString("name"),
                    result.getInt("age"));
        }
    }
    private static String findVillainById2(int villainId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT name FROM villains WHERE id = ?;");

        statement.setInt(1, villainId);

        ResultSet result = statement.executeQuery();

        if (result.isBeforeFirst()){

            result.next();
            return String.format("%s was deleted", result.getString("name"));
        }

        return "No such villain was found";
    }
    private static String findVillainNameById(int villainId) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(
                "SELECT name FROM villains WHERE id = ?;");

        statement.setInt(1,villainId);

        ResultSet result = statement.executeQuery();

        if (result.isBeforeFirst()){

            result.next();
            return String.format("Villain: %s",result.getString("name"));
        }

        return String.format("No villain with ID %d exists in the database.",villainId);
    }
    private static Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        // Please put your password here !!!!!!!!
        properties.setProperty("password", "");

        return DriverManager.getConnection(CONNECTION_STRING + DATABASE_NAME,properties);
    }
}
