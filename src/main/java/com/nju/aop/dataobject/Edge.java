package com.nju.aop.dataobject;

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
    private Integer id;
    private Integer sourceId;
    private String sourceTitle;
    private Integer targetId;
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
