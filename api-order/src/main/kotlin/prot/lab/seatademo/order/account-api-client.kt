package prot.lab.seatademo.order

import org.springframework.cloud.openfeign.FeignClient
import prot.lab.seatademo.account.Account
import prot.lab.seatademo.account.AccountApi
import prot.lab.seatademo.account.Transaction
import prot.lab.seatademo.common.getLogger
//import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono

//@ReactiveFeignClient(name = "account-api", fallback = AccountApiFallback::class)
@FeignClient(name = "account-api", fallback = AccountApiFallback::class)
interface AccountApiClient: AccountApi

class AccountApiFallback: AccountApiClient {
    override fun newTranaction(accountId: Long, tx: Transaction): Account {
//        println("send via MQ: #{tx}")
        log.error("Fail in accounting sub-system, and send via MQ: {}", tx)
        throw RuntimeException("Fail in accounting sub-system");
//        return Mono.empty()
    }

    override fun getAccount(accountId: Long): Account {
        throw RuntimeException("Fail in getting account ${accountId} info")
    }

    override fun resetAccount(accountId: Long): Account {
        throw RuntimeException("Fail in reset account ${accountId} info")
    }

    companion object {
        //      private val log = getLogger(OrderController::class.java)        // 1. need to change class-name everytime; 2. singleton -- not static
        @JvmStatic
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val log = getLogger(javaClass.enclosingClass)
    }
}
