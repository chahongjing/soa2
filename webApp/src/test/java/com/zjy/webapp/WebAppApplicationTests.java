package com.zjy.webapp;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.runner.RunnerException;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebAppApplicationTests {

    /**
     * 性能测试
     * @throws RunnerException
     */
    @Test
    void contextLoads() throws RunnerException {
        LinkedListIterationBenchMark.main(null);
    }

}
