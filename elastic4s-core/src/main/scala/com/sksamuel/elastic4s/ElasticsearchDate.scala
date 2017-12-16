package com.sksamuel.elastic4s

sealed trait ElasticsearchTimeUnit {
  private[elastic4s] def unit: String
}

object ElasticsearchTimeUnit {

  case object Years extends ElasticsearchTimeUnit {
    private[elastic4s] def unit: String = "y"
  }

  case object Months extends ElasticsearchTimeUnit {
    private[elastic4s] def unit: String = "M"
  }

  case object Weeks extends ElasticsearchTimeUnit {
    private[elastic4s] def unit: String = "w"
  }

  case object Days extends ElasticsearchTimeUnit {
    private[elastic4s] def unit: String = "d"
  }

  case object Hours extends ElasticsearchTimeUnit {
    private[elastic4s] def unit: String = "h"
  }

  case object Minutes extends ElasticsearchTimeUnit {
    private[elastic4s] def unit: String = "m"
  }

  case object Seconds extends ElasticsearchTimeUnit {
    private[elastic4s] def unit: String = "s"
  }

}

/**
  * An elasticsearch date. It can be a number or a string,
  * see https://www.elastic.co/guide/en/elasticsearch/reference/current/date.html
  */
sealed trait ElasticsearchDate {
  type Date

  def date: Date
}

object ElasticsearchDate {

  val Now = ElasticsearchStringDate("now")

  def apply(date: String): ElasticsearchStringDate = ElasticsearchStringDate(date)
  def apply(date: Long): ElasticsearchEpochDate = ElasticsearchEpochDate(date)
}


private[elastic4s] case class ElasticsearchStringDate(date: String) extends ElasticsearchDate {
  type Date = String

  /**
    * Elasticsearch Date Math
    * see https://www.elastic.co/guide/en/elasticsearch/reference/current/common-options.html#date-math
    */
  def plus(amount: Int, unit: ElasticsearchTimeUnit): ElasticsearchStringDate =
    dateMath("+", amount.toString, unit.unit)

  def minus(amount: Int, unit: ElasticsearchTimeUnit): ElasticsearchStringDate =
    dateMath("-", amount.toString, unit.unit)

  def roundedTo(unit: ElasticsearchTimeUnit): ElasticsearchStringDate =
    dateMath("/", "", unit.unit)

  def hasDateMath: Boolean = date.startsWith(ElasticsearchDate.Now.date) || date.contains("||")

  private def dateMath(operation: String,
                       amount: String,
                       unit: String
                      ): ElasticsearchStringDate = {
    if (hasDateMath) ElasticsearchStringDate(s"$date$operation$amount$unit")
    else ElasticsearchStringDate(s"$date||$operation$amount$unit")
  }

  def plusYears(years: Int): ElasticsearchStringDate = plus(years, ElasticsearchTimeUnit.Years)
  def plusMonths(months: Int): ElasticsearchStringDate = plus(months, ElasticsearchTimeUnit.Months)
  def plusWeeks(weeks: Int): ElasticsearchStringDate = plus(weeks, ElasticsearchTimeUnit.Weeks)
  def plusDays(days: Int): ElasticsearchStringDate = plus(days, ElasticsearchTimeUnit.Days)
  def plusHours(hours: Int): ElasticsearchStringDate = plus(hours, ElasticsearchTimeUnit.Hours)
  def plusMinutes(minutes: Int): ElasticsearchStringDate = plus(minutes, ElasticsearchTimeUnit.Minutes)
  def plusSeconds(seconds: Int): ElasticsearchStringDate = plus(seconds, ElasticsearchTimeUnit.Seconds)

  def minusYears(years: Int): ElasticsearchStringDate = minus(years, ElasticsearchTimeUnit.Years)
  def minusMonths(months: Int): ElasticsearchStringDate = minus(months, ElasticsearchTimeUnit.Months)
  def minusWeeks(weeks: Int): ElasticsearchStringDate = minus(weeks, ElasticsearchTimeUnit.Weeks)
  def minusDays(days: Int): ElasticsearchStringDate = minus(days, ElasticsearchTimeUnit.Days)
  def minusHours(hours: Int): ElasticsearchStringDate = minus(hours, ElasticsearchTimeUnit.Hours)
  def minusMinutes(minutes: Int): ElasticsearchStringDate = minus(minutes, ElasticsearchTimeUnit.Minutes)
  def minusSeconds(seconds: Int): ElasticsearchStringDate = minus(seconds, ElasticsearchTimeUnit.Seconds)
}

private[elastic4s] case class ElasticsearchEpochDate(date: Long) extends ElasticsearchDate {
  type Date = Long
}
