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
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    DbHandler dbHandler;
    SQLiteDatabase sqLiteDatabase;

    Button btn_addAllLinks, btn_addProduct;

    Document document;
    Connection.Response response;
    ContentValues contentValues;

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

        btn_addProduct = (Button) findViewById(R.id.btn_addProduct);
        btn_addProduct.setOnClickListener(this);
        btn_addAllLinks = (Button) findViewById(R.id.btn_addAllLinks);
        btn_addAllLinks.setOnClickListener(this);


        dbHandler = new DbHandler(this);
        // SQLiteDatabase - предназначен для управления БД SQLite и имеет в себе методы
        // query(), insert(), delete(), update(), а также execSQL, который позволяет выполнить любой код на языке SQL
        sqLiteDatabase = dbHandler.getWritableDatabase();


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


        // ContentValues используется для добавления новых строк в таблицу
        // каждый объект этого класса выглядит как массив с именами столбцов и значениями, которые им соответствуют
        // | _id | code | name | image | coast |
        // |  1  | 2344 | суши | .img3 | 14400 |
        contentValues = new ContentValues();

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
                AtbConnect atbConnect = new AtbConnect();
                atbConnect.execute();
        }
//        dbHandler.close();
    }

    class AtbConnect extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // получаем ссылки на страницы всех подкатегорий товаров
            document = getCookies(html+location);
            Elements links = document.getElementsByTag("a");
            links = links.select(catalogLinks);
//            for (Element l: links) {
//                System.out.println(l.absUrl("href"));
//            }

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
                    contentValues.clear();
                }
            }
            dbHandler.close();
            return null;
        }

        // () получение cookies
        public Document getCookies(String html) {

            document = null;
            System.out.println(html);
            try {
                response = Jsoup.connect(html).execute();
                document = response.parse();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return document;
        }
    }



//
//    // () получение списка всех ссылок на все товары в локации
//
//    // () перебор списка ссылок на все товары локациии с извлечением из них данных
}