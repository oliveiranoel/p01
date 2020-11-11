package io.vsia.p01.model;

/**
 * @author noel.oliveira
 * @since 20.4.2018
 */
public class Highscore {

    public long time;

    public Highscore(long time) {
        this.time = time;
    }

    public Highscore() {}

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append("Time: ").append(time).append(" s");
        return response.toString();
    }
}
