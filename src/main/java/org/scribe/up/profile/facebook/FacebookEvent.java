/*
  Copyright 2012 Jerome Leleu

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.scribe.up.profile.facebook;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.JsonNode;
import org.scribe.up.profile.JsonObject;
import org.scribe.up.profile.converter.Converters;

/**
 * This class represents a Facebook event.
 * 
 * @author Jerome Leleu
 * @since 1.1.0
 */
public class FacebookEvent extends JsonObject implements Serializable {
    
    private static final long serialVersionUID = -1757110718480085979L;
    
    private String id;
    
    private String name;
    
    private Date startTime;
    
    private Date endTime;
    
    private String location;
    
    private String rsvpStatus;
    
    public FacebookEvent(Object json) {
        super(json);
    }
    
    @Override
    protected void buildFromJson(JsonNode json) {
        this.id = (String) Converters.stringConverter.convertFromJson(json, "id");
        this.name = (String) Converters.stringConverter.convertFromJson(json, "name");
        this.startTime = (Date) FacebookConverters.eventDateConverter.convertFromJson(json, "start_time");
        this.endTime = (Date) FacebookConverters.eventDateConverter.convertFromJson(json, "end_time");
        this.location = (String) Converters.stringConverter.convertFromJson(json, "location");
        this.rsvpStatus = (String) Converters.stringConverter.convertFromJson(json, "rsvp_status");
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Date getStartTime() {
        return startTime;
    }
    
    public Date getEndTime() {
        return endTime;
    }
    
    public String getLocation() {
        return location;
    }
    
    public String getRsvpStatus() {
        return rsvpStatus;
    }
}
