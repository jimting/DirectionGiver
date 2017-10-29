package jt.directiongiver000;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by lp123 on 2017/10/24.
 */

public class NPC
{
    private String id;
    private String name;
    private LatLng position;
    private String picURL;
    private String mp3URL;
    private Bitmap pic;
    private String intro;
    private NPCStory[] story;

    NPC(String id,String name,LatLng position,String picURL,String mp3URL,Bitmap pic,String intro,NPCStory[] story)
    {
        this.id = id;
        this.name = name;
        this.position = position;
        this.picURL = picURL;
        this.mp3URL = mp3URL;
        this.pic = pic;
        this.intro = intro;
        this.story = story;
    }

    public NPCStory[] getStory() {
        return story;
    }

    public void setStory(NPCStory[] story) {
        this.story = story;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public String getMp3URL()
    {
        return mp3URL;
    }

    public void setMp3URL(String mp3URL)
    {
        this.mp3URL = mp3URL;
    }
}
