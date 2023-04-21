public class TwoPlayerGameModel {
    private String input;
    private int currentPlayer;
    private int player1Score;
    private int player2Score;
    //Count the number of turn;
    private int turnCount = 0;

    public TwoPlayerGameModel(String input) {
        this.input = input;
        currentPlayer = 1;
        player1Score = 0;
        player2Score = 0;
    }

    public String getInput() {
        return input;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void addPlayer1Score(int points) {
        player1Score += points;
    }

    public void addPlayer2Score(int points) {
        player2Score += points;
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer == 1 ? 2 : 1;
    }

    public boolean update(int position1, int position2) {
        if (position1 == position2 - 1 && (input.charAt(position1) == '1' || input.charAt(position1) == '0')
                && (input.charAt(position2) == '1' || input.charAt(position2) == '0')) {
            String substring = input.substring(position1, position2 + 1);
            if (substring.equals("11")) {
                replace(position1, position2, "0");
                addPlayerScore(2);
            }
            if (substring.equals("10")) {
                replace(position1, position2, "0");
                addPlayerScore(1);
            }if (substring.equals("01")) {
                replace(position1, position2, "0");
                addPlayerScore(1);
            }
            if (substring.equals("00")) {
                replace(position1, position2, "0");
                addPlayerScore(0);
            }

            switchPlayer();
        }

        turnCount++;
        return input.indexOf("1") != -1;
    }


    private void addPlayerScore(int i) {
        if (currentPlayer == 1) {
            addPlayer1Score(i);
        } else {
            addPlayer2Score(i);
        }
    }

    public void replace(int start, int end, String replacement) {
        input = input.substring(0, start) + replacement + input.substring(end + 1);
    }

    public int getLength() {
        return input.length();
    }

    public String getPosition(int i) {
        return input.charAt(i) + "";
    }

    public int getTurnCount() {
        return turnCount;
    }


    //Clone the model
    public TwoPlayerGameModel clone() {
        TwoPlayerGameModel clone = new TwoPlayerGameModel(input);
        clone.currentPlayer = currentPlayer;
        clone.player1Score = player1Score;
        clone.player2Score = player2Score;
        clone.turnCount = turnCount;
        return clone;
    }
}
