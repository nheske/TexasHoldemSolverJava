package icybee.solver.comparer;

import icybee.solver.Card;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by huangxuefeng on 2019/10/6.
 * Abstract class Comparer
 */
public abstract class Comparer {
    String dic_dir;
    int lines;
    public enum CompareResult {LARGER,EQUAL,SMALLER}
    public Comparer(String dic_dir, int lines) throws FileNotFoundException{
        this.dic_dir = dic_dir;
        this.lines = lines;
    }
    abstract public CompareResult compare(List<Card> private_former, List<Card> private_latter, List<Card> public_board) throws Exception;
    abstract public CompareResult compare(int[] private_former, int[] private_latter, int[] public_board) throws Exception;
    abstract public int get_rank(List<Card> private_hand,List<Card> public_board);
    abstract public int get_rank(int[] private_hand,int[] public_board);
    abstract public int get_rank(long private_hand,long public_board);
}

