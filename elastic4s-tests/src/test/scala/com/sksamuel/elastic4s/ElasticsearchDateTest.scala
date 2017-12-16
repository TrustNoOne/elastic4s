package com.sksamuel.elastic4s

import com.sksamuel.elastic4s.ElasticsearchTimeUnit._
import org.scalatest.{Matchers, WordSpec}

class ElasticsearchDateTest extends WordSpec with Matchers {

  "ElasticsearchDate" should {
    "parse a string date" in {
      ElasticsearchDate("2018-01-01") shouldBe ElasticsearchStringDate("2018-01-01")
    }

    "parse an epoch date" in {
      ElasticsearchDate(1234567890L) shouldBe ElasticsearchEpochDate(1234567890L)
    }

    "add amounts of time" in {
      val d1 = ElasticsearchDate("2018-01-01")
      d1.plus(1, Years).date shouldBe "2018-01-01||+1y"
      d1.plus(2, Months).date shouldBe "2018-01-01||+2M"
      d1.plus(3, Weeks).date shouldBe "2018-01-01||+3w"
      d1.plus(4, Days).date shouldBe "2018-01-01||+4d"
      d1.plus(5, Hours).date shouldBe "2018-01-01||+5h"
      d1.plus(6, Minutes).date shouldBe "2018-01-01||+6m"
      d1.plus(7, Seconds).date shouldBe "2018-01-01||+7s"
    }

    "add amounts of time (helper methods)" in {
      val d1 = ElasticsearchDate("2018-01-01")
      d1.plusYears(1) shouldBe d1.plus(1, Years)
      d1.plusMonths(2) shouldBe d1.plus(2, Months)
      d1.plusWeeks(3) shouldBe d1.plus(3, Weeks)
      d1.plusDays(4) shouldBe d1.plus(4, Days)
      d1.plusHours(5) shouldBe d1.plus(5, Hours)
      d1.plusMinutes(6) shouldBe d1.plus(6, Minutes)
      d1.plusSeconds(7) shouldBe d1.plus(7, Seconds)
    }

    "subtract amounts of time" in {
      val d1 = ElasticsearchDate("2018-01-01")
      d1.minus(1, Years).date shouldBe "2018-01-01||-1y"
      d1.minus(2, Months).date shouldBe "2018-01-01||-2M"
      d1.minus(3, Weeks).date shouldBe "2018-01-01||-3w"
      d1.minus(4, Days).date shouldBe "2018-01-01||-4d"
      d1.minus(5, Hours).date shouldBe "2018-01-01||-5h"
      d1.minus(6, Minutes).date shouldBe "2018-01-01||-6m"
      d1.minus(7, Seconds).date shouldBe "2018-01-01||-7s"
    }

    "subtract amounts of time (helper methods)" in {
      val d1 = ElasticsearchDate("2018-01-01")
      d1.minusYears(1) shouldBe d1.minus(1, Years)
      d1.minusMonths(2) shouldBe d1.minus(2, Months)
      d1.minusWeeks(3) shouldBe d1.minus(3, Weeks)
      d1.minusDays(4) shouldBe d1.minus(4, Days)
      d1.minusHours(5) shouldBe d1.minus(5, Hours)
      d1.minusMinutes(6) shouldBe d1.minus(6, Minutes)
      d1.minusSeconds(7) shouldBe d1.minus(7, Seconds)
    }

    "round down to a time unit" in {
      val d1 = ElasticsearchDate("2018-01-01")
      d1.roundedTo(Years).date shouldBe "2018-01-01||/y"
      d1.roundedTo(Months).date shouldBe "2018-01-01||/M"
      d1.roundedTo(Weeks).date shouldBe "2018-01-01||/w"
      d1.roundedTo(Days).date shouldBe "2018-01-01||/d"
      d1.roundedTo(Hours).date shouldBe "2018-01-01||/h"
      d1.roundedTo(Minutes).date shouldBe "2018-01-01||/m"
      d1.roundedTo(Seconds).date shouldBe "2018-01-01||/s"
    }

    "support 'now' date" in {
      val now = ElasticsearchDate("now")
      now shouldBe ElasticsearchDate.Now
      now.plus(1, Years).date shouldBe "now+1y"
      now.minus(2, Days).date shouldBe "now-2d"
    }

    "support multiple date math operations" in {
      ElasticsearchDate.Now.plusDays(1).minusHours(2).roundedTo(Months)
        .date shouldBe "now+1d-2h/M"

      ElasticsearchDate("2018-01-01").minusSeconds(1).roundedTo(Days).plusHours(1)
          .date shouldBe "2018-01-01||-1s/d+1h"
    }
  }
}
