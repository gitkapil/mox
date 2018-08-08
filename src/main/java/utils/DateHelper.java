package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateHelper{

    /**
     *
     * @returns the current system date and timestamp
     */
    public Calendar getSystemDateandTimeStamp(){
        Calendar currDateTime = Calendar.getInstance();
        return currDateTime;
    }

    /**
     *
     * @param cal Calendar instance
     * @param pattern format to whuch the date needs to get converted
     * @returns the formatted string
     */
   public String convertDateTimeIntoAFormat(Calendar cal, String pattern){
       SimpleDateFormat sdf= new SimpleDateFormat();
        try{
         sdf   = new SimpleDateFormat(pattern, Locale.US);
       }

       catch (Exception e){
           return "";
       }

       return sdf.format(cal.getTime());
   }


    /**
     *
     * @param cal Calendar instance
     * @param minutes to be subtracted
     * @returns the new value
     */
    public Calendar subtractMinutesFromSystemDateTime(Calendar cal, int minutes){
        try{

            cal.add(Calendar.MINUTE, -minutes);
        }

        catch (Exception e){
            return null;
        }

        return cal;
    }




}
