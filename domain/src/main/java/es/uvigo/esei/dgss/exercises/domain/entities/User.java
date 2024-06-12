package es.uvigo.esei.dgss.exercises.domain.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class User {

    @Id
    private String login;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String role = "user";

    @Column
    private byte[] picture;

    @OneToMany(mappedBy = "user")
    private Collection<UserPostLike> userPostLike;

    @OneToMany(mappedBy = "user")
    private Collection<Comment> comment;

    @OneToMany(mappedBy = "user")
    private Collection<Post> post;



    public User() {
    }

    public User(String login) {
        this.login = login;
    }

    public User(String login, String name, String password, byte[] picture) {
        this.login = login;
        this.name = name;
        this.password = password;
        this.picture = picture;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Collection<Post> getPost() {
        return post;
    }
}
