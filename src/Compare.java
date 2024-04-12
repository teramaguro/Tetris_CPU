import java.util.Comparator;

public class Compare implements Comparator<Optimum>{
    @Override
    public int compare(Optimum o1, Optimum o2){
        return o1.evaluation < o2.evaluation ? 1 : -1;
    }
}
