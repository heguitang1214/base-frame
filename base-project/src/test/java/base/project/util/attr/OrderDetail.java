package base.project.util.attr;

/**
 * 订单详情
 *
 * @date 2020/10/26
 */
public class OrderDetail {

    private Integer orderid;
    private String orderPrice;
    private String orderSku;

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderSku() {
        return orderSku;
    }

    public void setOrderSku(String orderSku) {
        this.orderSku = orderSku;
    }
}
