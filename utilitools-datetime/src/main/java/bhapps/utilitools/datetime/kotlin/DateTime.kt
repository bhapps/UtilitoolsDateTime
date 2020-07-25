/*
     *
     * BH Apps
     * version 0.0.2
     * Methods for Date/Time
     * bhapps.utilitools.kotlin.datetime
     *
*/

//todo: add java8 and joda time based functions

package bhapps.utilitools.datetime.kotlin

import android.util.Log
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit

class DateTime {

    //region syntax
    /*

        Pattern Syntax
        You can use the following symbols in your formatting pattern:
        
        G 	Era designator (before christ, after christ)
        y 	Year (e.g. 12 or 2012). Use either yy or yyyy.
        M 	Month in year. Number of M's determine length of format (e.g. MM, MMM or MMMMM)
        d 	Day in month. Number of d's determine length of format (e.g. d or dd)
        h 	Hour of day, 1-12 (AM / PM) (normally hh)
        H 	Hour of day, 0-23 (normally HH)
        m 	Minute in hour, 0-59 (normally mm)
        s 	Second in minute, 0-59 (normally ss)
        S 	Millisecond in second, 0-999 (normally SSS)
        E 	Day in week (e.g Monday, Tuesday etc.)
        D 	Day in year (1-366)
        F 	Day of week in month (e.g. 1st Thursday of December)
        w 	Week in year (1-53)
        W 	Week in month (0-5)
        a 	AM / PM marker
        k 	Hour in day (1-24, unlike HH's 0-23)
        K 	Hour in day, AM / PM (0-11)
        z 	Time Zone
        ' 	Escape for text delimiter
        ' 	Single quote

        Pattern 	                        Example
        dd-MM-yy 	                        31-01-12
        dd-MM-yyyy 	                        31-01-2012
        MM-dd-yyyy 	                        01-31-2012
        yyyy-MM-dd 	                        2012-01-31
        yyyy-MM-dd HH:mm:ss 	            2012-01-31 23:59:59
        yyyy-MM-dd HH:mm:ss.SSS 	        2012-01-31 23:59:59.999
        yyyy-MM-dd HH:mm:ss.SSSZ 	        2012-01-31 23:59:59.999+0100
        EEEEE MMMMM yyyy HH:mm:ss.SSSZ 	    Saturday November 2012 10:45:42.720+0100

     */
    //endregion syntax

    var yyyyMMddFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd")
    var yyyyMMddHHmmssFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    var dayStartFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd 00:00:00")
    var dayEndFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd 11:59:59")
    var yyyyFormat: DateFormat = SimpleDateFormat("yyyy")
    var MMFormat: DateFormat = SimpleDateFormat("MM")
    var ddFormat: DateFormat = SimpleDateFormat("dd")
    var HHFormat: DateFormat = SimpleDateFormat("HH")
    var mmFormat: DateFormat = SimpleDateFormat("mm")
    var ampmFormat: DateFormat = SimpleDateFormat("a")

    var dayNameLongFormat: DateFormat = SimpleDateFormat("EEEE")
    var dayNameShortFormat: DateFormat = SimpleDateFormat("EEE")

    var monthNameLongFormat: DateFormat = SimpleDateFormat("MMMM")
    var monthNameShortFormat: DateFormat = SimpleDateFormat("MMM")

    var date = Date()

    //region timeago UI
    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    fun getCurrentDate(): Date {
        return Date()
    }

    fun getCurrentDateTimeAsLong(): Long {
        return Date().time
    }

    fun getCurrentDateFromStringFormat(format: String): String {
        return getFormattedDate(getCurrentDate().time, format)
    }

    fun covertTimeToTimeAgo(dataDate: String): String? {

        var convTime: String? = null

        val prefix = ""
        val suffix = "ago"

        try {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val pasTime = dateFormat.parse(dataDate)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime.time

            val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day = TimeUnit.MILLISECONDS.toDays(dateDiff)

            if (second < 60) {
                convTime = "$second seconds $suffix"
            } else if (minute < 60) {
                if (minute == 1L) {
                    convTime = "a minute $suffix"
                } else {
                    convTime = "$minute minutes $suffix"
                }

            } else if (hour < 24) {

                if (hour == 1L) {
                    convTime = "$hour hour $suffix"
                } else {
                    convTime = "$hour hours $suffix"
                }

            } else if (day >= 7) {

                if (day > 30) {

                    if (day == 1L) {
                        convTime = "$day month $suffix"
                    } else {
                        convTime = "$day months $suffix"
                    }

                } else if (day > 360) {

                    if (day == 365L) {
                        convTime = "$day year $suffix"
                    } else {
                        convTime = "$day years $suffix"
                    }

                } else {
                    convTime = (day / 7).toString() + " week " + suffix

                    if (day == 7L) {
                        convTime = (day / 7).toString() + " week " + suffix
                    } else {
                        convTime = "$day week $suffix"
                    }

                }
            } else if (day < 7) {

                if (day == 1L) {
                    convTime = "$day day $suffix"
                } else {
                    convTime = "$day days $suffix"
                }
            }

        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message)
        }

