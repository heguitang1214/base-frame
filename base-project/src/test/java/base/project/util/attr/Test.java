package base.project.util.attr;

import com.alibaba.fastjson.JSON;
import com.tang.project.utils.ClassUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 类动态添加属性测试
 *
 * @date 2020/10/26
 */
public class Test {

    public static void main(String[] args) {
        Order order = new Order();
        order.setId(1);
        order.setName("order1");
        List<OrderDetail> orderDetailList = new ArrayList<>();


        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderid(1);
        orderDetail.setOrderPrice("1USD");
        orderDetail.setOrderSku("Sku1");

        orderDetailList.add(orderDetail);

        OrderDetail orderDetail2 = new OrderDetail();
        orderDetail2.setOrderid(1);
        orderDetail2.setOrderPrice("2USD");
        orderDetail2.setOrderSku("Sku2");
        orderDetailList.add(orderDetail2);

        try {
            HashMap<String, Class<?>> addMap = new HashMap<>();
            HashMap<String, Object> addValMap = new HashMap<>();
            addMap.put("orderDetail", Class.forName("java.util.List"));
            addMap.put("testAttr", Class.forName("java.lang.String"));
            addValMap.put("orderDetail", orderDetailList);
            addValMap.put("testAttr", "这是用来测试的一个属性");
            Object obj2 = new ClassUtil().dynamicClass(order, addMap, addValMap);

            System.out.println(JSON.toJSONString(obj2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
