public class TwoPlayerGameMain {
    public static void main(String[] args) {
        String input = "10110111";
        TwoPlayerGameModel model = new TwoPlayerGameModel(input);
        TwoPlayerGameGUI gui = new TwoPlayerGameGUI(model);
        gui.setVisible(true);
    }
}