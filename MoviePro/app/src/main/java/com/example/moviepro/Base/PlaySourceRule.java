package com.example.moviepro.Base;

import java.util.HashMap;
import java.util.Map;

public class PlaySourceRule {
    private String PlaySourceGroup;
    private String PlaySourceName;
    private String PlaySourceType;

    private String hostUrl;
    private String searchUrl;
    private String searchMethod;
    private Map postPara=new HashMap();

    //搜索页面规则
    private String videoSearchNameRule;           //视频名
    private String videoSearchTypeRule;           //视频类型
    private String videoSearchLinkRule;           //视频链接

    //视频详情页面规则
    private String videoNameRule;           //名称
    private String videoCoverImageRule;     //封面
    private String videoDirectorRule;       //导演
    private String videoTypeRule;           //类型
    private String videoActorRule;          //主演
    private String videoAliasRule;          //别名
    private String videoAreaRule;           //地区
    private String videoLanguageRule;       //语言
    private String videoTimeRule;           //上映时间
    private String videoLengthRule;         //片长
    private String videoScoreRule;          //评分
    private String videoIntroduceRule;      //简介

    //播放链接规则
    private String allPlaylinkRule;         //所有链接规则
    private String videoPlaylinkM3u8Rule;   //m3u8链接
    private String videoPlaylinkMp4Rule;    //mp4链接
    private String videoPlaylinkKuyunRule;  //kuyun链接

    public String getPlaySourceGroup() {
        return PlaySourceGroup;
    }

    public void setPlaySourceGroup(String playSourceGroup) {
        PlaySourceGroup = playSourceGroup;
    }

    public String getPlaySourceName() {
        return PlaySourceName;
    }

    public void setPlaySourceName(String playSourceName) {
        PlaySourceName = playSourceName;
    }

    public String getPlaySourceType() {
        return PlaySourceType;
    }

