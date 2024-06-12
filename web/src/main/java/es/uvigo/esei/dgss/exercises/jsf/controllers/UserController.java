package es.uvigo.esei.dgss.exercises.jsf.controllers;

import es.uvigo.esei.dgss.exercises.domain.entities.Post;
import es.uvigo.esei.dgss.exercises.domain.entities.User;
import es.uvigo.esei.dgss.exercises.domain.entities.UserFriend;
import es.uvigo.esei.dgss.exercises.service.PostEJB;
import es.uvigo.esei.dgss.exercises.service.UserEJB;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value="userController")
@SessionScoped
public class UserController implements Serializable {

    @EJB
    UserEJB userEJB;

    @EJB
    PostEJB postEJB;

    private String text;

    private List<User> users;

    private User userProfile;

    private List<Post> userPosts;



    public String getLogin() {
        return text;
    }

    public void setLogin(String login) {
        this.text = login;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(User userProfile) {
        this.userProfile = userProfile;
    }

    public List<Post> getUserPosts() {
        return userPosts;
    }

    public void setUserPosts(List<Post> userPosts) {
        this.userPosts = userPosts;
    }

    public String getUsersByText()
    {
        this.setUsers(userEJB.getUserByText(this.text));
        return "searchUser";
    }

    public String getUser(String login){
        this.setUserProfile(userEJB.findUserByLogin(login));
        this.setUserPosts(postEJB.getUserPostsNoAuth(login));
        return "searchUser";
    }

}
