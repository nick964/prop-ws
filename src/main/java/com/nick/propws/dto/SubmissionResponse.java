package com.nick.propws.dto;


import lombok.Data;

import java.util.List;

@Data
public class SubmissionResponse {

    public GroupDetailsResponse groupDetails;

    public int position;

    public int totalScore;

    public List<TrackResponse> responses;
}
