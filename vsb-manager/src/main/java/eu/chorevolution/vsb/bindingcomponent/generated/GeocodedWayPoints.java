
package eu.chorevolution.vsb.bindingcomponent.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * This class was generated by the CHOReVOLUTION BindingComponent Generator using com.sun.codemodel 2.6
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "GeocodedWayPoints")
public class GeocodedWayPoints {

    @XmlElement(name = "geocoder_status", required = true)
    private String geocoder_status;
    @XmlElement(name = "place_id", required = true)
    private String place_id;
    @XmlElement(name = "types", required = true)
    private String types;

    public String getgeocoder_status() {
        return geocoder_status;
    }

    public void setgeocoder_status(String geocoder_status) {
        this.geocoder_status = geocoder_status;
    }

    public String getplace_id() {
        return place_id;
    }

    public void setplace_id(String place_id) {
        this.place_id = place_id;
    }

    public String gettypes() {
        return types;
    }

    public void settypes(String types) {
        this.types = types;
    }

}
