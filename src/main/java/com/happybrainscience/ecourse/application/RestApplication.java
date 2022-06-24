package com.happybrainscience.ecourse.application;

import com.happybrainscience.ecourse.auth.ECourseResource;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("resources")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(WebApplicationExceptionMapper.class);
        classes.add(ECourseResource.class);
        return classes;
    }



}
