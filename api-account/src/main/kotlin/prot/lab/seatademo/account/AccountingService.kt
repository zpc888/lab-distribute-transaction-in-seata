package prot.lab.seatademo.account

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.math.BigDecimal

@Service
class AccountingService {
    val zero = BigDecimal(0)

    @Autowired
    lateinit var accountRepository: AccountRepository

    fun getAccountById(id: Long) = accountRepository.selectAccountById(id)

    fun addTransaction(tx: Transaction): Account {
        if (TxType.DEPOSIT != tx.txType) {
            tx.txAmount = tx.txAmount.negate();
        }
        accountRepository.insertTransaction(tx)
        accountRepository.updateAccountBalance(tx)
//        try {
//            Thread.sleep(1000)                // Feign default timeout is 1 second
//        } catch (ex: Exception) {
//        }
        val account = getAccountById(tx.accountId!!)
        account.lastTransaction = tx
        if (account.balance!! < zero) {
            throw RuntimeException("balance cannot be less than zero, but it is [${account.balance}] now")
        }
        return account
    }
}
