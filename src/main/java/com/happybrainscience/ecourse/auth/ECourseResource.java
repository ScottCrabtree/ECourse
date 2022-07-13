package com.happybrainscience.ecourse.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.happybrainscience.ecourse.application.ApplicationConfig;
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
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.lilycode.core.configbundle.ConfigException;
import org.apache.log4j.Logger;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.ContactsApi;
import sibModel.GetExtendedContactDetails;

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
        
    private static final long THRIVE_9TO5_LIST_ID = 13L;
    
    private boolean contactInList(String emailAddress) throws IOException, ApiException, ConfigException {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        // Configure API key authorization: api-key
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey(ApplicationConfig.SENDINBLUE_APIKEY.value());
        ContactsApi contactsApi = new ContactsApi();
        GetExtendedContactDetails contact = contactsApi.getContactInfo(emailAddress);
        LOGGER.debug(contact);
        if (contact != null) {
            LOGGER.debug("found existing contact " + contact.getEmail());
            if(contact.getListIds().contains(THRIVE_9TO5_LIST_ID)) {
                LOGGER.debug("contact is in membership list");
                return true;
            }
        }                
        return false;        
    }
    
    
    
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/credential")
    @POST
    public PostCredentialResponse postCredential(PostCredentialRequest request) {
        try {
            if(!request.getClientId().equals(ApplicationConfig.GOOGLE_IDENTITY_ID.value())) {
                LOGGER.error("client ID mismatch");
                throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
            }
            String credential = request.getCredential();
            PostCredentialResponse response = new PostCredentialResponse();
            LOGGER.debug("google auth credential " + credential);
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Arrays.asList(ApplicationConfig.GOOGLE_IDENTITY_ID.value()))
                    .build();
            GoogleIdToken googleToken = verifier.verify(credential);
            if (googleToken == null) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            } else {
                if (verifier.verify(googleToken)) {
                    LOGGER.debug("token is valid " + googleToken.toString());
                    String name = (String) googleToken.getPayload().get("name");
                    String emailAddress = googleToken.getPayload().getEmail();                    
                    LOGGER.debug("resolved Google account " + emailAddress);
                    // now we need to verify the contact is in the list on SendInBlue
                    if(contactInList(emailAddress)) {                    
                        response.setEmailAddress(emailAddress);
                        response.setName(name);
                        response.setSessionToken(UUID.randomUUID().toString());
                        return response;
                    } else {
                        LOGGER.debug("contact is not a member:" + emailAddress);
                        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                    }
                } else {
                    LOGGER.error("invalid token");
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                }
            }
        } catch (GeneralSecurityException | IOException ex) {
            LOGGER.error("Google auth failure", ex);
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } catch (ApiException ex) {
            LOGGER.error("SendInBlue failure", ex);
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } catch (ConfigException ex) {
            LOGGER.error("configuration exception", ex);
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
