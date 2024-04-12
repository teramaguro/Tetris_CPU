import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.paint.Color;

//CPUクラス
public class CPU{
    //行と列
    int row;
    int col;
    //目標の座標
    double targetX;
    double targetY;
    //目標の右回転の回数
    int rotateR;
    //目標のミノの形
    int shape;
    //ホールドするかのフラグ
    boolean hold;
    //左右スピンのするかのフラグ
    boolean spinR;
    boolean spinL;
    //スピンしたかのフラグ
    boolean spined;
    //回転は長押しではなく一度離すため、押したかどうかのフラグ
    boolean rotateInterval;
    Mino target;
    // Evaluate evaluate;
    //コンストラクタ
    public CPU(int row, int col){
        this.row = row;
        this.col = col;
        // evaluate = new Evaluate(row, col);
    }
    //ミノ置き場決定メソッド
    public void decide(Game game, Field field, Mino mino, Mino holdMino, Mino nextMino){
        ArrayList<Optimum> holdOpt;
        double holdEvaluation = Double.NEGATIVE_INFINITY;
        int holdIndex = 0;
        //ホールドしてないとき次のミノをホールドミノとする
        if(game.firstHold){
            holdOpt = singleBest10(field, nextMino, nextMino); //評価する際のホールドミノは次のミノとする
            shape = nextMino.shape;
            holdEvaluation = holdOpt.get(0).evaluation;
            holdIndex = 0;
        }else{
            holdOpt = singleBest10(field, holdMino, mino);
            shape = holdMino.shape;
            for(int i = 0; i < holdOpt.size(); i++){
                Field preField = field.clone();
                Mino preMino = nextMino.clone();
                Mino placedMino = holdMino.clone();
                Mino preHoldMino = mino.clone();
                //ミノを回転
                for(int j = 0; j < holdOpt.get(i).rotateRnum; j++){
                    placedMino.rotateR(preField);
                }
                //座標を上書き
                placedMino.x = holdOpt.get(i).x;
                placedMino.y = holdOpt.get(i).y;
                //スピンするときはスピンする
                if(holdOpt.get(i).spinR){
                    placedMino.rotateR(preField);
                }
                if(holdOpt.get(i).spinL){
                    placedMino.rotateL(preField);
                }
                //固定する
                preField.fixed(placedMino);
                //一手先の評価
                ArrayList<Optimum> nextOpt = singleBest10(preField, preMino, preHoldMino);
                //直後と一手先の評価の和で比較
                if(holdOpt.get(i).evaluation + nextOpt.get(0).evaluation > holdEvaluation){
                    holdEvaluation = holdOpt.get(i).evaluation + nextOpt.get(0).evaluation;
                    holdIndex = i;
                }
            }
        }
        ArrayList<Optimum> normalOpt = singleBest10(field, mino, holdMino);
        double normalEvaluation = Double.NEGATIVE_INFINITY;
        int normalIndex = 0;
        for(int i = 0; i < normalOpt.size(); i++){
            Field preField = field.clone();
            Mino preMino = nextMino.clone();
            Mino placedMino = mino.clone();
            Mino preHoldMino = holdMino.clone();
            //ミノを回転
            for(int j = 0; j < normalOpt.get(i).rotateRnum; j++){
                placedMino.rotateR(preField);
            }
            //座標を上書き
            placedMino.x = normalOpt.get(i).x;
            placedMino.y = normalOpt.get(i).y;
            //スピンするときはスピンする
            if(normalOpt.get(i).spinR){
                placedMino.rotateR(preField);
            }
            if(normalOpt.get(i).spinL){
                placedMino.rotateL(preField);
            }
            //固定する
            preField.fixed(placedMino);
            //一手先の評価
            ArrayList<Optimum> nextOpt = singleBest10(preField, preMino, preHoldMino);
            //直後と一手先の評価の和で比較
            if(normalOpt.get(i).evaluation + nextOpt.get(0).evaluation > normalEvaluation){
                normalEvaluation = normalOpt.get(i).evaluation + nextOpt.get(0).evaluation;
                normalIndex = i;
            }
            // String str = String.format("(%d,%d)", nextOpt.get(0).x, nextOpt.get(0).y);
            // System.out.print(str);
            // String str2 = String.format("(%d,%d)", normalOpt.get(i).x, normalOpt.get(i).y);
            // System.out.println(str2);
        }
        //ホールドした時としないときの評価を比較
        if(holdEvaluation > normalEvaluation){
            //ホールドする場合
            targetX = holdOpt.get(holdIndex).x;
            targetY = holdOpt.get(holdIndex).y;
            rotateR = holdOpt.get(holdIndex).rotateRnum;
            hold = true;
            spinR = holdOpt.get(holdIndex).spinR;
            spinL = holdOpt.get(holdIndex).spinL;
        }else{
            //ホールドしない場合
            targetX = normalOpt.get(normalIndex).x;
            targetY = normalOpt.get(normalIndex).y;
            rotateR = normalOpt.get(normalIndex).rotateRnum;
            shape = mino.shape;
            hold = false;
            spinR = normalOpt.get(normalIndex).spinR;
            spinL = normalOpt.get(normalIndex).spinL;
        }
        //action時のフラグをリセット
        spined = false;
        rotateInterval = false;
        //ターゲットミノを生成
        target = new Mino();
        target.generate(shape);
        // target.x = targetX;
        target.y = 0;
        for(int i = 0; i < rotateR; i++){
            target.rotateR(field);
        }
        target.x = targetX;
        target.y = targetY;
        if(spinR){
            target.rotateR(field);
        }
        if(spinL){
            target.rotateL(field);
        }
        target.color = Color.GRAY;
    }
    //ミノ一つの最適解10個を返すメソッド
    public ArrayList<Optimum> singleBest10(Field field, Mino mino, Mino holdMino){
        ArrayList<Optimum> sol = new ArrayList<>();
        //ミノのクローンを生成
        Mino mCopy = new Mino();
        mCopy.generate(mino.shape);
        //ミノの置き場最適解を一番左から順に判定
        while(mCopy.move(Constant.LEFT, field));
        // double evaluationValue = Double.NEGATIVE_INFINITY;
        while(true){
            //各列で4回転判定
            for(int i = 0; i < 4; i++){
                //回転前のミノからクローン
                Mino judgeMino = mCopy.clone();
                for(int j = 0; j < i; j++){
                    //回転回数
                    judgeMino.rotateR(field);
                }
                //ハードドロップ
                judgeMino.bottom(field);
                sol.addAll(ground(field, judgeMino, holdMino, i));
                while(sol.size() > 10){
                    Collections.sort(sol, new Compare());
                    sol.remove(sol.size() - 1);
                }
            }
            //右端まで来たら終了
            if(!mCopy.move(Constant.RIGHT, field)){
                break;
            }
        }
        //右回転の一番左を判定
        Mino judgeLeft = mCopy.clone();
        judgeLeft.rotateR(field);
        while(judgeLeft.move(Constant.LEFT, field));
        //ハードドロップ
        judgeLeft.bottom(field);
        sol.addAll(ground(field, judgeLeft, holdMino, 1));
        while(sol.size() > 10){
            Collections.sort(sol, new Compare());
            sol.remove(sol.size() - 1);
        }
        //左回転の一番右を判定
        Mino judgeRight = mCopy.clone();
        judgeRight.rotateL(field);
        while(judgeRight.move(Constant.RIGHT, field));
        //ハードドロップ
        judgeRight.bottom(field);
        sol.addAll(ground(field, judgeRight, holdMino, 3));
        while(sol.size() > 10){
            Collections.sort(sol, new Compare());
            sol.remove(sol.size() - 1);
        }
        return sol;
    }
    //接地後と左右スピンのOptimum配列を返すメソッド
    public ArrayList<Optimum> ground(Field field, Mino mino, Mino holdMino, int rotateRnum){
        ArrayList<Optimum> array = new ArrayList<>();
        Field copy = field.clone();
        Mino copyMino = mino.clone();
        int x = (int)copyMino.x;
        int y = (int)copyMino.y;
        double evaluation;
        //接地後なにもしないとき
        copy.fixed(copyMino);
        evaluation = evaluate(copy, copyMino, holdMino);
        array.add(new Optimum(x, y, rotateRnum, evaluation, false, false));
        //右スピン
        copy = field.clone();
        copyMino = mino.clone();
        if(copyMino.rotateR(copy)){
            copy.fixed(copyMino);
            evaluation = evaluate(copy, copyMino, holdMino);
            //スピンする動作は時間がかかるためマイナス評価
            evaluation -= 1;
            array.add(new Optimum(x, y, rotateRnum, evaluation, true, false));
        }
        //左スピン
        copy = field.clone();
        copyMino = mino.clone();
        if(copyMino.rotateL(copy)){
            copy.fixed(copyMino);
            evaluation = evaluate(copy, copyMino, holdMino);
            //スピンする動作は時間がかかるためマイナス評価
            evaluation -= 1;
            array.add(new Optimum(x, y, rotateRnum, evaluation, false, true));
        }
        return array;
    }
    //評価値を返すメソッド
    public double evaluate(Field field, Mino mino, Mino holdMino){
        //クローンを生成
        Field judgeField = field.clone();
        Mino judgeMino = mino.clone();
        //一番下に落として固定する
        judgeMino.bottom(judgeField);
        judgeField.fixed(judgeMino);
        //固定した盤面を評価
        Evaluate eva = new Evaluate(row, col);
        eva.evaluate(judgeField, holdMino);
        return eva.evaluation;
    }
    //行動メソッド
    public String action(Field field, Mino mino){
        if(spined){
            return "W";
        }
        if(hold){
            if(mino.y < 0){
                return "S";
            }
            hold = false;
            return "L";
        }
        //左回転
        if(rotateR == 3){
            rotateR = 0;
            return "J";
        }
        //右回転
        if(rotateR > 0){
            if(rotateInterval){
                rotateInterval = false;
                return null;
            }else{
                rotateInterval = true;
                rotateR--;
                return "I";
            }
        }
        //左移動
        if(targetX < mino.x){
            return "A";
        }
        //右移動
        if(targetX > mino.x){
            return "D";
        }
        //スピンするとき
        if(spinR){
            if(targetY > mino.y){
                return "S";
            }
            //接地したら右回転
            spinR = false;
            spined = true;
            return "I";
        }
        if(spinL){
            if(targetY > mino.y){
                return "S";
            }
            //接地したら左回転
            spinL = false;
            spined = true;
            return "J";
        }
        //ハードドロップ
        // System.out.println("------------------");
        return "W";
    }
}
