package com.appteam.hillfair2k19.Wall;

/**
 * Coded by ThisIsNSH on 9/20/2018.
 */

public class wall {

    String image_url;
    String likes;
    Integer liked;


    public wall(String image_url, String likes , Integer liked) {
        this.image_url = image_url;
        this.liked = liked;
        this.likes = likes;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public Integer getLiked() {
        return liked;
    }

    public void setLiked(Integer liked) {
        this.liked = liked;
    }
}
