package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.grabber.utils.Post;
import ru.job4j.html.SqlRuParse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab {
    private final Properties cfg = new Properties();

    public Store store() throws SQLException {
        return new PsqlStore(cfg);
    }

    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    public void cfg() throws IOException {
        try (InputStream in = new FileInputStream(new File("./src/main/resources/app.properties"))) {
            cfg.load(in);
        }
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("time")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public void web(Store store) {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(Integer.parseInt(cfg.getProperty("port")))) {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try (OutputStream out = socket.getOutputStream()) {
                        out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                        for (Post post : store.getAll()) {
                            out.write(post.toString().getBytes("CP1251"));
                            out.write((System.lineSeparator()).getBytes());
                            out.write("================================================================".getBytes());
                            out.write(System.lineSeparator().getBytes());
                            out.write(System.lineSeparator().getBytes());
                        }
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static class GrabJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            String baseUrl = "https://www.sql.ru/forum/job-offers";
            List<String> urls = new ArrayList<>();
            urls.add(baseUrl);
            int numPages = 5;
            for (int i = 2; i <= numPages; i++) {
                urls.add(String.format("%s/%d", baseUrl, i));
        }
/*          int start = 2;
            boolean count = true;
            int responseCode = 0;
            String link = "";
            while (count) {
                try {
                    link = String.format("%s/%d", baseUrl, start);
                    URL url = new URL(link);
                    HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                    responseCode = huc.getResponseCode();
                    System.out.println(link);
                    System.out.println(responseCode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (responseCode != 200) {
                    System.out.println("BAD");
                    count = false;
                }
                urls.add(link);
                start++;
           } */

            List<Post> posts = new ArrayList<>();
            for (String url : urls) {
                posts.addAll(parse.list(url));
            }
            for (Post post : posts) {
                store.save(post);
            }
        }
    }


    public static void main(String[] args) throws Exception {
        Grabber grab = new Grabber();
        grab.cfg();
        Scheduler scheduler = grab.scheduler();
        Store store = grab.store();
        grab.init(new SqlRuParse(), store, scheduler);
        grab.web(store);
    }
}