package com.nikitiuk.javabeansinitializer.annotations.testbeans;

import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.listener.ApplicationListener;
import com.nikitiuk.javabeansinitializer.annotations.annotationtypes.listener.ContextInitialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationListener
public class ListenerBean {

    private static final Logger logger = LoggerFactory.getLogger(ListenerBean.class);

    @ContextInitialized
    public void initializedMethod() {
        logger.info("Listener Method called.");
    }
}
