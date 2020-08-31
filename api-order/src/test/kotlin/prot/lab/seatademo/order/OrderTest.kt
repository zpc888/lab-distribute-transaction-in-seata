package prot.lab.seatademo.order

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

internal class OrderTest {

    @Test
    fun whenJson_thenOk() {
        val prod10 = OrderItem(0, 0, 10, 4, BigDecimal("4.25"))
        val prod20 = OrderItem(0, 0, 20, 2, BigDecimal("6.00"))
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val order = Order(1, 2, Date(), listOf(prod10, prod20))
        val serialized = mapper.writeValueAsString(order)
        println( serialized )

        val order2 = mapper.readValue(serialized, order.javaClass)
        println( order2 )

        val json = """
{
    "accountId": 1,
    "items": [
        {
            "productId": 10,
            "orderQuality": 4,
            "orderUnitPrice": 4.25
        },
        {
            "productId": 20,
            "orderQuality": 2,
            "orderUnitPrice": 6
        }
    ]
}
        """.trimIndent()

    val order3 = mapper.readValue(json, order.javaClass)
    println( order3 )

    }

}