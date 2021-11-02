package com.baloise.open.edw.domain.services;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  private final String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ss";
  private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(isoDatePattern);
  private final SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd");

  @Test
  public void localDateTimeToDate() {
    LocalDateTime localDateTime = LocalDateTime.from(dateTimeFormatter.parse("2011-12-03T10:15:30"));

    Date d1 = DateUtil.convertToDate(localDateTime);

    assertEquals(simpleDateFormat.format(d1), dateTimeFormatter.format(localDateTime));
  }

  @Test
  public void localDateToDate() {
    LocalDate localDate = LocalDate.from(dateTimeFormatter.parse("2011-12-03T10:15:30"));

    Date d1 = DateUtil.convertToDate(localDate);

    assertEquals(d1.toString(), localDate.toString());
  }

  @Test
  public void dateToLocalDate() {
    Calendar cal = Calendar.getInstance();
    cal.set(2011, Calendar.DECEMBER, 24);
    Date date = cal.getTime();

    LocalDate localDate = DateUtil.convertToLocalDate(date);

    assertEquals(localDateFormat.format(date), localDate.toString());
  }

  @Test
  public void dateToLocalDateWithFallback() {
    LocalDate localDateFallback = LocalDate.from(dateTimeFormatter.parse("2011-12-03T10:15:30"));
    LocalDate localDate = DateUtil.convertToLocalDate(null, localDateFallback);

    assertEquals(localDateFallback, localDate);
  }

  @Test
  public void createDate() {
    assertEquals(localDateFormat.format(new Date()), localDateFormat.format(DateUtil.createDate()));
  }

  @Test
  public void createDateWithParameters() {
    Date createdDate = DateUtil.createDate(1995, Month.DECEMBER.getValue(), 27);

    assertEquals("1995-12-27", localDateFormat.format(createdDate));
  }

  @Test
  public void convertToTimestamp() {
    assertNull(DateUtil.convertToTimestamp(null));
    assertEquals("2012-01-26T08:12:34", simpleDateFormat.format(DateUtil.convertToTimestamp(LocalDateTime.of(2012, Month.JANUARY, 26, 8, 12, 34, 8))));
    assertEquals("2012-01-26T16:12:34", simpleDateFormat.format(DateUtil.convertToTimestamp(LocalDateTime.of(2012, Month.JANUARY, 26, 16, 12, 34, 8))));
  }

  @Test
  public void getDaysBetween_LocalDate() {
    LocalDate localDate1 = LocalDate.of(2020, Month.MARCH, 31);
    LocalDate localDate2 = LocalDate.of(2020, Month.APRIL, 3);
    assertEquals(3, DateUtil.getDaysBetween(localDate1, localDate2));
    assertEquals(3, DateUtil.getDaysBetween(localDate2, localDate1));
  }

  @Test
  public void getDaysBetween_LocalDateTime() {
    LocalDateTime localDateTime1 = LocalDateTime.of(2020, Month.MARCH, 5, 15, 34, 16);
    LocalDateTime localDateTime2 = LocalDateTime.of(2020, Month.FEBRUARY, 29, 19, 15, 22);
    LocalDateTime localDateTime3 = LocalDateTime.of(2020, Month.FEBRUARY, 29, 7, 15, 22);

    assertEquals(4, DateUtil.getDaysBetween(localDateTime1, localDateTime2));
    assertEquals(4, DateUtil.getDaysBetween(localDateTime2, localDateTime1));
    assertEquals(5, DateUtil.getDaysBetween(localDateTime3, localDateTime1));
  }

  @Test
  public void removeTimeInfo() {
    assertNull(DateUtil.removeTimeInfo(null));
    String expectation = localDateFormat.format(new Date()) + "T00:00:00";
    Date dateWithoutTime = DateUtil.removeTimeInfo(new Date());

    assertEquals(expectation, simpleDateFormat.format(dateWithoutTime));
  }

  @Test
  public void adjustTimeInfo() {
    assertNull(DateUtil.adjustTimeInfo(null, 0, 0, 0, 0));
    Date dateAdjusted = DateUtil.adjustTimeInfo(new Date(), 7, 8, 9, 10);
    String expectation = localDateFormat.format(new Date()) + "T07:08:09";
    Date dateAdjusted2 = DateUtil.adjustTimeInfo(new Date(), 14, 13, 12, 11);
    String expectation2 = localDateFormat.format(new Date()) + "T14:13:12";
    assertEquals(expectation, simpleDateFormat.format(dateAdjusted));
    assertEquals(expectation2, simpleDateFormat.format(dateAdjusted2));
  }

  @Test
  public void parseStringDates() {
    assertAll(
        () -> assertEquals(LocalDate.of(2021,Month.MAY, 1), DateUtil.parseAsDate("2021-05-01")),
        () -> assertEquals(LocalDate.of(1978,Month.DECEMBER, 19), DateUtil.parseAsDate("1978-12-19 00:00:00")),
        () -> assertEquals(LocalDate.of(1978,Month.APRIL, 16), DateUtil.parseAsDate("1978-04-16T23:00:00Z")),
        () -> assertEquals(LocalDate.of(2012,Month.DECEMBER, 19), DateUtil.parseAsDate("2012-12-19T00:00.00Z")),
        () -> assertEquals(LocalDate.of(1957,Month.JULY, 12), DateUtil.parseAsDate("1957-07-12T00:00:00"))
    );
  }
}
