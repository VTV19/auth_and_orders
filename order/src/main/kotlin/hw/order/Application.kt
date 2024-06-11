package hw.order

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["bll", "dal", "api"])
class OrderApplication

fun main(args: Array<String>) {
	runApplication<OrderApplication>(*args)
}
