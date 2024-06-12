package es.uvigo.esei.dgss.exercises.service;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import java.util.concurrent.TimeUnit;

@Startup
@Singleton
@Lock(LockType.WRITE)
@AccessTimeout(value = 30, unit = TimeUnit.SECONDS)
public class StatisticsEJB {

    private int numberOfUsers;
    private int numberOfPosts;

    @PostConstruct
    void init() {
        numberOfPosts = 0;
        numberOfUsers = 0;
    }

    @Lock(LockType.READ)
    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    @Lock(LockType.READ)
    public int getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }

    public void userAdded()
    {
        numberOfUsers++;
    }

    public void userRemoved(){
        numberOfUsers--;
    }

    public void postAdded(){
        numberOfPosts++;
    }

    public void postRemoved(){
        numberOfPosts--;
    }
}
