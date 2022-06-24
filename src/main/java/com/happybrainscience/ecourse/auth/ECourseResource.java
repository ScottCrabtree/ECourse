package com.happybrainscience.ecourse.auth;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import com.happybrainscience.ecourse.application.ProductVersion;
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

}
