package com.example.moviepro.bean;
import com.google.gson.annotations.SerializedName;

/**
 * Auto-generated: 2020-06-25 21:17:8
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Playlistrule {

    @SerializedName("allPlaylinkRule")
    private String allplaylinkrule;
    @SerializedName("videoPlaylinkM3u8Rule")
    private String videoplaylinkm3u8rule;
    @SerializedName("videoPlaylinkMp4Rule")
    private String videoplaylinkmp4rule;
    @SerializedName("videoPlaylinkKuyunRule")
    private String videoplaylinkkuyunrule;
    public void setAllplaylinkrule(String allplaylinkrule) {
         this.allplaylinkrule = allplaylinkrule;
     }
     public String getAllplaylinkrule() {
         return allplaylinkrule;
     }

    public void setVideoplaylinkm3u8rule(String videoplaylinkm3u8rule) {
         this.videoplaylinkm3u8rule = videoplaylinkm3u8rule;
     }
     public String getVideoplaylinkm3u8rule() {
         return videoplaylinkm3u8rule;
     }

    public void setVideoplaylinkmp4rule(String videoplaylinkmp4rule) {
         this.videoplaylinkmp4rule = videoplaylinkmp4rule;
     }
     public String getVideoplaylinkmp4rule() {
         return videoplaylinkmp4rule;
     }

    public void setVideoplaylinkkuyunrule(String videoplaylinkkuyunrule) {
         this.videoplaylinkkuyunrule = videoplaylinkkuyunrule;
     }
     public String getVideoplaylinkkuyunrule() {
         return videoplaylinkkuyunrule;
     }

}