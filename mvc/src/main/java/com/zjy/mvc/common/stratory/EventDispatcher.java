package com.zjy.mvc.common.stratory;

import com.zjy.mvc.common.stratory.create.CreateResult;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class EventDispatcher implements ApplicationListener<ActionEvent>, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private final Map<EventHandlerType, List<BaseEventHandler>> actionMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getContext() {
        return applicationContext;
    }

    @Synchronized
    public void register(BaseEventHandler baseEventHandler) {
        EventHandlerType eventType = baseEventHandler.getEventEnum();
        List<BaseEventHandler> list = actionMap.get(eventType);
        if(list == null) {
            list = new ArrayList<>();
            actionMap.put(eventType, list);
        }
        list.add(baseEventHandler);
        Collections.sort(list);
    }

    public <R extends BaseActionResult> R fireAction(BaseActionParam param) {
        ActionEvent event = new ActionEvent(param);
        return (R)fireAction(event);
    }

    public <P extends BaseActionParam, R extends BaseActionResult> R fireAction(ActionEvent<P, R> event) {
        List<BaseEventHandler> actionList = actionMap.get(event.getParam().getEventEnum());
        if(CollectionUtils.isEmpty(actionList)) {
            log.warn("no action found for {}", event.getParam().getEventEnum());
            throw new IllegalArgumentException("unknown event type " + event.getParam().getEventEnum());
        }
        for(BaseEventHandler baseEventHandler : actionList) {
            if(baseEventHandler.matches(event.getParam())) {
                try {
                    return (R)baseEventHandler.execute(event);
                } catch (Exception ex) {
                    log.error("fireAction error!", ex);
                    throw ex;
                }
            }
        }
        throw new UnsupportedOperationException("Not supported for current");
    }

    @Override
    public void onApplicationEvent(ActionEvent event) {
        event.setResult(this.fireAction(event));
    }

    public BaseActionResult publishEvent(BaseActionParam actionParam) {
        ActionEvent event = new ActionEvent(actionParam);
        applicationContext.publishEvent(event);
        return event.getResult();
    }
}
