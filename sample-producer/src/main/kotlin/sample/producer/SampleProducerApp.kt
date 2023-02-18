package sample.producer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SampleProducerApp

fun main(args: Array<String>) {
    runApplication<SampleProducerApp>(*args)
}