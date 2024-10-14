class GameBoard {
    private String[][] board;

    public GameBoard() {
        board = new String[3][3];
        reset();
    }

    public boolean makeMove(int row, int col, String playerType) {
        if (board[row][col].equals("")) {
            board[row][col] = playerType;
            return true;
        }
        return false;
    }

    public boolean checkWin(Player player) {
        String typeMove = player.getTypeMove();
        return (checkRows(typeMove) || checkCols(typeMove) || checkDiagonals(typeMove));
    }

    private boolean checkRows(String typeMove) {
        for (int row = 0; row < 3; row++) {
            if (board[row][0].equals(typeMove) &&
                board[row][1].equals(typeMove) &&
                board[row][2].equals(typeMove)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkCols(String typeMove) {
        for (int col = 0; col < 3; col++) {
            if (board[0][col].equals(typeMove) &&
                board[1][col].equals(typeMove) &&
                board[2][col].equals(typeMove)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonals(String typeMove) {
        return (board[0][0].equals(typeMove) &&
                board[1][1].equals(typeMove) &&
                board[2][2].equals(typeMove)) ||
               (board[0][2].equals(typeMove) &&
                board[1][1].equals(typeMove) &&
                board[2][0].equals(typeMove));
    }

    public boolean isFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col].equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    public void resetCell(int row, int col) {
        board[row][col] = "";
    }

    public void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "";
            }
        }
    }
}