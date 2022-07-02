package com.happybrainscience.ecourse.application;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

/**
 *
 * @author timothyheider
 */
@XStreamAlias("captions")
public class CaptionTextResourceSet {

    @XStreamImplicit(itemFieldName="caption")
    private List<CaptionTextResource> captions;

    public List<CaptionTextResource> getCaptions() {
        return captions;
    }

    public void setCaptions(List<CaptionTextResource> captions) {
        this.captions = captions;
    }

    @Override
    public String toString() {
        return "CaptionTextResourceSet{" + "captions=" + captions + '}';
    }
        
        
}
