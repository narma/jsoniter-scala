package com.github.plokhotnyuk.jsoniter_scala.benchmark

import java.util.Base64
import com.github.plokhotnyuk.jsoniter_scala.benchmark.BitMask.toBitMask
import io.circe.Decoder._
import io.circe.Encoder._
import io.circe._
import io.circe.generic.extras._
import io.circe.generic.extras.decoding.UnwrappedDecoder
import io.circe.generic.extras.encoding.UnwrappedEncoder
import io.circe.generic.extras.semiauto._
import scala.collection.immutable.{BitSet, IntMap}
import scala.collection.mutable
import scala.util.Try

object CirceEncodersDecoders {
  val printer: Printer = Printer.noSpaces.copy(dropNullValues = true, reuseWriters = true, predictSize = true)
  val prettyPrinter: Printer = Printer.spaces2.copy(dropNullValues = true, reuseWriters = true, predictSize = true)
  val escapingPrinter: Printer = printer.copy(escapeNonAscii = true)
  implicit val config: Configuration = Configuration.default.withDefaults.withDiscriminator("type")
  implicit val adtC3c: Codec[ADTBase] = deriveConfiguredCodec[ADTBase]
  implicit val anyRefsC3c: Codec[AnyRefs] = deriveConfiguredCodec[AnyRefs]
  implicit val anyValsC3c: Codec[AnyVals] = {
    implicit def valueClassEncoder[A <: AnyVal : UnwrappedEncoder]: Encoder[A] = implicitly

    implicit def valueClassDecoder[A <: AnyVal : UnwrappedDecoder]: Decoder[A] = implicitly

    deriveConfiguredCodec[AnyVals]
  }
  val (base64D5r: Decoder[Array[Byte]], base64E5r: Encoder[Array[Byte]]) =
    (Decoder.decodeString.map[Array[Byte]](Base64.getDecoder.decode),
      Encoder.encodeString.contramap[Array[Byte]](Base64.getEncoder.encodeToString))
  implicit val bidRequestC3c: Codec[OpenRTB.BidRequest] = {
    import io.circe.generic.extras.auto._

    deriveConfiguredCodec[OpenRTB.BidRequest]
  }
  implicit val bigIntE5r: Encoder[BigInt] = encodeJsonNumber
    .contramap(x => JsonNumber.fromDecimalStringUnsafe(new java.math.BigDecimal(x.bigInteger).toPlainString))
  implicit val (bitSetD5r: Decoder[BitSet], bitSetE5r: Encoder[BitSet]) =
    (Decoder.decodeArray[Int].map(arr => BitSet.fromBitMaskNoCopy(toBitMask(arr, Int.MaxValue /* WARNING: don't do this for open-systems */))),
      Encoder.encodeSet[Int].contramapArray((m: BitSet) => m))
  implicit val (mutableBitSetD5r: Decoder[mutable.BitSet], mutableBitSetE5r: Encoder[mutable.BitSet]) =
    (Decoder.decodeArray[Int].map(arr => mutable.BitSet.fromBitMaskNoCopy(toBitMask(arr, Int.MaxValue /* WARNING: don't do this for open-systems */))),
      Encoder.encodeSeq[Int].contramapArray((m: mutable.BitSet) => m.toVector))
  implicit val distanceMatrixC3c: Codec[GoogleMapsAPI.DistanceMatrix] = {
    import io.circe.generic.auto._

    deriveConfiguredCodec[GoogleMapsAPI.DistanceMatrix]
  }
  implicit val extractFieldsC3c: Codec[ExtractFields] = deriveConfiguredCodec[ExtractFields]
  implicit val geoJSONC3c: Codec[GeoJSON.GeoJSON] = {
    implicit val simpleGeometryC3c: Codec[GeoJSON.SimpleGeometry] = deriveConfiguredCodec[GeoJSON.SimpleGeometry]
    implicit val geometryC3c: Codec[GeoJSON.Geometry] = deriveConfiguredCodec[GeoJSON.Geometry]
    implicit val simpleGeoJSONC3c: Codec[GeoJSON.SimpleGeoJSON] = deriveConfiguredCodec[GeoJSON.SimpleGeoJSON]
    deriveConfiguredCodec[GeoJSON.GeoJSON]
  }
  implicit val (intMapD5r: Decoder[IntMap[Boolean]], intMapE5r: Encoder[IntMap[Boolean]]) =
    (Decoder.decodeMap[Int, Boolean].map(_.foldLeft(IntMap.empty[Boolean])((m, p) => m.updated(p._1, p._2))),
      Encoder.encodeMap[Int, Boolean].contramapObject((m: IntMap[Boolean]) => m))
  implicit val (longMapD5r: Decoder[mutable.LongMap[Boolean]], longMapE5r: Encoder[mutable.LongMap[Boolean]]) =
    (Decoder.decodeMap[Long, Boolean].map(_.foldLeft(new mutable.LongMap[Boolean])((m, p) => m += (p._1, p._2))),
      Encoder.encodeMapLike[Long, Boolean, mutable.Map].contramapObject((m: mutable.LongMap[Boolean]) => m))
  implicit val missingRequiredFieldsC3c: Codec[MissingRequiredFields] = deriveConfiguredCodec[MissingRequiredFields]
  implicit val nestedStructsC3c: Codec[NestedStructs] = deriveConfiguredCodec[NestedStructs]
  implicit val (suitD5r: Decoder[Suit], suitE5r: Encoder[Suit]) =
    (decodeString.emap(s => Try(Suit.valueOf(s)).fold[Either[String, Suit]](_ => Left("Suit"), Right.apply)),
      encodeString.contramap[Suit](_.name))
  implicit val suitADTC3c: Codec[SuitADT] = deriveEnumerationCodec[SuitADT]
  implicit val (suitEnumDecoder: Decoder[SuitEnum.Value], suitEnumEncoder: Encoder[SuitEnum.Value]) =
    (decodeEnumeration(SuitEnum), encodeEnumeration(SuitEnum))
  implicit val primitivesC3c: Codec[Primitives] = deriveConfiguredCodec[Primitives]
  implicit val tweetC3c: Codec[TwitterAPI.Tweet] = {
    import io.circe.generic.auto._

    deriveConfiguredCodec[TwitterAPI.Tweet]
  }
}