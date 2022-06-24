package com.happybrainscience.ecourse.auth;

/**
 *
 * @author timothyheider
 */
public class BuildInfo {

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "BuildInfo{" + "version=" + version + '}';
    }        
    
}
