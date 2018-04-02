package cn.lightfish.offheap;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.of(2017, 9, 10, 0, 0);
        for (int i = 0; i < 365; i++) {
            dateTime = dateTime.plusDays(1);
            if (!(dateTime.getDayOfWeek() == DayOfWeek.SATURDAY || dateTime.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                System.out.println(dateTime);
            }
        }
    }
}
