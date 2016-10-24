package com.bogueratcreations.eaftoolkit.DCP.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by jodyroth on 10/24/16.
 */

public class Project extends RealmObject {

    private String projName;
    private String location;
    private String projInfo;
    private Date dateCreated;
    private RealmList<Site> sites;

    public void cascadeDeleteProject() {
        for (Site site : sites) {
            site.cascadeDeletePoints(); // removes points and readings
        }
        sites.deleteAllFromRealm(); // removes sites
        deleteFromRealm(); // removes self
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProjInfo() {
        return projInfo;
    }

    public void setProjInfo(String projInfo) {
        this.projInfo = projInfo;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public RealmList<Site> getSites() {
        return sites;
    }

    public void setSites(RealmList<Site> sites) {
        this.sites = sites;
    }

}
