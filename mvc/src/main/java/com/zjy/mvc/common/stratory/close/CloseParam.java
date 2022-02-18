package com.zjy.mvc.common.stratory.close;

import com.zjy.mvc.common.stratory.BaseActionParam;
import com.zjy.mvc.common.stratory.EventHandlerType;

public class CloseParam implements BaseActionParam {
    @Override
    public EventHandlerType getEventEnum() {
        return EventHandlerType.CLOSE;
    }
}
