package com.happybrainscience.ecourse.servlet;

import com.happybrainscience.ecourse.application.ProductVersion;
import com.happybrainscience.ecourse.application.TextResourceController;
import com.happybrainscience.ecourse.auth.UserAuthController;
import java.io.IOException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

/**
 * Web application lifecycle listener.
 * @author timothyheider
 */
public class GameApplicationListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(GameApplicationListener.class);
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.debug("ECourse " + ProductVersion.getVersionString() + " start ==== ");
        try {
            UserAuthController.getInstance().start(sce.getServletContext());
            TextResourceController.getInstance().loadTextResources(sce.getServletContext());
        } catch (IOException ex) {
            throw new RuntimeException("failed to load text resources", ex);
        }        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        UserAuthController.getInstance().stop();
        LOGGER.debug("ECourse " + ProductVersion.getVersionString() + " stopped ==== ");        
    }
}
