package com.nick.propws.dto;

import com.nick.propws.entity.MemberAnswer;
import lombok.Data;

import java.util.List;

@Data
public class SubmissionResponse {



    public int position;

    public int totalScore;


    public List<TrackResponse> responses;



}
