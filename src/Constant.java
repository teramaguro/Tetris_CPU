import javafx.scene.paint.Color;

public class Constant {
    //行と列数
    static final int ROW = 20;
    static final int COL = 10;
    //タイルサイズ
    static final int TILESIZE = 40;

    //方向
    static final int UP = 0;
    static final int DOWN = 2;
    static final int RIGHT = 1;
    static final int LEFT = 3;

    //ミノの形定数
    static final int I = 1;
    static final int O = 2;
    static final int S = 3;
    static final int Z = 4;
    static final int J = 5;
    static final int L = 6;
    static final int T = 7;

    //色
    static final Color iColor = Color.valueOf("#04D3F9");
    static final Color oColor = Color.valueOf("#FDCD07");
    static final Color sColor = Color.valueOf("#70EC23");
    static final Color zColor = Color.valueOf("#F41842");
    static final Color jColor = Color.valueOf("#3118F4");
    static final Color lColor = Color.valueOf("#FC6F08");
    static final Color tColor = Color.valueOf("#AC03F5");

    //スコア
    static final int SINGLE = 10;
    static final int DOUBLE = 30;
    static final int TRIPLE = 50;
    static final int TETRIS = 80;
    static final int TSINGLE = 80;
    static final int TDOUBLE = 120;
    static final int TTRIPLE = 200;
}
