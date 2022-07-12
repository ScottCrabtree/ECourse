package com.happybrainscience.ecourse.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.happybrainscience.ecourse.application.CaptionTextResource;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import com.happybrainscience.ecourse.application.ProductVersion;
import com.happybrainscience.ecourse.application.TextResourceController;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    
    private static final String[] VIDEO_FILENAMES = {
        "lesson_1_working_happiness",
        "lesson_2_choose_happiness",
        "lesson_3_soothe_stress",
        "lesson_4_practice_positivity",
        "lesson_5_flow_to_goals",
        "lesson_6_minimize_multi-tasking",
        "lesson_7_prioritize_people",
        "action_plan_-_thrive_from_nine_to_five"
    };

    private static final String GOOGLE_OAUTH_CLIENT_ID = "886115731938-6la64nljk0av0bguht0vqpdnjt5n3hjc.apps.googleusercontent.com";
    
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/credential")
    @POST
    public void postCredentials(@FormParam("credential") String credential, @FormParam("g_csrf_token") String token) {
        try {
            LOGGER.debug("google auth credential " + credential + " token " + token);
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Arrays.asList(GOOGLE_OAUTH_CLIENT_ID))
                    .build();
            GoogleIdToken googleToken = verifier.verify(credential);
            if (googleToken == null) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            } else {
                if (verifier.verify(googleToken)) {
                    LOGGER.debug("token is valid " + googleToken.toString());
                    String emailAddress = googleToken.getPayload().getEmail();
                    LOGGER.debug("resolved Google account " + emailAddress);
                } else {
                    LOGGER.error("invalid token");
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                }
            }
        } catch (GeneralSecurityException | IOException ex) {
            LOGGER.error("Google auth failure", ex);
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }        
    }
    
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/videos")
    @GET
    public Map<String, String> getVideofilenames() {
        Map<String, String> videoFilenames = new HashMap<>();
        int fileIndex = 1;
        for(String videoFilename : VIDEO_FILENAMES) {
            videoFilenames.put(Integer.toString(fileIndex), videoFilename);
            fileIndex++;
        }
        return videoFilenames;
    }    

}
