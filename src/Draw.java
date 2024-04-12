import javafx.geometry.VPos;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

//Drawクラス
public class Draw{
    int gamewidth;
    int gameheight;
    int rightwidth;
    int rightheight;
    int leftwidth;
    int leftheight;

    public Draw(int gW, int gH, int rW, int rH, int lW, int lH){
        gamewidth = gW;
        gameheight = gH;
        rightwidth = rW;
        rightheight = rH;
        leftwidth = lW;
        leftheight = lH;
    }
    //描画初期化メソッド
    public void init(GraphicsContext gcGame, GraphicsContext gcRight, GraphicsContext gcLeft){
        //gcGame描画初期化
        gcGame.clearRect(0, 0, gamewidth, gameheight);
        gcGame.setFill(Color.BLACK);
        gcGame.fillRect(0, 0, gamewidth, gameheight);
        //gcRight描画初期化
        gcRight.clearRect(0, 0, rightwidth, rightheight);
        gcRight.setFill(Color.BLACK);
        gcRight.fillRect(0, 0, rightwidth, rightheight);
        //gcLeft描画初期化
        gcLeft.clearRect(0, 0, leftwidth, leftheight);
        gcLeft.setFill(Color.BLACK);
        gcLeft.fillRect(0, 0, leftwidth, leftheight);
    }
    //描画メソッド
    public void drawGame(GraphicsContext gcGame, GraphicsContext gcRight, GraphicsContext gcLeft, Game game, Field field, Mino mino, Mino hold, MinoShape shapes, CPU cpu){
        //フィールド描画
        field.draw(gcGame);
        // game.operationPrevent = false;
        //CPUが設置予定の場所をグレーで表示
        // cpu.target.draw(gcGame, Constant.TILESIZE);
        //シャドー描画
        Mino shadow = mino.clone(); //クローンする
        shadow.bottom(field); //下まで落とす
        shadow.color = shadow.color.deriveColor(0, 1, 0.3, 1); //色を暗くする
        shadow.draw(gcGame, Constant.TILESIZE);

        //ミノ描画
        mino.draw(gcGame, Constant.TILESIZE);
        //次のミノ描画
        shapes.draw(gcRight, rightwidth);

        //置いたミノの数描画
        Font font = Font.font("Times New Roman", FontWeight.BOLD, 20);
        gcRight.setFont(font);
        gcRight.setFill(Color.WHITE);
        gcRight.setTextAlign(TextAlignment.CENTER);
        int i = 0;
        gcRight.fillText("ミノの数 : " + field.minoNUM, leftwidth / 2, 500 + 20 * i); i++;
        //同時消しライン数描画
        gcRight.fillText("シングル : " + field.line1, leftwidth / 2, 500 + 20 * i); i++;
        gcRight.fillText("ダブル : " + field.line2, leftwidth / 2, 500 + 20 * i); i++;
        gcRight.fillText("トリプル : " + field.line3, leftwidth / 2, 500 + 20 * i); i++;
        gcRight.fillText("テトリス : " + field.line4, leftwidth / 2, 500 + 20 * i); i++;
        //Tスピン数描画
        i++;
        gcRight.fillText("TSM : " + field.tsm, leftwidth / 2, 500 + 20 * i); i++;
        gcRight.fillText("TSS : " + field.tss, leftwidth / 2, 500 + 20 * i); i++;
        gcRight.fillText("TSD : " + field.tsd, leftwidth / 2, 500 + 20 * i); i++;
        gcRight.fillText("TST : " + field.tst, leftwidth / 2, 500 + 20 * i); i++;

        //ホールドミノ描画
        gcLeft.setStroke(Color.WHITE);//枠線描画
        gcLeft.strokeRect(60 - 10, 50, 80 + 10 * 2, 4 * 20 * 1 + 10 * 2);
        font = Font.font("Times New Roman", FontWeight.BOLD, 30);
        gcLeft.setFont(font);
        gcLeft.setFill(Color.WHITE);
        gcLeft.setTextAlign(TextAlignment.CENTER);
        gcLeft.fillText("HOLD", leftwidth / 2, 40); //文字描画
        if(!game.firstHold){//ホールドされているときホールドミノ描画
            hold.draw(gcLeft, 20);
        }
        //スコア描画
        font = Font.font("Times New Roman", FontWeight.BOLD, 20);
        gcLeft.setFont(font);
        gcLeft.fillText("SCORE : " + game.score, leftwidth / 2, 180);
    }
    //タイトル描画メソッド
    public void drawTitle(GraphicsContext gcGame, Selector selector){
        gcGame.setFill(Color.WHITE);
        gcGame.setTextAlign(TextAlignment.CENTER);
        gcGame.setTextBaseline(VPos.CENTER);
        //現在の評価
        Font font = Font.font("Times New Roman", FontWeight.BOLD, 30);
        gcGame.setFont(font);
        gcGame.fillText("Tetris Game", gamewidth / 2, gameheight / 2 - 80);
        font = Font.font("Times New Roman", FontWeight.BOLD, 20);
        gcGame.setFont(font);
        gcGame.fillText("PLAY", gamewidth / 2, gameheight / 2);
        gcGame.fillText("CPU PLAY", gamewidth / 2, gameheight / 2 + 50);
        double[] x = {gamewidth / 2 - 100, gamewidth / 2 - 100, gamewidth / 2 - 100 + 10};
        double[] y = null;
        switch(selector){
            case PLAY:
                y = new double[]{gameheight / 2 - 10, gameheight / 2 + 10, gameheight / 2};
                break;
            case CPU:
                y = new double[]{gameheight / 2 - 10 + 50, gameheight / 2 + 10 + 50, gameheight / 2 + 50};
                break;
        }
        gcGame.fillPolygon(x, y, 3);
    }
    //ゲームオーバー描画メソッド
    public void drawGameOver(GraphicsContext gcGame, Game game){
        Font font = Font.font("Times New Roman", FontWeight.BOLD, 20);
        gcGame.setFont(font);
        gcGame.setFill(Color.WHITE);
        gcGame.setTextAlign(TextAlignment.CENTER);
        //現在の評価
        gcGame.fillText("Game Over", gamewidth / 2, gameheight / 2);
        gcGame.fillText("Score : " + game.score, gamewidth / 2, gameheight / 2 + 50);
    }

