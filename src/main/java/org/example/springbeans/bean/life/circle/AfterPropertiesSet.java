package org.example.springbeans.bean.life.circle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AfterPropertiesSet {

    private static final Logger log = LoggerFactory.getLogger(AfterPropertiesSet.class);

    public void test(){
        log.info("AfterPropertiesSet -> test()");
    }
}
