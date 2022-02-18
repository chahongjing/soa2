package com.zjy.mvc.common.stratory.close;

import com.zjy.mvc.common.stratory.ActionEvent;
import com.zjy.mvc.common.stratory.BaseEventHandler;
import com.zjy.mvc.common.stratory.EventHandlerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CloseHandler extends BaseEventHandler<CloseParam, CloseResult> {

    @Override
    public EventHandlerType getEventEnum() {
        return EventHandlerType.CLOSE;
    }

    @Override
    public void perpare(ActionEvent<CloseParam, CloseResult> event) {
        super.perpare(event);
    }

    @Override
    public boolean check(ActionEvent<CloseParam, CloseResult> event) {
        return super.check(event);
    }

    @Override
    public void beforeDo(ActionEvent<CloseParam, CloseResult> event) {
        super.beforeDo(event);
    }

    @Override
    public void opDo(ActionEvent<CloseParam, CloseResult> event) {
        super.opDo(event);
    }

    @Override
    public void afterDo(ActionEvent<CloseParam, CloseResult> event) {
        super.afterDo(event);
        log.info("CloseHandler.afterDo");
        CloseResult closeResult = new CloseResult();
        closeResult.setResult(this.getClass().getName());
        event.setResult(closeResult);
    }
}
