package prot.lab.seatademo.order

import feign.Contract
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import reactivefeign.spring.config.EnableReactiveFeignClients

@EnableReactiveFeignClients
@EnableFeignClients
@SpringBootApplication
class OrderApplication

fun main(args: Array<String>) {
	runApplication<OrderApplication>(*args)
}

//@Bean
//fun objectMapperBuilder(): Jackson2ObjectMapperBuilder
//		= Jackson2ObjectMapperBuilder().modulesToInstall(KotlinModule())

@Bean
fun feignContract(): Contract = Contract.Default()
