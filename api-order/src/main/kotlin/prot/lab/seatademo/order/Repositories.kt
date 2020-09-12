package prot.lab.seatademo.order

import org.apache.ibatis.annotations.*

@Mapper
//@Repository
interface OrderRepository {
    // param1 or order is the parameters name
    @Insert("insert into prot_order (account_id, order_date, total_price) values " +
            "(#{order.accountId}, #{order.orderDate}, #{order.totalPrice})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    fun newOrder(@Param("order") order: Order): Int

//    @Delete("delete o, oi from prot_order o inner join prot_order_item oi on o.order_id = oi.order_id where o.account_id = #{accountId}")
//    @Delete("delete from prot_order where account_id = #{accountId}")
    @Delete("delete from prot_order")
    fun deleteOrdersByAccountId(@Param("accountId") accountId: Long): Int

//    @Delete("delete from prot_order_item where order_id in (select order_id from prot_order where account_id = #{accountId})")
    @Delete("delete from prot_order_item")
    fun deleteOrderItemsByAccountId(@Param("accountId") accountId: Long): Int

    @Insert(value = [
        "<script>",
        "insert into prot_order_item (order_id, prod_id, order_quantity, order_unit_price) values ",
        "<foreach collection='orderItems' item='item' separator=','>",
        "( #{item.orderId}, #{item.productId}, #{item.orderQuantity}, #{item.orderUnitPrice})",
        "</foreach>",
        "</script>"
    ])
    // *** caveat: keyProperty is must even when key-prop = 'id'
    //     documentation says id is default, no need keyProperty, but it doesn't work after testing
    @Options(useGeneratedKeys = true, keyProperty = "itemId")
    fun insertOrderItems(@Param("orderItems") items: List<OrderItem>): Int
}
