package com.sksamuel.elastic4s.searches.aggs

import com.sksamuel.elastic4s.ElasticsearchDate

sealed trait ExtendedBounds {
  type Bound
  def min: Bound
  def max: Bound
}

object ExtendedBounds {
  def apply(min: Double, max: Double): DoubleExtendedBounds = DoubleExtendedBounds(min, max)
  def apply(min: Long, max: Long): LongExtendedBounds = LongExtendedBounds(min, max)
  def apply(min: ElasticsearchDate, max: ElasticsearchDate): DateExtendedBounds = DateExtendedBounds(min, max)
}

case class DoubleExtendedBounds(min: Double, max: Double) extends ExtendedBounds {
  type Bound = Double
}

case class LongExtendedBounds(min: Long, max: Long) extends ExtendedBounds {
  type Bound = Long
}

case class DateExtendedBounds(min: ElasticsearchDate, max: ElasticsearchDate) extends ExtendedBounds {
  type Bound = ElasticsearchDate
}
