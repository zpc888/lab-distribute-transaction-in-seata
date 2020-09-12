package prot.lab.seatademo.inventory

import io.seata.spring.annotation.GlobalTransactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalDateTime
import kotlin.jvm.javaClass
import prot.lab.seatademo.common.getLogger
import java.lang.Exception

@CrossOrigin
@RestController
//@RequestMapping("/api-account")
class InventoryController: InventoryApi {
    @Autowired
    lateinit var inventoryService: InventoryService

    @GlobalTransactional(rollbackFor = [Exception::class])
    override fun updateInventory(inventory: InventoryRecord): Inventory {
        log.debug(">>> inventory-tx: {}", inventory)
        val ret = inventoryService.updateInventory(inventory)
        log.debug("<<< after update: {}", ret)
        return ret
    }

    override fun getInventories(): List<Inventory> {
        return inventoryService.getInventories()
    }

    override fun resetInventories(): Int {
        return inventoryService.resetInventories()
    }

    @GlobalTransactional(rollbackFor = [Exception::class])
    override fun newOutboundOrder(outboundOrder: OutboundOrder): List<Inventory> {
        log.debug(">>> outbound order: {}", outboundOrder)
        val ret = outboundOrder.items.map {
            it.recordType = RecordType.OUTBOUND
            inventoryService.updateInventory(it)
        }
        log.debug("<<< after new: {}", ret)
        return ret
    }

    @GetMapping(path = ["/api-account/server-time-teller"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun heartbeat(): Flux<String> = Flux.range(1, 21)
            .delayElements(Duration.ofSeconds(1))
            .map { i ->
                if (i <= 20) "%02d: %s".format(i, LocalDateTime.now())
                else "--- end ---"
            }

    companion object {
        //      private val log = getLogger(OrderController::class.java)        // 1. need to change class-name everytime; 2. singleton -- not static
        @JvmStatic
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass.enclosingClass)
    }
}
