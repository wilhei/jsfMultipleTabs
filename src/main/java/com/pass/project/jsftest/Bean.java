package com.pass.project.jsftest;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named("bean")
@ViewScoped
public class Bean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String someValue;

    @Inject
    private HttpServletRequest request;
    
    @PostConstruct
    public void init() {
        setSomeValue(request.getParameter("someValue"));
        if (someValue == null) {
            throw new IllegalArgumentException("someValue must be set.");
        }
    }
    
    /**
     * @return the someValue
     */
    public String getSomeValue() {
        return someValue;
    }

    
    /**
     * @param someValue the someValue to set
     */
    public void setSomeValue(String someValue) {
        this.someValue = someValue;
    }
    
    
}
