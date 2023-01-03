package icybee.solver.compairer;

public class HandAndRank {
    long hand;
    int rank;

    public int getRank() {
        return rank;
    }

    public long getHand() {
        return hand;
    }
    public HandAndRank(long hand, int rank){
        this.hand = hand;
        this.rank = rank;
    }
}
