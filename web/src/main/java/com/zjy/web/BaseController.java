package com.zjy.web;

import com.zjy.service.common.MyCustomDateEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Date;

/**
 * TODO
 */
public class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @InitBinder
    public void bindingPreparation(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MyCustomDateEditor());
//        binder.registerCustomEditor(ZonedDateTime.class, new MyCustomZonedDateEditor());
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }

}
