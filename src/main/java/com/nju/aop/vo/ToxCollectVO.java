package com.nju.aop.vo;

import com.nju.aop.dataobject.Event;
import com.nju.aop.dataobject.Tox;
import com.nju.aop.dto.EventWithDistance;
import lombok.Data;

import java.util.List;

/**
 * @author Euterpe on 2020/6/22
 */
@Data
public class ToxCollectVO {
    private Event KE;
    private List<EventWithDistance> AOs;
    private Tox tox;

}
