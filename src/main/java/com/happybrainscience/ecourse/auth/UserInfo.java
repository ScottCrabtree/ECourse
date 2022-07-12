package com.happybrainscience.ecourse.auth;

import com.happybrainscience.ecourse.application.ProductVersion;


/**
 *
 * @author timothyheider
 */
public class UserInfo {

    private String fullName;
    
    private String emailAddress;
    
    private String locale;
    
    private String version;

    public UserInfo() {
        this.version = ProductVersion.getVersionString();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    @Override
    public String toString() {
        return "UserInfo{" + "fullName=" + fullName + ", emailAddress=" + emailAddress + '}';
    }        
    
}
