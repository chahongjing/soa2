package com.zjy.service;

import com.zjy.api.TestApi;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * TODO
 */
//@Service(interfaceClass = TestApi.class)
@Component
@Service
public class TestApiImpl implements TestApi {
    @Override
    public String test() {
        return "zjy11";
    }
}
