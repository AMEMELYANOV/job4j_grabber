package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;

public class Post {
    private int id;
    private String name;
    private String url;
    private String text;
    private LocalDateTime date;

    public Post() {

    }

    public Post(int id, String name, String url, String text, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.text = text;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void extract(String url) throws IOException, ParseException {
        this.url = url;

        Document doc = Jsoup.connect(url).get();
        Element elementName = doc.getElementsByClass("messageHeader").get(0);
        this.name = elementName.text().split("\\[")[0];

        Element elementText = doc.getElementsByClass("msgBody").get(1);
        this.text = elementText.text();

        Element elementDate = doc.getElementsByClass("msgFooter").get(0);
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        String[] arrDate = elementDate.text().split(" ", 5);
        this.date = parser.parse(String.join(" ", arrDate[0], arrDate[1], arrDate[2], arrDate[3]));
    }

    public static void main(String[] args) throws IOException, ParseException {
        Post post = new Post();
        post.extract("https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t");
        System.out.println(post.getUrl()
                + System.lineSeparator() + post.getName()
                + System.lineSeparator() + post.getText()
                + System.lineSeparator() + post.getDate());
    }
}