/*
 * THIS IS A PLAIN OLD JAVA OBJECT WHICH ALLOWS A USER TO BE EASILY
 * TRANSFERED FROM THE BACK END, THE INSTANCES OF THIS CLASS ARE STORED IN
 * AN ARRAYLIST IN MainActivity CLASS
 * 
*/


package fi.metropolia.challengedemo;


/**
 * Created by mike on 18/11/2014.
 */
class Users {
    private int id;

    private String name;

    private boolean challengeActive;

    private boolean notified;

    private int totalPoints;

    private int currentChallenge;


    public Users(int id, int totalPoints, int currentChallenge, String name, boolean challengeActive, boolean notified){
        setId(id);
        setTotalPoints(totalPoints);
        setCurrentChallenge(currentChallenge);
        setName(name);
        setChallengeActive(challengeActive);
        setNotified(notified);

    }

    public int getCurrentChallenge() {
        return currentChallenge;
    }

    public void setCurrentChallenge(int currentChallenge) {
        this.currentChallenge = currentChallenge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChallengeActive() {
        return challengeActive;
    }

    public void setChallengeActive(boolean challengeActive) {
        this.challengeActive = challengeActive;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
}
