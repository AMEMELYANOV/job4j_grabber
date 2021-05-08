package ru.job4j.grabber.utils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SqlRuDateTimeParser implements DateTimeParser {
    private final Map<String, String> month = new HashMap<>();
    {
        month.put("янв", "января");
        month.put("фев", "февраля");
        month.put("мар", "марта");
        month.put("апр", "апреля");
        month.put("май", "мая");
        month.put("июн", "июня");
        month.put("июл", "июля");
        month.put("авг", "августа");
        month.put("сен", "сентября");
        month.put("окт", "октября");
        month.put("ноя", "ноября");
        month.put("дек", "декабря");
    }

    @Override
    public LocalDateTime parse(String parse) throws ParseException {
        String rsl = "";
        String[] dateTimeArr = parse.split(" ");
        Locale localeRU = new Locale("ru", "RU");
        DateTimeFormatter mainFormatter = DateTimeFormatter.ofPattern("d MMM yy, HH:mm").withLocale(localeRU);
        DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern("d MMM yy,").withLocale(localeRU);
        if (dateTimeArr.length > 1 && dateTimeArr[0].startsWith("сегодня")) {
            dateTimeArr[0] = LocalDateTime.now().format(shortFormatter);
            rsl = String.join(" ", dateTimeArr);
        } else if (dateTimeArr.length > 1 && dateTimeArr[0].startsWith("вчера")) {
            dateTimeArr[0] = LocalDateTime.now().minusDays(1).format(shortFormatter);
            rsl = String.join(" ", dateTimeArr);
        } else if (dateTimeArr.length > 1) {
            String oldMonth = dateTimeArr[1].substring(0, 3);
            dateTimeArr[1] = month.get(oldMonth);
            rsl = String.join(" ", dateTimeArr);
        }
        return LocalDateTime.parse(rsl, mainFormatter);
    }

    public static void main(String[] args) throws ParseException {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        String dateTime = "вчера, 17:34";
        String dateTime1 = "сегодня, 18:20";
        String dateTime2 = "12 май 20, 08:17";
        System.out.println(parser.parse(dateTime));
        System.out.println(parser.parse(dateTime1));
        System.out.println(parser.parse(dateTime2));
    }
}