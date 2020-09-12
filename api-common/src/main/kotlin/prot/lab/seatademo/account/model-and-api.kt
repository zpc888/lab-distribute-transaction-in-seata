package prot.lab.seatademo.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.math.BigDecimal
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Account (var id: Long?, val name: String, var balance: BigDecimal?) {
    var lastTransaction: Transaction? = null;
}

enum class TxType {
    PURCHASE, DEPOSIT, FEE
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Transaction(var txId: Long? = null, var accountId: Long? = null
                       , var txDate: Date? = null, val txType: TxType, var txAmount: BigDecimal
                       , val refId: String)

interface AccountApi {
    @PostMapping(path = ["/api-account/accounts/{accountId}/transactions"])
    fun newTranaction(@PathVariable accountId: Long, @RequestBody tx: Transaction): Account
    // reactive-style rest-api transaction doesn't work properly using seata at-mode
//    fun newTranaction(@RequestBody tx: Transaction): Mono<Account>

    @GetMapping(path = ["/api-account/accounts/{accountId}"])
    fun getAccount(@PathVariable accountId: Long): Account

    @PostMapping(path = ["/api-account/accounts/{accountId}/reset"])
    fun resetAccount(@PathVariable accountId: Long): Account;
}
