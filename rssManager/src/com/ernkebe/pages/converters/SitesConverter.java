package com.ernkebe.pages.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.ernkebe.database.SqlUtils;
import com.ernkebe.entities.Sites;

 
@FacesConverter("sitesConverter")
public class SitesConverter implements Converter {
 
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
            	Sites s = new Sites();
            	s = (Sites) SqlUtils.findByField(Sites.class, "id", new Integer(value));
            	return s;
            } catch(NumberFormatException e) {
            	 return null;
            }
        }
        else {
            return null;
        }
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((Sites) object).getId());
        }
        else {
            return null;
        }
    }   
}