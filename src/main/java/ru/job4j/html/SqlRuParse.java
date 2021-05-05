package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
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
}