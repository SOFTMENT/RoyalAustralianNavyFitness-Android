package au.gov.defence.royalaustraliannavyfitness.Interface;

import java.util.List;

import au.gov.defence.royalaustraliannavyfitness.Model.MultiVideoModel;

public interface MultiVideoCallback {
    void onCallback(List<MultiVideoModel> videoList);
}
