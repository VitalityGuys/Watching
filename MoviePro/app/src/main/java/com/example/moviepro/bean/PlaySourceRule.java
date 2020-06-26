package com.example.moviepro.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Auto-generated: 2020-06-25 21:17:8
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class PlaySourceRule {

    @SerializedName("playSourceGroup")
    private String playsourcegroup;
    @SerializedName("playSourceName")
    private String playsourcename;
    @SerializedName("playSourceType")
    private int playsourcetype;
    @SerializedName("playSourceUrl")
    private String playsourceurl;
    @SerializedName("searchRequestBody")
    private Searchrequestbody searchrequestbody;
    @SerializedName("ruleSearch")
    private Rulesearch rulesearch;
    @SerializedName("ruleDetail")
    private Ruledetail ruledetail;
    @SerializedName("searchUrl")
    private String searchurl;
    public void setPlaysourcegroup(String playsourcegroup) {
         this.playsourcegroup = playsourcegroup;
     }
     public String getPlaysourcegroup() {
         return playsourcegroup;
     }

    public void setPlaysourcename(String playsourcename) {
         this.playsourcename = playsourcename;
     }
     public String getPlaysourcename() {
         return playsourcename;
     }

    public void setPlaysourcetype(int playsourcetype) {
         this.playsourcetype = playsourcetype;
     }
     public int getPlaysourcetype() {
         return playsourcetype;
     }

    public void setPlaysourceurl(String playsourceurl) {
         this.playsourceurl = playsourceurl;
     }
     public String getPlaysourceurl() {
         return playsourceurl;
     }

    public void setSearchrequestbody(Searchrequestbody searchrequestbody) {
         this.searchrequestbody = searchrequestbody;
     }
     public Searchrequestbody getSearchrequestbody() {
         return searchrequestbody;
     }

    public void setRulesearch(Rulesearch rulesearch) {
         this.rulesearch = rulesearch;
     }
     public Rulesearch getRulesearch() {
         return rulesearch;
     }

    public void setRuledetail(Ruledetail ruledetail) {
         this.ruledetail = ruledetail;
     }
     public Ruledetail getRuledetail() {
         return ruledetail;
     }

    public void setSearchurl(String searchurl) {
         this.searchurl = searchurl;
     }
     public String getSearchurl() {
         return searchurl;
     }

}