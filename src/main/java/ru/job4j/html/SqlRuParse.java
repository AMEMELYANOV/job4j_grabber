package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.utils.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(link).get();
            Elements elements = doc.getElementsByClass("forumTable").get(0).getElementsByTag("tr");
            for (org.jsoup.nodes.Element element : elements) {
                if (element.child(1).hasClass("postslisttopic")) {
                    posts.add(detail(element.child(1).child(0).attr("href")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post detail(String link) {
        Post post = new Post();
        post.setUrl(link);
        try {
            Document doc = Jsoup.connect(link).get();
            Element elementName = doc.getElementsByClass("messageHeader").get(0);
            String[] name = elementName.text().split(" \\[");
            if (name.length > 0) {
                post.setName(name[0]);
            }

            Element elementText = doc.getElementsByClass("msgBody").get(1);
            post.setText(elementText.text());

            Element elementDate = doc.getElementsByClass("msgFooter").get(0);
            SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
            String[] arrDate = elementDate.text().split(" ");
            if (arrDate.length > 3) {
                post.setDate(parser.parse(String.join(" ", arrDate[0], arrDate[1], arrDate[2], arrDate[3])));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return post;
    }

    public static void main(String[] args) throws Exception {
        String baseUrl = "https://www.sql.ru/forum/job-offers";
//        int numPages = 5;
//        parseUrl(baseUrl);
//        for (int i = 2; i <= numPages; i++) {
//            parseUrl(String.format("%s/%d", baseUrl, i));
//        }

        SqlRuParse ruParse = new SqlRuParse();
        List<Post> posts = ruParse.list(baseUrl);
        for (Post p : posts) {
            System.out.println(p.getUrl()
                    + System.lineSeparator() + p.getName()
                    + System.lineSeparator() + p.getText()
                    + System.lineSeparator() + p.getDate());
            System.out.println("--------------------------------------------");
        }
    }
}