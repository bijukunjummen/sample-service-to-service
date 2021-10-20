package sample.producer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SampleServiceApp

fun main(args: Array<String>) {
    runApplication<SampleServiceApp>(*args)
}