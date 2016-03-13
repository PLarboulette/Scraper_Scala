package perso.iadvize.utils

import org.joda.time.DateTime

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
    val dt : DateTime = new DateTime(year, month, day, hour, minutes, 0, 0)
    dt
  }

}
