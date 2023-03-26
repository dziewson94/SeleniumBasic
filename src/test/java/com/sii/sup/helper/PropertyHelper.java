package com.sii.sup.helper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class PropertyHelper {
    public static final String TESTPARAM = "testparam";
    private static Logger logger = LoggerFactory.getLogger(PropertyHelper.class.getSimpleName());
    private Properties allProperties = new Properties();


    public PropertyHelper(String className) {
        logger.info("Initializing properties configuration for " + className);
        initializeProperties(className);
    }


    private void initializeProperties(String className) {
        switch (className) {
            case ("AccordionTest") -> allProperties = FileHelper.readAccordionProperties();
            case ("FormTest") -> allProperties = FileHelper.readFormProperties();
            case ("AlertTest") -> allProperties = FileHelper.readAlertProperties();
            case ("TableTest") -> allProperties = FileHelper.readTableProperties();
            case ("WindowTest") -> allProperties = FileHelper.readWindowProperties();
            case ("IframeTest") -> allProperties = FileHelper.readIframeProperties();
            case ("MenuTest") -> allProperties = FileHelper.readMenuProperties();
        }
        allProperties.entrySet().forEach(entry ->
                logger.info(String.format("FOUND property:%s value:%s", entry.getKey(), entry.getValue())));

    }

    public int getInt(String key) {
        if (StringUtils.isNumeric(allProperties.getProperty(key))) {
            return Integer.parseInt(allProperties.getProperty(key));
        }
        return -1;
    }

    public String get(String key) {
        logger.info("Loading property " + key);
        logger.info("Return value property " + allProperties.getProperty(key));
        return allProperties.getProperty(key);
    }
}
