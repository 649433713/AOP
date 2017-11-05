package yinywf.springbootdemo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品（包含类目）
 * Created by yinywf on 2017/10/13
 */
@Data
public class ProductVO implements Serializable {

    private static final long serialVersionUID = 6622841458420636145L;
    @JsonProperty("name")
    private String categoryName;

    @JsonProperty("type")
    private int categoryType;


    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;

}
