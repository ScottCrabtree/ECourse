package com.happybrainscience.ecourse.auth;

/**
 *
 * @author timothyheider
 */
public interface UserSessions {
    
    public UserSessionContext createNewSession(UserInfo userInfo);

    public void destroySession(UserSessionContext userSession);
    
    public boolean isValidSession(UserSessionContext userSession);
    
    public UserSessionContext getBySessionId(String sessionId);
    
    public boolean authenticateUser(UserInfo userInfo, String passwordText);
    
    public UserInfo getUserInfo(String emailAddress);    

    public void stop();
    
}
