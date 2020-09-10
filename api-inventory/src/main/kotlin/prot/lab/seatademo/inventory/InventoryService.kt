package prot.lab.seatademo.inventory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import prot.lab.seatademo.inventory.InventoryRecord
import prot.lab.seatademo.inventory.InventoryRepository
import java.lang.RuntimeException
import java.math.BigDecimal

@Service
class InventoryService {
    @Autowired
    lateinit var inventoryRepository: InventoryRepository

    fun updateInventory(record: InventoryRecord): Inventory {
        val count = inventoryRepository.updateInventory(record)
        if (count == 1) {
            // ok
            val ret = inventoryRepository.selectInventoryByProductId(record.productId)
            if (ret.quantity < 0) {
                throw RuntimeException("Negative inventory for product id: ${record.productId}")
            } else {
                return ret;
            }
        } else {
            throw RuntimeException("No inventory for product id: ${record.productId}")
        }
    }
}
