package dev.valium.sweetmeme.domain;

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
