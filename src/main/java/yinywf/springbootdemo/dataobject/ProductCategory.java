package yinywf.springbootdemo.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yinywf
 * Created on 2017/10/11
 */
@Entity
@Table(name = "product_category", schema = "sell")
@DynamicUpdate
@DynamicInsert
@Data
public class ProductCategory implements Serializable{

    private static final long serialVersionUID = 3652638601153339838L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int categoryId;
    private String categoryName;
    private int categoryType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public ProductCategory(String categoryName, int categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

    public ProductCategory() {
    }
}
