package com.example.atbactions;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.*;
import androidx.annotation.Nullable;

public class DbHandler extends SQLiteOpenHelper {
    // поля базы данных
    public final static int DATABASE_VERSION = 1;
    public final static String DATABASE_NAME = "ATBProductsDB";
//    public final static String TABLE_PRODUCTS = "ATBProductsTable";
    public final static String TABLE_LINKS = "ATBLinksTable";

    // поля таблицы ATBProductsTable
    public final static String KEY_ID = "_id";
//    public final static String KEY_CODE = "code";
//    public final static String KEY_NAME = "name";
//    public final static String KEY_IMAGE = "image";
//    public final static String KEY_COAST = "coast";

    // поля таблицы ATBLinksTable
    public final static String KEY_LINK = "link";

    // конструктор
    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // сработает при создании базы данных (вызывается только если БД не существует и ее надо создавать)
    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table " + TABLE_PRODUCTS + "(" + KEY_ID + " integer primary key, " + KEY_CODE +
//                " integer, " + KEY_NAME + " text, " + KEY_IMAGE + " text, " + KEY_COAST + " integer)");
        db.execSQL("create table " + TABLE_LINKS + "(" + KEY_ID + " integer primary key, " + KEY_LINK + " text" + ");");

    }

    // сработает при изменении номера версии DATABASE_VERSION
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // для обновления структуры таблицы БД можно сначала уничтожить существующую таблицу
//        db.execSQL("drop table if exists " + TABLE_PRODUCTS);
        // после чего вызвать метод onCreate для создания новой версии таблицы с обновленной структурой
        onCreate(db);

    }


//    // Константа, в которой хранится адрес подключения
//    private static final String CON_STR = "jdbc:sqlite:C:\\Users\\yanus\\IdeaProjects\\testboard\\atb_db.s3db";
//
//    // Используем шаблон одиночка, чтобы не плодить множество
//    // экземпляров класса DbHandler
//    private static DbHandler instance = null;
//
//
//
//    public static synchronized DbHandler getInstance() throws SQLException {
//        if (instance == null)
//            instance = new DbHandler();
//        return instance;
//    }
//
//    // Объект, в котором будет храниться соединение с БД
//    private Connection connection;
//
//    private DbHandler() throws SQLException {
//        // Регистрируем драйвер, с которым будем работать
//        // в нашем случае Sqlite
//        DriverManager.registerDriver(new JDBC());
//        // Выполняем подключение к базе данных
//        this.connection = DriverManager.getConnection(CON_STR);
//    }
//
////    public List<Product> getAllProducts() {
////
////        // Statement используется для того, чтобы выполнить sql-запрос
////        try (Statement statement = this.connection.createStatement()) {
////            // В данный список будем загружать наши продукты, полученные из БД
////            List<Product> products = new ArrayList<Product>();
////            // В resultSet будет храниться результат нашего запроса,
////            // который выполняется командой statement.executeQuery()
////            ResultSet resultSet = statement.executeQuery("SELECT id, good, price, category_name FROM products");
////            // Проходимся по нашему resultSet и заносим данные в products
////            while (resultSet.next()) {
////                products.add(new Product(resultSet.getInt("id"),
////                        resultSet.getString("good"),
////                        resultSet.getDouble("price"),
////                        resultSet.getString("category_name")));
////            }
////            // Возвращаем наш список
////            return products;
////
////        } catch (SQLException e) {
////            e.printStackTrace();
////            // Если произошла ошибка - возвращаем пустую коллекцию
////            return Collections.emptyList();
////        }
////    }
//
//    // Добавление продукта в БД
//    public void addProduct() {
//        // Создадим подготовленное выражение, чтобы избежать SQL-инъекций
//        try (PreparedStatement statement = this.connection.prepareStatement(
//
//                "INSERT INTO Product(`name`, `image`, `coast`) " +
//                        "VALUES(?, ?, ?)")) {
//            statement.setObject(1, "Суши");
//            statement.setObject(2, "Японская кухня");
//            statement.setObject(3, 11400);
//            // Выполняем запрос
//            statement.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Удаление продукта по id
//    public void deleteProduct(int id) {
//        try (PreparedStatement statement = this.connection.prepareStatement(
//                "DELETE FROM Products WHERE id = ?")) {
//            statement.setObject(1, id);
//            // Выполняем запрос
//            statement.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//

}
