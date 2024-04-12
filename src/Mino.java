import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

//Minoクラス
public class Mino{
    int[][] blocks;
    //座標
    double x;
    double y;
    //方向
    int dir;
    //行数と列数
    int row;
    int col;
    //形の値
    int shape;
    //スピンスラグ
    boolean spin;
    //色
    Color color = Color.WHITE;
    //生成メソッド
    public void generate(int shape){
        this.shape = shape;
        dir = Constant.UP;
        spin = false;
        switch(this.shape){
            case 1:
                iMino();
                break;
            case 2:
                oMino();
                break;
            case 3:
                sMino();
                break;
            case 4:
                zMino();
                break;
            case 5:
                jMino();
                break;
            case 6:
                lMino();
                break;
            case 7:
                tMino();
                break;
            case 0:
                System.out.println("空ミノ生成");
                emptyMino();
                break;
            default:
                System.out.println("init error");
                break;
        }
    }
    //空ミノ生成メソッド
    public void emptyMino(){
        x = 3;
        y = 0;
        row = 1;
        col = 1;
        blocks = new int[row][col];
        color = Color.WHITE;
        blocks[0][0] = 0;
    }
    //iMino生成メソッド
    public void iMino(){
        x = 3;
        y = 0;
        row = 4;
        col = 4;
        blocks = new int[row][col];
        color = Constant.iColor;
        blocks[0][0] = 0; blocks[0][1] = 0; blocks[0][2] = 0; blocks[0][3] = 0;
        blocks[1][0] = 1; blocks[1][1] = 1; blocks[1][2] = 1; blocks[1][3] = 1;
        blocks[2][0] = 0; blocks[2][1] = 0; blocks[2][2] = 0; blocks[2][3] = 0;
        blocks[3][0] = 0; blocks[3][1] = 0; blocks[3][2] = 0; blocks[3][3] = 0;
    }
    //oMino生成メソッド
    public void oMino(){
        x = 3;
        y = -1;
        row = 4;
        col = 4;
        color = Constant.oColor;
        blocks = new int[row][col];
        blocks[0][0] = 0; blocks[0][1] = 0; blocks[0][2] = 0; blocks[0][3] = 0;
        blocks[1][0] = 0; blocks[1][1] = 2; blocks[1][2] = 2; blocks[1][3] = 0;
        blocks[2][0] = 0; blocks[2][1] = 2; blocks[2][2] = 2; blocks[2][3] = 0;
        blocks[3][0] = 0; blocks[3][1] = 0; blocks[3][2] = 0; blocks[3][3] = 0;
        // blocks[0][0] = 2; blocks[0][1] = 2;
        // blocks[1][0] = 2; blocks[1][1] = 2;
    }
    //sMino生成メソッド
    public void sMino(){
        x = 3;
        y = 0;
        row = 3;
        col = 3;
        color = Constant.sColor;
        blocks = new int[row][col];
        blocks[0][0] = 0; blocks[0][1] = 3; blocks[0][2] = 3;
        blocks[1][0] = 3; blocks[1][1] = 3; blocks[1][2] = 0;
        blocks[2][0] = 0; blocks[2][1] = 0; blocks[2][2] = 0;
    }
    //zMino生成メソッド
    public void zMino(){
        x = 3;
        y = 0;
        row = 3;
        col = 3;
        color = Constant.zColor;
        blocks = new int[row][col];
        blocks[0][0] = 4; blocks[0][1] = 4; blocks[0][2] = 0;
        blocks[1][0] = 0; blocks[1][1] = 4; blocks[1][2] = 4;
        blocks[2][0] = 0; blocks[2][1] = 0; blocks[2][2] = 0;
    }
    //jMino生成メソッド
    public void jMino(){
        x = 3;
        y = 0;
        row = 3;
        col = 3;
        color = Constant.jColor;
        blocks = new int[row][col];
        blocks[0][0] = 5; blocks[0][1] = 0; blocks[0][2] = 0;
        blocks[1][0] = 5; blocks[1][1] = 5; blocks[1][2] = 5;
        blocks[2][0] = 0; blocks[2][1] = 0; blocks[2][2] = 0;
    }
    //lMino生成メソッド
    public void lMino(){
        x = 3;
        y = 0;
        row = 3;
        col = 3;
        color = Constant.lColor;
        blocks = new int[row][col];
        blocks[0][0] = 0; blocks[0][1] = 0; blocks[0][2] = 6;
        blocks[1][0] = 6; blocks[1][1] = 6; blocks[1][2] = 6;
        blocks[2][0] = 0; blocks[2][1] = 0; blocks[2][2] = 0;
    }
    //tMino生成メソッド
    public void tMino(){
        x = 3;
        y = 0;
        row = 3;
        col = 3;
        color = Constant.tColor;
        blocks = new int[row][col];
        blocks[0][0] = 0; blocks[0][1] = 7; blocks[0][2] = 0;
        blocks[1][0] = 7; blocks[1][1] = 7; blocks[1][2] = 7;
        blocks[2][0] = 0; blocks[2][1] = 0; blocks[2][2] = 0;
    }
    //一気に下に移動するメソッド
    public void bottom(Field field){
        //下に動き続けられる限り下移動
        while(move(Constant.O, field));
    }
    //移動メソッド
    public boolean move(int dir, Field field){
        //移動先のミノをクローンして作成
        Mino next = clone();
        switch(dir){
            case Constant.O:
                next.y++;
                break;
            case Constant.RIGHT:
                next.x++;
                break;
            case Constant.LEFT:
                next.x--;
        }
        //クローンがフィールドと衝突しなければ移動可能
        if(!field.isCollide(next)){
            //座標更新
            x = next.x;
            y = next.y;
            spin = false;
            return true;
        }else{
            return false;
        }
    }
    //回転メソッド
    public boolean rotate(Mino next, Field field){
        //Oミノは回転しない
        if(next.shape == Constant.O){
            return false;
        }
        //接地したときの回転をスピンとみなす
        Mino onGround = clone();
        spin = !onGround.move(Constant.O, field);
        //クローンがフィールドと衝突しなければ移動可能
        if(!field.isCollide(next)){
            for(int i = 0; i < row; i++){
                for(int j = 0; j < col; j++){
                    blocks[i][j] = next.blocks[i][j];
                }
            }
            x = next.x;
            y = next.y;
            return true;
        }
        spin = false;
        return false;
    }
    //右回転メソッド
    public boolean rotateR(Field field){
        //移動先のミノをクローンして作成
        Mino next = this.clone();
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                next.blocks[j][col - i - 1] = blocks[i][j];
            }
        }
        int[][] translation = translate(Constant.RIGHT);
        for(int i = 0; i < 5; i++){
            next.x = (int)x + translation[i][0];
            next.y = (int)y + translation[i][1];
            if(rotate(next, field)){
                if(dir < 3){dir++;}
                else{dir = 0;}
                return true;
            }
        }
        return false;
    }
    //左回転メソッド
    public boolean rotateL(Field field){
        //移動先のミノをクローンして作成
        Mino next = this.clone();
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                next.blocks[row - j - 1][i] = blocks[i][j];
            }
        }
        int[][] translation = translate(Constant.LEFT);
        for(int i = 0; i < 5; i++){
            next.x = (int)x + translation[i][0];
            next.y = (int)y + translation[i][1];
            if(rotate(next, field)){
                if(dir > 0){
                    dir--;
                }
                else{
                    dir = 3;
                }
                return true;
            }
        }
        return false;
    }
    //回転平行移動の座標補正を返すメソッド
    //参考 https://tetrisch.github.io/main/srs.html(間違いあり)
    //     https://tetris.fandom.com/wiki/SRS
    public int[][] translate(int dir){
        int[][] translation = new int[5][2];
        translation[0][0] = 0;
        translation[0][1] = 0;
        if(shape == Constant.I){
            switch(this.dir){
                case Constant.UP:
                    if(dir == Constant.RIGHT){
                        translation[1][0] = -2;
                        translation[1][1] = 0;

                        translation[2][0] = 1;
                        translation[2][1] = 0;

                        translation[3][0] = -2;
                        translation[3][1] = 1;

                        translation[4][0] = 1;
                        translation[4][1] = -2;
                    }else{
                        translation[1][0] = -1;
                        translation[1][1] = 0;

                        translation[2][0] = 2;
                        translation[2][1] = 0;

                        translation[3][0] = -1;
                        translation[3][1] = -2;

                        translation[4][0] = 2;
                        translation[4][1] = 1;
                    }
                    break;
                case Constant.RIGHT:
                    if(dir == Constant.RIGHT){
                        translation[1][0] = -1;
                        translation[1][1] = 0;

                        translation[2][0] = 2;
                        translation[2][1] = 0;

                        translation[3][0] = -1;
                        translation[3][1] = -2;

                        translation[4][0] = 2;
                        translation[4][1] = 1;
                    }else{
                        translation[1][0] = 2;
                        translation[1][1] = 0;

                        translation[2][0] =-1;
                        translation[2][1] = 0;

                        translation[3][0] = 2;
                        translation[3][1] = -1;

                        translation[4][0] = -1;
                        translation[4][1] = 2;
                    }
                    break;
                case Constant.O:
                    if(dir == Constant.RIGHT){
                        translation[1][0] = 2;
                        translation[1][1] = 0;

                        translation[2][0] = -1;
                        translation[2][1] = 0;

                        translation[3][0] = 2;
                        translation[3][1] = -1;

                        translation[4][0] = -1;
                        translation[4][1] = 2;
                    }else{
                        translation[1][0] = 1;
                        translation[1][1] = 0;

                        translation[2][0] = -2;
                        translation[2][1] = 0;

                        translation[3][0] = 1;
                        translation[3][1] = 2;

                        translation[4][0] = -2;
                        translation[4][1] = -1;
                    }
                    break;
                case Constant.LEFT:
                    if(dir == Constant.RIGHT){
                        translation[1][0] = 1;
                        translation[1][1] = 0;

                        translation[2][0] = -2;
                        translation[2][1] = 0;

                        translation[3][0] = 1;
                        translation[3][1] = 2;

                        translation[4][0] = -2;
                        translation[4][1] = -1;
                    }else{
                        translation[1][0] = -2;
                        translation[1][1] = 0;

                        translation[2][0] = 1;
                        translation[2][1] = 0;

                        translation[3][0] = -2;
                        translation[3][1] = 1;

                        translation[4][0] = 1;
                        translation[4][1] = -2;
                    }
                    break;
            }
        }else{
            switch(this.dir){
                case Constant.UP:
                    translation[1][0] = -1;
                    translation[1][1] = 0;

                    translation[2][0] = -1;
                    translation[2][1] = -1;

                    translation[3][0] = 0;
                    translation[3][1] = 2;

                    translation[4][0] = -1;
                    translation[4][1] = 2;
                    if(dir == Constant.RIGHT){
                        break;
                    }else{
                        for(int i = 0; i < 5; i++){
                            translation[i][0] *= -1;
                        }
                        break;
                    }
                case Constant.RIGHT:
                    translation[1][0] = 1;
                    translation[1][1] = 0;

                    translation[2][0] = 1;
                    translation[2][1] = 1;

                    translation[3][0] = 0;
                    translation[3][1] = -2;

                    translation[4][0] = 1;
                    translation[4][1] = -2;
                    if(dir == Constant.RIGHT){
                        break;
                    }else{
                        break;
                    }
                case Constant.O:
                    translation[1][0] = 1;
                    translation[1][1] = 0;

                    translation[2][0] = 1;
                    translation[2][1] = -1;

                    translation[3][0] = 0;
                    translation[3][1] = 2;

                    translation[4][0] = 1;
                    translation[4][1] = 2;
                    if(dir == Constant.RIGHT){
                        break;
                    }else{
                        for(int i = 0; i < 5; i++){
                            translation[i][0] *= -1;
                        }
                        break;
                    }
                case Constant.LEFT:
                    translation[1][0] = -1;
                    translation[1][1] = 0;

                    translation[2][0] = -1;
                    translation[2][1] = 1;

                    translation[3][0] = 0;
                    translation[3][1] = -2;

                    translation[4][0] = -1;
                    translation[4][1] = -2;
                    if(dir == Constant.RIGHT){
                        break;
                    }else{
                        break;
                    }
            }
        }
        return translation;
    }
    //hold判定メソッド
    public boolean canHold(Mino hold, Field field){
        return true;
        // Mino collisionCheck = new Mino();
        //     collisionCheck.generate(hold.shape);
        //     collisionCheck.x = x;
        //     collisionCheck.y = y;
        //     //ホールドミノがフィールドと衝突するか判定
        //     if(!field.isCollide(collisionCheck)){
        //         return true;
        //     }
        //     else{
        //         return false;
        //     }
    }
    //下に落ちたか判定メソッド
    public boolean isBottom(Field field){
        Mino next = clone();
        next.y++;
        if(field.isCollide(next)){
            return true;
        }
        else{
            return false;
        }
    }
    //クローンメソッド
    public Mino clone(){
        Mino to = new Mino();
        to.x = this.x;
        to.y = this.y;
        to.row = this.row;
        to.col = this.col;
        to.color = this.color;
        to.shape = this.shape;
        to.blocks = new int[row][col];
        to.spin = this.spin;
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                to.blocks[i][j] = this.blocks[i][j];
            }
        }
        return to;
    }
    //描画メソッド
    public void draw(GraphicsContext gc, int size){
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                //ブロックがあるか判定
                if(blocks[i][j] != 0){
                    //ふちの太さ
                    double w = size / 8;
                    //マスを塗りつぶす
                    gc.setFill(color);
                    gc.fillRect((x + j) * size, (y + i) * size, size, size);
                    //右と下を暗く塗りつぶす
                    gc.setFill(color.darker());
                    gc.fillRect((x + j) * size, (y + i) * size + size - w, size, w);
                    gc.fillRect((x + j) * size + size - w, (y + i) * size, w, size);
                    //色の明るさをもつ白色を生成
                    gc.setFill(Color.WHITE.deriveColor(0, 1, color.getBrightness(), 1));
                    //白く三角で塗りつぶす座標
                    double x1 = (x + j) * size + w;
                    double y1 = (y + i) * size + w;

                    double x2 = (x + j) * size + size - w;
                    double y2 = (y + i) * size + w;

                    double x3 = (x + j) * size + w;
                    double y3 = (y + i) * size + w * 3;
                    //白く三角で塗りつぶす
                    gc.fillPolygon(new double[]{x1, x2, x3}, new double[]{y1, y2, y3}, 3);
                }
            }
        }
    }
}
