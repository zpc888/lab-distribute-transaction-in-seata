package prot.lab.seatademo.account

import org.apache.ibatis.annotations.*
import org.apache.ibatis.type.JdbcType

@Mapper
//@Repository
interface AccountRepository {
    // param1 or order is the parameters name
    @Update("update prot_account set account_balance = account_balance + #{tx.txAmount}" +
            " where account_id = #{tx.accountId}")
    fun updateAccountBalance(@Param("tx") tx: Transaction): Int

    @Insert(
        "insert into prot_transaction (account_id, tx_date, tx_type, tx_amount, ref_id) " +
                " values ( #{tx.accountId}, #{tx.txDate}, #{tx.txType}, #{tx.txAmount}, #{tx.refId})"
    )
    // *** caveat: keyProperty is must even when key-prop = 'id'
    //     documentation says id is default, no need keyProperty, but it doesn't work after testing
    @Options(useGeneratedKeys = true, keyProperty = "txId")
    fun insertTransaction(@Param("tx") tx: Transaction): Int

    @Select("select account_id, account_name, account_balance from prot_account where account_id = #{param1}")
    @Results( value = [
        Result(id = true, column = "account_id", property = "id", jdbcType = JdbcType.BIGINT),
        Result(column = "account_name", property = "name", jdbcType = JdbcType.VARCHAR),
        Result(column = "account_balance", property = "balance", jdbcType = JdbcType.DECIMAL)
    ])
    fun selectAccountById(accountId: Long): Account

    @Update("update prot_account set account_balance = 100" +
            " where account_id = #{accountId}")
    fun resetAccount(@Param("accountId") accountId: Long): Int
}
