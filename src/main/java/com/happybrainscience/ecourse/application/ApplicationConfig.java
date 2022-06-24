package com.happybrainscience.ecourse.application;

import net.lilycode.core.configbundle.ConfigPropertyInt;
import net.lilycode.core.configbundle.ConfigPropertyString;

/**
 *
 * @author timothyheider
 */
public class ApplicationConfig {

    public static ConfigPropertyString DB_HOST = new ConfigPropertyString("com.happybrainscience.db.host");

    public static ConfigPropertyInt    DB_PORT = new ConfigPropertyInt("com.happybrainscience.db.port");

    public static ConfigPropertyString DB_DATABASE = new ConfigPropertyString("com.happybrainscience.db.database");

    public static ConfigPropertyString DB_USERNAME = new ConfigPropertyString("com.happybrainscience.db.username");

    public static ConfigPropertyString DB_PASSWORD = new ConfigPropertyString("com.happybrainscience.db.password");

}
