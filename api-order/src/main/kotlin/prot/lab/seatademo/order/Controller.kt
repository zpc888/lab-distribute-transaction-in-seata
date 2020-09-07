package prot.lab.seatademo.order

import io.seata.spring.annotation.GlobalTransactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import prot.lab.seatademo.account.Account
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.jvm.javaClass

import prot.lab.seatademo.common.getLogger
import java.math.BigDecimal

@RestController
@RequestMapping("/api-order")
class OrderController {
    @Autowired
    lateinit var orderRepository: OrderRepository
    @Autowired
    lateinit var accountApiClient: AccountApiClient
    @Autowired
    lateinit var inventoryApiClient: InventoryApiClient

    @Value("\${app.account.balance.error.range.from: 40}")
    private var accountBalanceErrorRangeFrom: String = "40"
    @Value("\${app.account.balance.error.range.to: 50}")
    private var accountBalanceErrorRangeTo: String = "50"

//    @Transactional
    @GlobalTransactional(rollbackFor = [Exception::class])
    @PostMapping(path = ["orders"])
    fun newOrder(@RequestBody order: Order): Mono<Order> {
        log.debug(">>> order: {}", order)

        log.debug("Step.1 order >")
        order.orderDate = order.orderDate ?: Date()
        val insertedCnt = orderRepository.newOrder(order)

//        order.items.forEach ( { i -> i.orderId = order.orderId } )    // same as below, like scala
        order.items.forEach { i -> i.orderId = order.orderId }
        val insertedItemsCnt = orderRepository.insertOrderItems(order.items)
        log.trace("---> order: {} after {} order(s) and {} item(s) inserted"
                , order, insertedCnt, insertedItemsCnt)
        log.debug("Step.1 order < OK")

        log.debug("Step.2 account >")
        val acct = accountApiClient.newTranaction(order.accountId, order.transaction)
            log.debug("account latest info: ${acct}")
//    monoAcct.subscribe (
//                { log.debug("--- account: #{it}") }
//                , { ex -> throw ex } )
        log.debug("Step.2 account < OK")

        log.debug("Step.3 inventory >")
        val inventoryList = inventoryApiClient.newOutboundOrder( order.outboundOrder )
        log.debug("inventory latest info: ${inventoryList}")
        log.debug("Step.3 inventory < OK")

        log.debug("Step.4 ensure account still valid >")
        ensureAccountValid(acct)
        log.debug("Step.4 yes account still valid < OK")

        log.debug("<<< order: {} all done", order)
        return Mono.just(order)
    }

    private fun ensureAccountValid(acct: Account) {
        if (acct.balance!! < BigDecimal(accountBalanceErrorRangeTo)
                && acct.balance!! > BigDecimal(accountBalanceErrorRangeFrom)) {
            throw RuntimeException("account balance is not allowed between ${accountBalanceErrorRangeFrom} and ${accountBalanceErrorRangeTo} (exclusive), but it is [${acct.balance}] now")
        }

    }

    @GetMapping(path = ["server-time-teller"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
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
