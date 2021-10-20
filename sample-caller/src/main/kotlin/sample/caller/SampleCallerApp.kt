package sample.caller

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SampleCallerApp

fun main(args: Array<String>) {
    runApplication<SampleCallerApp>(*args)
}