import java.util.ArrayList;

import javafx.animation.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.scene.*;

public class Tetris extends Application{
    public static void main(String[] args) throws Exception {
        launch();
    }

    //画面サイズ
    final int GAMEWIDTH = Constant.COL * Constant.TILESIZE;
    final int GAMEHEIGHT = Constant.ROW * Constant.TILESIZE;

    final int  RIGHTWIDTH = 200;
    final int  RIGHTHEIGHT = GAMEHEIGHT;

    final int  LEFTWIDTH = 200;
    final int  LEFTHEIGHT = GAMEHEIGHT;

    //表示する次のミノの数
    final int NEXTNUM = 5;

    //スピード
    final double SPEED = 1;
    private int cnt = 0;

    //モード
    enum Mode{
        TITLE, INGAME, GAMEOVER;
    }
    Mode mode = Mode.TITLE;
    Selector selector = Selector.PLAY;
    Draw draw = new Draw(GAMEWIDTH, GAMEHEIGHT, RIGHTWIDTH, RIGHTHEIGHT, LEFTWIDTH, LEFTHEIGHT);
    Game game = new Game();
    Field field = new Field(Constant.ROW, Constant.COL, Constant.TILESIZE);
    Mino mino = new Mino();
    Mino hold = new Mino();
    Mino next = new Mino();
    MinoShape shapes = new MinoShape(NEXTNUM);
    Evaluate evaNow = new Evaluate(Constant.ROW, Constant.COL);
    Evaluate evaNext = new Evaluate(Constant.ROW, Constant.COL);
    CPU cpu = new CPU(Constant.ROW, Constant.COL);
    boolean player = true;

    @Override
    public void start(Stage stage)throws Exception{
        Group root = new Group();
        Scene scene = new Scene(root, GAMEWIDTH + RIGHTWIDTH + LEFTWIDTH, GAMEHEIGHT);
        stage.setTitle("Tetris");
        stage.setScene(scene);
        stage.show();

        Canvas gameCanvas = new Canvas(GAMEWIDTH, GAMEHEIGHT);
        GraphicsContext gcGame = gameCanvas.getGraphicsContext2D();
        gameCanvas.setTranslateX(LEFTWIDTH);
        root.getChildren().addAll(gameCanvas);

        Canvas leftCanvas = new Canvas(LEFTWIDTH, LEFTHEIGHT);
        GraphicsContext gcLeft = leftCanvas.getGraphicsContext2D();
        root.getChildren().addAll(leftCanvas);

        Canvas rightCanvas = new Canvas(RIGHTWIDTH, RIGHTHEIGHT);
        GraphicsContext gcRight = rightCanvas.getGraphicsContext2D();
        rightCanvas.setTranslateX(LEFTWIDTH + GAMEWIDTH);
        root.getChildren().addAll(rightCanvas);

        mino.generate(shapes.shapeQ.poll());
        shapes.addQueue();
        next.generate(shapes.shapeQ.peek());
        hold.generate(Constant.O);
        cpu.decide(game, field, mino, hold, next);

        // //キーが押されたときの処理
        ArrayList<String> input = new ArrayList<>();
        scene.setOnKeyPressed(
            new EventHandler<KeyEvent>(){
                public void handle(KeyEvent e){
                    String key = e.getCode().toString();
                    if(!input.contains(key)){
                        input.add(key);
                    }
                }
            }
        );
        //キーが離されたときの処理
        scene.setOnKeyReleased(
            new EventHandler<KeyEvent>(){
                public void handle(KeyEvent e){
                    String key = e.getCode().toString();
                    input.remove(key);
                }
            }
        );

        Timeline operate = new Timeline(new KeyFrame(Duration.seconds(0.1 / SPEED), new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e){
                switch(mode){
                    case TITLE:
                        if(input.contains("SPACE") || input.contains("ENTER")){
                            switch(selector){
                                case PLAY:
                                    player = true;
                                    break;
                                case CPU:
                                    player = false;
                                    break;
                            }
                            System.out.println("Game Start");
                            mode = Mode.INGAME;
                            game.restart(field, mino, next, hold, shapes, cpu, player);
                            input.clear();
                            cpu.decide(game, field, mino, hold, next);
                        }else if(input.contains("W")){
                            selector = Selector.PLAY;
                        }else if(input.contains("S")){
                            selector = Selector.CPU;
                        }
                        draw.init(gcGame, gcRight, gcLeft);
                        draw.drawTitle(gcGame, selector);
                        break;
                    case GAMEOVER:
                        draw.init(gcGame, gcRight, gcLeft);
                        draw.drawGameOver(gcGame, game);
                        if(input.isEmpty() == false){
                            mode = Mode.TITLE;
                            selector = Selector.PLAY;
                            input.clear();
                        }
                        break;
                    case INGAME:
                            if(input.contains("ESCAPE")){
                                mode = Mode.TITLE;
                                selector = Selector.PLAY;
                                input.clear();
                            }
                            //ユーザー操作
                            if(!player){
                                input.clear();
                                input.add(cpu.action(field, mino));
                            }
                            game.operate(field, mino, hold, shapes, input);
                                if(cnt < 8){
                                    cnt++;
                                }else{
                                    cnt = 0;
                                    game.interrupt(field, mino, hold, next, input, shapes, evaNow, cpu, player);
                                    if(game.gameOver == true){
                                        System.out.println("置いたミノ数 : " + field.minoNUM);
                                        System.out.println("1列 : " + field.line1);
                                        System.out.println("2列 : " + field.line2);
                                        System.out.println("3列 : " + field.line3);
                                        System.out.println("4列 : " + field.line4);
                                        mode = Mode.GAMEOVER;
                                    }
                                }
                            //描画処理
                            draw.init(gcGame, gcRight, gcLeft);
                            draw.drawGame(gcGame, gcRight, gcLeft, game, field, mino, hold, shapes, cpu);
                            if(!player){
                                draw.drawEva(gcLeft, field, mino, hold, evaNow, evaNext);
                            }else{
                                draw.drawGuide(gcLeft);
                            }
                        break;
                }
            }
        }));
        operate.setCycleCount(Timeline.INDEFINITE);
        operate.play();
    }
}