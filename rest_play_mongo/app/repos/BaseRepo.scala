package repos

import models._

import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromRegistries, fromProviders}

abstract class BaseRepo {

  val codecRegistry = fromRegistries(fromProviders(classOf[Widget]), DEFAULT_CODEC_REGISTRY)

  val mongoClient: MongoClient = MongoClient("mongodb://localhost:27017")
  val database: MongoDatabase = mongoClient.getDatabase("test").withCodecRegistry(codecRegistry)

}
