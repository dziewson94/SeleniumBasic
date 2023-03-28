package com.sii.sup.helper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public  class PropertyHelper {
    public static final String TESTPARAM = "testparam";
    private static Logger logger = LoggerFactory.getLogger(PropertyHelper.class.getSimpleName());
    private Properties allProperties = new Properties();


    PropertyHelper(Properties allProperties) {
        this.allProperties=allProperties;
        allProperties.entrySet().forEach(entry ->
               logger.debug(String.format("Found property:%s value:%s", entry.getKey(), entry.getValue())));
    }



    public int getInt(String key) {
        if (StringUtils.isNumeric(allProperties.getProperty(key))) {
            return Integer.parseInt(allProperties.getProperty(key));
        }
        return -1;
    }

    public String get(String key) {
        logger.debug("Loading property " + key);
        logger.debug("Return value property " + allProperties.getProperty(key));
        return allProperties.getProperty(key);
    }
}
