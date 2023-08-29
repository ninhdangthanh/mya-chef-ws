package vn.com.ids.myachef.business.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    public static int randomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max + 1 - min) + min;
    }

    public static LocalDate randomDate(LocalDate startInclusive, LocalDate endExclusive) {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    public static LocalDateTime randomDateTime(LocalDate startInclusive, LocalDate endExclusive, LocalTime startTime, LocalTime endTime) {
        // random date
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);
        LocalDate date = LocalDate.ofEpochDay(randomDay);

        // random time
        int secondOfDayTime1 = startTime.toSecondOfDay();
        int secondOfDayTime2 = endTime.toSecondOfDay();
        Random random = new Random();
        int randomSecondOfDay = secondOfDayTime1 + random.nextInt(secondOfDayTime2 - secondOfDayTime1);
        LocalTime randomLocalTime = LocalTime.ofSecondOfDay(randomSecondOfDay);

        return date.atTime(randomLocalTime);
    }
}
