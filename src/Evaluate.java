import java.util.ArrayList;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

//Evaluateクラス
public class Evaluate{
    //行数と列数
    int row;
    int col;
    //フィールドの評価値
    double evaluation;
    //高低差
    double difference;
    //最大の高さ
    double maxHeight;
    //平均の高さ
    double aveHeight;
    // //穴の数
    // double hole;
    //屋根の下の数(穴の数)
    double ceiling;
    //穴の深さ
    double ceilingDepth;
    //そろった行数
    double line;
    //Tスピン数
    double tSpin;
    //Tスピン地形評価
    double tSpinTerrain;
    //そろった行の最大高さ
    double lineMaxHeight;
    //リーチ行数
    double oneMoreNum;
    //ホールドミノの評価値
    int holdEvaluation;
    //コンストラクタ
    public Evaluate(int row, int col){
        this.row = row;
        this.col = col;
    }
    //フィールド評価メソッド
    public void evaluate(Field field, Mino holdMino){
        //ブロックのコピーを生成
        int[][] copy = new int[row][col];
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                copy[i][j] = field.blocks[i][j];
            }
        }
        //評価値をリセット
        evaluation = 0;
        difference = 0;
        maxHeight = 0;
        aveHeight = 0;
        // hole = 0;
        ceiling = 0;
        ceilingDepth = 0;
        line = 0;
        lineMaxHeight = 0;
        tSpin = 0;
        tSpinTerrain = 0;
        oneMoreNum = 0;
        holdEvaluation = 0;

        //そろった行数を評価
        for(int i = 0; i < row; i++){
            if(field.isAlign(copy[i])){
                line++;
                if(lineMaxHeight < row - i + 1){
                    lineMaxHeight = row - i + 1;
                }
            }
        }

        //リーチ行と空白の列を保持 Tスピン地形評価に使用
        ArrayList<int[]> oneMoreCol = new ArrayList<>();
        //リーチ行数を評価
        for(int i = 0; i < row; i++){
            int airNum = 0;
            int airPos = -1;
            boolean oneMore = true;
            for(int j = 0; j < col; j++){
                if(copy[i][j] == 0){
                    airNum++;
                    airPos = j;
                }
            }
            if(airNum == 1){
                for(int j = 0; j < i; j++){
                    if(copy[j][airPos] != 0){
                        oneMore = false;
                        break;
                    }
                }
                if(oneMore){
                    oneMoreNum++;
                    oneMoreCol.add(new int[]{i, airPos});
                }
            }
        }

        //高低差と平均の高さと最大の高さを評価
        //各列の高さを保持する配列
        int[] height = new int[col];
        //各列の高さを取得
        for(int i = 0; i < col; i++){
            height[i] = 0;
            for(int j = 0; j < row; j++){
                if(copy[j][i] != 0){
                    height[i] = row - j;
                    break;
                }
            }
            if(height[i] > maxHeight){maxHeight = height[i];}
            aveHeight += (height[i]);
        }
        aveHeight /= col;
        for(int i = 0; i < col - 1; i++){
            difference += Math.abs(height[i] - height[i + 1]);
        }

        // //穴の数を評価
        // int[][] holes = new int[row][col];
        // //穴を-1で初期化
        // for(int i = 0; i < row; i++){
        //     for(int j = 0; j < col; j++){
        //         holes[i][j] = copy[i][j];
        //         if(holes[i][j] == 0){holes[i][j] = -1;}
        //     }
        // }
        // //ミノ出現場所は0で初期化
        // holes[0][(int)col / 2] = 0;
        // while(true){
        //     boolean update = false;
        //     for(int i = 0; i < row; i++){
        //         for(int j = 0; j < col; j++){
        //             int up = i - 1;
        //             int down = i + 1;
        //             int left = j - 1;
        //             int right = j + 1;
        //             if(holes[i][j] == -1){
        //                 if(up >= 0 && holes[up][j] == 0){
        //                     holes[i][j] = 0;
        //                     update = true;
        //                 }else if(down < row && holes[down][j] == 0){
        //                     holes[i][j] = 0;
        //                     update = true;
        //                 }else if(left >= 0 && holes[i][left] == 0){
        //                     holes[i][j] = 0;
        //                     update = true;
        //                 }
        //                 else if(right < col && holes[i][right] == 0){
        //                     holes[i][j] = 0;
        //                     update = true;
        //                 }
        //             }
        //         }
        //     }
        //     if(!update){break;}
        // }
        // for(int i = 0; i < row; i++){
        //     for(int j = 0; j < col; j++){
        //         if(holes[i][j] == -1){hole++;}
        //     }
        // }

        //屋根があるマスを評価(穴の数)
        for(int i = 0; i < col; i++){
            int yS = row;
            int deepest = 0;
            for(int j = 0; j < row; j++){
                if(copy[j][i] != 0){
                    yS = j;
                    break;
                }
            }
            for(int j = yS; j < row; j++){
                if(copy[j][i] == 0){
                    deepest = j;
                    ceiling++;
                }
            }
            for(int j = yS; j < deepest; j++){
                if(copy[j][i] != 0){
                    ceilingDepth++;
                }
            }
        }
        //最大の高さの補正値k
        double maxHeightCor = 0;
        if(maxHeight > 8){maxHeightCor = maxHeight - 8;}
        //ホールドミノの評価
        switch(holdMino.shape){
            case Constant.I:
                holdEvaluation = 7;
                break;
            case Constant.T:
                holdEvaluation = 3;
                break;
            default:
                holdEvaluation = 0;
                break;
        }

        if(field.spin == true && field.spinMini == false){
            tSpin = line;
        }
        // //Tスピン評価
        // if(field.spin){
        //     if(line == 1 && field.spinMini){
        //         tSpin = 0;
        //     }else{
        //         tSpin = line;
        //     }
        //     // System.out.println("Tspin");
        // }
        //Tスピン地形評価
        for(int i = 0; i < oneMoreCol.size(); i++){
            int airNum = 0;
            int airCol = oneMoreCol.get(i)[0];
            int airRow = oneMoreCol.get(i)[1];
            for(int j = 0; j < col; j++){
                if(airCol > 0 && copy[airCol - 1][j] == 0){
                    airNum++;
                }
            }
            if(airNum == 3 && airCol > 4 && airRow > 0 && airRow < col - 1 && copy[airCol - 1][airRow - 1] == 0 && copy[airCol - 1][airRow + 1] == 0){
                boolean aboveClear = true;
                for(int k = 0; k < airCol - 2; k++){
                    if(copy[k][airRow - 1] != 0 || copy[k][airRow + 1] != 0){
                        aboveClear = false;
                        break;
                    }
                }
                int left = (copy[airCol - 2][airRow - 1] != 0) ? 1 : 0;
                int right = (copy[airCol - 2][airRow + 1] != 0) ? 1 : 0;
                if(aboveClear && left + right != 2){
                    tSpinTerrain += 1;
                    //屋根がついているとさらに加点
                    if(left + right == 1){
                        tSpinTerrain += 1;
                    }
                    // System.out.println("T字");
                }
            }
        }

        //Tスピンを考慮した評価関数
        evaluation = Math.pow(line * (1 + 1), 2) //ライン評価
                    + Math.max(lineMaxHeight - 10, 0) * 50 //ライン最大高さ評価
                    + tSpin * 50
                    + tSpinTerrain * 50
                    + Math.sqrt(Math.min(oneMoreNum, 4) * 100) * (maxHeightCor/ 2 + 1) //リーチ評価
                    + holdEvaluation //ホールド評価
                    - difference * 1 //高低差評価
                    - aveHeight * 0.5 //平均高さ評価
                    - Math.pow(maxHeightCor * 3, 2) //最大高さ評価
                    - Math.max(maxHeight - 10, 0) * 100 //6行を危険域とする
                    - ceiling * 10 //穴の数評価
                    -ceilingDepth * 5; //穴の深さ評価

        //Tスピンを考慮しない評価関数
    //     evaluation = Math.pow(line * 1, 3) //ライン評価
    //                 + Math.max(lineMaxHeight - 10, 0) * 50 //ライン最大高さ評価
    //                 + Math.sqrt(Math.min(oneMoreNum, 4) * 100) * (maxHeightCor/ 2 + 1) //リーチ評価
    //                 + holdEvaluation //ホールド評価
    //                 - difference * 1 //高低差評価
    //                 - aveHeight * 0.5 //平均高さ評価
    //                 - Math.pow(maxHeightCor * 3, 2) //最大高さ評価
    //                 - Math.max(maxHeight - 10, 0) * 100 //6行を危険域とする
    //                 - ceiling * 10 //穴の数評価
    //                 -ceilingDepth * 5; //穴の深さ評価
    }
    //マスが穴かどうか判定するメソッド
    public boolean isHole(int y, int x, int[][] copy){
        if(copy[y][x] != 0){
            return false;
        }
        int up = y - 1;
        int down = y + 1;
        int left = x - 1;
        int right = x + 1;
        if(up >= 0 && copy[up][x] == 0){
            return false;
        }
        if(down < row && copy[down][x] == 0){
            return false;
        }
        if(left >= 0 && copy[y][left] == 0){
            return false;
        }
        if(right < col && copy[y][right] == 0){
            return false;
        }
        return true;
    }
    //評価値描画メソッド
    public void drawEvaluation(GraphicsContext gc, double x, double y){
        double size = 20;
        //文字を描画
        Font font = Font.font("Times New Roman", FontWeight.BOLD, 20);
        gc.setFont(font);
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("評価値 : " + String.format("%.1f", evaluation), x, y + size * 0);
        gc.fillText("ライン数 : " + line, x, y + size * 1);
        gc.fillText("リーチ数 : " + oneMoreNum, x, y + size * 2);
        gc.fillText("高低差 : " + difference, x, y + size * 3);
        gc.fillText("最大高さ : " + maxHeight, x, y + size * 4);
        gc.fillText("平均高さ : " + aveHeight, x, y + size * 5);
        // gc.fillText("穴の数 : " + hole, x, y + size * 4);
        gc.fillText("穴の数 : " + ceiling, x, y + size * 6);
        gc.fillText("穴の深さ : " + ceilingDepth, x, y + size * 7);
        gc.fillText("ホールド評価 : " + holdEvaluation, x, y + size * 8);
    }
}
