package com.happybrainscience.ecourse.application;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;

/**
 *
 * @author timothyheider
 */
public class TextResourceController {
    
    private TextResourceController() {
    }
    
    private CaptionTextResourceSet captions;

    public CaptionTextResourceSet getCaptions() {
        return captions;
    }

    public void loadTextResources(ServletContext servletContext) throws IOException {
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.processAnnotations(CaptionTextResource.class);
        xstream.processAnnotations(CaptionTextResourceSet.class);
        try(InputStream in = servletContext.getResourceAsStream("WEB-INF/classes/text/en-US/captions.xml")) {
            captions = (CaptionTextResourceSet) xstream.fromXML(in);            
        }
    }

    public static TextResourceController getInstance() {
        return TextResourceControllerHolder.INSTANCE;
    }
    
    private static class TextResourceControllerHolder {

        private static final TextResourceController INSTANCE = new TextResourceController();
    }
}
