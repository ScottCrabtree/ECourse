package com.happybrainscience.ecourse.auth;

import javax.servlet.ServletContext;
import org.apache.log4j.Logger;

/**
 *
 * @author timothyheider
 */
public class UserAuthController {
    
    private static final Logger LOGGER = Logger.getLogger(UserAuthController.class);
    
    private UserSessions userSessions;
    
    public void start(ServletContext sc) {
        // asset path means "development mode" for running locally
        userSessions = new HostUserSessions(sc);
    }
    
    public void stop() {
        LOGGER.debug("stop user auth controller");
        if(userSessions != null) {
            userSessions.stop();
        }
    }
    
    private UserAuthController() {
    }
    
    public static UserAuthController getInstance() {
        return UserAuthControllerHolder.INSTANCE;
    }
    
    public boolean validSession(String sessionId) {
        UserSessionContext userSession = userSessions.getBySessionId(sessionId);
        if(userSession != null) {
            return userSessions.isValidSession(userSession);        
        } else {
            return false;
        }
    }

    public UserInfo getUserInfo(String sessionId) {
        UserSessionContext userSession = userSessions.getBySessionId(sessionId);       
        if(userSession != null) {
            return userSession.getUserInfo();
        } else {
            return null;
        }                    
    }
    
    public String authenticateUser(String emailAddress, String passwordText) {
        UserInfo userInfo = userSessions.getUserInfo(emailAddress);
        if(userInfo == null) {
            return null;
        }
        if(userSessions.authenticateUser(userInfo, passwordText)) {            
            UserSessionContext userSession = userSessions.createNewSession(userInfo);
            LOGGER.trace("created new session " + userSession);               
            return userSession.getSessionId();
        } else {
            return null;
        }
    }
    
    private static class UserAuthControllerHolder {

        private static final UserAuthController INSTANCE = new UserAuthController();
    }
}
