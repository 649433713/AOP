package yinywf.springbootdemo.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import yinywf.springbootdemo.dto.OrderDTO;

/**
 * @author yinywf
 * Created on 2017/10/15
 */
public interface OrderService {

    /**创建订单*/
    OrderDTO create(OrderDTO orderDTO);

    /**查询单个订单*/
    OrderDTO findOne(String orderId);

    /**查询订单列表*/
    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);

    /**取消订单*/
    OrderDTO cancel(OrderDTO orderDTO);

    /**完结订单*/
    OrderDTO finish(OrderDTO orderDTO);

    /**支付订单*/
    OrderDTO paid(OrderDTO orderDTO);

    /**查询订单列表*/
    Page<OrderDTO> findList(Pageable pageable);


}
