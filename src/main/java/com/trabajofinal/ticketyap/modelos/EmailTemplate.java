package com.trabajofinal.ticketyap.modelos;

public class EmailTemplate {
    private String templateemail;
    private String templateUsername;


    public EmailTemplate(String templateemail, String templateUsername) {
        this.templateemail = templateemail;
        this.templateUsername = templateUsername;
    }

    public String getTemplateemail() {
        return templateemail;
    }
    public void setTemplateemail(String templateemail) {
        this.templateemail = templateemail;
    }
    public String getTemplateUsername() {
        return templateUsername;
    }
    public void setTemplateUsername(String templateUsername) {
        this.templateUsername = templateUsername;
    }
    

}
