package com.happybrainscience.ecourse.auth;

/**
 *
 * @author timothyheider
 */
public class PostCredentialRequest {

    private String clientId;
    
    private String credential;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }
    
    
}
