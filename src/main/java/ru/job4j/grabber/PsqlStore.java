package ru.job4j.grabber;

import ru.job4j.grabber.utils.Post;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;

    public PsqlStore(Properties cfg) throws SQLException {
        try {
            Class.forName(cfg.getProperty("driver-class-name"));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement =
                     cnn.prepareStatement("insert into post(name, text, link, created) values (?, ?, ?, ?)"
                             + "on conflict (link) do nothing;"
                             )) {
            statement.setString(1, post.getName());
            statement.setString(2, post.getText());
            statement.setString(3, post.getUrl());
            statement.setTimestamp(4, Timestamp.valueOf(post.getDate()));
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement ps = cnn.prepareStatement("select * from post")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("link"),
                            resultSet.getString("text"),
                            resultSet.getTimestamp("created").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(String id) {
        Post post = null;
        try (PreparedStatement ps = cnn.prepareStatement("select * from post where id = ?")) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    post = (new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("link"),
                            resultSet.getString("text"),
                            resultSet.getTimestamp("created").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
        try (InputStream in = PsqlStore.class.getClassLoader()
                    .getResourceAsStream("app.properties")) {
            Properties cfg = new Properties();
            cfg.load(in);
            PsqlStore psqlStore = new PsqlStore(cfg);
            Post post1 = new Post();
            post1.setName("First");
            post1.setText("Hello first");
            post1.setUrl("http://1.ru");
            post1.setDate(LocalDateTime.now());
            Post post2 = new Post();
            post2.setName("Second");
            post2.setText("Hello second");
            post2.setUrl("http://2.ru");
            post2.setDate(LocalDateTime.now());

            psqlStore.save(post1);
            psqlStore.save(post2);

            List<Post> posts = psqlStore.getAll();
            for (Post post : posts) {
                System.out.println(post.getId());
                System.out.println(post.getName());
                System.out.println(post.getUrl());
                System.out.println(post.getText());
                System.out.println(post.getDate());
            }
            System.out.println(psqlStore.findById("1").getUrl());
            System.out.println(psqlStore.findById("2").getUrl());

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}