package com.example.moviepro.Utils;

import com.example.moviepro.Base.PlayLink;
import com.example.moviepro.Manager.PlaySourceRuleManager;
import com.example.moviepro.bean.PlaySourceRule;
import com.example.moviepro.Base.VideoDetail;
import com.example.moviepro.Base.VideoInfo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ParseHtml {
//    /**
//     * 解析搜索结果
//     * @param html
//     * @return
//     */
//    public static ArrayList<VideoInfo> parsebaseinfo(String html,PlaySourceRule playSourceRule){
//        Document document= Jsoup.parse(html);
//        Elements names=document.select("body > div.xing_vb > ul > li > span.xing_vb4 > a");
//        Elements types=document.select("body > div.xing_vb > ul > li > span.xing_vb5");
//        Elements links=document.select("body > div.xing_vb > ul > li > span.xing_vb4 > a");
//
//        int length=names.size();
//        ArrayList<VideoInfo> searchresult=new ArrayList<VideoInfo>();
//        for(int i=0;i<length;i++){
//            VideoInfo videoInfo=new VideoInfo();
//            videoInfo.setVideoname(names.get(i).text());
//            videoInfo.setVideotype(types.get(i).text());
//            videoInfo.setVideourl(links.get(i).attr("href"));
//            searchresult.add(videoInfo);
//        }
//
//        return searchresult;
//
//    }
//    /**
//     * 解析视频详细信息（播放链接。。。）
//     * @param html
//     * @return
//     */
//    public static VideoDetail parsedetailinfo(String html){
//        Document document= Jsoup.parse(html);
//
//        VideoDetail videoDetail=new VideoDetail();
//        Elements videoname=document.select(".vodh h2");
////        System.out.println("影片名称："+videoname.text());
//        Elements director=document.select(".vodinfobox li:nth-child(2) span");
////        System.out.println("影片导演："+director.text());
//        Elements actors=document.select(".vodinfobox li:nth-child(3) span");
////        System.out.println("影片主演："+actors.text());
//        Elements videotype=document.select(".vodinfobox li:nth-child(4) span");
////        System.out.println("影片类型："+videotype.text());
//        Elements area=document.select(".vodinfobox li:nth-child(5) span");
////        System.out.println("影片地区："+area.text());
//        Elements language=document.select(".vodinfobox li:nth-child(6) span");
////        System.out.println("影片语言："+language.text());
//        Elements introduce=document.select(".vodinfobox li.cont span.more");
////        System.out.println("影片简介："+introduce.text());
//        Elements coverimg=document.select(".warp div:nth-child(1) .vodImg img");
////        System.out.println("影片封面："+coverimg.attr("src"));
//
//        //获取播放链接
//        Elements elements=document.select(".vodplayinfo ul li");
//        for(Element element:elements){
//            String []tmp=element.text().split(" ");
//            String[]content=tmp[0].split("\\$");
//            String string=content[1];
//            String s=string.substring(string.length()-4);
//            if(s.equals("m3u8")){
//                videoDetail.getPlaylists_m3u8().add(new PlayLink(content[0],content[1]));
//            }else if(s.equals(".mp4")){
//                videoDetail.getPlaylists_mp4().add(new PlayLink(content[0],content[1]));
//            }else{
//                videoDetail.getPlaylists_web().add(new PlayLink(content[0],content[1]));
//            }
////            System.out.println(element.text());
//        }
//
//        //为videodetail添加内容
//        videoDetail.setVideoname(videoname.text());
//        videoDetail.setCoverimage(coverimg.attr("src"));
//        videoDetail.setDirector(director.text());
//        videoDetail.setVideotype(videotype.text());
//        videoDetail.setActors(actors.text());
//        videoDetail.setArea(area.text());
//        videoDetail.setLanguage(language.text());
//        videoDetail.setIntroduce(introduce.text());
//
//
//        return videoDetail;
//
//    }

    /**
     * 解析搜索结果
     * @param html
     * @return
     */
    public static ArrayList<VideoInfo> parsebaseinfo(String html,PlaySourceRule playSourceRule){
        Document document= Jsoup.parse(html);
        Elements names=document.select(playSourceRule.getRulesearch().getVideonamerule());
        Elements types=document.select(playSourceRule.getRulesearch().getVideotyperule());
        Elements links=document.select(playSourceRule.getRulesearch().getVideonamerule());

        int length=names.size();
        ArrayList<VideoInfo> searchresult=new ArrayList<VideoInfo>();
        for(int i=0;i<length;i++){
            VideoInfo videoInfo=new VideoInfo();
            videoInfo.setVideoname(names.get(i).text());
            videoInfo.setVideotype(types.get(i).text());
            videoInfo.setVideourl(playSourceRule.getPlaysourceurl()+links.get(i).attr("href"));
            searchresult.add(videoInfo);
        }

        return searchresult;

    }

    /**
     * 解析视频详细信息（播放链接。。。）
     * @param html
     * @return
     */
    public static VideoDetail parsedetailinfo(String html,PlaySourceRule playSourceRule){
        Document document= Jsoup.parse(html);

        VideoDetail videoDetail=new VideoDetail();
        Elements videoname=document.select(playSourceRule.getRuledetail().getVideonamerule());
//        System.out.println("影片名称："+videoname.text());
        Elements director=document.select(playSourceRule.getRuledetail().getVideodirectorrule());
//        System.out.println("影片导演："+director.text());
        Elements actors=document.select(playSourceRule.getRuledetail().getVideoactorrule());
//        System.out.println("影片主演："+actors.text());
        Elements videotype=document.select(playSourceRule.getRuledetail().getVideotyperule());
//        System.out.println("影片类型："+videotype.text());
        Elements area=document.select(playSourceRule.getRuledetail().getVideoarearule());
//        System.out.println("影片地区："+area.text());
        Elements language=document.select(playSourceRule.getRuledetail().getVideolanguagerule());
//        System.out.println("影片语言："+language.text());
        Elements introduce=document.select(playSourceRule.getRuledetail().getVideointroducerule());
//        System.out.println("影片简介："+introduce.text());
        Elements coverimg=document.select(playSourceRule.getRuledetail().getVideocoverimagerule());
//        System.out.println("影片封面："+coverimg.attr("src"));

        //获取播放链接
        if(!playSourceRule.getRuledetail().getPlaylistrule().getAllplaylinkrule().equals("")){
            Elements elements=document.select(playSourceRule.getRuledetail().getPlaylistrule().getAllplaylinkrule());
            for(Element element:elements){
                String []tmp=element.text().split(" ");
                String[]content=tmp[0].split("\\$");
                String string=content[1];
                String s=string.substring(string.length()-4);
                if(s.equals("m3u8")){
                    videoDetail.getPlaylists_m3u8().add(new PlayLink(content[0],content[1]));
                }else if(s.equals(".mp4")){
                    videoDetail.getPlaylists_mp4().add(new PlayLink(content[0],content[1]));
                }else{
                    videoDetail.getPlaylists_web().add(new PlayLink(content[0],content[1]));
                }
//            System.out.println(element.text());
            }
        }


        //为videodetail添加内容
        videoDetail.setVideoname(videoname.text());
        videoDetail.setCoverimage(coverimg.attr("src"));
        videoDetail.setDirector(director.text());
        videoDetail.setVideotype(videotype.text());
        videoDetail.setActors(actors.text());
        videoDetail.setArea(area.text());
        videoDetail.setLanguage(language.text());
        videoDetail.setIntroduce(introduce.text());


        return videoDetail;

    }

    public static PlaySourceRuleManager ParsePlayResource(String response) throws JSONException {
//        JSONObject jsonObject=new JSONObject(response);
//        JSONArray jsonArray=jsonObject.getJSONArray("PlayResourceRule");
//        String playresourceContent=jsonArray.getJSONObject(0).toString();
//        return new Gson().fromJson(playresourceContent, PlaySourceRule.class);
        JSONObject jsonObject=new JSONObject(response);
        JSONArray jsonArray=jsonObject.getJSONArray("PlayResourceRule");
        PlaySourceRuleManager playSourceRuleManager=new PlaySourceRuleManager();
        for(int i=0;i<jsonArray.length();i++){
            String playresourceContent=jsonArray.getJSONObject(i).toString();
            PlaySourceRule playSourceRule=new Gson().fromJson(playresourceContent,PlaySourceRule.class);
            playSourceRuleManager.getPlaySourceRules().add(playSourceRule);
        }
        return playSourceRuleManager;

    }

}
