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

import android.os.Bundle;
import android.widget.TextView;

import static com.example.atbactions.DbHandler.getInstance;

public class MainActivity extends AppCompatActivity {

    TextView tv1 = findViewById(R.id.tv1);

    // локация
    String location = "17";
    // исходная ссылка HTML
    String html = "https://zakaz.atbmarket.com/";
    // сессия id в cookies
    String sessionId;
    // шаблоны на выборку из HTML
    String catalogLinks = "[href*=catalog/" + location + "]"; //ссылки на все подкатегории товаров
    String paginationLinks = "ul[class=pagination] a[class=page-link]"; // подстраницы

    String productImg = "div[class=img-wrapper] img"; // фото товара
//    <img src="https://src.zakaz.atbmarket.com/cache/photos/catalog_list_3614.jpg" class="img-fluid blur-up lazyload
//    bg-img" alt="Корм 85г Gourmet Gold консерви для дорослих котів ніжні биточки з індічкою та шпинатом,">

    String productPrice = "span[class=price]"; // цена товара
//    <span class="price">
//            1540

    String productName = "div[class=product-detail text-center]"; // название товара
//    <div class="product-detail text-center">
//    Корм 85г Gourmet Gold консерви для дорослих котів ніжні биточки з індічкою та шпинатом, 1 шт

    String productCode = "div[class=product-detail text-center] a[href]"; // ссылка с кодом товара
//    <a href="/product/64/136257">
//    Корм 85г Gourmet Gold консерви для дорослих котів ніжні биточки з індічкою та шпинатом,

    BufferedWriter bw;

    // список всех ссылок на все товары в локации
    Set<String> linksOnProduct = new TreeSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity appMain = new MainActivity();
//        DbWork.Conn();
//        DbWork.CreateDB();
//        DbWork.WriteDB();
        // Создаем экземпляр по работе с БД
        DbHandler dbHandler = null;
        try {
            dbHandler = getInstance();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        // получаем ссылки на страницы всех подкатегорий товаров
        Document document = appMain.getCookies(appMain.html+appMain.location);
        Elements links = document.getElementsByTag("a");
        links = links.select(appMain.catalogLinks);
//        for (Element l: links) {
//            System.out.println(l.absUrl("href"));
//        }

        // получаем ссылки на подстраницы всех подкатегорий товаров (страницы пагинации)
        for (Element el: links) {
            Document documentPagination = appMain.getCookies(el.absUrl("href")); // ложим каждую ссылку в циклн
            Elements linksPagination = documentPagination.select(appMain.paginationLinks);
            for (Element l: linksPagination) {
                System.out.println(l.absUrl("href"));
                appMain.saveDataOnStorage(l.absUrl("href") + "\n");
                // Добавляем запись
                dbHandler.addProduct();
//                System.out.println(l.text());
            }
        }

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
    public void saveDataOnStorage (String se){
        try {
            bw = new BufferedWriter(new FileWriter("atb_links.txt", true));
            bw.write(se);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // () получение списка всех ссылок на все товары в локации

    // () перебор списка ссылок на все товары локациии с извлечением из них данных
}