package com.macro.dev.models;


import javax.persistence.*;


@Entity
@Table(name="comment")
@NamedQuery(name="Comment.findAll", query="SELECT l FROM Comment l")
public class Comment {

    @Id
    @GeneratedValue
    private Long Id;

    private String text;

    @ManyToOne
    private Post post;

    @ManyToOne
    private LutUser creator;

    public Comment() {
    }

    public Comment(String text, Post post, LutUser creator) {
        this.text = text;
        this.post = post;
        this.creator = creator;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LutUser getCreator() {
        return creator;
    }

    public void setCreator(LutUser creator) {
        this.creator = creator;
    }
}
