package au.gov.defence.royalaustraliannavyfitness.Model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContentModel implements Serializable {


    public String id = "";
    public String title = "";
    public String hyperLink = "";
    public String hyperLinkId = "";
    public String pdfLink = "";
    public String type = "";
    public Date date = new Date();
    public String image = "";
    public int orderIndex = 0;
    public int videoCount = 0;
    public List<MultiVideoModel> multiVideoModels = new ArrayList<MultiVideoModel>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHyperLink() {
        return hyperLink;
    }

    public void setHyperLink(String hyperLink) {
        this.hyperLink = hyperLink;
    }

    public String getHyperLinkId() {
        return hyperLinkId;
    }

    public void setHyperLinkId(String hyperLinkId) {
        this.hyperLinkId = hyperLinkId;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }

    public List<MultiVideoModel> getMultiVideoModels() {
        return multiVideoModels;
    }

    public void setMultiVideoModels(List<MultiVideoModel> multiVideoModels) {
        this.multiVideoModels = multiVideoModels;
    }
}
