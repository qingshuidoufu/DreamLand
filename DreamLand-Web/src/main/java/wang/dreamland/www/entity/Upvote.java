package wang.dreamland.www.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Upvote {
    @Id//标识主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增长策略
    private Long id;

    private Long uId;

    private Long contentId;

    private String ip;
    //点赞
    private String upvote;
    //点踩
    private String downvote;

    private Date upvoteTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getUpvote() {
        return upvote;
    }

    public void setUpvote(String upvote) {
        this.upvote = upvote == null ? null : upvote.trim();
    }

    public String getDownvote() {
        //标记点踩
        return downvote;
    }

    public void setDownvote(String downvote) {
        this.downvote = downvote == null ? null : downvote.trim();
    }

    public Date getUpvoteTime() {
        return upvoteTime;
    }

    public void setUpvoteTime(Date upvoteTime) {
        this.upvoteTime = upvoteTime;
    }
}