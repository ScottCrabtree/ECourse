package com.happybrainscience.ecourse.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Timothy Heider
 */
public class ModelClasses {

    private static final Class[] CLASSES = {
        User.class,
        UserSession.class
    };
    
    public static List<Class> getClasses() {
        return new ArrayList<>(Arrays.asList(CLASSES));
    }
    
}
