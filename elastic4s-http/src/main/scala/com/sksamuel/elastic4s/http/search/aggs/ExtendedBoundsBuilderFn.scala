package com.sksamuel.elastic4s.http.search.aggs

import com.sksamuel.elastic4s.{ElasticsearchEpochDate, ElasticsearchStringDate}
import com.sksamuel.elastic4s.json.XContentBuilder
import com.sksamuel.elastic4s.searches.aggs.{DateExtendedBounds, DoubleExtendedBounds, ExtendedBounds, LongExtendedBounds}

object ExtendedBoundsBuilderFn {
  def apply(bounds: ExtendedBounds, builder: XContentBuilder): XContentBuilder = {
    builder.startObject("extended_bounds")
    bounds match {
      case DoubleExtendedBounds(min, max) =>
        builder.field("min", min)
        builder.field("max", max)

      case LongExtendedBounds(min, max) =>
        builder.field("min", min)
        builder.field("max", max)

      case DateExtendedBounds(min, max) =>
        min match {
          case ElasticsearchStringDate(value) => builder.field("min", value)
          case ElasticsearchEpochDate(value) => builder.field("min", value)
        }

        max match {
          case ElasticsearchStringDate(value) => builder.field("max", value)
          case ElasticsearchEpochDate(value) => builder.field("max", value)
        }
    }
    builder.endObject()
  }
}
