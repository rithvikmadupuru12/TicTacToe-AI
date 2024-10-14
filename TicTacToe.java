import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class TicTacToe {
    private GameBoard gameBoard;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private JButton[][] buttons;
    private JFrame frame;
    private JComboBox<String> difficultyComboBox;
    private JLabel statusLabel;
    private String[] difficulties = {"Easy", "Medium", "Hard"};

    // Cool color scheme
    private static final Color BACKGROUND_COLOR = new Color(25, 25, 25);  // Dark gray, almost black
    private static final Color BUTTON_COLOR = new Color(45, 45, 45);      // Slightly lighter gray
    private static final Color TEXT_COLOR = new Color(200, 200, 200);     // Light gray
    private static final Color PLAYER_X_COLOR = new Color(0, 255, 255);   // Cyan
    private static final Color PLAYER_O_COLOR = new Color(255, 105, 180); // Hot pink

    public TicTacToe() {
        gameBoard = new GameBoard();
        player1 = new Player("Player", "X");
        player2 = new Player("AI", "O");
        currentPlayer = player1;

        frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 3, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttons = new JButton[3][3];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton button = new JButton("");
                button.setFont(new Font("Arial", Font.BOLD, 60));
                button.setFocusPainted(false);
                button.setBackground(BUTTON_COLOR);
                button.setForeground(TEXT_COLOR);
                button.setBorderPainted(false);
                button.setOpaque(true);
                final int r = row;
                final int c = col;
                button.addActionListener(e -> handleMove(r, c));
                buttons[row][col] = button;
                buttonPanel.add(button);
            }
        }

        difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setBackground(BUTTON_COLOR);
        difficultyComboBox.setForeground(TEXT_COLOR);
        JButton startButton = new JButton("New Game");
        startButton.addActionListener(e -> resetGame());
        startButton.setBackground(BUTTON_COLOR);
        startButton.setForeground(TEXT_COLOR);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        JLabel diffLabel = new JLabel("Difficulty:");
        diffLabel.setForeground(TEXT_COLOR);
        topPanel.add(diffLabel);
        topPanel.add(difficultyComboBox);
        topPanel.add(startButton);

        statusLabel = new JLabel("Player's turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setForeground(TEXT_COLOR);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(statusLabel, BorderLayout.SOUTH);
        frame.setVisible(true);

        resetGame();
    }

    private void handleMove(int row, int col) {
        if (gameBoard.makeMove(row, col, currentPlayer.getTypeMove())) {
            updateButton(row, col);

            if (gameBoard.checkWin(currentPlayer)) {
                showMessage(currentPlayer.getName() + " wins!");
                disableAllButtons();
            } else if (gameBoard.isFull()) {
                showMessage("It's a tie!");
                disableAllButtons();
            } else {
                switchPlayers();
                if (currentPlayer == player2) {
                    makeAIMove();
                }
            }
        }
    }

    private void updateButton(int row, int col) {
        buttons[row][col].setText(currentPlayer.getTypeMove());
        buttons[row][col].setEnabled(false);
        if (currentPlayer == player1) {
            buttons[row][col].setForeground(PLAYER_X_COLOR);
        } else {
            buttons[row][col].setForeground(PLAYER_O_COLOR);
        }
    }

    private void switchPlayers() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        statusLabel.setText(currentPlayer.getName() + "'s turn");
    }

    private void resetGame() {
        gameBoard.reset();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);
                buttons[row][col].setBackground(BUTTON_COLOR);
                buttons[row][col].setForeground(TEXT_COLOR);
            }
        }
        currentPlayer = player1;
        statusLabel.setText(currentPlayer.getName() + "'s turn");
    }

    private void makeAIMove() {
        String difficulty = (String) difficultyComboBox.getSelectedItem();
        switch (difficulty) {
            case "Easy":
                makeRandomMove();
                break;
            case "Medium":
                makeMediumMove();
                break;
            case "Hard":
                makeSmartMove();
                break;
        }

        if (gameBoard.checkWin(player2)) {
            showMessage(player2.getName() + " wins!");
            disableAllButtons();
        } else if (gameBoard.isFull()) {
            showMessage("It's a tie!");
            disableAllButtons();
        } else {
            switchPlayers();
        }
    }

    private void makeRandomMove() {
        Random rand = new Random();
        while (true) {
            int row = rand.nextInt(3);
            int col = rand.nextInt(3);
            if (gameBoard.makeMove(row, col, player2.getTypeMove())) {
                updateButton(row, col);
                break;
            }
        }
    }

    private void makeMediumMove() {
        // Try to block player's winning move
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().equals("")) {
                    gameBoard.makeMove(row, col, player1.getTypeMove());
                    if (gameBoard.checkWin(player1)) {
                        gameBoard.resetCell(row, col);
                        gameBoard.makeMove(row, col, player2.getTypeMove());
                        updateButton(row, col);
                        return;
                    }
                    gameBoard.resetCell(row, col);
                }
            }
        }
        makeRandomMove();
    }

    private void makeSmartMove() {
        // First, check if AI can win
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().equals("")) {
                    gameBoard.makeMove(row, col, player2.getTypeMove());
                    if (gameBoard.checkWin(player2)) {
                        updateButton(row, col);
                        return;
                    }
                    gameBoard.resetCell(row, col);
                }
            }
        }

        // Second, block player's winning move
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().equals("")) {
                    gameBoard.makeMove(row, col, player1.getTypeMove());
                    if (gameBoard.checkWin(player1)) {
                        gameBoard.resetCell(row, col);
                        gameBoard.makeMove(row, col, player2.getTypeMove());
                        updateButton(row, col);
                        return;
                    }
                    gameBoard.resetCell(row, col);
                }
            }
        }

        // Finally, if no win or block, make a strategic move
        int[][] preferredMoves = {{1,1}, {0,0}, {0,2}, {2,0}, {2,2}, {0,1}, {1,0}, {1,2}, {2,1}};
        for (int[] move : preferredMoves) {
            int row = move[0];
            int col = move[1];
            if (buttons[row][col].getText().equals("")) {
                gameBoard.makeMove(row, col, player2.getTypeMove());
                updateButton(row, col);
                return;
            }
        }
    }

    private void showMessage(String message) {
        statusLabel.setText(message);
    }

    private void disableAllButtons() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setEnabled(false);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}
