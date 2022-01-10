package service;

import model.Hotel;
import exception.CustomExceptions.YearCannotBeBefore2020AndAfter2025;
import exception.CustomExceptions.StartDateIsAfterEndDateException;
import exception.CustomExceptions.MonthValueCannotBeGreaterThanTwelveException;
import exception.CustomExceptions.DayValueCannotBeGreaterThanThirtyOneException;
import exception.CustomExceptions.CannotBookHotelForPreviousDatesException;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.regex.Matcher;

public class HotelBooking {
    private ArrayList<Hotel> hotelReservationSystem = new ArrayList<>();

    // enum containing the days of the week
    enum DaysOfWeek{
        MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY
    }

    public ArrayList<Hotel> getHotelReservationSystem(){
        return hotelReservationSystem;
    }
    public void setHotelReservationSystem(ArrayList<Hotel> hotelReservationSystem){
        this.hotelReservationSystem = hotelReservationSystem;
    }

    public boolean addHotel(Hotel hotel){
        boolean hotel_added = false;

        // count number of hotels
        int init_hotels = hotelReservationSystem.size();

        // Add hotel to the hotel reservation system (arraylist)
        hotelReservationSystem.add(hotel);

        int fin_hotels = hotelReservationSystem.size();

        // If number of hotels increase by one, then addition is successful
        if (fin_hotels-init_hotels == 1){
            hotel_added = true;
        }
        return hotel_added;
    }
    /*
    Use Case 2: Find cheapest hotel for a given date range
     */
    public String findCheapestHotel(LocalDate strtdate, LocalDate finldate){
        addHotel(new Hotel("Lakewood",110,90));
        addHotel(new Hotel("Bridgewood",150,50));
        addHotel(new Hotel("Ridgewood",220,150));

        // Find minimum rate for given range dates
        Integer min_rate = hotelReservationSystem.stream()
                .map(hotel->{
                    Integer tot_rate = 0;

                    // Compute total rate of hotel for given range dates
                    for (LocalDate locdate=strtdate;locdate.isBefore(finldate.plusDays(01));locdate=locdate.plusDays(01)){
                        tot_rate += hotel.getRoomRate(locdate);
                    }
                    return tot_rate;})
                .min((hotel1, hotel2)-> (hotel1.compareTo(hotel2))).get();

        // Get name of hotel with minimum rate
        Optional<Hotel> chp_hotel = hotelReservationSystem.stream()
                .filter(hotel -> min_rate == hotel.getRoomRate(strtdate)+hotel.getRoomRate(finldate))
                .findFirst();


        return chp_hotel.get().getHotelName();
    }
    /*
    Use case 3: Add weekday and weekend rates for each hotel
     */
    public void addWeekdayWeekendRates(){
        addHotel(new Hotel("Lakewood",110,90));
        addHotel(new Hotel("Bridgewood",150,50));
        addHotel(new Hotel("Ridgewood",220,150));
    }

