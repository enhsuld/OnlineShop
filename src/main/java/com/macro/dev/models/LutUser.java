package com.macro.dev.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="lut_user")
@NamedQuery(name="LutUser.findAll", query="SELECT l FROM LutUser l")
public class LutUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String givenname;
    private String familyname;
    private String email;
    private boolean isactive;
    private String mobile;


    @JsonIgnore
    private String password;
    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private List<LutRole> roles;

    public LutUser() {}

    public LutUser(String username, String password, List<LutRole> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFamilyname() {
        return familyname;
    }

    public void setFamilyname(String familyname) {
        this.familyname = familyname;
    }

    public String getGivenname() {
        return givenname;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<LutRole> getRoles() {
        return roles;
    }

    public void setRoles(List<LutRole> roles) {
        this.roles = roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
