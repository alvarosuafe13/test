package es.uvigo.esei.dgss.exercises.service;

import es.uvigo.esei.dgss.exercises.domain.entities.Post;
import es.uvigo.esei.dgss.exercises.domain.entities.User;
import es.uvigo.esei.dgss.exercises.domain.entities.UserFriend;
import es.uvigo.esei.dgss.exercises.domain.entities.UserPostLike;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class UserEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private StatisticsEJB statisticsEJB;

    @EJB
    private EmailServiceEJB emailServiceEJB;

    @Resource
    private SessionContext ctx;

    public User findUser(User user) {
        return em.find(User.class, user.getLogin());
    }

    public User findUserByLogin(String login) {

        return em.find(User.class, login);
    }

    public List<User> findAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public User createUser(User user) {
        try {
            em.persist(user);
            statisticsEJB.userAdded();
        } catch (EntityExistsException eee) {
            throw new IllegalArgumentException("The user already exists");
        }

        em.persist(user);
        statisticsEJB.userAdded();
        return user;
    }

    public void updateUser(User user) {
        em.merge(user);
    }

    public void removeUser(User user) {
        User u = findUser(user);
        if (u != null) {
            em.remove(u);
            statisticsEJB.userRemoved();
        }
    }

    public void removeUserByLogin(String login) {
        User u = findUserByLogin(login);
        if (u != null) {
            em.remove(u);
            statisticsEJB.userRemoved();
        }
    }

    public UserPostLike addUserPostLike(User user, Post post) {
        UserPostLike upl = new UserPostLike();
        upl.setPost(post);
        upl.setUser(user);
        em.persist(upl);

        String topic = "You have received a like";
        String textMessage = "User" + user.getLogin() + " likes your post " + post.getId();
        emailServiceEJB.send("emailtest@gmail.com", topic, textMessage);
        return upl;
    }

    public UserFriend requestFriendship(String login, String requestFriendLogin) {
        checkLoggedUser(login);

        User userLogged = findUserByLogin(login);
        User userRequested = findUserByLogin(requestFriendLogin);

        UserFriend requestFriend = new UserFriend(userLogged, userRequested, LocalDate.now(), false);
        em.persist(requestFriend);
        return requestFriend;
    }

    public List<UserFriend> getFriendshipRequest(String login) {
        checkLoggedUser(login);

        User userLogged = findUserByLogin(login);

        List<UserFriend> friendsList = em.createQuery("SELECT uf FROM UserFriend uf " +
                        "WHERE (uf.userA = :user OR uf.userB = :user) AND" +
                        " uf.friendshipAccepted = FALSE ", UserFriend.class)
                .setParameter("user", userLogged)
                .getResultList();

        return friendsList;
    }

    public UserFriend acceptUserFriend(String login, String requestFriendLogin) {
        checkLoggedUser(login);

        User userLogged = findUserByLogin(login);
        User userAccepted = findUserByLogin(requestFriendLogin);
        UserFriend userFriend = new UserFriend(userLogged, userAccepted);
        UserFriend friendAccepted = em.find(UserFriend.class, userFriend);

        friendAccepted.setFriendshipAccepted(true);
        em.persist(friendAccepted);

        return friendAccepted;
    }

    public UserFriend addUserFriend(User userA, User userB) {
        UserFriend friend = new UserFriend(userA, userB, LocalDate.now(), true);
        em.persist(friend);
        return friend;
    }

    public void checkLoggedUser(String login) {
        String currentLogin = ctx.getCallerPrincipal().getName(); // accessing the logged user

        if (!currentLogin.equals(login)) {
            throw new IllegalArgumentException("You are not logged user, " + currentLogin + " is not " + login);
        }
    }

    public List<User> getUserByText(String text) {
        text = "%" + text.trim() +"%";

        List<User> friendsList = em.createQuery("SELECT u FROM User u  "
                + "WHERE u.login LIKE :text ", User.class)
                .setParameter("text", text)
                .getResultList();

        return friendsList;
    }
}