    /*
    Use case 4: Find cheapest hotel(s) for a given date range based on weekday or weekend
     */
    public ArrayList<String[]> findCheapHotelWeekDayOrEnd(LocalDate localDate_strt, LocalDate localDate_end) {

        // Get the minimum total rate for the given date range
        Integer tot_rate = hotelReservationSystem.stream().map(hotel->{
            Integer total_rate = hotel.getRoomRate(localDate_strt)+hotel.getRoomRate(localDate_end);
            return total_rate;}).min((arr1,arr2)->arr1.compareTo(arr2)).get();

        // Get the name of the hotel(s) having the above total rate
        List hotl_list = hotelReservationSystem.stream()
                .filter(hotel ->tot_rate == (hotel.getRoomRate(localDate_end)+hotel.getRoomRate(localDate_strt)))
                .map(hotel->hotel.getHotelName())
                .collect(Collectors.toList());

        // Create an arraylist to store cheap hotel name and total rate
        ArrayList<String[]> chpst_hotel_name_rate = new ArrayList<>();
        for (int i=0;i<hotl_list.size();i++){
            String[] name_rate = new String[]{(String) hotl_list.get(i), String.valueOf(tot_rate)};
            chpst_hotel_name_rate.add(name_rate);
        }
        return chpst_hotel_name_rate;
    }
    /*
    Use Case 5: Add ratings to each hotel
     */
    public void addRatingsToHotel(String hotelname, Integer rating){
        addHotel(new Hotel("Lakewood",110,90));
        addHotel(new Hotel("Bridgewood",150,50));
        addHotel(new Hotel("Ridgewood",220,150));

        // Get the hotel with the given name
        Hotel hotl = hotelReservationSystem.stream().filter(hotel->hotelname==hotel.getHotelName())
                .findFirst().get();

        // Set the rating of the hotel
        hotl.setRating(rating);
    }
    /*
    Use Case 6: Cheapest best rated hotel for given date range
     */
    public String[] findCheapestBestRatedHotel(LocalDate loclDate1,LocalDate loclDate2){
        addHotel(new Hotel(3,"Lakewood",110,90));
        addHotel(new Hotel(4,"Bridgewood",150,50));
        addHotel(new Hotel(5,"Ridgewood",220,150));

        // Get the lowest possible rates for given date range
        Integer cheapest_rate = hotelReservationSystem.stream()
                .map(hotl->{
                    Integer tot_rate = 0;
                    // Get total rate of a hotel for the given date range
                    for (LocalDate ldate=loclDate1;ldate.isBefore(loclDate2.plusDays(01));
                         ldate = ldate.plusDays(01)){
                        tot_rate += hotl.getRoomRate(ldate);
                    }
                    return tot_rate;})
                .min((rate1,rate2)->rate1.compareTo(rate2)).get();

        // Get the maximum rating hotel for the cheapest rate of above
        Hotel max_rating_hotl = hotelReservationSystem.stream()
                .filter(hotl->cheapest_rate ==(hotl.getRoomRate(loclDate1)+hotl.getRoomRate(loclDate2)))
                .max((hotl1,hotl2)->hotl1.getRating().compareTo(hotl2.getRating())).get();
        String max_rating_hotl_name = max_rating_hotl.getHotelName();   // get hotel name

        // Store the name, rating and total rate in an array of string
        String []result = {max_rating_hotl_name,String.valueOf(max_rating_hotl.getRating()),String.valueOf(cheapest_rate)};
        return result;
    }
    /*
    Use case 7: Hotel with maximum rating for given date range
     */
    public String[] findMaxRatedHotel(LocalDate locdat1, LocalDate locdat2) {
        addHotel(new Hotel(3,"Lakewood",110,90));
        addHotel(new Hotel(4,"Bridgewood",150,50));
        addHotel(new Hotel(5,"Ridgewood",220,150));

        // Find the best rating
        Integer rating = hotelReservationSystem.stream()
                .map(hotel->hotel.getRating())
                .max((rating1,rating2)-> rating1.compareTo(rating2)).get();

        // Find the name of hotel with best rating
        String hotel_name = hotelReservationSystem.stream()
                .filter(hotel->rating== hotel.getRating())
                .findFirst().get().getHotelName();

        // Find the total rate of the best rated hotel for given date range
        Integer total_rate = hotelReservationSystem.stream()
                .filter(hotel->hotel.getHotelName().equals(hotel_name))
                .map(hotel->hotel.getRoomRate(locdat1)+hotel.getRoomRate(locdat2))
                .findFirst().get();

        // Return an array containing name and total rate of best rated hotel
        String[] max_rated_hotel_info = {hotel_name,String.valueOf(total_rate)};
        return max_rated_hotel_info;
    }
    /*
    Use Case 9: Add special rates for reward customers as a part of loyalty program
     */
    public void addSpecialRatesRewardCustomers(String hotelName, Integer reward_cust_weekday_rate, Integer reward_cust_weekend_rate) {
        addHotel(new Hotel(3,"Lakewood",110,90));
        addHotel(new Hotel(4,"Bridgewood",150,50));
        addHotel(new Hotel(5,"Ridgewood",220,150));

        Hotel hotl = hotelReservationSystem.stream().filter(hotel->hotelName== hotel.getHotelName()).findFirst().get();
        hotl.setRewardCustWeekdayRate(reward_cust_weekday_rate);
        hotl.setRewardCustWeekendRate(reward_cust_weekend_rate);
    }
    /*
    Use Case 10: Find cheapest best rated hotel for a given date range for a reward customer
     */
    public List<String[]> findCheapstBestRatedHotelRewardCust(LocalDate strtdate,LocalDate enddate) throws StartDateIsAfterEndDateException {
        addHotel(new Hotel("Lakewood",110,90,3,80,80));
        addHotel(new Hotel("Bridgewood",150,50,4,110,50));
        addHotel(new Hotel("Ridgewood",220,150,5,100,40));


        LocalDate strt_date = strtdate;
        LocalDate end_date = enddate;

        // validate the dates
        if (strt_date.isAfter(end_date)) {
            throw new StartDateIsAfterEndDateException("Start date is before the end date.");
        }

        // Get a list of string array with each array containing hotel name, rating and total rate
        List<String[]> list_hotel_rate_rating = hotelReservationSystem.stream().map(hotel->{
            Integer tot_rate = 0 ;

            // compute the total rate of the hotel for the given date range
            for (LocalDate ldate = strt_date; ldate.isBefore(end_date.plusDays(01));ldate = ldate.plusDays(01)){
                tot_rate += hotel.getRoomRate(true,ldate);
            }

            String[] hotel_info = {hotel.getHotelName(),String.valueOf(hotel.getRating()),String.valueOf(tot_rate)};
            return hotel_info;
        }).collect(Collectors.toList());

        // Find the minimum rate among all the total rates
        Integer min_rate = list_hotel_rate_rating.stream()
                .map(hotel_arr->hotel_arr[2])
                .map(rate_str->Integer.parseInt(rate_str))
                .min((rate1,rate2)-> rate1.compareTo(rate2)).get();

        // Get a list of hotels having total rate equal to minimum rate
        List<String[]> list_hotel_info = list_hotel_rate_rating.stream()
                .filter(hotel_arr-> min_rate.equals(Integer.parseInt(hotel_arr[2])))
                .collect(Collectors.toList());

        // If more than one hotel have minimum rate, get the one with maximum rating
        if (list_hotel_info.size()>1){
            Integer max_rating = list_hotel_rate_rating.stream().filter(hotel_arr-> hotel_arr[2].equals(min_rate))
                    .map(hotel_arr->Integer.parseInt(hotel_arr[1]))
                    .max((rating1,rating2)->rating1.compareTo(rating2)).get();
            list_hotel_info = list_hotel_rate_rating.stream().filter(hotel_arr->min_rate.equals(Integer.parseInt(hotel_arr[2])))
                    .filter(hotel_arr->max_rating.equals(Integer.parseInt(hotel_arr[1])))
                    .collect(Collectors.toList());
        }

        // Return a list of string array type containing hotel name
        // whose rate is minimum and among them the hotel rating with maximum rating
        return list_hotel_info;
    }
    /*
    Use Case 11: Find cheapest best rated hotel for a given date range for a reward customer using Java Streams
                   For a reward customer, isRewardCustomer = true;

    Use Case 12: Find cheapest best rated hotel for a given date range for a regular customer using Java Streams
                    For a regular customer, isRewardCustomer = false;
     */
    public List<String[]> cheapestBestRatedHotelRewardCustom(LocalDate strtdate, LocalDate enddate, Boolean isRewardCustomer) throws
            StartDateIsAfterEndDateException, YearCannotBeBefore2020AndAfter2025,
            CannotBookHotelForPreviousDatesException, MonthValueCannotBeGreaterThanTwelveException,
            DayValueCannotBeGreaterThanThirtyOneException {
        addHotel(new Hotel("Lakewood",110,90,3,80,80));
        addHotel(new Hotel("Bridgewood",150,50,4,110,50));
        addHotel(new Hotel("Ridgewood",220,150,5,100,40));

        // Validate the start date and end date by calling validateDateEntered method
        LocalDate strt_date = validateDateEntered(strtdate);
        LocalDate end_date = validateDateEntered(enddate);

        // If the start date entered comes after the end date, throw custom Exception
        if (strt_date.isAfter(end_date)) {
            throw new StartDateIsAfterEndDateException("Start date is before the end date.");
        }

        // Get a list of string array with each array containing hotel name, rating and total rate
        List<String[]> list_hotel_rate_rating = hotelReservationSystem.stream().map(hotel->{
            Integer tot_rate = 0 ;

            // Compute the total rate of the hotel for the given date range
            for (LocalDate ldate = strt_date; ldate.isBefore(end_date.plusDays(01));ldate = ldate.plusDays(01)){
                tot_rate += hotel.getRoomRate(isRewardCustomer,ldate);
            }

            String[] hotel_info = {hotel.getHotelName(),String.valueOf(hotel.getRating()),String.valueOf(tot_rate)};
            return hotel_info;
        }).collect(Collectors.toList());

        // Find the minimum rate among all the total rates
        Integer min_rate = list_hotel_rate_rating.stream()
                .map(hotel_arr->hotel_arr[2])
                .map(rate_str->Integer.parseInt(rate_str))
                .min((rate1,rate2)-> rate1.compareTo(rate2)).get();

        // Get a list of hotels having total rate equal to minimum rate
        List<String[]> list_hotel_info = list_hotel_rate_rating.stream()
                .filter(hotel_arr-> min_rate.equals(Integer.parseInt(hotel_arr[2])))
                .collect(Collectors.toList());

        // If more than one hotel have minimum rate, get the one with maximum rating
        if (list_hotel_info.size()>1){
            // Get the maximum rated hotel at this minimum price
            Integer max_rating = list_hotel_info.stream()
                    .map(hotel_arr->Integer.parseInt(hotel_arr[1]))
                    .max((rating1,rating2)->rating1.compareTo(rating2)).get();

            // Get the name of the hotel with this maximum rating
            list_hotel_info = list_hotel_info.stream()
                    .filter(hotel_arr->max_rating.equals(Integer.parseInt(hotel_arr[1])))
                    .collect(Collectors.toList());
        }

        // Return a list of string array type containing hotel name
        // whose rate is minimum and among them the hotel with maximum rating
        return list_hotel_info;
    }

