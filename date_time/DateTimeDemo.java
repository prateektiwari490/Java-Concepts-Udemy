package date_time;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTimeDemo {

	private static void testLegacyDateAPI() {

		System.out.println("\nDate class ... ");
		Date currentDate = new Date();
		System.out.println("currentDate: " + currentDate);

		System.out.println("currentDate in ms: " + currentDate.getTime());

		// Use-case: Software renewal
		System.out.println("\nCalendar class ... ");
		Calendar expiryDate = new GregorianCalendar(2017, 05, 30);
		System.out.println("expiryDate: " + expiryDate);
		System.out.println("expiryDate: " + expiryDate.getTime());
		expiryDate.add(Calendar.MONTH, 8);
		System.out.println("new expiryDate: " + expiryDate.getTime());

		expiryDate.roll(Calendar.MONTH, 11);
		System.out.println("new expiryDate (roll): " + expiryDate.getTime());

		// Time Zone Demo
		// Use-case: Game start time localized to time zone
		// System.out.println("\nTimeZones ... ");
		String[] timeZones = TimeZone.getAvailableIDs();
		/*
		 * for (String timeZone : timeZones) { System.out.println(timeZone); }
		 */
		// no-arg constructor below ==> default timezone
		// Calendar.getInstance()
		Calendar gameStartTime = new GregorianCalendar(TimeZone.getTimeZone("Europe/London"));
		gameStartTime.set(2017, Calendar.JULY, 03, 9, 00);
		// System.out.println("gameStartTime: " + gameStartTime);
		System.out.println("gameStartTime.getTime: " + gameStartTime.getTime());
		System.out.println("London time -- MONTH/DAY at hr:min:sec (AM/PM) -- "
				+ (gameStartTime.get(Calendar.MONTH) + 1) + "/" + gameStartTime.get(Calendar.DAY_OF_MONTH) + " at "
				+ gameStartTime.get(Calendar.HOUR) + ":" + gameStartTime.get(Calendar.MINUTE) + " ("
				+ ((gameStartTime.get(Calendar.AM_PM) == 0) ? "AM" : "PM") + ")");

		gameStartTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		System.out.println("Indian time -- MONTH/DAY at hr:min:sec (AM/PM) -- "
				+ (gameStartTime.get(Calendar.MONTH) + 1) + "/" + gameStartTime.get(Calendar.DAY_OF_MONTH) + " at "
				+ gameStartTime.get(Calendar.HOUR) + ":" + gameStartTime.get(Calendar.MINUTE) + " ("
				+ ((gameStartTime.get(Calendar.AM_PM) == 0) ? "AM" : "PM") + ")");

		gameStartTime.setTimeZone(TimeZone.getTimeZone("GMT-08:30"));
		System.out.println("Custome time -- MONTH/DAY at hr:min:sec (AM/PM) -- "
				+ (gameStartTime.get(Calendar.MONTH) + 1) + "/" + gameStartTime.get(Calendar.DAY_OF_MONTH) + " at "
				+ gameStartTime.get(Calendar.HOUR) + ":" + gameStartTime.get(Calendar.MINUTE) + " ("
				+ ((gameStartTime.get(Calendar.AM_PM) == 0) ? "AM" : "PM") + ")");

		// DST: Change Calendar.JANUARY to Calendar.JULY. GMT would be 8 and
		// London would be at 9 (GMT+1)
		// UK observes DST from March to October (British Summer Time)

		// After/Before demonstration
		Calendar gameFinal = new GregorianCalendar(TimeZone.getTimeZone("Europe/London"));
		gameFinal.set(2017, Calendar.JULY, 16, 9, 00);
		System.out.println("After? " + gameStartTime.after(gameFinal));
		System.out.println("Before? " + gameStartTime.before(gameFinal));
	}

	private static void testDateTimeAPI() {

		// Use-case 1: Software renewal		
		LocalDate expiryDate = LocalDate.of(2017, Month.JUNE, 30);
		System.out.println("expiryDate: " + expiryDate);
		LocalDate newExpiryDate = expiryDate.plusMonths(8);
		System.out.println("newExpiryDate: " + newExpiryDate);

		// Other methods: plus & minus methods, isBefore, isAfter
		
		 System.out.println("\nyear: " + newExpiryDate.getYear());
		 System.out.println("month: " + newExpiryDate.getMonth());
		 System.out.println("day of month: " + newExpiryDate.getDayOfMonth()); // returns Month enum 
		 System.out.println("day of week: " + newExpiryDate.getDayOfWeek()); 
		 System.out.println("Leap Year? " +	 newExpiryDate.isLeapYear()); 
		 System.out.println("length of month: " + newExpiryDate.lengthOfMonth());
		 
		 // get returns an int 
		 System.out.println("year again: " + newExpiryDate.get(ChronoField.YEAR)); 
		 System.out.println("month again: " + newExpiryDate.get(ChronoField.MONTH_OF_YEAR));
		 System.out.println("day of month again: " + newExpiryDate.get(ChronoField.DAY_OF_MONTH));
		 
		 // parse string 
		 LocalDate epoch = LocalDate.parse("1970-01-01"); // yyyy-mm-dd 
		 System.out.println("epoch year: " + epoch.getYear());
		 

		// Use-case 2: Game start time localized to time zone

		// LocalTime
		LocalTime time = LocalTime.of(9, 00, 00);
		System.out.println("\ntime -- hr: " + time.getHour());

		// LocalDate
		LocalDate gameStartDate = LocalDate.of(2017, Month.JULY, 03);

		// LocalDateTime
		LocalDateTime gameStartTime = LocalDateTime.of(gameStartDate, time);
		System.out.println("gameStartTime: " + gameStartTime);

		// TimeZone ==> ZoneId
		ZonedDateTime zonedGameStartTime = ZonedDateTime.of(gameStartTime, ZoneId.of("Europe/London"));
		System.out.println("zonedGameStartTime: " + zonedGameStartTime);
		
		ZonedDateTime indiaTime = zonedGameStartTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
		System.out.println("indiaTime: " + indiaTime);
		
		ZonedDateTime pst = zonedGameStartTime.withZoneSameInstant(ZoneId.of("America/Los_Angeles"));
		System.out.println("pst: " + pst);
		
		// Use-Case 3: Age calculation (Period)
		
		 LocalDate birthDay = LocalDate.of(1978, Month.JANUARY, 1); 
		 LocalDate today = LocalDate.now(); // current date from system clock 
		 Period period = birthDay.until(today); 
		 System.out.println("\nComplete Age: " + period.toString()); 
		 System.out.println("Age: " + period.getYears());
		 

		// Use-Case 4: Interval timing (Instant & Duration)
		
		 Instant startTime = Instant.now(); 
		 testLegacyDateAPI(); 
		 Instant endTime = Instant.now(); 
		 Duration timeElapsed =	 Duration.between(startTime, endTime); 
		 System.out.println("timeElapsed in ms: " + timeElapsed.toMillis());
		 

		// Partial Classes
		System.out.println("Christmas: " + MonthDay.of(Month.DECEMBER, 25));
		System.out.println("Credit card expiry date: " + YearMonth.of(2017, Month.JULY));

		// Don't forget to check out the API

	}

	public static void main(String[] args) {
		// testLegacyDateAPI();
		testDateTimeAPI();
	}
}
