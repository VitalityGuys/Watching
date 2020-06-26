package com.example.moviepro.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Auto-generated: 2020-06-25 21:17:8
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Rulesearch {

    @SerializedName("videoNameRule")
    private String videonamerule;
    @SerializedName("videoTypeRule")
    private String videotyperule;
    @SerializedName("videoLinkRule")
    private String videolinkrule;
    public void setVideonamerule(String videonamerule) {
         this.videonamerule = videonamerule;
     }
     public String getVideonamerule() {
         return videonamerule;
     }

    public void setVideotyperule(String videotyperule) {
         this.videotyperule = videotyperule;
     }
     public String getVideotyperule() {
         return videotyperule;
     }

    public void setVideolinkrule(String videolinkrule) {
         this.videolinkrule = videolinkrule;
     }
     public String getVideolinkrule() {
         return videolinkrule;
     }

}