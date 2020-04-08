package com.nju.aop.dataobject;

import com.nju.aop.utils.excel.ExcelCell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * @author yinywf
 * Created on 2019-11-06
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Edge {
    @Id
    @ExcelCell("KER-ID")
    private Integer id;
    @ExcelCell("Source-ID")
    private Integer sourceId;
    @ExcelCell("Source-Name")
    private String sourceTitle;
    @ExcelCell("Target-ID")
    private Integer targetId;
    @ExcelCell("Target-Name")
    private String targetTitle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(sourceId, edge.sourceId) && Objects.equals(targetId, edge.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceId, sourceTitle, targetId, targetTitle);
    }
}
