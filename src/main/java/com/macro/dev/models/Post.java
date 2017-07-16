package com.macro.dev.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="post")
@NamedQuery(name="Post.findAll", query="SELECT l FROM Post l")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;
    private Date dateCreated;

    @ManyToOne
    private LutUser creator;



    public Post() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LutUser getCreator() {
        return creator;
    }

    public void setCreator(LutUser creator) {
        this.creator = creator;
    }
}
