package hello.board.domain;

import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    private static final PrettyTime PRETTY_TIME = new PrettyTime(Locale.KOREAN);

    public static String getTime(LocalDateTime dateTime) {
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        return PRETTY_TIME.format(date);
    }
}
