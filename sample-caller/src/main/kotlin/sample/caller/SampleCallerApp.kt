package sample.caller

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SampleServiceApp

fun main(args: Array<String>) {
    SpringApplication.run(SampleServiceApp::class.java, *args)
}