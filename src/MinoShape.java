import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

//MinoShapeクラス
public class MinoShape{
    //次のミノを表示する数
    int showNextNum;
    //次のミノの形の値を保持するキュー
    Queue<Integer> shapeQ = new ArrayDeque<>();
    //ミノの形の値の候補を保持するリスト
    ArrayList<Integer> candidate = new ArrayList<>();
    //コンストラクタ
    public MinoShape(int showNextNum){
        this.showNextNum = showNextNum;
        init();
    }
    //初期化メソッド
    public void init(){
        //リスト初期化
        candidateInit();
        //キュー初期化
        shapeQ.clear();
        //表示する数だけキューに追加
        for(int i = 0; i < showNextNum; i++){
            addQueue();
        }
    }
    //ミノ候補をキューに追加するメソッド
    public void addQueue(){
        shapeQ.add(getShape());
    }
    //ミノの種類を決定するメソッド
    public int getShape(){
        Random r = new Random();
        //リストから乱数で決定
        int index = r.nextInt(candidate.size());
        int shape = candidate.get(index);
        candidate.remove(index);
        // System.out.println("" + shape);
        //リストが0になったら候補を初期化
        if(candidate.size() <= 0){
            candidateInit();
        }
        return shape;
    }
    //ミノ候補初期化メソッド
    public void candidateInit(){
        //リストリセット
        candidate.clear();
        //ミノの種類だけ候補を追加
        for(int i = 1; i <= 7; i++){
            candidate.add(i);
        }
        // System.out.println("-------------");
    }
    //描画メソッド
    public void draw(GraphicsContext gc, int width){
        //枠線描画
        gc.setStroke(Color.WHITE);
        gc.strokeRect(60 - 10, 50, 80 + 10 * 2, 4 * 20 * showNextNum + 10 * 2);
        for(int i = 0; i < showNextNum; i++){
            int shape = shapeQ.poll();
            Mino nextMino = new Mino();
            //キューから取り出してミノを生成
            nextMino.generate(shape);
            //描画のために座標を補正
            nextMino.y += 4 * i + 4;
            if(nextMino.col == 3){
                nextMino.x = 3.5;
            }
            //ミノを描画
            nextMino.draw(gc, 20);
            //文字を描画
            Font font = Font.font("Times New Roman", FontWeight.BOLD, 30);
            gc.setFont(font);
            gc.setFill(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("NEXT", width / 2, 40);
            //キューに戻す
            shapeQ.add(shape);
        }
    }
}
