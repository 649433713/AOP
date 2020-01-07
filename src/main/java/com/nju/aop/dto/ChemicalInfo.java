package com.nju.aop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

/**
 * created by Kimone
 * date 2020/1/7
 */
@Data
@AllArgsConstructor
public class ChemicalInfo {
    private String cas;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChemicalInfo that = (ChemicalInfo) o;
        return cas.equals(that.cas) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cas, name);
    }
}
