import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, ProducerConfig}
import org.apache.kafka.clients.consumer.{KafkaConsumer, ConsumerConfig, ConsumerRecords}
import java.util.{Properties, Collections}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.jdk.CollectionConverters._

object Main {

  def main(args: Array[String]): Unit = {
    val topic = "demo-topic"

    val producerFuture = Future { runProducer(topic) }
    val consumerFuture = Future { runConsumer(topic) }

    producerFuture.foreach(_ => println("Producer finished"))
    consumerFuture.foreach(_ => println("Consumer finished"))
  }

  def runProducer(topic: String): Unit = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    try {
      for (i <- 1 to 10) {
        val record = new ProducerRecord[String, String](topic, s"key-$i", s"value-$i")
        producer.send(record)
        println(s"Produced: key-$i, value-$i")
        Thread.sleep(500)
      }
    } finally {
      producer.close()
    }
  }

  def runConsumer(topic: String): Unit = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "example-group")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val consumer = new KafkaConsumer[String, String](props)
    consumer.subscribe(Collections.singletonList(topic))

    try {
      while (true) {
        val records: ConsumerRecords[String, String] = consumer.poll(java.time.Duration.ofMillis(1000))
        for (record <- records.asScala) {
          println(s"Consumed: key=${record.key()}, value=${record.value()}, offset=${record.offset()}")
        }
      }
    } finally {
      consumer.close()
    }
  }
}

