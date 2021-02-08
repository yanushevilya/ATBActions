package com.example.atbactions;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;
//import static com.example.atbactions.DbHandler.getInstance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    DbHandler dbHandler;

//    TextView tv1 = findViewById(R.id.tv1);
//
    // локация
    String location = "17";
    // исходная ссылка HTML
    String html = "https://zakaz.atbmarket.com/";
//    // сессия id в cookies
//    String sessionId;
    // шаблоны на выборку из HTML
    String catalogLinks = "[href*=catalog/" + location + "]"; //ссылки на все подкатегории товаров
    String paginationLinks = "ul[class=pagination] a[class=page-link]"; // подстраницы
//
//    String productImg = "div[class=img-wrapper] img"; // фото товара
////    <img src="https://src.zakaz.atbmarket.com/cache/photos/catalog_list_3614.jpg" class="img-fluid blur-up lazyload
////    bg-img" alt="Корм 85г Gourmet Gold консерви для дорослих котів ніжні биточки з індічкою та шпинатом,">
//
//    String productPrice = "span[class=price]"; // цена товара
////    <span class="price">
////            1540
//
//    String productName = "div[class=product-detail text-center]"; // название товара
////    <div class="product-detail text-center">
////    Корм 85г Gourmet Gold консерви для дорослих котів ніжні биточки з індічкою та шпинатом, 1 шт
//
//    String productCode = "div[class=product-detail text-center] a[href]"; // ссылка с кодом товара
////    <a href="/product/64/136257">
////    Корм 85г Gourmet Gold консерви для дорослих котів ніжні биточки з індічкою та шпинатом,
//
//    BufferedWriter bw;
//
//    // список всех ссылок на все товары в локации
//    Set<String> linksOnProduct = new TreeSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DbHandler(this);

    }

    // () получение cookies
    public Document getCookies(String html) {
        Connection.Response response;
        Document document = Jsoup.parse("");
        {
            try {
                response = Jsoup.connect(html).execute();
                document = response.parse();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return document;
    }

    // запись извлеченных данных в файл
//    public void saveDataOnStorage (String se){
//        try {
//            bw = new BufferedWriter(new FileWriter("atb_links.txt", true));
//            bw.write(se);
//            bw.flush();
//            bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onClick(View v) {
        // SQLiteDatabase - предназначен для управления БД SQLite и имеет в себе методы
        // query(), insert(), delete(), update(), а также execSQL, который позволяет выполнить любой код на языке SQL
        SQLiteDatabase sqLiteDatabase = dbHandler.getWritableDatabase(); // getWritableDatabase() возвращает instance БД

        // ContentValues используется для добавления новых строк в таблицу
        // каждый объект этого класса выглядит как массив с именами столбцов и значениями, которые им соответствуют
        // | _id | code | name | image | coast |
        // |  1  | 2344 | суши | .img3 | 14400 |
        ContentValues contentValues = new ContentValues();

        switch (v.getId()) {
            case R.id.btn_addProduct:

//                // получаем ссылки на страницы всех подкатегорий товаров
//                Document document = getCookies(html+location);
//                Elements links = document.getElementsByTag("a");
//                links = links.select(catalogLinks);
//]
//                // получаем ссылки на подстраницы всех подкатегорий товаров (страницы пагинации)
//                for (Element el: links) {
//                    Document documentPagination = getCookies(el.absUrl("href")); // ложим каждую ссылку в цикл
//                    Elements linksPagination = documentPagination.select(paginationLinks);
//                    for (Element l: linksPagination) {
//                        System.out.println(l.absUrl("href"));
//                        // Добавляем запись
//                        contentValues.put(DbHandler.KEY_CODE, code);
//                        contentValues.put(DbHandler.KEY_NAME, name);
//                        contentValues.put(DbHandler.KEY_IMAGE, image);
//                        contentValues.put(DbHandler.KEY_COAST, coast);
//                    }
//                }
            case R.id.btn_addAllLinks:
                // получаем ссылки на страницы всех подкатегорий товаров
                Document document = getCookies(html+location);
                Elements links = document.getElementsByTag("a");
                links = links.select(catalogLinks);
//                for (Element l: links) {
//                    System.out.println(l.absUrl("href"));
//                }

                // получаем ссылки на подстраницы всех подкатегорий товаров (страницы пагинации)
                for (Element el: links) {
                    Document documentPagination = getCookies(el.absUrl("href")); // ложим каждую ссылку в цикл
                    Elements linksPagination = documentPagination.select(paginationLinks);
                    for (Element l: linksPagination) {
                        System.out.println(l.absUrl("href"));
//                     saveDataOnStorage(l.absUrl("href") + "\n"); // запись ссылок в файл
                        // Добавляем запись
                        contentValues.put(DbHandler.KEY_LINK, l.absUrl("href"));
                        sqLiteDatabase.insert(DbHandler.TABLE_LINKS, null, contentValues);
                        break;
                    }
                }


        }
        dbHandler.close();
    }

//
//    // () получение списка всех ссылок на все товары в локации
//
//    // () перебор списка ссылок на все товары локациии с извлечением из них данных
}