package com.zjy.mvc.common.stratory.create;

import com.zjy.mvc.common.stratory.ActionEvent;
import com.zjy.mvc.common.stratory.BaseEventHandler;
import com.zjy.mvc.common.stratory.EventHandlerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateHandler extends BaseEventHandler<CreateParam, CreateResult> {
    @Override
    public boolean matches(CreateParam actionParam) {
        return "1".equals(actionParam.getType());
    }

    @Override
    public EventHandlerType getEventEnum() {
        return EventHandlerType.CREATE;
    }

    @Override
    public void perpare(ActionEvent<CreateParam, CreateResult> event) {
        super.perpare(event);
        log.info("CreateHandler.perpare");
    }

    @Override
    public boolean check(ActionEvent<CreateParam, CreateResult> event) {
        boolean result = super.check(event);
        log.info("CreateHandler.check");
        return result;
    }

    @Override
    public void beforeDo(ActionEvent<CreateParam, CreateResult> event) {
        super.beforeDo(event);
        log.info("CreateHandler.beforeDo");
    }

    @Override
    public void opDo(ActionEvent<CreateParam, CreateResult> event) {
        super.opDo(event);
        log.info("CreateHandler.opDo");
    }

    @Override
    public void afterDo(ActionEvent<CreateParam, CreateResult> event) {
        super.afterDo(event);
        log.info("CreateHandler.afterDo");
        CreateResult createResult = new CreateResult();
        createResult.setResult(this.getClass().getName());
        event.setResult(createResult);
    }
}
