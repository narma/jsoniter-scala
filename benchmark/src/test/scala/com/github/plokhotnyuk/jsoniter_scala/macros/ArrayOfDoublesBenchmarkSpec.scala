package com.github.plokhotnyuk.jsoniter_scala.macros

class ArrayOfDoublesBenchmarkSpec extends BenchmarkSpecBase {
  val benchmark = new ArrayOfDoublesBenchmark
  
  "DoubleArrayBenchmark" should {
    "deserialize properly" in {
      benchmark.readCirce().deep shouldBe benchmark.obj.deep
      benchmark.readJacksonScala().deep shouldBe benchmark.obj.deep
      benchmark.readJsoniterScala().deep shouldBe benchmark.obj.deep
      benchmark.readPlayJson().deep shouldBe benchmark.obj.deep
    }
    "serialize properly" in {
      toString(benchmark.writeCirce()) shouldBe benchmark.jsonString
      toString(benchmark.writeJacksonScala()) shouldBe benchmark.jsonString
      toString(benchmark.writeJsoniterScala()) shouldBe benchmark.jsonString
      toString(benchmark.preallocatedBuf, benchmark.writeJsoniterScalaPrealloc()) shouldBe benchmark.jsonString
      //FIXME: Play serializes doubles in different format than toString: 0.0 as 0, 7.0687002407403325E18 as 7068700240740332500
      //toString(benchmark.writePlayJson()) shouldBe benchmark.jsonString
    }
  }
}