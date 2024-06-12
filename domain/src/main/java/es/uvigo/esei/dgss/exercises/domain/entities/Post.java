package es.uvigo.esei.dgss.exercises.domain.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@DiscriminatorColumn(
        name = "commentType",
        discriminatorType = DiscriminatorType.STRING,
        length = 5
)
public abstract class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private Date date;

    @OneToMany(mappedBy = "post")
    private Collection<UserPostLike>  userPostLike;

    @OneToMany(mappedBy = "post")
    private Collection<Comment> comment;

    @ManyToOne
    private User user;

    public Post() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Collection<UserPostLike> getUserPostLike() {
        return userPostLike;
    }

    public void setUserPostLike(Collection<UserPostLike> userPostLike) {
        this.userPostLike = userPostLike;
    }

    public Collection<Comment> getComment() {
        return comment;
    }

    public void setComment(Collection<Comment> comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
