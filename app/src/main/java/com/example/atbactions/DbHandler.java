package com.example.atbactions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import android.database.sqlite.*;

public class DbHandler {
    // Константа, в которой хранится адрес подключения
    private static final String CON_STR = "jdbc:sqlite:C:\\Users\\yanus\\IdeaProjects\\testboard\\atb_db.s3db";

    // Используем шаблон одиночка, чтобы не плодить множество
    // экземпляров класса DbHandler
    private static DbHandler instance = null;

    public static synchronized DbHandler getInstance() throws SQLException {
        if (instance == null)
            instance = new DbHandler();
        return instance;
    }

    // Объект, в котором будет храниться соединение с БД
    private Connection connection;

    private DbHandler() throws SQLException {
        // Регистрируем драйвер, с которым будем работать
        // в нашем случае Sqlite
        DriverManager.registerDriver(new JDBC());
        // Выполняем подключение к базе данных
        this.connection = DriverManager.getConnection(CON_STR);
    }

//    public List<Product> getAllProducts() {
//
//        // Statement используется для того, чтобы выполнить sql-запрос
//        try (Statement statement = this.connection.createStatement()) {
//            // В данный список будем загружать наши продукты, полученные из БД
//            List<Product> products = new ArrayList<Product>();
//            // В resultSet будет храниться результат нашего запроса,
//            // который выполняется командой statement.executeQuery()
//            ResultSet resultSet = statement.executeQuery("SELECT id, good, price, category_name FROM products");
//            // Проходимся по нашему resultSet и заносим данные в products
//            while (resultSet.next()) {
//                products.add(new Product(resultSet.getInt("id"),
//                        resultSet.getString("good"),
//                        resultSet.getDouble("price"),
//                        resultSet.getString("category_name")));
//            }
//            // Возвращаем наш список
//            return products;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // Если произошла ошибка - возвращаем пустую коллекцию
//            return Collections.emptyList();
//        }
//    }

    // Добавление продукта в БД
    public void addProduct() {
        // Создадим подготовленное выражение, чтобы избежать SQL-инъекций
        try (PreparedStatement statement = this.connection.prepareStatement(

                "INSERT INTO Product(`name`, `image`, `coast`) " +
                        "VALUES(?, ?, ?)")) {
            statement.setObject(1, "Суши");
            statement.setObject(2, "Японская кухня");
            statement.setObject(3, 11400);
            // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Удаление продукта по id
    public void deleteProduct(int id) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "DELETE FROM Products WHERE id = ?")) {
            statement.setObject(1, id);
            // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