    public void setPlaySourceType(String playSourceType) {
        PlaySourceType = playSourceType;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getSearchMethod() {
        return searchMethod;
    }

    public void setSearchMethod(String searchMethod) {
        this.searchMethod = searchMethod;
    }

    public Map getPostPara() {
        return postPara;
    }

    public void setPostPara(Map postPara) {
        this.postPara = postPara;
    }

    public String getVideoSearchNameRule() {
        return videoSearchNameRule;
    }

    public void setVideoSearchNameRule(String videoSearchNameRule) {
        this.videoSearchNameRule = videoSearchNameRule;
    }

    public String getVideoSearchTypeRule() {
        return videoSearchTypeRule;
    }

    public void setVideoSearchTypeRule(String videoSearchTypeRule) {
        this.videoSearchTypeRule = videoSearchTypeRule;
    }

    public String getVideoSearchLinkRule() {
        return videoSearchLinkRule;
    }

    public void setVideoSearchLinkRule(String videoSearchLinkRule) {
        this.videoSearchLinkRule = videoSearchLinkRule;
    }

    public String getVideoNameRule() {
        return videoNameRule;
    }

    public void setVideoNameRule(String videoNameRule) {
        this.videoNameRule = videoNameRule;
    }

    public String getVideoCoverImageRule() {
        return videoCoverImageRule;
    }

    public void setVideoCoverImageRule(String videoCoverImageRule) {
        this.videoCoverImageRule = videoCoverImageRule;
    }

    public String getVideoDirectorRule() {
        return videoDirectorRule;
    }

    public void setVideoDirectorRule(String videoDirectorRule) {
        this.videoDirectorRule = videoDirectorRule;
    }

    public String getVideoTypeRule() {
        return videoTypeRule;
    }

    public void setVideoTypeRule(String videoTypeRule) {
        this.videoTypeRule = videoTypeRule;
    }

    public String getVideoActorRule() {
        return videoActorRule;
    }

    public void setVideoActorRule(String videoActorRule) {
        this.videoActorRule = videoActorRule;
    }

    public String getVideoAliasRule() {
        return videoAliasRule;
    }

    public void setVideoAliasRule(String videoAliasRule) {
        this.videoAliasRule = videoAliasRule;
    }

    public String getVideoAreaRule() {
        return videoAreaRule;
    }

    public void setVideoAreaRule(String videoAreaRule) {
        this.videoAreaRule = videoAreaRule;
    }

    public String getVideoLanguageRule() {
        return videoLanguageRule;
    }

    public void setVideoLanguageRule(String videoLanguageRule) {
        this.videoLanguageRule = videoLanguageRule;
    }

    public String getVideoTimeRule() {
        return videoTimeRule;
    }

    public void setVideoTimeRule(String videoTimeRule) {
        this.videoTimeRule = videoTimeRule;
    }

    public String getVideoLengthRule() {
        return videoLengthRule;
    }

    public void setVideoLengthRule(String videoLengthRule) {
        this.videoLengthRule = videoLengthRule;
    }

    public String getVideoScoreRule() {
        return videoScoreRule;
    }

    public void setVideoScoreRule(String videoScoreRule) {
        this.videoScoreRule = videoScoreRule;
    }

    public String getVideoIntroduceRule() {
        return videoIntroduceRule;
    }

    public void setVideoIntroduceRule(String videoIntroduceRule) {
        this.videoIntroduceRule = videoIntroduceRule;
    }

    public String getAllPlaylinkRule() {
        return allPlaylinkRule;
    }

    public void setAllPlaylinkRule(String allPlaylinkRule) {
        this.allPlaylinkRule = allPlaylinkRule;
    }

    public String getVideoPlaylinkM3u8Rule() {
        return videoPlaylinkM3u8Rule;
    }

    public void setVideoPlaylinkM3u8Rule(String videoPlaylinkM3u8Rule) {
        this.videoPlaylinkM3u8Rule = videoPlaylinkM3u8Rule;
    }

    public String getVideoPlaylinkMp4Rule() {
        return videoPlaylinkMp4Rule;
    }

    public void setVideoPlaylinkMp4Rule(String videoPlaylinkMp4Rule) {
        this.videoPlaylinkMp4Rule = videoPlaylinkMp4Rule;
    }

    public String getVideoPlaylinkKuyunRule() {
        return videoPlaylinkKuyunRule;
    }

    public void setVideoPlaylinkKuyunRule(String videoPlaylinkKuyunRule) {
        this.videoPlaylinkKuyunRule = videoPlaylinkKuyunRule;
    }

    @Override
    public String toString() {
        return "PlaySourceRule{" +
                "PlaySourceGroup='" + PlaySourceGroup + '\'' +
                ", PlaySourceName='" + PlaySourceName + '\'' +
                ", PlaySourceType='" + PlaySourceType + '\'' +
                ", hostUrl='" + hostUrl + '\'' +
                ", searchUrl='" + searchUrl + '\'' +
                ", searchMethod='" + searchMethod + '\'' +
                ", postPara=" + postPara +
                ", videoSearchNameRule='" + videoSearchNameRule + '\'' +
                ", videoSearchTypeRule='" + videoSearchTypeRule + '\'' +
                ", videoSearchLinkRule='" + videoSearchLinkRule + '\'' +
                ", videoNameRule='" + videoNameRule + '\'' +
                ", videoCoverImageRule='" + videoCoverImageRule + '\'' +
                ", videoDirectorRule='" + videoDirectorRule + '\'' +
                ", videoTypeRule='" + videoTypeRule + '\'' +
                ", videoActorRule='" + videoActorRule + '\'' +
                ", videoAliasRule='" + videoAliasRule + '\'' +
                ", videoAreaRule='" + videoAreaRule + '\'' +
                ", videoLanguageRule='" + videoLanguageRule + '\'' +
                ", videoTimeRule='" + videoTimeRule + '\'' +
                ", videoLengthRule='" + videoLengthRule + '\'' +
                ", videoScoreRule='" + videoScoreRule + '\'' +
                ", videoIntroduceRule='" + videoIntroduceRule + '\'' +
                ", allPlaylinkRule='" + allPlaylinkRule + '\'' +
                ", videoPlaylinkM3u8Rule='" + videoPlaylinkM3u8Rule + '\'' +
                ", videoPlaylinkMp4Rule='" + videoPlaylinkMp4Rule + '\'' +
                ", videoPlaylinkKuyunRule='" + videoPlaylinkKuyunRule + '\'' +
                '}';
    }
}
