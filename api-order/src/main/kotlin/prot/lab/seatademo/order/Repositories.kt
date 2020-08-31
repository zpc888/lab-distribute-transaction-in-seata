package prot.lab.seatademo.order

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Param

@Mapper
//@Repository
interface OrderRepository {
    // param1 or order is the parameters name
    @Insert("insert into prot_order (account_id, order_date, total_price) values " +
            "(#{order.accountId}, #{order.orderDate}, #{order.totalPrice})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    fun newOrder(@Param("order") order: Order): Int

    @Insert(value = [
        "<script>",
        "insert into prot_order_item (order_id, prod_id, order_quality, order_unit_price) values ",
        "<foreach collection='orderItems' item='item' separator=','>",
        "( #{item.orderId}, #{item.productId}, #{item.orderQuality}, #{item.orderUnitPrice})",
        "</foreach>",
        "</script>"
    ])
    // *** caveat: keyProperty is must even when key-prop = 'id'
    //     documentation says id is default, no need keyProperty, but it doesn't work after testing
    @Options(useGeneratedKeys = true, keyProperty = "itemId")
    fun insertOrderItems(@Param("orderItems") items: List<OrderItem>): Int
}
