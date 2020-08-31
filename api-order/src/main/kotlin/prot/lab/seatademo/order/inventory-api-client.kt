package prot.lab.seatademo.order

import org.springframework.cloud.openfeign.FeignClient
import prot.lab.seatademo.common.getLogger
import prot.lab.seatademo.inventory.Inventory
import prot.lab.seatademo.inventory.InventoryApi
import prot.lab.seatademo.inventory.InventoryRecord
//import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono

//@ReactiveFeignClient(name = "inventory-api", fallback = InventoryApiFallback::class)
@FeignClient(name = "inventory-api")
interface InventoryApiClient: InventoryApi

//class InventoryApiFallback: InventoryApiClient {
//    override fun updateInventory(inventory: InventoryRecord): Inventory {
//        log.error("Fail in inventory sub-system")
//        throw RuntimeException("Fail in inventory sub-system");
////        return Mono.empty()
//    }
//
//    companion object {
//        //      private val log = getLogger(OrderController::class.java)        // 1. need to change class-name everytime; 2. singleton -- not static
//        @JvmStatic
//        @Suppress("JAVA_CLASS_ON_COMPANION")
//        private val log = getLogger(javaClass.enclosingClass)
//    }
//}
