package com.happybrainscience.ecourse.auth;

import java.util.Date;

/**
 *
 * @author timothyheider
 */
public class UserSessionContext {
    
    private String sessionId;
    
    private UserInfo userInfo;
        
    private Date whenCreated;
    
    private String locale;
    
    public Date getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Date whenCreated) {
        this.whenCreated = whenCreated;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "UserSession{" + "sessionId=" + sessionId + ", userInfo=" + userInfo + ", whenCreated=" + whenCreated + '}';
    }
    
}
