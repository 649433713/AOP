package com.nju.aop.utils

import java.util

import com.nju.aop.dataobject.Event
import com.nju.aop.dto.EventRank
import org.apache.log4j.{Level, Logger}
import org.apache.spark.graphx.{Graph, GraphLoader, VertexId, VertexRDD}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConverters
import scala.collection.mutable.ArrayBuffer

/**
  * created by Kimone
  * date 2020/1/10
  */
object GraphUtil {
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

  val conf = new SparkConf().setAppName("SimpleApp").setMaster("local")
  val sc = new SparkContext(conf)

  def pagerank(events:List[Event], edges:List[com.nju.aop.dataobject.Edge]): util.ArrayList[EventRank] ={
    val verticesArrBuff = new ArrayBuffer[(VertexId,(String,String))]
    for(v <- events) {
      verticesArrBuff+=((v.getId.toLong,(v.getTitle,v.getChinese)))
    }
    val vertices: RDD[(VertexId,(String,String))] = sc.parallelize(verticesArrBuff)
    val edgeArrBuff = new ArrayBuffer[org.apache.spark.graphx.Edge[Int]]
    for(e <- edges) {
      edgeArrBuff += org.apache.spark.graphx.Edge(e.getSourceId.longValue(),e.getTargetId.longValue())
    }
    val edgeArr = edgeArrBuff.toArray
    val edgesRdd : RDD[org.apache.spark.graphx.Edge[Int]] = sc.parallelize(edgeArr)
    val graph = Graph(vertices,edgesRdd)
    val rank = graph.pageRank(0.001).vertices.collect().toList
    rank.map(t=>{t._1+":"+t._2}).foreach(println(_))
    val eventRankList = new util.ArrayList[EventRank]()
    for(r <- rank) {
      val eventRank = new EventRank(r._1.toInt,r._2)
      eventRankList.add(eventRank)
    }
    eventRankList
  }

  def connectedNodes(filePath:String): util.List[util.List[Int]]  ={
    val graph: Graph[Int,Int] = GraphLoader.edgeListFile(sc,filePath)
    val components: Graph[VertexId, Int] = graph.connectedComponents()
    val vertices: VertexRDD[VertexId] = components.vertices
    val connectedNodes: Seq[Seq[Int]]= vertices.map(x=>(x._2.toInt,Array(x._1.toInt))).reduceByKey((a,b)=>a++b).values.map(x=>x.sorted).map(x=>x.toSeq).collect().toSeq
    val nodesList = new util.ArrayList[util.List[Int]]
    for(nodes <- connectedNodes) {
      nodesList.add(JavaConverters.seqAsJavaList(nodes))
    }
    nodesList
  }
}
