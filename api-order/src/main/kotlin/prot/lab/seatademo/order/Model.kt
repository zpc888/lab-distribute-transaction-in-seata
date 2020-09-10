package prot.lab.seatademo.order

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.util.*
import prot.lab.seatademo.account.*;
import prot.lab.seatademo.inventory.InventoryRecord
import prot.lab.seatademo.inventory.OutboundOrder
import prot.lab.seatademo.inventory.RecordType

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Order (var orderId: Long?, val accountId: Long, var orderDate: Date?, val items: List<OrderItem>) {
//    @get:JsonIgnore
    val totalPrice: BigDecimal
        get()  { return items.map(OrderItem::amount).reduce { amt1, amt2 -> amt1.add(amt2) } }

    @get:JsonIgnore
    val transaction: Transaction
        get() {
           val amt = items.map { it.orderUnitPrice.multiply( BigDecimal(it.orderQuantity)) }
                   .reduce { acc, bigDecimal ->  acc.add(bigDecimal) }
            return Transaction(
                    txType = TxType.PURCHASE
                    , txAmount = amt
                    , refId = (orderId?.toString() ?: ""))
        }

    @get:JsonIgnore
    val outboundOrder: OutboundOrder
        get() {
            val records = items.map {
                InventoryRecord(it.productId, RecordType.OUTBOUND, it.orderQuantity, it.orderUnitPrice)
            }
            return OutboundOrder(this.orderId!!, records)
        }
}

data class OrderItem(var itemId: Long?, var orderId: Long?
                       , val productId: Long = 0L, val orderQuantity: Int, val orderUnitPrice: BigDecimal) {
    // computed property - get is must for jsonIgnore since amount can be field, setter, getter, constructor in kotlin
    @get:JsonIgnore
    val amount: BigDecimal
        get() { return if (orderQuantity == 1) orderUnitPrice else orderUnitPrice.multiply(BigDecimal(orderQuantity))}
}

