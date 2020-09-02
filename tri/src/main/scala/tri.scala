import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

import org.apache.log4j.Logger
import org.apache.log4j.Level



object tri {
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    
    // a file on your system
    val logFile = "/home/hadoop/zxt/zxtSpark/tri/log.txt"
    val conf = new SparkConf().setAppName("tri")
    val sc = new SparkContext(conf)

    // load the graph, be sure set the canonicalOrientation to be true and the graph to be partitioned using Graph.partitionBy.
    // load the input file from hadoop file system
    var graph = GraphLoader.edgeListFile(sc, "/zxt/input/triTest.txt",true).partitionBy(PartitionStrategy.RandomVertexCut)
    
    // the computing start
    val start = System.nanoTime
    
    
    var triCounts = graph.triangleCount().vertices.collect();
    //triCounts.foreach{println}
    var triangle_total = 0;

    // sum up the number of triangles
    for(i<-0 to (triCounts.length-1))
    {
        val temp = triCounts(i)
        triangle_total += temp._2
    }


    println("Total number of triangles are ..")
    //since each triangle is counted third times, the real number of triangles should be divided by 3
    println(triangle_total/3)

    val duration = (System.nanoTime - start)/ 1e9d
    println(duration)
    sc.stop()
  }
}
