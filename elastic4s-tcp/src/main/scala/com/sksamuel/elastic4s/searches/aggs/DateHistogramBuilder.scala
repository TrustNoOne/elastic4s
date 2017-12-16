package com.sksamuel.elastic4s.searches.aggs

import java.nio.ByteBuffer

import com.sksamuel.elastic4s.{ElasticsearchEpochDate, ElasticsearchStringDate, EnumConversions, ScriptBuilder}
import org.elasticsearch.common.io.stream.ByteBufferStreamInput
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.histogram.{DateHistogramAggregationBuilder, DateHistogramInterval}
import org.elasticsearch.search.aggregations.bucket.histogram.{ExtendedBounds => ESExtendedBounds}

import scala.collection.JavaConverters._

object DateHistogramBuilder {
  def apply(agg: DateHistogramAggregation): DateHistogramAggregationBuilder = {
    val builder = AggregationBuilders.dateHistogram(agg.name)
    agg.extendedBounds.foreach {
      case DoubleExtendedBounds(min, max) =>
        builder.extendedBounds(new ESExtendedBounds(min.toLong, max.toLong))

      case LongExtendedBounds(min, max) =>
        builder.extendedBounds(new ESExtendedBounds(min, max))

      case DateExtendedBounds(ElasticsearchStringDate(min), ElasticsearchStringDate(max)) =>
        builder.extendedBounds(new ESExtendedBounds(min, max))

      case DateExtendedBounds(ElasticsearchEpochDate(min), ElasticsearchEpochDate(max)) =>
        builder.extendedBounds(new ESExtendedBounds(min, max))

      case DateExtendedBounds(_, _) =>
        sys.error("Extendend bounds must be both strings or longs when using TCPClient")
        // TCP Client ExtendedBounds constructors don't support mixed string/long bounds
//      case DateExtendedBounds(ElasticsearchStringDate(min), ElasticsearchEpochDate(max)) =>
//        val minBytes = min.getBytes("UTF-8")
//        val buf = ByteBuffer.allocate(java.lang.Long.BYTES + java.lang.Integer.BYTES + minBytes.length + 4)
//        buf.put(Array[Byte](0, 1))
//        buf.putLong(max)
//        buf.put(1: Byte)
//        buf.putInt(minBytes.length)
//        buf.put(minBytes)
//        buf.put(0: Byte)
//        buf.flip()
//
//        builder.extendedBounds(new ESExtendedBounds(new ByteBufferStreamInput(buf)))
//
//      case DateExtendedBounds(ElasticsearchEpochDate(min), ElasticsearchStringDate(max)) =>
//        val maxBytes = max.getBytes("UTF-8")
//        val buf = ByteBuffer.allocate(java.lang.Long.BYTES + java.lang.Integer.BYTES + maxBytes.length + 4)
//        buf.put(1: Byte)
//        buf.putLong(min)
//        buf.put(Array[Byte](0, 0, 1 ))
//        buf.putInt(maxBytes.length)
//        buf.put(maxBytes)
//        buf.flip()
//
//        builder.extendedBounds(new ESExtendedBounds(new ByteBufferStreamInput(buf)))
    }
    agg.field.foreach(builder.field)
    agg.format.foreach(builder.format)
    agg.interval.map(_.interval).map(new DateHistogramInterval(_)).foreach(builder.dateHistogramInterval)
    agg.minDocCount.foreach(builder.minDocCount)
    agg.missing.foreach(builder.missing)
    agg.offset.foreach(builder.offset)
    agg.order.map(EnumConversions.histogramOrder).foreach(builder.order)
    agg.script.map(ScriptBuilder.apply).foreach(builder.script)
    agg.timeZone.foreach(builder.timeZone)
    SubAggsFn(builder, agg.subaggs)
    agg.script.map(ScriptBuilder.apply).foreach(builder.script)
    if (agg.metadata.nonEmpty) builder.setMetaData(agg.metadata.asJava)
    builder
  }
}
