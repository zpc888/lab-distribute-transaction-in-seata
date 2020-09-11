package prot.lab.seatademo.inventory

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*

enum class RecordType {
    INBOUND,
    OUTBOUND;

    fun shouldIncrease() = this == INBOUND
    fun shouldDecrease() = this != INBOUND
}

data class OutboundOrder (val orderId: Long, val items: List<InventoryRecord>) {
}
data class WarehousingEntry (val entryId: Long, val items: List<InventoryRecord>)

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Inventory (val productId: Long, val productName: String
                            , var quantity: Int, var unitPrice: BigDecimal? = null) {
}


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class InventoryRecord (val productId: Long, var recordType: RecordType = RecordType.OUTBOUND
                    , var quantity: Int, var unitPrice: BigDecimal? = null) {
}

interface InventoryApi {
    @PutMapping(path = ["/api-inventory/inventories"])
    fun updateInventory(@RequestBody inventory: InventoryRecord): Inventory
    // reactive-style rest-api transaction doesn't work properly using seata at-mode
//    fun newTranaction(@RequestBody tx: Transaction): Mono<Account>

    @PostMapping(path = ["/api-inventory/outbound-orders"])
    fun newOutboundOrder(@RequestBody outboundOrder: OutboundOrder): List<Inventory>

    @GetMapping(path = ["/api-inventory/inventories"])
    fun getInventories(): List<Inventory>
}
