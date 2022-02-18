package com.zjy.mvc.common.stratory.create;

import com.zjy.mvc.common.stratory.BaseActionParam;
import com.zjy.mvc.common.stratory.EventHandlerType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateParam implements BaseActionParam {
    @Override
    public EventHandlerType getEventEnum() {
        return EventHandlerType.CREATE;
    }

    private String type;
}
