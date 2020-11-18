import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TicTacToe extends Application{
    private static final int SQUARE_SIZE = 120;
    private static final int NUMBER_OF_SQUARES = 3;
    private static int numberOfClicks = 0;
    private static boolean isWon;
    private static boolean isEven;
    private static int round = 0;
    Square[][] board = new Square[NUMBER_OF_SQUARES][NUMBER_OF_SQUARES];
    Scene scene ;
    Pane pane;
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage){
        scene = new Scene(createBoard());
        stage.setScene(scene);
        stage.setTitle("M7Mansour - TicTacToe");
        stage.show();
    }

    private Pane createBoard(){
        pane = new Pane();
        pane.setPrefSize(SQUARE_SIZE * NUMBER_OF_SQUARES , SQUARE_SIZE * NUMBER_OF_SQUARES);
        for(int i = 0 ; i < NUMBER_OF_SQUARES ; i++)
            for(int j = 0 ; j < NUMBER_OF_SQUARES ; j++){
                Square square = new Square(i,j);
                board[i][j] = square;
                pane.getChildren().add(square);
            }
        return pane;
    }

    private class Square extends StackPane{
        private int xCoord;
        private int yCoord;
        private int value = 0;
        Rectangle squareBody = new Rectangle(SQUARE_SIZE - 1 , SQUARE_SIZE - 1);
        Text text = new Text();
        public Square(int xCoord , int yCoord){
            this.xCoord = xCoord;
            this.yCoord = yCoord;
            squareBody.setFill(Color.WHITE);
            squareBody.setStroke(Color.BLACK);
            super.getChildren().addAll(squareBody,text);
            super.setTranslateX(xCoord * SQUARE_SIZE);
            super.setTranslateY(yCoord * SQUARE_SIZE);
            setOnMouseClicked(event -> {
                if(isWon){
                    isWon = false;
                    isEven = false;
                    numberOfClicks = 0;
                    round = 0;
                    scene.setRoot(createBoard());
                }
                else if(isEven){
                    isEven = false;
                    round++;
                    scene.setRoot(createBoard());
                }
                else put(this);
            });
        }
        private void setX(){
            value = 1;
            text.setText("X");
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 60));
            text.setFill(Color.BLUE);
        }
        private void setO(){
            value = 2;
            text.setText("O");
            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 60));
            text.setFill(Color.GREEN);
        }
        private boolean isX(){
            return value == 1;
        }
        private boolean isO(){
            return value == 2;
        }
        private boolean isEmpty(){
            return value == 0;
        }
        private void put(Square square){
            if(numberOfClicks % 2 == 0 && square.isEmpty()) {
                square.setX();
                numberOfClicks++;
            }
            else if(numberOfClicks % 2 != 0 && square.isEmpty()){
                square.setO();
                numberOfClicks++;
            }
            if(numberOfClicks - (NUMBER_OF_SQUARES * NUMBER_OF_SQUARES * round) >= NUMBER_OF_SQUARES * 2 - 1){
                check(square);
            }
            if(isWon) return;
            if(numberOfClicks - (NUMBER_OF_SQUARES * NUMBER_OF_SQUARES * round) == NUMBER_OF_SQUARES * NUMBER_OF_SQUARES){
                isEven = true;
            }

        }

        private void check(Square square){
            if(checkLine(xCoord)){
                isWon = true;
                drawLine(xCoord);
                return;
            }
            if(checkCol(yCoord)){
                isWon = true;
                drawCol(yCoord);
                return;
            }
            if(xCoord == yCoord){
                if(checkRadius1()){
                    isWon = true;
                    drawRadius1();
                    return;
                }
                if(xCoord == 1)
                    if(checkRadius2()){
                        isWon = true;
                        drawRadius2();
                        return;
                    }
            }
            if(Math.abs(xCoord - yCoord) == 2)
                if(checkRadius2()){
                    isWon = true;
                    drawRadius2();
                    return;
                }
        }
        private boolean checkLine(int x){
            return board[x][0].value == board[x][1].value && board[x][1].value == board[x][2].value;
        }
        private boolean checkCol(int y){
            return board[0][y].value == board[1][y].value && board[1][y].value == board[2][y].value;
        }
        private boolean checkRadius1(){
            return board[0][0].value == board[1][1].value && board[1][1].value == board[2][2].value;
        }
        private boolean checkRadius2(){
            return board[0][2].value == board[1][1].value && board[1][1].value == board[2][0].value;
        }

        private void drawLine(int x){
            draw(board[x][0].getCenter()[0],board[x][0].getCenter()[1],board[x][2].getCenter()[0],board[x][2].getCenter()[1]);
        }
        private void drawCol(int y){
            draw(board[0][y].getCenter()[0],board[0][y].getCenter()[1],board[2][y].getCenter()[0],board[2][y].getCenter()[1]);
        }
        private void drawRadius1(){
            draw(board[0][0].getCenter()[0],board[0][0].getCenter()[1],board[2][2].getCenter()[0],board[2][2].getCenter()[1]);
        }
        private void drawRadius2(){
            draw(board[2][0].getCenter()[0],board[2][0].getCenter()[1],board[0][2].getCenter()[0],board[0][2].getCenter()[1]);
        }
        private int[] getCenter(){
            return new int[]{xCoord * SQUARE_SIZE + SQUARE_SIZE / 2 , yCoord * SQUARE_SIZE + SQUARE_SIZE / 2};
        }
        private void draw(int x1 , int y1 , int x2 , int y2){
            Line line = new Line();
            line.setStartX(x1);
            line.setStartY(y1);
            line.setEndX(x1);
            line.setEndY(y1);
            pane.getChildren().add(line);
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),new KeyValue(line.endXProperty(), x2),new KeyValue(line.endYProperty(), y2)));
            timeline.play();

        }
    }
}
