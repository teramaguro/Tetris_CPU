import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

//Fieldクラス
public class Field{
    //行数と列数
    int row;
    int col;
    int tilesize;
    int blocks[][];
    //スピンフラグ
    boolean spin;
    boolean spinMini;
    //置いたミノの数
    int minoNUM;
    //同時消しライン数
    int line1;
    int line2;
    int line3;
    int line4;
    //Tスピン数
    int tsm;
    int tss;
    int tsd;
    int tst;

    //コンストラクタ
    public Field(int row, int col, int tilesize){
        this.row = row;
        this.col = col;
        this.tilesize = tilesize;
        init();
    }
    //初期化メソッド
    public void init(){
        blocks = new int[row][col];
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                blocks[i][j] = 0;
            }
        }
        spin = false;
        minoNUM = 0;
        line1 = 0;
        line2 = 0;
        line3 = 0;
        line4 = 0;
        tsm = 0;
        tss = 0;
        tsd = 0;
        tst = 0;
    }
    //衝突判定メソッド
    public boolean isCollide(Mino mino){
        for(int i = 0; i < mino.row; i++){
            for(int j = 0; j < mino.col; j++){
                //ミノのブロックがあるとき
                if(mino.blocks[i][j] != 0){
                    //ブロックのマスがフィールド外もしくはフィールドにブロックがあるときtrue
                    if(mino.y + i < 0 || mino.y + i >= row || mino.x + j < 0 || mino.x + j >= col || blocks[(int)mino.y + i][(int)mino.x + j] != 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //固定メソッド
    public void fixed(Mino mino){
        for(int i = 0; i < mino.row; i++){
            for(int j = 0; j < mino.col; j++){
                //ミノのブロックがあるとき
                if(mino.blocks[i][j] != 0){
                    // if(mino.x + j < 0){System.out.println("x < 0");}
                    // else if(mino.x + j > col - 1){System.out.println("x > col - 1");}
                    // else if(mino.y + i < 0){System.out.println("y < 0");}
                    // else if(mino.y + i > row - 1){System.out.println("y > row - 1");}
                    if(mino.x + j < 0){}
                    else if(mino.x + j > col - 1){}
                    else if(mino.y + i < 0){}
                    else if(mino.y + i > row - 1){}
                    else{
                        //フィールドに追加
                        blocks[(int)mino.y + i][(int)mino.x + j] = mino.blocks[i][j];
                    }
                }
            }
        }
        //Tスピンか判定
        int x = (int)mino.x;
        int y = (int)mino.y;
        int nw = (x < 0 || x > col - 1 || y < 0 || y > row - 1 || blocks[y][x] != 0) ? 1 : 0;
        x+=2;
        int ne = (x < 0 || x > col - 1 || y < 0 || y > row - 1 || blocks[y][x] != 0) ? 1 : 0;
        x-=2;
        y+=2;
        int sw = (x < 0 || x > col - 1 || y < 0 || y > row - 1 || blocks[y][x] != 0) ? 1 : 0;
        x+=2;
        int se = (x < 0 || x > col - 1 || y < 0 || y > row - 1 || blocks[y][x] != 0) ? 1 : 0;
        //Tスピンミニか判定
        x = (int)mino.x;
        y = (int)mino.y;
        x++;
        int up = (x >= 0 && x < col && y >= 0 && y < row && blocks[y][x] == 0) ? 1 : 0;
        y+=2;
        int down = (x >= 0 && x < col && y >= 0 && y < row && blocks[y][x] == 0) ? 1 : 0;
        x++;
        y--;
        int right = (x >= 0 && x < col && y >= 0 && y < row && blocks[y][x] == 0) ? 1 : 0;
        x-=2;
        int left = (x >= 0 && x < col && y >= 0 && y < row && blocks[y][x] == 0) ? 1 : 0;
        if(mino.shape == Constant.T && mino.spin && nw + ne + sw + se >= 3){
            spin = true;
            // System.out.println("spin");
            if(up + down + right + left == 1){
                spinMini = false;
            }else{
                spinMini = true;
            }
        }
        minoNUM++;
    }
    //ライン消しメソッド
    public int delete(){
        int line = 0;
        for(int i = 0; i < row; i++){
            //横一列そろっているとき
            if(isAlign(blocks[i])){
                line++;
                for(int y = i; y > 0; y--){
                    for(int x = 0; x < col; x++){
                        //一段下に落とす
                        blocks[y][x] = blocks[y - 1][x];
                    }
                }
            }
        }
        return line;
    }
    //ライン判定メソッド
    public boolean isAlign(int[] line){
        //横一列そろっているか判定
        for(int i = 0; i < line.length; i++){
            if(line[i] == 0){
                return false;
            }
        }
        return true;
    }
    //ゲームオーバー判定メソッド
    public boolean isGameOver(){
        //一番上の行にブロックがあるときtrue
        for(int i = 0; i < col; i++){
            if(blocks[0][i] != 0){
                return true;
            }
        }
        //一番上の一つ下の行の真ん中4マスにブロックがあるときtrue
        for(int i = 0; i < 4; i++){
            if(blocks[1][i + 3] != 0){
                return true;
            }
        }
        return false;
    }
    //cloneメソッド
    public Field clone(){
        Field to = new Field(row, col, tilesize);
        to.spin = spin;
        to.minoNUM = minoNUM;
        to.line1 = line1;
        to.line2 = line2;
        to.line3 = line3;
        to.line4 = line4;
        to.tss = tss;
        to.tsd = tsd;
        to.tst = tst;
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                to.blocks[i][j] = blocks[i][j];
            }
        }
        return to;
    }
    //色取得メソッド
    public Color getColor(int shape){
        switch(shape){
            case 1:
                return Constant.iColor;
            case 2:
                return Constant.oColor;
            case 3:
                return Constant.sColor;
            case 4:
                return Constant.zColor;
            case 5:
                return Constant.jColor;
            case 6:
                return Constant.lColor;
            case 7:
                return Constant.tColor;
            default:
                return Color.WHITE;
        }
    }
    //描画メソッド
    public void draw(GraphicsContext gc){
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                //ブロックがあるとき
                if(blocks[i][j] != 0){
                    //色取得
                    Color color = getColor(blocks[i][j]);
                    gc.setFill(color);
                    //マスを塗りつぶす
                    gc.fillRect(j *tilesize, i * tilesize, tilesize, tilesize);
                    //ふちの太さ
                    int w = 5;
                    //マスの下と右を暗く塗る
                    gc.setFill(color.darker());
                    gc.fillRect(j * tilesize, i * tilesize + tilesize - w, tilesize, w);
                    gc.fillRect(j * tilesize + tilesize - w, i * tilesize, w, tilesize);
                    //マスの明るさをもつ白色を生成
                    gc.setFill(Color.WHITE.deriveColor(0, 1, color.getBrightness(), 1));
                    //白色で塗る三角の座標
                    double x1 = j * tilesize + w;
                    double y1 = i * tilesize + w;

                    double x2 = j * tilesize + tilesize - w;
                    double y2 = i * tilesize + w;

                    double x3 = j * tilesize + w;
                    double y3 = i * tilesize + w * 3;
                    //三角で塗る
                    gc.fillPolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
                //ブロックがないとき
                }else{
                    //ダークグレーでストロークを塗る
                    gc.setStroke(Color.DARKGRAY);
                    gc.strokeRect(j * tilesize, i *tilesize, tilesize, tilesize);
                }
            }
        }
    }
}