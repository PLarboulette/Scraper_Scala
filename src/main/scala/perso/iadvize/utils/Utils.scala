package perso.iadvize.utils

import org.joda.time.{DateTime, DateTimeZone}

/**
  * Created by pierre on 13/03/16.
  */
object Utils {

  /**
    * Convert DD/MM/YYYY to YYYY-MM-DD  Example : 2/03/2016  05:03  --> 2016-03-2 05:03)
    *
    * @param originalDate
    * @return
    */
  def convertDate (originalDate : String) : DateTime = {
    val day = originalDate.split(" ")(0).split("/")(0).toInt
    val month = originalDate.split(" ")(0).split("/")(1).toInt
    val year = originalDate.split(" ")(0).split("/")(2).toInt
    val hour = originalDate.split("  ")(1).split(":")(0).toInt
    val minutes = originalDate.split("  ")(1).split(":")(1).trim.toInt
    val dt : DateTime = new DateTime(year, month, day, hour, minutes, 0, DateTimeZone.UTC)
    dt
  }

  def getMinimalDate (originalDate : String) : String = {
    originalDate.split(" ")(0).trim
  }

  def dateStringFormat (originalDate : String) : String = {
    Utils.convertDate(originalDate).toString("YYYY-MM-dd HH:mm:ss")
  }

  def initDatetimeFrom(from: String): DateTime = {
    val datetimeFrom: DateTime = new DateTime(from)
    datetimeFrom.withHourOfDay(0)
    datetimeFrom.withMinuteOfHour(0)
    datetimeFrom.withSecondOfMinute(0)
    datetimeFrom
  }

  def initDatetimeTo(to: String): DateTime = {
    val datetimeTo: DateTime = new DateTime(to)
    datetimeTo.withHourOfDay(23)
    datetimeTo.withMinuteOfHour(59)
    datetimeTo.withSecondOfMinute(59)
    datetimeTo
  }
}
