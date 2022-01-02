package net.ab0oo.aprs.parser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("Calendar/Date Tests")
class CalendarDateTest {
    @Nested
    @DisplayName("Given a calendar object in the UTC Timezone")
    class GivenAUtcCalendarObject {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        @Nested
        @DisplayName("When using getTime() to retrieve Date")
        class WhenGetTime {
            Date date = calendar.getTime();

            @Disabled("Calendar.getTime() returns local time always")
            @Test
            @DisplayName("Then each subfield should match")
            void thenEachSubfieldMatch() {
                assertEquals(calendar.get(Calendar.YEAR), date.getYear() + 1900, "Year");
                assertEquals(calendar.get(Calendar.MONTH) + 1, date.getMonth() + 1, "Month");
                assertEquals(calendar.get(Calendar.DAY_OF_MONTH), date.getDate(), "Day");
                assertEquals(calendar.get(Calendar.HOUR_OF_DAY), date.getHours(), "Hour");
                assertEquals(calendar.get(Calendar.MINUTE), date.getMinutes(), "Minute");
                assertEquals(calendar.get(Calendar.SECOND), date.getSeconds(), "Second");
            }
        }

        @Nested
        @DisplayName("When using getTimeMillis() to generate Date")
        class WhenGetTimeInMillis {
            Date date = new Date(calendar.getTimeInMillis());

            @Disabled("Calendar.getTimeInMillis() returns local time always")
            @Test
            @DisplayName("Then each subfield should match")
            void thenEachSubfieldMatch() {
                assertEquals(calendar.get(Calendar.YEAR), date.getYear() + 1900, "Year");
                assertEquals(calendar.get(Calendar.MONTH) + 1, date.getMonth() + 1, "Month");
                assertEquals(calendar.get(Calendar.DAY_OF_MONTH), date.getDate(), "Day");
                assertEquals(calendar.get(Calendar.HOUR_OF_DAY), date.getHours(), "Hour");
                assertEquals(calendar.get(Calendar.MINUTE), date.getMinutes(), "Minute");
                assertEquals(calendar.get(Calendar.SECOND), date.getSeconds(), "Second");
            }
        }

        @Nested
        @DisplayName("When generating Date from individual fields")
        class WhenGenerateDateByField {
            Date date = new Date(
                    calendar.get(Calendar.YEAR) - 1900,
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND));

            @Test
            @DisplayName("Then each subfield should match")
            void thenEachSubfieldMatch() {
                assertEquals(calendar.get(Calendar.YEAR), date.getYear() + 1900, "Year");
                assertEquals(calendar.get(Calendar.MONTH) + 1, date.getMonth() + 1, "Month");
                assertEquals(calendar.get(Calendar.DAY_OF_MONTH), date.getDate(), "Day");
                assertEquals(calendar.get(Calendar.HOUR_OF_DAY), date.getHours(), "Hour");
                assertEquals(calendar.get(Calendar.MINUTE), date.getMinutes(), "Minute");
                assertEquals(calendar.get(Calendar.SECOND), date.getSeconds(), "Second");
            }
        }
    }
}
