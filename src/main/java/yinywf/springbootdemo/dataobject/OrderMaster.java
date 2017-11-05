package yinywf.springbootdemo.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yinywf
 * Created on 2017/10/11
 */
@Entity
@Table(name = "order_master", schema = "sell")
@Data
@DynamicUpdate
@DynamicInsert
public class OrderMaster implements Serializable {
    private static final long serialVersionUID = 4483345633098439413L;
    @Id
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

}
