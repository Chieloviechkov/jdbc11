package Jdbc1;

import java.sql.*;

public class Main {
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/mydb?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "107324DdD107324";

    public static void main(String[] args) {
        try {

            Connection connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);

            createTable(connection);
            insertApartment(connection, "Центр", "ул. Шевченка, д. 1", 75.5, 2, 150000.00);
            insertApartment(connection, "Парковый", "ул. Джавистов, д. 5", 95.2, 3, 220000.50);

            selectAllApartments(connection);

            selectApartmentsByParams(connection, "Центр", 2, 150000.00);

            //deleteAllApartments(connection);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS apartments (" +
                    "apartment_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "district VARCHAR(255)," +
                    "address VARCHAR(255)," +
                    "area DECIMAL(10, 2)," +
                    "rooms INT," +
                    "price DECIMAL(12, 2)" +
                    ")";
            statement.executeUpdate(createTableQuery);
        }
    }

    public static void insertApartment(Connection connection, String district, String address, double area, int rooms, double price) throws SQLException {
        String insertQuery = "INSERT INTO apartments (district, address, area, rooms, price) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, district);
            preparedStatement.setString(2, address);
            preparedStatement.setDouble(3, area);
            preparedStatement.setInt(4, rooms);
            preparedStatement.setDouble(5, price);
            preparedStatement.executeUpdate();
        }
    }

    public static void selectAllApartments(Connection connection) throws SQLException {
        String selectQuery = "SELECT * FROM apartments";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                int apartmentId = resultSet.getInt("apartment_id");
                String district = resultSet.getString("district");
                String address = resultSet.getString("address");
                double area = resultSet.getDouble("area");
                int rooms = resultSet.getInt("rooms");
                double price = resultSet.getDouble("price");
                System.out.println("Apartment ID: " + apartmentId +
                        ", District: " + district +
                        ", Address: " + address +
                        ", Area: " + area +
                        ", Rooms: " + rooms +
                        ", Price: " + price);
            }
            resultSet.close();
        }
    }

    public static void selectApartmentsByParams(Connection connection, String district, int rooms, double maxPrice) throws SQLException {
        String selectQuery = "SELECT * FROM apartments WHERE district = ? AND rooms = ? AND price <= ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, district);
            preparedStatement.setInt(2, rooms);
            preparedStatement.setDouble(3, maxPrice);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int apartmentId = resultSet.getInt("apartment_id");
                String address = resultSet.getString("address");
                double area = resultSet.getDouble("area");
                double apartmentPrice = resultSet.getDouble("price");
                System.out.println("Apartment ID: " + apartmentId +
                        ", Address: " + address +
                        ", Area: " + area +
                        ", Price: " + apartmentPrice);
            }
            resultSet.close();
        }
    }
    public static void deleteAllApartments(Connection connection) throws SQLException {
        String deleteQuery = "DELETE FROM apartments";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(deleteQuery);
        }
    }
}
