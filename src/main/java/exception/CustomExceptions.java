package exception;

public class CustomExceptions {


    // Exception thrown when start date entered comes after the end date in chronological order
    public static class StartDateIsAfterEndDateException extends Exception {
        public StartDateIsAfterEndDateException(String message) {
            super(message);
        }
    }

    // Exception thrown when day value entered is greater than 31
    public static class DayValueCannotBeGreaterThanThirtyOneException extends Exception {
        public DayValueCannotBeGreaterThanThirtyOneException(String message) {
            super(message);
        }
    }

    // Exception thrown when month entered is greater than 12
    public static class MonthValueCannotBeGreaterThanTwelveException extends Exception {
        public MonthValueCannotBeGreaterThanTwelveException(String message) {
            super(message);
        }
    }

    // Exception thrown when year entered is before 2020
    public static class CannotBookHotelForPreviousDatesException extends Exception {
        public CannotBookHotelForPreviousDatesException(String message) {
            super(message);
        }
    }
    public static class YearCannotBeBefore2020AndAfter2025 extends Exception{
        public YearCannotBeBefore2020AndAfter2025(String message) {
            super(message);
        }
    }
}