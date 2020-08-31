package prot.lab.seatademo.account

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class AccountApplication

fun main(args: Array<String>) {
	runApplication<AccountApplication>(*args)
}

//@Bean
//fun objectMapperBuilder(): Jackson2ObjectMapperBuilder
//		= Jackson2ObjectMapperBuilder().modulesToInstall(KotlinModule())