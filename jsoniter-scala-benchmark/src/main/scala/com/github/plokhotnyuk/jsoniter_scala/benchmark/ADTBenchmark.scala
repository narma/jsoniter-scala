package com.github.plokhotnyuk.jsoniter_scala.benchmark

import java.nio.charset.StandardCharsets.UTF_8

import com.avsystem.commons.serialization.flatten
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import com.rallyhealth.weepickle.v1.implicits.{discriminator, key}

@discriminator("type")
@flatten("type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(Array(
  new Type(value = classOf[X], name = "X"),
  new Type(value = classOf[Y], name = "Y"),
  new Type(value = classOf[Z], name = "Z")))
sealed trait ADTBase extends Product with Serializable

@key("X")
case class X(a: Int) extends ADTBase

@key("Y")
case class Y(b: String) extends ADTBase

@key("Z")
case class Z(l: ADTBase, r: ADTBase) extends ADTBase

abstract class ADTBenchmark extends CommonParams {
  var obj: ADTBase = Z(X(1), Y("VVV"))
  var jsonString1: String = """{"type":"Z","l":{"type":"X","a":1},"r":{"type":"Y","b":"VVV"}}"""
  var jsonString2: String = """{"l":{"a":1,"type":"X"},"r":{"b":"VVV","type":"Y"},"type":"Z"}"""
  var jsonBytes: Array[Byte] = jsonString1.getBytes(UTF_8)
  var preallocatedBuf: Array[Byte] = new Array(jsonBytes.length + 100/*to avoid possible out of bounds error*/)
}