import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class TicTacToe implements ActionListener {
    public static void main(String[] args) {
        new TicTacToe();
    }

    private JFrame frame;
    private JLabel whoseTurn;
    private JLabel score;
    private JFrame resultFrame;
    private JLabel resultLabel;
    private JButton[][] gridButtons = new JButton[3][3];
    private int nextPlayer;
    private int[] scores = new int[2];
    private Stack<String> undoList = new Stack<>();
    private int numberOfMoves;

    public TicTacToe() {
        frame = new JFrame("Tic-Tac-Toe");

        whoseTurn = new JLabel("Player 1 Next");
        whoseTurn.setBounds(20, 20, 100, 20);
        frame.add(whoseTurn);

        addGrid();

        addButton("New Game",20,270,100,50);
        addButton("Reset",125,270,50,50);
        addButton("Undo",180,270,50,50);

        score = new JLabel("Score: 0 - 0");
        score.setBounds(25, 320, 150, 50);
        frame.add(score);

        frame.setLayout(null);
        frame.setSize(250,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        numberOfMoves = 0;
        nextPlayer = 0;
        resetScores();

        resultFrame = new JFrame();
        resultLabel = new JLabel();
        resultLabel.setBounds(10,5, 100, 30);
        resultFrame.add(resultLabel);
        resultFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        resultFrame.setLayout(null);
        resultFrame.setSize(120,70);
        resultFrame.setVisible(false);
    }

    private void addButton(String text, int x, int y, int width, int height){
        JButton button = new JButton(text);
        button.setBounds(x,y,width,height);
        button.addActionListener(this);
        frame.add(button);
    }

    private void addGrid(){
        int xOrigin = 20;
        int yOrigin = 50;
        int xSize = 70;
        int ySize = 70;
        int xPosition;
        int yPosition;

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                xPosition = xOrigin + j * xSize;
                yPosition = yOrigin + i * ySize;

                gridButtons[i][j] = new JButton();
                gridButtons[i][j].setFont(new Font("Arial", Font.PLAIN, 50));
                gridButtons[i][j].setBounds(xPosition, yPosition, xSize, ySize);
                gridButtons[i][j].setActionCommand(Integer.toString(i) + Integer.toString(j));
                gridButtons[i][j].addActionListener(this);
                gridButtons[i][j].setName("button" + i + j);
                frame.add(gridButtons[i][j]);
            }
        }
    }

    private void resetScores(){
        scores[0] = 0;
        scores[1] = 0;
        score.setText("Score: 0 - 0");
    }
    private void updateScores(int player){
        scores[player]++;
        score.setText("Score: " + scores[0] + " - " + scores[1]);
    }
    private void getNextPlayer(){
        nextPlayer = (nextPlayer+1) % 2;
    }

    private void gridReset(){
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++) {
                gridButtons[i][j].setText("");
                gridButtons[i][j].setEnabled(true);
            }
        numberOfMoves = 0;
    }

    private void undoLastMove(){
        if(!undoList.isEmpty()) {
            String lastButton = undoList.pop();
            int i = Integer.parseInt(String.valueOf(lastButton.charAt(0)));
            int j = Integer.parseInt(String.valueOf(lastButton.charAt(1)));
            gridButtons[i][j].setText("");
            gridButtons[i][j].setEnabled(true);
            getNextPlayer();
            numberOfMoves = Math.max(--numberOfMoves, 0);
        }
    }

    private boolean checkWin(int i, int j){
        boolean win = false;
        String lastMove = gridButtons[i][j].getText();

//      Check row and column.
        win = (lastMove == gridButtons[i][0].getText() && lastMove == gridButtons[i][1].getText() && lastMove == gridButtons[i][2].getText())
            || (lastMove == gridButtons[0][j].getText() && lastMove == gridButtons[1][j].getText() && lastMove == gridButtons[2][j].getText());

        if(win) return win;

//      Check diagonal top-left to bottom right.
        if(i == j)
            win = (lastMove == gridButtons[0][0].getText() && lastMove == gridButtons[1][1].getText() && lastMove == gridButtons[2][2].getText());

        if(win) return win;

//      Check diagonal bottom-left to top-right.
        if((i-j)%2 == 0)
            win = (lastMove == gridButtons[2][0].getText() && lastMove == gridButtons[1][1].getText() && lastMove == gridButtons[0][2].getText());

        return win;
    }

    private void endGame(boolean stalemate){
        gridReset();
        if(!stalemate){
            updateScores(nextPlayer);
            resultLabel.setText("Player " + (nextPlayer+1) + " wins!");
        }
        else
            resultLabel.setText("Draw!");

        resultFrame.setVisible(true);

        getNextPlayer();
        whoseTurn.setText("Player " + (nextPlayer+1) +" Next");
        undoList.removeAllElements();
        numberOfMoves = 0;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String buttonPressed = event.getActionCommand().toString();

        if(buttonPressed == "New Game"){
            gridReset();
            resetScores();
            whoseTurn.setText("Player 1 Next");
            nextPlayer = 0;
        }
        else if (buttonPressed == "Reset") {
            gridReset();
        }
        else if (buttonPressed == "Undo") {
            undoLastMove();
        }
        else{
            int i = Integer.parseInt(String.valueOf(buttonPressed.charAt(0)));
            int j = Integer.parseInt(String.valueOf(buttonPressed.charAt(1)));
            if(nextPlayer == 0)
                gridButtons[i][j].setText("O");
            else
                gridButtons[i][j].setText("X");

            gridButtons[i][j].setEnabled(false);
            numberOfMoves++;

            if(checkWin(i,j))
                endGame(false);
            else if(numberOfMoves == 9)
                endGame(true);
            else{
                undoList.push(buttonPressed);
                getNextPlayer();
            }
        }

    }
}