package es.uvigo.esei.dgss.exercises.service;

import es.uvigo.esei.dgss.exercises.domain.entities.Post;
import es.uvigo.esei.dgss.exercises.domain.entities.User;
import es.uvigo.esei.dgss.exercises.domain.entities.UserPostLike;
import es.uvigo.esei.dgss.exercises.domain.entities.Comment;
import es.uvigo.esei.dgss.exercises.domain.entities.Video;
import es.uvigo.esei.dgss.exercises.domain.entities.Photo;
import es.uvigo.esei.dgss.exercises.domain.entities.Link;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import java.util.List;

@Stateless
public class PostEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private StatisticsEJB statisticsEJB;

    @EJB
    private UserEJB userEJB;

    @Resource
    private SessionContext ctx;

    public Post findPost(Post post) {
        return em.find(Post.class, post.getId());
    }

    public Post findPostById(int postId) {
        return em.find(Post.class, postId);
    }

    public List<Post> findAllPosts() {
        return em.createQuery("SELECT p FROM Post p", Post.class).getResultList();
    }

    public Post createPost(Post post) {
        em.persist(post);
        statisticsEJB.postAdded();
        return post;
    }

    public Post updatePost(Post post) {
        em.merge(post);
        return post;
    }

    public void removePost(Post post) {
        Post p = findPost(post);
        if (p != null) {
            em.remove(p);
            statisticsEJB.postRemoved();
        }
    }

    public void removePostById(int id) {
        Post p = findPostById(id);
        if (p != null) {
            em.remove(p);
            statisticsEJB.postRemoved();
        }
    }

    public void addCommentToPost(Post post, Comment comment) {
        Post p = findPostById(post.getId());
        if (p != null) {
            comment.setPost(post);
            em.persist(comment);
        }
    }

    public List<Post> getWallPosts(String login){

        userEJB.checkLoggedUser(login);

        User userLogged = userEJB.findUserByLogin(login);

        List<Post> posts = em.createQuery("SELECT p FROM Post p  "+
                        " WHERE p.user in (SELECT uf.userA FROM UserFriend uf WHERE uf.userB = :user) OR "+
                        " p.user in (SELECT uf.userB FROM UserFriend uf WHERE uf.userA = :user)  OR " +
                        " p.user = :user", Post.class)
                .setParameter("user", userLogged).getResultList();

        return posts;
    }

    public List<Post> getUserPosts(String login){
        userEJB.checkLoggedUser(login);

        User userLogged = userEJB.findUserByLogin(login);

        List<Post> posts = em.createQuery("SELECT p FROM Post p WHERE p.user = :user", Post.class)
                .setParameter("user", userLogged)
                .getResultList();

        return posts;
    }

    public List<Post> getUserPostsNoAuth(String login){
        User userLogged = userEJB.findUserByLogin(login);

        List<Post> posts = em.createQuery("SELECT p FROM Post p WHERE p.user = :user", Post.class)
                .setParameter("user", userLogged)
                .getResultList();

        return posts;
    }

    public UserPostLike addPostLike(String login, int idPost){
        userEJB.checkLoggedUser(login);

        User userLogged = userEJB.findUserByLogin(login);

        Post post = this.findPostById(idPost);

        if(post == null){
            throw new IllegalArgumentException("The post does not exists");
        }

        UserPostLike userPostLike = new UserPostLike();
        userPostLike.setPost(post);
        userPostLike.setUser(userLogged);
        em.persist(userPostLike);

        return userPostLike;
    }

    public Link addLink(String login, Link link){
        userEJB.checkLoggedUser(login);
        User userLogged = userEJB.findUserByLogin(login);

        link.setUser(userLogged);
        em.persist(link);
        statisticsEJB.postAdded();
        return link;
    }

    public Photo addPhoto(String login, Photo photo){
        userEJB.checkLoggedUser(login);
        User userLogged = userEJB.findUserByLogin(login);

        photo.setUser(userLogged);
        em.persist(photo);
        statisticsEJB.postAdded();
        return photo;
    }

    public Video addVideo(String login, Video video){
        userEJB.checkLoggedUser(login);
        User userLogged = userEJB.findUserByLogin(login);

        video.setUser(userLogged);
        em.persist(video);
        statisticsEJB.postAdded();
        return video;
    }

    public boolean removePost(String login, int postId){
        userEJB.checkLoggedUser(login);

        Post post = this.findPostById(postId);

        if(post == null){
            throw new WebApplicationException("The post does not exist");
        }

        em.remove(post);
        statisticsEJB.postRemoved();

        return true;
    }

    public Link updateLink(String login, Link link){
        userEJB.checkLoggedUser(login);

        em.merge(link);
        return link;
    }

    public Photo updatePhoto(String login, Photo photo){
        userEJB.checkLoggedUser(login);

        em.merge(photo);
        return photo;
    }

    public Video updateVideo(String login, Video video){
        userEJB.checkLoggedUser(login);

        em.merge(video);
        return video;
    }
}