        return convTime

    }

    fun getTimeAgo(time: Long): String {
        var time = time
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000
        }

        // TODO: localize
        val diff = System.currentTimeMillis() - time
        return if (diff < MINUTE_MILLIS) {
            "just now"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "a minute ago"
        } else if (diff < 50 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " minutes ago"
        } else if (diff < 90 * MINUTE_MILLIS) {
            "an hour ago"
        } else if (diff < 24 * HOUR_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " hours ago"
        } else if (diff < 48 * HOUR_MILLIS) {
            "yesterday"
        } else {
            (diff / DAY_MILLIS).toString() + " days ago"
        }
    }

    val times: MutableMap<String, Long> = LinkedHashMap()

    fun setTimesArray()
    {
        times["year"] = TimeUnit.DAYS.toMillis(365)
        times["month"] = TimeUnit.DAYS.toMillis(30)
        times["week"] = TimeUnit.DAYS.toMillis(7)
        times["day"] = TimeUnit.DAYS.toMillis(1)
        times["hour"] = TimeUnit.HOURS.toMillis(1)
    }

    fun toRelative(duration: Long, maxLevel: Int): String {
        var duration = duration
        val res = StringBuilder()
        var level = 0

        setTimesArray()
        for ((key, value) in times) {
            val timeDelta = duration / value
            if (timeDelta > 0) {
                res.append(timeDelta)
                    .append(" ")
                    .append(key)
                    .append(if (timeDelta > 1) "s" else "")
                    .append(", ")
                duration -= value * timeDelta
                level++
            }
            if (level == maxLevel) {
                break
            }
        }
        if ("" == res.toString()) {
            return "a moment ago"
        } else {
            res.setLength(res.length - 2)
            res.append(" ago")
            return res.toString()
        }
    }

    fun toRelative(duration: Long): String {
        return toRelative(duration, times.size)
    }

    fun toRelative(start: Date, end: Date): String {
        assert(start.after(end))
        return toRelative(end.time - start.time)
    }

    fun toRelative(start: Date, end: Date, level: Int): String {
        assert(start.after(end))
        return toRelative(end.time - start.time, level)
    }
    // endregion timeago UI

    //region time Utilities

    fun getDateTimeIsAM(): Boolean {

        var result = false
        val current = ampmFormat
        if(ampmFormat.toString().contains("AM")){
            result = true;
        }

        return result
    }

    fun getDateTimeIsPM(): Boolean {

        var result = false
        if(ampmFormat.toString().contains("PM")){
            result = true;
        }

        return result
    }

    fun getDateTimeIsYear(year: String): Boolean {

        var result = false
        if(yyyyFormat.toString().equals(year)){
            result = true;
        }

        return result
    }

    fun getDateTimeIsMonth(month: String): Boolean {

        var result = false
        if(MMFormat.toString().equals(month)){
            result = true;
        }

        return result
    }

    fun getDateTimeIsDay(day: String): Boolean {

        var result = false
        if(ddFormat.toString().equals(day)){
            result = true;
        }

        return result
    }

    fun getDateTimeIsHour(hour: String): Boolean {

        var result = false
        if(HHFormat.toString().equals(hour)){
            result = true;
        }

        return result
    }

    fun getDateTimeIsMinute(minute: String): Boolean {

        var result = false
        if(mmFormat.toString().equals(minute)){
            result = true;
        }

        return result
    }

    fun getFormattedDate(dateTime: Long?, pattern: String): String {
        val newFormat = SimpleDateFormat(pattern)
        return newFormat.format(Date(dateTime!!))
    }

    fun checkIfDateIsToday(date: String): Boolean {
        var result = false
        var currentDateTimeFormat = SimpleDateFormat("yyyy/MM/dd").parse(
            yyyyMMddFormat.format(
                Date()
            ).toString()
        )

        var startDateTimeFormat = SimpleDateFormat("yyyy/MM/dd").parse(
            date
        )

        if(currentDateTimeFormat == startDateTimeFormat){
            result = true
        }
        return result
    }

    fun getWeekStartDate(): Date? {
        val calendar = Calendar.getInstance()
        while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1)
        }
        return calendar.time
    }

    fun getWeekEndDate(): Date? {
        val calendar = Calendar.getInstance()
        while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
            calendar.add(Calendar.DATE, 1)
        }
        calendar.add(Calendar.DATE, -1)
        return calendar.time
    }

    fun getCurrentWeekStartDate(): String? {
        val c = Calendar.getInstance()
        //ensure the method works within current month
        c[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        System.out.println("Before Start Date " + c.time)
        val date = c.time
        val dfDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val CurrentDate = dfDate.format(date)
        println("Start Date $CurrentDate")
        return CurrentDate
    }

    fun getCurrentWeekEndDate(): String? {
        val c = Calendar.getInstance()
        //ensure the method works within current month
        c[Calendar.DAY_OF_WEEK] = Calendar.SATURDAY
        System.out.println("Before End Date " + c.time)
        val date = c.time
        val dfDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val CurrentDate = dfDate.format(date)
        println("End Date $CurrentDate")
        return CurrentDate
    }

    fun getDateArrayOfDaysBetweenDates(startDate: Date?, endDate: Date?): List<Date>? {
        val dates = ArrayList<Date>()
        val cal1 = Calendar.getInstance()
        cal1.time = startDate
        val cal2 = Calendar.getInstance()
        cal2.time = endDate
        while (cal1.before(cal2) || cal1.equals(cal2)) {
            dates.add(cal1.time)
            cal1.add(Calendar.DATE, 1)
        }
        return dates
    }

    fun getStringArrayOfDaysBetweenDates(startDate: Date?, endDate: Date?): List<String>? {
        val dates = ArrayList<String>()
        val cal1 = Calendar.getInstance()
        cal1.time = startDate
        val cal2 = Calendar.getInstance()
        cal2.time = endDate
        while (cal1.before(cal2) || cal1.equals(cal2)) {
            val dfDate = SimpleDateFormat("yyyy/MM/dd")
            dates.add(dfDate.format(cal1.time))
            cal1.add(Calendar.DATE, 1)
        }
        return dates
    }

    fun getDayOfWeekAsNumber(date: Date?): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal[Calendar.DAY_OF_WEEK]
    }

    fun checkIfDateTimeUsingHasPassedCurrentDateTime(
        theDateTimeAsString: String,
        theTimeBetweenTimestampsToCheck: Int,
        //0 = seconds
        //1 = minutes
        //2 = hours
        //3 = days
        theTimeTypeToCheck: Int
    ): Boolean {
        var result = false
        try {
            if (theDateTimeAsString!!.isNotEmpty()) {
                val theDateTimeAsStringToDateTimeFormat =
                    SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(theDateTimeAsString)
                val currentDateTimeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(
                    yyyyMMddHHmmssFormat.format(
                        Date()
                    ).toString()
                )
                val diff = currentDateTimeFormat.time - theDateTimeAsStringToDateTimeFormat.time
                if(theTimeTypeToCheck == 0){
                    //0 = seconds
                    val seconds = diff / 1000
                    if (seconds > theTimeBetweenTimestampsToCheck) {
                        result = true
                    }
                }else if(theTimeTypeToCheck == 1){
                    //1 = minutes
                    val seconds = diff / 1000
                    val minutes = seconds / 60
                    if (minutes > theTimeBetweenTimestampsToCheck) {
                        result = true
                    }
                }else if(theTimeTypeToCheck == 2){
                    //2 = hours
                    val seconds = diff / 1000
                    val minutes = seconds / 60
                    val hours = minutes / 60
                    if (hours > theTimeBetweenTimestampsToCheck) {
                        result = true
                    }
                }else if(theTimeTypeToCheck == 3){
                    //3 = days
                    val seconds = diff / 1000
                    val minutes = seconds / 60
                    val hours = minutes / 60
                    val days = hours / 24
                    if (days > theTimeBetweenTimestampsToCheck) {
                        result = true
                    }
                }
            }
        }catch (exception: Exception) {
            Log.e("exception", "exception: "+  exception.message)
        }
        return result
    }

    inline fun calculateTimeDifferenceAsFormat(
        startTimestamp: String,
        endTimestamp: String,
        //0 = Days Hours Minutes Seconds
        //1 = 00:00:00
        //2 = 0h 0m 0s
        //3 = 0 hour 0 mins
        format: Int
    ): String {
        var result = ""
        try {

            val theDateTimeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val passedStartTimestamp = theDateTimeFormat.parse(startTimestamp)
            val passedEndTimestamp = theDateTimeFormat.parse(endTimestamp)
            val diffInMillis = passedEndTimestamp.time - passedStartTimestamp.time

            val dateDiffInDays = TimeUnit.DAYS.convert(
                diffInMillis,
                TimeUnit.MILLISECONDS
            )

            val dateDiffInHours = TimeUnit.HOURS.convert(
                diffInMillis - dateDiffInDays * 24 * 60 * 60 * 1000,
                TimeUnit.MILLISECONDS
            )

            val dateDiffInMinutes = TimeUnit.MINUTES.convert(
                diffInMillis - dateDiffInDays * 24 * 60 * 60 * 1000 - dateDiffInHours * 60 * 60 * 1000,
                TimeUnit.MILLISECONDS
            )
            val dateDiffInSeconds = TimeUnit.SECONDS.convert(
                diffInMillis - dateDiffInDays * 24 * 60 * 60 * 1000 - dateDiffInHours * 60 * 60 * 1000 - dateDiffInMinutes * 60 * 1000,
                TimeUnit.MILLISECONDS
            )

            if(format == 0){
                //returns format 0 Day(s) 0 Hour(s) 0 Minute(s) 0 Second(s)
                result = "$dateDiffInDays Day(s) $dateDiffInHours Hour(s) $dateDiffInMinutes Minute(s) $dateDiffInSeconds Second(s)"
            }else if(format == 1){
                //returns format 00:00:00
                result =
                    "" +
                    if(dateDiffInHours !=null){
                    if(dateDiffInHours.toString().length == 1) {"0"+dateDiffInHours}else{dateDiffInHours}
                    }else{"00"} +
                    ":" +
                    if(dateDiffInMinutes !=null){
                        if(dateDiffInMinutes.toString().length == 1) {"0"+dateDiffInMinutes}else{dateDiffInMinutes}
                    }else{"00"} +
                    ":" +
                    if(dateDiffInSeconds !=null){
                        if(dateDiffInSeconds.toString().length == 1) {"0"+dateDiffInSeconds}else{dateDiffInSeconds}
                    }else{"00"}
            }else if(format == 2){
                //returns 0h 0m 0s
                result =
                    "" +
                    if(dateDiffInHours !=null){
                        dateDiffInHours.toString() + "h"
                    }else{"0h"} +
                    " " +
                    if(dateDiffInMinutes !=null){
                        dateDiffInMinutes.toString() + "m"
                    }else{"0m"} +
                    " " +
                    if(dateDiffInSeconds !=null){
                        dateDiffInSeconds.toString() + "s"
                    }else{"0s"}
            }else if(format == 3){
                //returns 0 hours 0 mins
                result =
                    "" +
                    if(dateDiffInHours !=null){
                        if(dateDiffInHours.equals(1)){
                            dateDiffInHours.toString() + " hour"
                        }else{
                            dateDiffInHours.toString() + " hours"
                        }
                    }else{""} +
                    " " +
                    if(dateDiffInMinutes !=null){
                        if(dateDiffInMinutes.equals(1)){
                            dateDiffInMinutes.toString() + " min"
                        }else{
                            dateDiffInMinutes.toString() + " mins"
                        }
                    }else{"0 mins "}
            }

        }catch (exception: Exception) {
            Log.e("exception", "exception: "+  exception.message)
        }

        return result
    }

    inline fun calculateTimeDifferenceAsMilliSeconds(
        startTimestamp: String,
        endTimestamp: String
    ): Long {

            val theDateTimeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val passedStartTimestamp = theDateTimeFormat.parse(startTimestamp)
            val passedEndTimestamp = theDateTimeFormat.parse(endTimestamp)
            val timestampDifferenceInMilliSeconds = passedEndTimestamp.time - passedStartTimestamp.time

            return TimeUnit.MILLISECONDS.convert(
                timestampDifferenceInMilliSeconds,
                TimeUnit.MILLISECONDS
            )
    }

    inline fun calculateTimeAmountAsFormatFromMilliseconds(
        milliseconds: Long,
        //0 = 0 Day(s) 0 Hour(s) 0 Minute(s) 0 Second(s)
        //1 = 00:00:00
        //2 = 0h 0m 0s
        //3 = 0 hours 0 mins
        //4 = hours
        //5 = minutes
        format: Int
    ): String {
        var result = ""
        try {
            
            val dateDiffInDays = TimeUnit.DAYS.convert(
                milliseconds,
                TimeUnit.MILLISECONDS
            )

            val dateDiffInHours = TimeUnit.HOURS.convert(
                milliseconds - dateDiffInDays * 24 * 60 * 60 * 1000,
                TimeUnit.MILLISECONDS
            )

            val dateDiffInMinutes = TimeUnit.MINUTES.convert(
                milliseconds - dateDiffInDays * 24 * 60 * 60 * 1000 - dateDiffInHours * 60 * 60 * 1000,
                TimeUnit.MILLISECONDS
            )
            val dateDiffInSeconds = TimeUnit.SECONDS.convert(
                milliseconds - dateDiffInDays * 24 * 60 * 60 * 1000 - dateDiffInHours * 60 * 60 * 1000 - dateDiffInMinutes * 60 * 1000,
                TimeUnit.MILLISECONDS
            )

            if(format == 0){
                //returns format 0 Day(s) 0 Hour(s) 0 Minute(s) 0 Second(s)
                result = "$dateDiffInHours Hour(s) $dateDiffInMinutes Minute(s) $dateDiffInSeconds Second(s)"
            }else if(format == 1){
                //returns format 00:00:00
                result =
                    "" +
                            if(dateDiffInHours !=null){
                                if(dateDiffInHours.toString().length == 1) {"0" + dateDiffInHours }else{ dateDiffInHours }
                            }else{"00"} +
                            ":" +
                            if(dateDiffInMinutes !=null){
                                if(dateDiffInMinutes.toString().length == 1) {"0" + dateDiffInMinutes }else{ dateDiffInMinutes }
                            }else{"00"} +
                            ":" +
                            if(dateDiffInSeconds !=null){
                                if(dateDiffInSeconds.toString().length == 1) {"0" + dateDiffInSeconds }else{ dateDiffInSeconds }
                            }else{"00"}
            }else if(format == 2){
                //returns 0h 0m 0s
                result =
                    "" +
                            if(dateDiffInHours !=null){
                                if(dateDiffInHours.toString().length == 1) {dateDiffInHours.toString() + "h" }else{"" + dateDiffInHours + "h" }
                            }else{"0h"} +
                            " " +
                            if(dateDiffInMinutes !=null){
                                if(dateDiffInMinutes.toString().length == 1) {dateDiffInMinutes.toString() + "m" }else{"" + dateDiffInMinutes + "m" }
                            }else{"0m"} +
                            " " +
                            if(dateDiffInSeconds !=null){
                                if(dateDiffInSeconds.toString().length == 1) {dateDiffInSeconds.toString() + "s" }else{"" + dateDiffInSeconds + "s" }
                            }else{"0s"}
            }else if(format == 3){
                //returns 0 hours 0 mins
                result =
                    "" +
                            if(dateDiffInHours !=null){
                                if(dateDiffInHours.equals(1)){
                                    dateDiffInHours.toString() + " hour"
                                }else{
                                    dateDiffInHours.toString() + " hours"
                                }
                            }else{""} +
                            " " +
                            if(dateDiffInMinutes !=null){
                                if(dateDiffInMinutes.equals(1)){
                                    dateDiffInMinutes.toString() + " min"
                                }else{
                                    dateDiffInMinutes.toString() + " mins"
                                }
                            }else{"0 mins "}
            }else if(format == 4){
                result = TimeUnit.HOURS.convert(
                    milliseconds,
                    TimeUnit.MILLISECONDS
                ).toString()
            }else if(format == 5){
                result = TimeUnit.MINUTES.convert(
                    milliseconds,
                    TimeUnit.MILLISECONDS
                ).toString()
            }

        }catch (exception: Exception) {
            Log.e("exception", "exception: "+  exception.message)
        }

        return result
    }

    fun yyyyMMddHHmmssDateFormatFromPassedDateFormat(passedDateString: String, passedDateFormat: String): String {
        var result = ""

        if(passedDateString !=null && passedDateFormat!=null){

            try {

                var passedDateFormatConvertedToSimpleDateFormatForParsing: DateFormat = SimpleDateFormat(passedDateFormat)
                var passedDateStringConverted: Date = passedDateFormatConvertedToSimpleDateFormatForParsing.parse(passedDateString)
                result = bhapps.utilitools.datetime.kotlin.DateTime().yyyyMMddHHmmssFormat.format(
                    passedDateStringConverted
                ).toString()

            }catch(exception: Exception) {
                Log.e("exception", "exception: "+  exception.message)
            }

        }

        return result
    }

    //endregion time Utilities

}