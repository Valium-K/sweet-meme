package dev.valium.sweetmeme.module.bases.embeddable;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Vote {

    private int upVote;
    private int downVote;

    public int getUpVote() {
        return upVote;
    }
    public void setUpVote(int upVote) {
        this.upVote = upVote;
    }
    public int getDownVote() {
        return downVote;
    }
    public void setDownVote(int downVote) {
        this.downVote = downVote;
    }

    public void addUpVote() {this.upVote++;}
    public void subUpvote() {this.upVote--;}
    public void addDownVote() {this.downVote++;}
    public void subDownVote() {this.downVote--;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vote vote = (Vote) obj;

        return Objects.equals(getUpVote(), vote.getUpVote()) &&
                Objects.equals(getDownVote(), vote.getDownVote());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUpVote(), getDownVote());
    }
}
