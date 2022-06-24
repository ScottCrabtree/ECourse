package com.happybrainscience.ecourse.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author theider
 */
public class ProductVersion { 

    private static final String VERSION_PROPERTIES_RESOURCE_NAME = "happybrainscience_application_version.properties";

    public static String getVersionString() {
        InputStream in = ProductVersion.class.getResourceAsStream(VERSION_PROPERTIES_RESOURCE_NAME);
        if(in == null) {
            in = ProductVersion.class.getResourceAsStream("/" + VERSION_PROPERTIES_RESOURCE_NAME);
        }
        if(in == null) {
            return "UNKNOWN";
        } else {
            Properties props = new Properties();
            try {
                props.load(in);
                String text = props.getProperty("happybrainscience.application.version");
                return text;
            } catch (IOException ex) {
                return "UNKNOWN";
            }
        }       
    }

}
