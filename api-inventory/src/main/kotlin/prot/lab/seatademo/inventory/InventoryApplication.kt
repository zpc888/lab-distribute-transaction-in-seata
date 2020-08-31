package prot.lab.seatademo.inventory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class InventoryApplication

fun main(args: Array<String>) {
	runApplication<InventoryApplication>(*args)
}

//@Bean
//fun objectMapperBuilder(): Jackson2ObjectMapperBuilder
//		= Jackson2ObjectMapperBuilder().modulesToInstall(KotlinModule())
