package au.gov.defence.royalaustraliannavyfitness.Model;

import java.util.Date;

public class UserModel {

    public String fullName = "";
    public String email = "";
    public String uid = "";
    public Date date = new Date();
    public String profilePic = "";


    public static UserModel data  = new UserModel();

    public UserModel() {
        data = this;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
