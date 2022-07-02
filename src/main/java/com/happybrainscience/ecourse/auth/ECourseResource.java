package com.happybrainscience.ecourse.auth;

import com.happybrainscience.ecourse.application.CaptionTextResource;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import com.happybrainscience.ecourse.application.ProductVersion;
import com.happybrainscience.ecourse.application.TextResourceController;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author timothyheider
 */
@Path("ecourse")
public class ECourseResource {

    private static final Logger LOGGER = Logger.getLogger(ECourseResource.class);

    @Produces({MediaType.APPLICATION_JSON})
    @Path("/version")
    @GET
    public BuildInfo getVersion() {
        BuildInfo info = new BuildInfo();
        info.setVersion(ProductVersion.getVersionString());
        return info;
    }    
    
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/captions")
    @GET
    public Map<String, String> getTextCaptions() {
        Map<String, String> captions = new HashMap<>();
        for (CaptionTextResource caption : TextResourceController.getInstance().getCaptions().getCaptions()) {
            captions.put(caption.getId(), caption.getContent());
        }
        return captions;
    }    
}
