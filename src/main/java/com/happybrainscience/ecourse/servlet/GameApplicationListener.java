package com.happybrainscience.ecourse.servlet;

import com.happybrainscience.ecourse.application.ProductVersion;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

/**
 * Web application lifecycle listener.
 *
 * @author timothyheider
 */
public class GameApplicationListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(GameApplicationListener.class);
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.debug("ECourse " + ProductVersion.getVersionString() + " start ==== ");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.debug("ECourse " + ProductVersion.getVersionString() + " stopped ==== ");        
    }
}
