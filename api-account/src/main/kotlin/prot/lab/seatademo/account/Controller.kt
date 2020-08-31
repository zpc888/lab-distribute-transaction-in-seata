package prot.lab.seatademo.account

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
import java.lang.IllegalArgumentException
import java.util.*

@RestController
//@RequestMapping("/api-account")
class AccountController: AccountApi {
    @Autowired
    lateinit var accountService: AccountingService

//    @Transactional
    @GlobalTransactional(rollbackFor = [Exception::class])
//    override fun newTranaction(@RequestBody tx: Transaction): Mono<Account> {
    override fun newTranaction(accountId:Long, tx: Transaction): Account {
        log.debug(">>> v2 tx: {}", tx)
        if (tx.accountId == null) {
            tx.accountId = accountId;
        } else if (tx.accountId != accountId) {
            throw IllegalArgumentException("conflict account-id ${tx.accountId} != ${accountId}")
        }
        tx.txDate = tx.txDate ?: Date()
        val acctBeforeTx = accountService.getAccountById(tx.accountId!!)
        val account = accountService.addTransaction(tx)
        account.lastTransaction = tx
        log.debug("<<< newTx: {} inserted and account is {} from {}"
                , tx, account, acctBeforeTx)
//        return Mono.just(account)
        return account
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
