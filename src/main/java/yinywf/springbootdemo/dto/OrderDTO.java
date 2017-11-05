package yinywf.springbootdemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import yinywf.springbootdemo.dataobject.OrderDetail;
import yinywf.springbootdemo.enums.OrderStatusEnum;
import yinywf.springbootdemo.utils.EnumUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yinywf
 * Created on 2017/10/15
 */
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO{

    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;
    private Integer orderStatus;
    private Integer payStatus ;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    List<OrderDetail> orderDetailList;

}
