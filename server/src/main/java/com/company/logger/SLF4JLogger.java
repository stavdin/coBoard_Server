package com.company.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class SLF4JLogger {
    public static final Logger logger = LoggerFactory.getLogger("SampleLogger");
    public static void main(String [] args){
        logger.info("");
    }
}