    // Method returns date after performing validation
    public LocalDate validateDateEntered(LocalDate loclDate) throws YearCannotBeBefore2020AndAfter2025,
            CannotBookHotelForPreviousDatesException, MonthValueCannotBeGreaterThanTwelveException,
            DayValueCannotBeGreaterThanThirtyOneException {

        Integer year = loclDate.getYear();
        Integer month = loclDate.getMonthValue();
        Integer day = loclDate.getDayOfMonth();
        Pattern pattern1 = Pattern.compile("[2][0][2][0-5]");
        Matcher matcher1 = pattern1.matcher(String.valueOf(year));
        boolean isYearCorrect = matcher1.matches();
        if (!isYearCorrect){
            throw new YearCannotBeBefore2020AndAfter2025("Year should lie between 2020 and 2025.");
        }
        if (year<2020){
            throw new CannotBookHotelForPreviousDatesException("Hotel cannot be booked for back dates.");
        }
        Pattern pattern2 = Pattern.compile("[1-9]|[1][0-2]");
        Matcher matcher2 = pattern2.matcher(String.valueOf(month));
        boolean isMonthCorrect = matcher2.matches();

        if(!isMonthCorrect){
            throw new MonthValueCannotBeGreaterThanTwelveException("Month value should lie between 1 and 12.");
        }

        Pattern pattern3 = Pattern.compile("[1-9]|[1-2][0-9]|[3][0-1]");
        Matcher matcher3 = pattern3.matcher(String.valueOf(day));
        boolean isDayCorrect = matcher3.matches();

        if(!isDayCorrect){
            throw new DayValueCannotBeGreaterThanThirtyOneException("Day value should be between 1 and 31.");
        }
        LocalDate date = LocalDate.of(year, month, day);
        return date;
    }
}