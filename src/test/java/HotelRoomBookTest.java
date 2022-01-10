import exception.CustomExceptions.YearCannotBeBefore2020AndAfter2025;
import exception.CustomExceptions.StartDateIsAfterEndDateException;
import exception.CustomExceptions.MonthValueCannotBeGreaterThanTwelveException;
import exception.CustomExceptions.DayValueCannotBeGreaterThanThirtyOneException;
import exception.CustomExceptions.CannotBookHotelForPreviousDatesException;
import model.Hotel;
import service.HotelBooking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HotelRoomBookTest {
    HotelBooking hotelbook = new HotelBooking();

    @Test    // Test method for Use case 1
    public void givenRoom_WhenAdded_ShouldReturnTrue(){
        Hotel hotel_ad = new Hotel("Taj",200,250);
        boolean result_hotel_added = hotelbook.addHotel(hotel_ad);
        assertEquals(true,result_hotel_added);
    }
    @Test    // Test method for Use case 2
    public void givenDateRange_WhenSearched_ShouldReturnCheapestHotel(){
        // Date range
        LocalDate localdate1 = LocalDate.of(2020,9,10);
        LocalDate localdate2 = LocalDate.of(2020,9,11);
        String chp_hot = hotelbook.findCheapestHotel(localdate1,localdate2);
        System.out.println(chp_hot);
        assertEquals("Lakewood",chp_hot);
    }
    @Test    // Test method for Use case 3
    public void givenHotels_WhenRoomRatesWeekDayWeekendAdded_ShouldContainWeekdayRates(){
        hotelbook.addWeekdayWeekendRates();
        Integer room_rate = hotelbook.getHotelReservationSystem().get(0).getRoomRateWeekday();
        Assertions.assertEquals(110,room_rate);
    }
    @Test    // Test method for Use case 4
    public void givenDateRange_WhenSearchCheapestHotels_ShouldReturnCheapstHotel(){
        // Add three hotels
        hotelbook.addHotel(new Hotel("Lakewood",110,90));
        hotelbook.addHotel(new Hotel("Bridgewood",150,50));
        hotelbook.addHotel(new Hotel("Ridgewood",220,150));

        // Dates for which cheap hotel has to be searched
        LocalDate localDate1 = LocalDate.of(2020,9,11);
        LocalDate localDate2 = LocalDate.of(2020,9,12);
        ArrayList<String[]> chp_hotels = hotelbook.findCheapHotelWeekDayOrEnd(localDate1, localDate2);

        // Assert total rates and hotel names
        assertEquals("200",chp_hotels.get(0)[1]);
        assertEquals("200",chp_hotels.get(1)[1]);
        assertEquals("Lakewood",chp_hotels.get(0)[0]);
        assertEquals("Bridgewood",chp_hotels.get(1)[0]);
    }

    @Test    // Test method for Use case 5
    public void whenRating_SetForEachHotel_FetchRatingOfHotelByName(){
        hotelbook.addRatingsToHotel("Lakewood",3);
        hotelbook.addRatingsToHotel("Bridgewood",4);
        hotelbook.addRatingsToHotel("Ridgewood",5);

        // Get rating of hotel Lakewood
        Integer ratng = hotelbook.getHotelReservationSystem().stream()
                .filter(hotel->hotel.getHotelName()=="Lakewood").map(hotel->hotel.getRating()).findFirst().get();

        assertEquals(3,ratng);
    }
    @Test    // Test method for Use case 6
    public void findCheapestBestRatedHotelForGivenDateRange(){
        // Dates for which cheapest hotel with maximum rating has to be searched
        LocalDate locdat1 = LocalDate.of(2020,9,11);
        LocalDate locdat2 = LocalDate.of(2020,9,12);

        // Get cheapest hotel name, rating and total rate
        String[] cheapest_max_rating_hotel = hotelbook.findCheapestBestRatedHotel(locdat1,locdat2);
        String hotel_name = cheapest_max_rating_hotel[0];
        String hotel_rating = cheapest_max_rating_hotel[1];
        String hotel_total_rate = cheapest_max_rating_hotel[2];

        assertEquals("Bridgewood",hotel_name);
        assertEquals("4",hotel_rating);
        assertEquals("200",hotel_total_rate);
    }

    @Test    // Test method for Use case 7
    public void givenDateRange_WhenSearchBestRatedHotel_ShouldReturnBestRatedHotelAndTotalRates(){
        // Dates for which hotel with maximum rating has to be searched
        LocalDate locdat1 = LocalDate.of(2020,9,11);
        LocalDate locdat2 = LocalDate.of(2020,9,12);

        String[] max_rating_hotel = hotelbook.findMaxRatedHotel(locdat1,locdat2);
        String hotel_name = max_rating_hotel[0];
        String hotel_total_rate = max_rating_hotel[1];

        assertEquals("Ridgewood",hotel_name);
        assertEquals("370",hotel_total_rate);
    }

    @Test    // Test method for Use case 9
    public void afterSpecialRatesForRewardCustomersAdded_fetchWeekdayAndWeekendRatesForRewardCustomers(){
        LocalDate locdat = LocalDate.of(2020,10,11);

        // Add special weekday and weekend rates for reward customers
        hotelbook.addSpecialRatesRewardCustomers("Bridgewood",110,50);

        // Fetch weekday and weekend rates for reward customers
        Integer reward_cust_weekday_rate = hotelbook.getHotelReservationSystem().stream()
                .filter(hotel->hotel.getHotelName()=="Bridgewood")
                .findFirst().get().getRewardCustWeekdayRate();
        Integer reward_cust_weekend_rate = hotelbook.getHotelReservationSystem().stream()
                .filter(hotel->hotel.getHotelName()=="Bridgewood")
                .findFirst().get().getRewardCustWeekendRate();

        assertEquals("110",String.valueOf(reward_cust_weekday_rate));
        assertEquals("50",String.valueOf(reward_cust_weekend_rate));
    }
    @Test  // Test method for Use case 10
    public void givenDateRangeRewardCustomer_ShouldReturn_CheapestBestRatedHotel() throws StartDateIsAfterEndDateException {
        List<String[]> hotel_name_rating_rate = null;
        try {
            LocalDate strt_date = LocalDate.of(2020,9,11);
            LocalDate end_date = LocalDate.of(2020,9,12);

            hotel_name_rating_rate = hotelbook.findCheapstBestRatedHotelRewardCust(strt_date,end_date);

            String hotel_name = hotel_name_rating_rate.get(0)[0];
            Integer hotel_rating = Integer.parseInt(hotel_name_rating_rate.get(0)[1]);
            Integer hotel_rate = Integer.parseInt(hotel_name_rating_rate.get(0)[2]);

            assertEquals("Ridgewood", hotel_name);
            assertEquals(5, hotel_rating);
            assertEquals(140, hotel_rate);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }catch(StartDateIsAfterEndDateException e1){
            e1.printStackTrace();
        }
    }
    @Test // Test method for Use case 11
    public void givenDateRangeForRewardCustomer_ShouldReturn_CheapestBestRatedHotel()
            throws StartDateIsAfterEndDateException, YearCannotBeBefore2020AndAfter2025,
            MonthValueCannotBeGreaterThanTwelveException, DayValueCannotBeGreaterThanThirtyOneException,
            CannotBookHotelForPreviousDatesException {

        try {
            Boolean isRewardCustomer = true;
            LocalDate strt_date = LocalDate.of(2020,9,11);
            LocalDate end_date = LocalDate.of(2020,9,12);

            List<String[]> hotel_name_rating_rate = hotelbook.cheapestBestRatedHotelRewardCustom(strt_date,end_date,isRewardCustomer);

            String hotel_name = hotel_name_rating_rate.get(0)[0];
            Integer hotel_rating = Integer.parseInt(hotel_name_rating_rate.get(0)[1]);
            Integer hotel_rate = Integer.parseInt(hotel_name_rating_rate.get(0)[2]);

            assertEquals("Ridgewood", hotel_name);
            assertEquals(5, hotel_rating);
            assertEquals(140, hotel_rate);
        } catch (NoSuchElementException e5) {
            e5.printStackTrace();
        }catch(StartDateIsAfterEndDateException e7){
            e7.printStackTrace();
        }
        catch(YearCannotBeBefore2020AndAfter2025 e1)  {
            e1.printStackTrace();}
        catch(CannotBookHotelForPreviousDatesException e2)  {
            e2.printStackTrace();}
        catch(MonthValueCannotBeGreaterThanTwelveException e3)  { e3.printStackTrace();}
        catch(DayValueCannotBeGreaterThanThirtyOneException e4) { e4.printStackTrace();}
        catch(DateTimeException e9)     {e9.printStackTrace();}
    }


    @Test  // Test method for Use case 12
    public void givenDateRangeForRegularCustomer_ShouldReturn_CheapestBestRatedHotel()
            throws StartDateIsAfterEndDateException, YearCannotBeBefore2020AndAfter2025,
            MonthValueCannotBeGreaterThanTwelveException, DayValueCannotBeGreaterThanThirtyOneException,
            CannotBookHotelForPreviousDatesException {
        //List<String[]> hotel_name_rating_rate = null;

        try {
            Boolean isRewardCustomer = false;
            LocalDate strt_date = LocalDate.of(2020,9,11);
            LocalDate end_date = LocalDate.of(2020,9,12);
            List<String[]> hotel_name_rating_rate = hotelbook.cheapestBestRatedHotelRewardCustom(strt_date,end_date,isRewardCustomer);
            String hotel_name = hotel_name_rating_rate.get(0)[0];
            Integer hotel_rating = Integer.parseInt(hotel_name_rating_rate.get(0)[1]);
            Integer hotel_rate = Integer.parseInt(hotel_name_rating_rate.get(0)[2]);

            assertEquals("Bridgewood", hotel_name);
            assertEquals(4, hotel_rating);
            assertEquals(200, hotel_rate);
        } catch(StartDateIsAfterEndDateException e7){
            e7.printStackTrace();
        }
        catch(YearCannotBeBefore2020AndAfter2025 e1)  {
            e1.printStackTrace();}
        catch(CannotBookHotelForPreviousDatesException e2)  {
            e2.printStackTrace();}
        catch(MonthValueCannotBeGreaterThanTwelveException e3)  { e3.printStackTrace();}
        catch(DayValueCannotBeGreaterThanThirtyOneException e4) { e4.printStackTrace();}
        catch(DateTimeException e9)     {e9.printStackTrace();}
        catch (NoSuchElementException e5) {
            e5.printStackTrace();
        }
    }
}
