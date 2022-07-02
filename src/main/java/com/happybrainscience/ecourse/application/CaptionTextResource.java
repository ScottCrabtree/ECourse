package com.happybrainscience.ecourse.application;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 *
 * @author timothyheider
 */
@XStreamAlias("caption")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"content"})
public class CaptionTextResource {
            
    @XStreamAsAttribute
    @XStreamAlias("id")
    private String id;
    
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }        

    @Override
    public String toString() {
        return "CaptionTextResource{" + "id=" + id + ", content=" + content + '}';
    }
    
}
