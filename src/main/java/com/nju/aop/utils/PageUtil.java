package com.nju.aop.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Comparator;
import java.util.List;

/**
 * @author yinywf
 * Created on 2019-7-16
 */
public class PageUtil {

    public static <T> Page<T> listConvertToPage(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public static <T> Page<T> listConvertToPage(List<T> list, Pageable pageable, Comparator comparator) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        List result = list.subList(start, end);
        result.sort(comparator);
        return new PageImpl<T>(result, pageable, list.size());
    }
}
