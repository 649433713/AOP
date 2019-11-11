//package com.nju.aop.dataobject.mapper;
//
//import org.apache.ibatis.annotations.Insert;
//import org.apache.ibatis.annotations.Result;
//import org.apache.ibatis.annotations.Results;
//import org.apache.ibatis.annotations.Select;
//
//import java.util.Map;
//
//
//public interface ProductCategoryMapper {
//
//    @Insert("insert into product_category(category_name,category_type) values(#{categoryName,jdbcType=VARCHAR},#{categoryType,jdbcType=INTEGER")
//    int insertByMap(Map<String, Object> map);
//
//    @Insert("insert into product_category(category_name,category_type) values(#{categoryName,jdbcType=VARCHAR},#{categoryType,jdbcType=INTEGER")
//    int insertByObject(ProductCategory productCategory);
//
//    @Select("select * from product_category where category_type = #{categoryType}")
//    @Results({
//            @Result(column = "category_id",property = "categoryId")
//    })
//    ProductCategory findByCategoryType(Integer categoryType);
//
//}
