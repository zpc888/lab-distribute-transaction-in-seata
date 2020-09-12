package prot.lab.seatademo.inventory

import org.apache.ibatis.annotations.*
import org.apache.ibatis.type.JdbcType


@Mapper
//@Repository
interface InventoryRepository {

    // param1 or order is the parameters name
    @Update("<script> update prot_inventory " +
//            "<if test=\"record.recordType.name().equals('OUTBOUND')\"> set prod_quantity = prod_quantity - #{record.quantity} </if>" +
//            "<if test=\"record.recordType.name().equals('INBOUND')\"> set prod_quantity = prod_quantity + #{record.quantity} </if> " +
            "<if test=\"record.recordType.shouldDecrease()\"> set prod_quantity = prod_quantity - #{record.quantity} </if>" +
            "<if test=\"record.recordType.shouldIncrease()\"> set prod_quantity = prod_quantity + #{record.quantity} </if> " +
            "where prod_id = #{record.productId} </script> ")
    fun updateInventory(@Param("record") record: InventoryRecord): Int

    @Select(InventoryRepository.SELECT_INVENTORY_FOR_PROD_ID)
    @Results(id = "InventoryMapping", value = [
        Result(id = true, column = "prod_id", property = "productId", jdbcType = JdbcType.BIGINT),
        Result(column = "prod_name", property = "productName", jdbcType = JdbcType.VARCHAR),
        Result(column = "prod_quantity", property = "quantity", jdbcType = JdbcType.INTEGER),
        Result(column = "prod_unit_price", property = "unitPrice", jdbcType = JdbcType.DECIMAL)
    ])
    fun selectInventoryByProductId(@Param("productId") productId: Long): Inventory


    @Select(InventoryRepository.SELECT_INVENTORY)
    @ResultMap("InventoryMapping")
    fun getInventories(): List<Inventory>

    @Update("update prot_inventory set prod_quantity = 60" )
    fun resetInventory(): Int

    companion object {
        const val SELECT_INVENTORY = "select i.prod_id, p.prod_name, i.prod_quantity, i.prod_unit_price from prot_inventory i join prot_product p on i.prod_id = p.prod_id"
        const val SELECT_INVENTORY_FOR_PROD_ID = "$SELECT_INVENTORY where i.prod_id = #{productId}"
    }

//    @Insert(
//        "insert into prot_transaction (account_id, tx_date, tx_type, tx_amount, ref_id) " +
//                " values ( #{tx.accountId}, #{tx.txDate}, #{tx.txType}, #{tx.txAmount}, #{tx.refId})"
//    )
//    // *** caveat: keyProperty is must even when key-prop = 'id'
//    //     documentation says id is default, no need keyProperty, but it doesn't work after testing
//    @Options(useGeneratedKeys = true, keyProperty = "txId")
//    fun insertTransaction(@Param("tx") tx: Transaction): Int

//    @Select("select account_id, account_name, account_balance from prot_account where account_id = #{param1}")
//    @Results( value = [
//        Result(id = true, column = "account_id", property = "id", jdbcType = JdbcType.BIGINT),
//        Result(column = "account_name", property = "name", jdbcType = JdbcType.VARCHAR),
//        Result(column = "account_balance", property = "balance", jdbcType = JdbcType.DECIMAL)
//    ])
//    fun selectAccountById(accountId: Long): Account
}
