package se.utility;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class DateTimeUtil {

    public final String YMDHMS_FORMAT =  "yyyy/MM/dd HH:mm:ss";
    public final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public String convertDateToCorrectFormat(@NotNull LocalDate expDate, String expFormat) {
        DateTimeFormatter _expFormat = DateTimeFormatter.ofPattern(expFormat);
        return expDate.format(_expFormat);
    }

   public int getDayOfCurrentDate(@NotNull LocalDate expDate) {
        return expDate.getDayOfMonth();
   }

   public int getMonthOfCurrentDate(@NotNull LocalDate expDate) {
        return expDate.getMonthValue();
   }

   public int getYearOfCurrentDate(@NotNull LocalDate expDate) {
        return expDate.getYear();
   }

   public String processMonth(@NotNull Object convertedMonth) {

        if (convertedMonth.getClass() == String.class) {

            for (EMonth month : EMonth.values()) {
                if (Objects.equals(month.getEValue().toLowerCase(),
                        convertedMonth.toString().toLowerCase().trim())) {

                    return month.getEValue();
                }
            }
        }
        else {

            for (EMonth month : EMonth.values()) {

                //Adding 1 because the ordinal started from 0
                if (Objects.equals(month.ordinal()+1, convertedMonth)) {

                    return month.getEValue();
                }
            }

        }

       throw new IllegalArgumentException("The desired month was invalid!");
   }

    public enum EMonth {

        JANUARY("January"),
        FEBRUARY("February"),
        MARCH("March"),
        APRIL("April"),
        MAY("May"),
        JUNE("June"),
        JULY("July"),
        AUGUST("August"),
        SEPTEMBER("September"),
        OCTOBER("October"),
        NOVEMBER("November"),
        DECEMBER("December");

        private final String _value;

        EMonth(String value) {
            this._value = value;
        }

        public String getEValue() {
            return _value;
        }

        @Override
        public String toString() {
            return this.getEValue();
        }

        @Contract("null -> fail")
        public static @NotNull EMonth getEMonth(String value) {

            if(value == null)

                throw new IllegalArgumentException("The desired month was empty!");
            for(EMonth month : values())
                if(value.equalsIgnoreCase(month.getEValue())) {

                    return month;
                }

            throw new IllegalArgumentException("The desired month did not match any values!");
        }
    }

    public String getCurrentDateAsString(String dateTimeFormat) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat);
        Date currentDate = new Date();

        return simpleDateFormat.format(currentDate);
    }
}
