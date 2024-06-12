package es.uvigo.esei.dgss.exercises.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class UserFriend implements Serializable {
    @Id
    @ManyToOne
    private User userA;

    @Id
    @ManyToOne
    private User userB;

    @Column()
    private LocalDate date;

    @Column()
    private boolean friendshipAccepted;

    public UserFriend() {
    }

    public UserFriend(User userA, User userB) {
        this.userA = userA;
        this.userB = userB;
    }

    public UserFriend(User userA, User userB, LocalDate date, boolean friendshipAccepted) {
        this.userA = userA;
        this.userB = userB;
        this.date = date;
        this.friendshipAccepted = friendshipAccepted;
    }

    public User getUserA() {
        return userA;
    }

    public void setUserA(User userA) {
        this.userA = userA;
    }

    public User getUserB() {
        return userB;
    }

    public void setUserB(User userB) {
        this.userB = userB;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isFriendshipAccepted() { return friendshipAccepted;}

    public void setFriendshipAccepted(boolean friendshipAccepted) { this.friendshipAccepted = friendshipAccepted; }
}
