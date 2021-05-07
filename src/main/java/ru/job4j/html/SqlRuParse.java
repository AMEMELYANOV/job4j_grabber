package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SqlRuParse {
    public static void parseUrl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.getElementsByClass("forumTable").get(0).getElementsByTag("tr");
        for (org.jsoup.nodes.Element element : elements) {
            if (element.child(1).hasClass("postslisttopic")) {
                System.out.println(element.child(1).child(0).text());
                System.out.println(element.child(1).child(0).attr("href"));
                System.out.println(element.child(5).text());
                System.out.println("----------------------------------");
            }
        }
    }
    public static void main(String[] args) throws Exception {
        String baseUrl = "https://www.sql.ru/forum/job-offers";
        int numPages = 5;
        parseUrl(baseUrl);
        for (int i = 2; i <= numPages; i++) {
            parseUrl(String.format("%s/%d", baseUrl, i));
        }
    }
}