package com.zjy.mvc.common.stratory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;


public abstract class BaseEventHandler<P extends BaseActionParam, R extends BaseActionResult> implements Comparable<BaseEventHandler<P, R>>{
    @Resource
    protected EventDispatcher eventDispatcher;

    @PostConstruct
    public void init() {
        eventDispatcher.register(this);
    }

    public boolean matches(P actionParam) {
        return true;
    }

    public R execute(ActionEvent<P, R> event){
        this.perpare(event);
        if(!this.check(event)) {
            throw new RuntimeException("check false");
        }
        this.beforeDo(event);
        this.opDo(event);
        this.afterDo(event);
        return event.getResult();
    }

    @Override
    public final int compareTo(BaseEventHandler other) {
        return other.getOrder() - this.getOrder();
    }

    public int getOrder() { return 0; }

    public abstract EventHandlerType getEventEnum();

    public void perpare(ActionEvent<P, R> event) {
        event.setOpDate(new Date());
        // 获取实例的旧数据
//        event.setOldIns(null);
    }
    public boolean check(ActionEvent<P, R> event){
//        return event.getOldIns() != null;
        return true;
    }
    public void beforeDo(ActionEvent<P, R> event){

    }
    public void opDo(ActionEvent<P, R> event) {

    }
    public void afterDo(ActionEvent<P, R> event){
    }
}
