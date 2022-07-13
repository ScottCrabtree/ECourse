package com.happybrainscience.ecourse.application;

import net.lilycode.core.configbundle.ConfigPropertyInt;
import net.lilycode.core.configbundle.ConfigPropertyString;

/**
 * Google OAUTH secret
 * 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
 */

/**
 *
 * @author timothyheider
 */
public class ApplicationConfig {

    public static ConfigPropertyString DB_HOST = new ConfigPropertyString("com.happybrainscience.thrive9to5.db.host");

    public static ConfigPropertyInt    DB_PORT = new ConfigPropertyInt("com.happybrainscience.thrive9to5.db.port");

    public static ConfigPropertyString DB_DATABASE = new ConfigPropertyString("com.happybrainscience.thrive9to5.db.database");

    public static ConfigPropertyString DB_USERNAME = new ConfigPropertyString("com.happybrainscience.thrive9to5.db.username");

    public static ConfigPropertyString DB_PASSWORD = new ConfigPropertyString("com.happybrainscience.thrive9to5.db.password");
    
    public static ConfigPropertyString GOOGLE_IDENTITY_ID = new ConfigPropertyString("com.happybrainscience.thrive9to5.google_identity_id");
    
    public static ConfigPropertyString SENDINBLUE_APIKEY = new ConfigPropertyString("com.happybrainscience.thrive9to5.sendinblue_apikey");

}
