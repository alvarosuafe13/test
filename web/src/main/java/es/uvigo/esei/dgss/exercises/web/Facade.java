package es.uvigo.esei.dgss.exercises.web;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.uvigo.esei.dgss.exercises.domain.entities.Photo;
import es.uvigo.esei.dgss.exercises.domain.entities.Comment;
import es.uvigo.esei.dgss.exercises.domain.entities.User;
import es.uvigo.esei.dgss.exercises.domain.entities.UserFriend;
import es.uvigo.esei.dgss.exercises.domain.entities.UserPostLike;
import es.uvigo.esei.dgss.exercises.domain.entities.Post;
import es.uvigo.esei.dgss.exercises.domain.entities.Video;
import es.uvigo.esei.dgss.exercises.domain.entities.Link;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Dependent
public class Facade {

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    //region Exercise 1 Task 2

    //1.
    public User addUser(String login, String name, String password, byte[] picture) {
        User user = new User(login);

        user.setName(name);
        user.setPassword(password);
        user.setPicture(picture);

        em.persist(user);

        return user;
    }

    //2.
    public UserFriend addUserFriend(User userA, User userB) {
        UserFriend friend = new UserFriend(userA, userB, LocalDate.now(), true);
        em.persist(friend);
        return friend;
    }

    //3.
    public List<User> getUserFriends(User user) {

        List<User> friendsList = em.createQuery("SELECT u FROM User u  "
                + "WHERE u in  (SELECT uf.userA FROM UserFriend uf WHERE uf.userB = :user) OR "
                + "u in (SELECT uf.userB FROM UserFriend uf WHERE uf.userA = :user)", User.class).setParameter("user", user).getResultList();

        return friendsList;
    }

    //4.
    public List<Post> getFriendsPostsFromUser(User user) {
        List<Post> friendsPostsList = em.createQuery("SELECT p FROM Post p, UserFriend uf "
                        + "WHERE  ( uf.userA = :user AND p.user = uf.userB) OR "
                        + " ( uf.userB = :user AND p.user = uf.userA)", Post.class)
                .setParameter("user", user).getResultList();

        return friendsPostsList;
    }

    //5.v1
    public List<Post> getCommentedPostsByUserFriendsV1(User user, Date date) {
        List<Post> commentedPostsList = em.createQuery("SELECT c.post FROM Comment c,UserFriend uf "
                        + " INNER JOIN c.post p2 "
                        + "WHERE ( uf.userA = :user AND  c.user = uf.userB ) OR"
                        + " ( uf.userB = :user AND c.user = uf.userA ) AND"
                        + " c.date > :date", Post.class)
                .setParameter("user", user)
                .setParameter("date", date)
                .getResultList();

        return commentedPostsList;
    }

    //5.v2
    public List<Comment> getCommentedPostsByUserFriendsV2(User user, Date date) {
        List<Comment> commentedPostsList = em.createQuery("SELECT c FROM Comment c,UserFriend uf "
                        + "WHERE ( uf.userA = :user AND  c.user = uf.userB ) OR"
                        + " ( uf.userB = :user AND c.user = uf.userA ) AND"
                        + " c.date > :date", Comment.class)
                .setParameter("user", user)
                .setParameter("date", date)
                .getResultList();

        return commentedPostsList;
    }

    //6.
    public List<User> getFriendsOfAnUserWhoLikesAPost(UserPostLike like) {
        List<User> friendsList = em.createQuery("SELECT u FROM User u  "
                        + "WHERE u in  (SELECT  uf.userA FROM UserFriend uf WHERE uf.userB = :user) OR "
                        + "u in (SELECT uf.userB FROM UserFriend uf WHERE uf.userA = :user)" +
                        "AND u IN (SELECT upl.user FROM UserPostLike upl where upl.post = :post)", User.class)
                .setParameter("user", like.getUser())
                .setParameter("post", like.getPost())
                .getResultList();

        return friendsList;
    }

    //7.
    public List<Photo> getPhotosUserLikes(User user) {
        List<Photo> photosList = em.createQuery("SELECT p FROM Photo p "
                        + "WHERE p in (SELECT upl.post FROM UserPostLike upl WHERE upl.user = :user)", Photo.class)
                .setParameter("user", user)
                .getResultList();

        return photosList;
    }

    //endregion Exercise 1 Task 2


    public void fillDataBase() {
        Date today = new java.util.Date();

        User userPepe = addUser("pepe", "name", "pass", new byte[]{});
        User userManolo = addUser("manolo", "name", "pass", new byte[]{});
        User userMaria = addUser("maria", "name", "pass", new byte[]{});
        User userPepa = addUser("pepa", "name", "pass", new byte[]{});
        User userManola = addUser("manola", "name", "pass", new byte[]{});
        User userMario = addUser("mario", "name", "pass", new byte[]{});

        addUserFriend(userMaria, userPepe);
        addUserFriend(userMaria, userManolo);
        addUserFriend(userPepa, userMaria);
        addUserFriend(userMaria, userManola);
        addUserFriend(userMaria, userMario);
        addUserFriend(userManola, userPepa);
        addUserFriend(userManola, userMario);

        Link link1 = new Link();
        link1.setUrl("url");
        Video video1 = new Video();
        video1.setDuration(123);
        Photo photo1 = new Photo();
        photo1.setContent("photo content");
        Link link2 = new Link();
        link2.setUrl("url2");
        Video video2 = new Video();
        video2.setDuration(321);
        Photo photo2 = new Photo();
        photo2.setContent("photo2 content");

        Post post1 = video1;
        post1.setUser(userPepa);
        post1.setDate(today);
        Post post2 = video2;
        post2.setUser(userMaria);
        post2.setDate(today);
        Post post3 = link1;
        post3.setUser(userMario);
        post3.setDate(today);
        Post post4 = link2;
        post4.setUser(userPepe);
        post4.setDate(today);
        Post post5 = photo1;
        post5.setUser(userMario);
        post5.setDate(today);
        Post post6 = photo2;
        post6.setUser(userPepa);
        post6.setDate(today);

        em.persist(post1);
        em.persist(post2);
        em.persist(post3);
        em.persist(post4);
        em.persist(post5);
        em.persist(post6);


        Comment comment1Post1 = new Comment();
        comment1Post1.setPost(post1);
        comment1Post1.setUser(userMaria);
        comment1Post1.setComment("I like the post!");
        comment1Post1.setDate(today);

        Comment comment2Post1 = new Comment();
        comment2Post1.setPost(post1);
        comment2Post1.setUser(userPepe);
        comment2Post1.setComment("I like the post!");
        comment2Post1.setDate(today);

        Comment comment3Post1 = new Comment();
        comment3Post1.setPost(post1);
        comment3Post1.setUser(userPepa);
        comment3Post1.setComment("I like the post!");
        comment3Post1.setDate(today);

        em.persist(comment1Post1);
        em.persist(comment2Post1);
        em.persist(comment3Post1);

        UserPostLike userPost1Like = new UserPostLike();
        userPost1Like.setUser(userManola);
        userPost1Like.setPost(post1);

        UserPostLike userPost2Like = new UserPostLike();
        userPost2Like.setUser(userManola);
        userPost2Like.setPost(post5);

        em.persist(userPost1Like);
        em.persist(userPost2Like);

    }
}
