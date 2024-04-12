//Optimnumクラス
public class Optimum{
    int x;
    int y;
    int rotateRnum;
    boolean spinR;
    boolean spinL;
    double evaluation;
    //コンストラクタ
    public Optimum(int x, int y, int rotateRnum, double evaluation, boolean spinR, boolean spinL){
        this.x = x;
        this.y = y;
        this.rotateRnum = rotateRnum;
        this.evaluation = evaluation;
        this.spinR = spinR;
        this.spinL = spinL;
    }
    //クローンメソッド
    public Optimum clone(){
        Optimum to = new Optimum(x, y, rotateRnum, evaluation, spinR, spinL);
        return to;
    }
}
