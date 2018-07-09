package Model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class State implements Serializable{
    private LocalDateTime time;
    private String content;

    public State(LocalDateTime time, String content){
        this.time = time;
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString(){
        return content + " ---- updated at " + time.toString();
    }
}
