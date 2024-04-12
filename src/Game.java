import java.util.ArrayList;

//Gameクラス
public class Game{
    //ゲームオーバー判定フラグ
    boolean gameOver = false;
    //ユーザーが下移動したか判定フラグ
    boolean moveDown = false;
    //下まで落ちたか判定フラグ
    boolean bottom = false;
    //ユーザー操作禁止フラグ
    boolean operationPrevent = false;
    //ゲーム中に初めてのホールドか判定するフラグ
    boolean firstHold = true;
    //１つのミノをホールドしたか判定するフラグ
    boolean holdflag = false;
    //回転インターバルフラグ
    boolean rotateInterval = false;
    //回転インターバル用カウンタ
    int cnt = 0;
    //スコア
    int score = 0;
    //コンストラクタ
    public Game(){
        gameOver = false;
        bottom = false;
        operationPrevent = false;
        firstHold = true;
        holdflag = false;
    }
    //ユーザー操作メソッド
    public boolean operate(Field field, Mino mino, Mino hold, MinoShape shapes, ArrayList<String> input){
        boolean flag = false;
        moveDown = false;
        //ユーザー操作禁止時はfalseを返す
        if(operationPrevent){
            return false;
        }
        //ゲームオーバーでないとき
        if(!gameOver){
            //ホールド処理
            if(input.contains("L") && !holdflag){
                //ホールドしてないとき
                if(firstHold){
                    //ホールドを生成
                    hold.generate(mino.shape);
                    //ホールドミノ描画のための座標補正
                    hold.y += 4;
                    if(hold.col == 3){
                        hold.x = 3.5;
                    }
                    //ホールドフラグ
                    firstHold = false;
                    //次のミノを生成
                    mino.generate(shapes.shapeQ.poll());
                    shapes.addQueue();
                    holdflag = true;
                    flag = true;
                //ホールド済みのとき
                }else{
                    //形を保持
                    int shapeNum = mino.shape;
                    //ミノをホールドミノとスワップ
                    mino.generate(hold.shape);
                    hold.generate(shapeNum);
                    //ホールドミノ描画のための座標補正
                    hold.y += 4;
                    if(hold.col == 3){
                        hold.x = 3.5;
                    }
                    holdflag = true;
                    flag = true;
                }
            }
            //下まで落ちる処理
            if(input.contains("W")){
                mino.bottom(field);
                bottom = true;
                operationPrevent = true;
            }
            //一つ下に移動
            if(input.contains("S")){
                moveDown = mino.move(Constant.DOWN, field);
                flag = true;
            }
            //1つ右に移動
            if(input.contains("D")){
                mino.move(Constant.RIGHT, field);
                flag = true;
            }
            //1つ左に移動
            if(input.contains("A")){
                mino.move(Constant.LEFT, field);
                flag = true;
            }
            //左回転
            if(input.contains("J")){
                if(!rotateInterval || cnt >= 4){
                    mino.rotateL(field);
                    flag = true;
                    rotateInterval = true;
                }
            //右回転
            }else if(input.contains("I")){
                //回転ボタンを押している最中は常にrotateInterval = true
                //rotateInterval=trueのときにcntを4までインクリメント
                if(!rotateInterval || cnt >= 4){
                    mino.rotateR(field);
                    flag = true;
                    rotateInterval = true;
                }
            }else{
                rotateInterval = false;
                cnt = 0;
            }
            if(rotateInterval){
                if(cnt < 4){
                    cnt++;
                }
            }
        }
        return flag;
    }
    //システム割り込みメソッド
    public boolean interrupt(Field field, Mino mino, Mino hold, Mino next, ArrayList<String> input, MinoShape shapes, Evaluate eva, CPU cpu, boolean player){
        boolean flag = false;
        operationPrevent = false;
        //ゲームオーバー判定
        if(gameOver){
            System.out.println("置いたミノ数 : " + field.minoNUM);
            return false;
        }
        //下まで落ちたとき
        if(mino.isBottom(field) && bottom){
            //フィールドにミノ固定
            field.fixed(mino);
            holdflag = false;
            // System.out.println("固定");
            //ライン消去
            int deleteline = field.delete();
            switch(deleteline){
                case 0:
                    break;
                case 1:
                    field.line1++;
                    if(field.spin){
                        if(field.spinMini){
                            field.tsm++;
                        }
                        else{
                            field.tss++;
                            score += Constant.TSINGLE;
                        }
                    }else{
                        score += Constant.SINGLE;
                    }
                    break;
                case 2:
                    field.line2++;
                    if(field.spin){
                        field.tsd++;
                        score += Constant.TDOUBLE;
                    }else{
                        score += Constant.DOUBLE;
                    }
                    break;
                case 3:
                    field.line3++;
                    if(field.spin){
                        field.tst++;
                        score += Constant.TTRIPLE;
                    }else{
                        score += Constant.TRIPLE;
                    }
                    break;
                case 4:
                    field.line4++;
                    score += Constant.TETRIS;
                    break;
            }
            field.spin = false;
            //評価値計算
            eva.evaluate(field, hold);
            //ゲームオーバー判定
            gameOver = field.isGameOver();
            //ゲームオーバーしてないとき
            if(!gameOver){
                //ミノ生成
                mino.generate(shapes.shapeQ.poll());
                shapes.addQueue();
                next.generate(shapes.shapeQ.peek());
                //CPUミノ置き場決定
                cpu.decide(this, field, mino, hold, next);
            }
            //フラグ更新
            bottom = false;
            flag = true;
            // operationPrevent = true;
        //ユーザーが下移動を行っていないとき
        }else if(!moveDown){
            //ミノ一つ下移動とフラグ更新
            bottom = !mino.move(Constant.DOWN, field);
            if(bottom){
                gameOver = field.isGameOver();
            }
            flag = true;
        }
        return flag;
    }
    //ゲームリスタートメソッド
    public void restart(Field field, Mino mino, Mino next, Mino hold, MinoShape shapes, CPU cpu, boolean player){
        //フィールド初期化
        field.init();
        //ミノの形初期化
        shapes.init();
        //ミノ生成
        mino.generate(shapes.shapeQ.poll());
        //次のミノ生成
        shapes.addQueue();
        //直後のミノ生成
        next.generate(shapes.shapeQ.peek());
        //CPUミノ置き場決定
        if(!player){
            cpu.decide(this, field, mino, hold, next);
        }
        //スコアリセット
        score = 0;
        //フラグ更新
        gameOver = false;
        bottom = false;
        operationPrevent = false;
        firstHold = true;
        holdflag = false;
    }
}
