package es.uvigo.esei.dgss.exercises.domain.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.Duration;

@Entity
@DiscriminatorValue("VIDEO")
public class Video extends Post{
    @Column
    private int duration;

    public Video() {
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
