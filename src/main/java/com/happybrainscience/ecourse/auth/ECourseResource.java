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
    
    private static final String[] VIDEO_FILENAMES = {
        "lesson_1_working_happiness.mp4",
        "lesson_2_choose_happiness.mp4",
        "lesson_3_soothe_stress.mp4",
        "lesson_4_practice_positivity.mp4",
        "lesson_5_flow_to_goals.mp4",
        "lesson_6_minimize_multi-tasking.mp4",
        "lesson_7_prioritize_people.mp4",
        "action_plan_-_thrive_from_nine_to_five.mp4"
    };

    @Produces({MediaType.APPLICATION_JSON})
    @Path("/videos")
    @GET
    public Map<Integer, String> getVideofilenames() {
        Map<Integer, String> videoFilenames = new HashMap<>();
        int fileIndex = 1;
        for(String videoFilename : VIDEO_FILENAMES) {
            videoFilenames.put(fileIndex++, videoFilename);
        }
        return videoFilenames;
    }    

}
