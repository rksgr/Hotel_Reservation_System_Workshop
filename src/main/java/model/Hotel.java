package model;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class Hotel {

    private String hotelName;
    private Integer roomRateWeekday;
    private Integer roomRateWeekend;
    private Integer rating;
    private Integer rewardCustWeekdayRate;
    private Integer rewardCustWeekendRate;

    //Enum for different days of the week
    enum WeekDay{
        MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY
    }

    // constructor with 3 parameters
    public Hotel(String hotelName, Integer roomRateWeekday, Integer roomRateWeekend) {
        this.hotelName = hotelName;
        this.roomRateWeekday = roomRateWeekday;
        this.roomRateWeekend = roomRateWeekend;
    }

    // Constructor with 4 parameters
    public Hotel(Integer rating,String hotelName, Integer roomRateWeekday, Integer roomRateWeekend) {
        this.hotelName = hotelName;
        this.roomRateWeekday = roomRateWeekday;
        this.roomRateWeekend = roomRateWeekend;
        this.rating = rating;
    }

    // Constructor with 6 parameters
    public Hotel(String hotelName, Integer roomRateWeekday, Integer roomRateWeekend,Integer rating,
                 Integer rewardCustWeekdayRate, Integer rewardCustWeekendRate) {
        this.rewardCustWeekdayRate = rewardCustWeekdayRate;
        this.rewardCustWeekendRate = rewardCustWeekendRate;
        this.hotelName = hotelName;
        this.roomRateWeekday = roomRateWeekday;
        this.roomRateWeekend = roomRateWeekend;
        this.rating = rating;
    }

    // Getters and setters
    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Integer getRoomRateWeekday() {
        return roomRateWeekday;
    }

    public void setRoomRateWeekday(Integer roomRateWeekday) {
        this.roomRateWeekday = roomRateWeekday;
    }

    public Integer getRoomRateWeekend() {
        return roomRateWeekend;
    }

    public void setRoomRateWeekend(Integer roomRateWeekend) {
        this.roomRateWeekend = roomRateWeekend;
    }

    public Integer getRewardCustWeekdayRate() {
        return rewardCustWeekdayRate;
    }

    public void setRewardCustWeekdayRate(Integer rewardCustWeekdayRate) {
        this.rewardCustWeekdayRate = rewardCustWeekdayRate;
    }

    public Integer getRewardCustWeekendRate() {
        return rewardCustWeekendRate;
    }

    public void setRewardCustWeekendRate(Integer rewardCustWeekendRate) {
        this.rewardCustWeekendRate = rewardCustWeekendRate;
    }
    public Integer getRating(){
        return rating;
    }
    public void setRating(Integer rating){
        this.rating = rating;
    }

    // Get roomrate for a given date
    public Integer getRoomRate(LocalDate localDate){
        Integer roomRate =0;
        if ((localDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) || (localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY))) {
            roomRate =  this.getRoomRateWeekend();
        }else if (!(localDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) || !(localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY))) {
            roomRate = this.getRoomRateWeekday();
        }
        return roomRate;
    }

    // Get roomrate for a given date for a customer (Method overloaded)
    public Integer getRoomRate(Boolean isRewardCust, LocalDate localDate) {
        Integer roomRate = 0;
        if (!isRewardCust) {
            roomRate = getRoomRate(localDate);
        } else {
            if ((localDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) || (localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY))) {
                roomRate = this.getRewardCustWeekendRate();
            } else if (!(localDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) || !(localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY))) {
                roomRate = this.getRewardCustWeekdayRate();
            }
        }
        return roomRate;
    }
}