    //評価値描画メソッド
    public void drawEva(GraphicsContext gcLeft1, Field field, Mino mino, Mino holdMino, Evaluate evaNow, Evaluate evaNext){
        Font font = Font.font("Times New Roman", FontWeight.BOLD, 20);
        gcLeft1.setFont(font);
        gcLeft1.setFill(Color.WHITE);
        gcLeft1.setTextAlign(TextAlignment.CENTER);
        int startH = 250;
        //現在の評価
        gcLeft1.fillText("現在", leftwidth / 2, startH);
        evaNow.drawEvaluation(gcLeft1, leftwidth / 2, startH + 20);
        //行動後評価
        Mino shadow = mino.clone(); //クローンする
        shadow.bottom(field); //下まで落とす
        Field nextField = field.clone(); //フィールドをクローン
        nextField.fixed(shadow); //落下後のフィールドを生成
        evaNext.evaluate(nextField, holdMino);
        gcLeft1.fillText("行動後", leftwidth / 2, startH + 20 * 13);
        evaNext.drawEvaluation(gcLeft1, leftwidth / 2, startH + 20 * 14);
    }

    //キーガイド描画メソッド
    public void drawGuide(GraphicsContext gcLeft){
                //操作方法描画
                int i = 0;
                gcLeft.fillText("       下移動         : S", leftwidth / 2, 500 + 30 * i); i++;
                gcLeft.fillText("       右移動         : D", leftwidth / 2, 500 + 30 * i); i++;
                gcLeft.fillText("       左移動         : A", leftwidth / 2, 500 + 30 * i); i++;
                gcLeft.fillText("ハードドロップ : W", leftwidth / 2, 500 + 30 * i); i++;
                gcLeft.fillText("       右回転         : I", leftwidth / 2, 500 + 30 * i); i++;
                gcLeft.fillText("       左回転         : J", leftwidth / 2, 500 + 30 * i); i++;
                gcLeft.fillText("     ホールド       : L", leftwidth / 2, 500 + 30 * i);
    }
}
