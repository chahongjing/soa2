package com.zjy.mvc.common.stratory.create2;

import com.zjy.mvc.common.stratory.ActionEvent;
import com.zjy.mvc.common.stratory.BaseEventHandler;
import com.zjy.mvc.common.stratory.EventHandlerType;
import com.zjy.mvc.common.stratory.create.CreateParam;
import com.zjy.mvc.common.stratory.create.CreateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateHandler2 extends BaseEventHandler<CreateParam, CreateResult> {
    @Override
    public boolean matches(CreateParam actionParam) {
        return "2".equals(actionParam.getType());
    }

    @Override
    public EventHandlerType getEventEnum() {
        return EventHandlerType.CREATE;
    }

    @Override
    public void perpare(ActionEvent<CreateParam, CreateResult> event) {
        super.perpare(event);
        log.info("CreateHandler2.perpare");
    }

    @Override
    public boolean check(ActionEvent<CreateParam, CreateResult> event) {
        boolean result = super.check(event);
        log.info("CreateHandler2.check");
        return result;
    }

    @Override
    public void beforeDo(ActionEvent<CreateParam, CreateResult> event) {
        super.beforeDo(event);
        log.info("CreateHandler2.beforeDo");
    }

    @Override
    public void opDo(ActionEvent<CreateParam, CreateResult> event) {
        super.opDo(event);
        log.info("CreateHandler2.opDo");
    }

    @Override
    public void afterDo(ActionEvent<CreateParam, CreateResult> event) {
        super.afterDo(event);
        log.info("CreateHandler2.afterDo");
        CreateResult createResult = new CreateResult();
        createResult.setResult(this.getClass().getName());
        event.setResult(createResult);
    }
}
