package com.sii.sup.helper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public  class PropertyHelper {
    private static final Logger logger = LoggerFactory.getLogger(PropertyHelper.class.getSimpleName());
    private final Properties allProperties;


    PropertyHelper(Properties allProperties) {
        this.allProperties=allProperties;
        allProperties.forEach((key, value) -> logger.debug(String.format("Found property:%s value:%s", key, value)));
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
