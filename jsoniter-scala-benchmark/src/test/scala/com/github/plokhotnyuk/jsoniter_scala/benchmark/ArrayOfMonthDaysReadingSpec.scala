package com.github.plokhotnyuk.jsoniter_scala.benchmark

class ArrayOfMonthDaysReadingSpec extends BenchmarkSpecBase {
  private val benchmark = new ArrayOfMonthDaysReading {
    setup()
  }
  
  "ArrayOfMonthDaysReading" should {
    "read properly" in {
      benchmark.avSystemGenCodec() shouldBe benchmark.obj
      benchmark.circe() shouldBe benchmark.obj
      benchmark.jacksonScala() shouldBe benchmark.obj
      benchmark.jsoniterScala() shouldBe benchmark.obj
      benchmark.playJson() shouldBe benchmark.obj
      benchmark.sprayJson() shouldBe benchmark.obj
      benchmark.uPickle() shouldBe benchmark.obj
    }
  }
}