
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.clients.consumer.{KafkaConsumer, ConsumerRecords}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.Pipeline
import java.util.{Collections,Properties}

object KafkaScalaProducer {
  def main(args: Array[String]): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    // Specify the topic
    val topic = "topic name"

    for (i <- 1 to 10) {
      val record = new ProducerRecord[String, String](topic, s"key_$i", s"message_$i")
      producer.send(record)
      println(s"Sent message: key_$i -> message_$i")
    }

    producer.close()
  }
}

object KafkaScalaConsumer {
  def main(args: Array[String]): Unit = {

    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("group.id", "scala-consumer-group")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

    val consumer = new KafkaConsumer[String, String](props)

    val topic = "your_topic_name"
    consumer.subscribe(Collections.singletonList(topic))

    while (true) {
      val records: ConsumerRecords[String, String] = consumer.poll(1000)
      records.forEach { record =>
        println(s"Received message: key = ${record.key()}, value = ${record.value()}")
      }
    }

    consumer.close()
  }
}


object Main {
  def main(args: Array[String]): Unit = {
    //IDK man
  }
}
