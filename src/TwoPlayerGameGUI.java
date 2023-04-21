import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TwoPlayerGameGUI extends JFrame {
    private TwoPlayerGameModel model;

    private MinimaxPlayer computerPlayer;
    JPanel positionPanel;
    private JButton[] positionButtons;
    private JButton moveButton;
    private JButton unselectButton;
    private JButton treeButton;
    private JLabel statusLabel;
    //Scorer label for each player
    private JLabel player1Score;
    private JLabel player2Score;
    private int startingPlayer = 0;
    private int firstSelected = -1;
    private int secondSelected = -1;


    public TwoPlayerGameGUI(TwoPlayerGameModel model) {
        this.model = model;
        this.positionButtons = new JButton[model.getLength()];
        this.moveButton = new JButton("Move");
        this.statusLabel = new JLabel(" ");
        this.player1Score = new JLabel("Player 1 Score: " + model.getPlayer1Score());
        this.player2Score = new JLabel("Player 2 Score: " + model.getPlayer2Score());
        this.computerPlayer = new MinimaxPlayer(model.getInput().length() - 1, true);

        // Create the position buttons
        positionPanel = new JPanel(new GridLayout(1, model.getLength()));
        for (int i = 0; i < model.getLength(); i++) {
            JButton button = new JButton(model.getPosition(i));
            button.addActionListener(new PositionButtonListener(i));
            positionButtons[i] = button;
            positionPanel.add(button);
        }

        // Create the move button
        JPanel movePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        moveButton.addActionListener(new MoveButtonListener());
        movePanel.add(moveButton);
//create unselect button
        unselectButton = new JButton("Unselect");
        unselectButton.addActionListener(new UnselectButtonListener());
        movePanel.add(unselectButton);
//create tree button
        treeButton = new JButton("Tree");
        treeButton.addActionListener(new TreeButtonListener());
        movePanel.add(treeButton);
        // Create the status label
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.add(statusLabel);
        statusPanel.add(player1Score);
        statusPanel.add(player2Score);

        // Add the components to the frame
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(positionPanel, BorderLayout.CENTER);
        contentPane.add(movePanel, BorderLayout.SOUTH);
        contentPane.add(statusPanel, BorderLayout.NORTH);
        StarterPlayer();
        if(startingPlayer==1)model.switchPlayer();
        setTitle("Two Player Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(model.getLength() * 50, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        //
    }

    public JDialog StarterPlayer() {
        JDialog playerSelectionDialog = new JDialog(this, "Select starting player", true);
        playerSelectionDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        playerSelectionDialog.setSize(200, 150);
        playerSelectionDialog.setLocationRelativeTo(this);
        playerSelectionDialog.setLayout(new GridLayout(3, 1));

        JRadioButton player1RadioButton = new JRadioButton("Human starts");
        JRadioButton player2RadioButton = new JRadioButton("Computer starts");
        ButtonGroup playerSelectionGroup = new ButtonGroup();
        playerSelectionGroup.add(player1RadioButton);
        playerSelectionGroup.add(player2RadioButton);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the starting player based on the selected radio button
                if (player1RadioButton.isSelected()) {
                    startingPlayer = 0;
                } else {
                    startingPlayer = 1;
                }
                playerSelectionDialog.dispose();
            }
        });

        // Add the components to the player selection dialog
        playerSelectionDialog.add(player1RadioButton);
        playerSelectionDialog.add(player2RadioButton);
        playerSelectionDialog.add(confirmButton);

        // Show the player selection dialog
        playerSelectionDialog.setVisible(true);
        return playerSelectionDialog;
    }


    //Button to unselect the selected buttons
    private class UnselectButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (firstSelected != -1) {
                positionButtons[firstSelected].setEnabled(true);
                firstSelected = -1;
            }
            if (secondSelected != -1) {
                positionButtons[secondSelected].setEnabled(true);
                secondSelected = -1;
            }
            // moveButton.setEnabled(false);
        }
    }

    //Button to open another window to show the game tree
    private class TreeButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JFrame treeFrame = new JFrame("Game Tree");
            treeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            treeFrame.setSize(5000, 5000);
            JLabel imageLabel = new JLabel(new ImageIcon(computerPlayer.draw(model)));
            treeFrame.add(imageLabel);
            treeFrame.setLocationRelativeTo(null);
            treeFrame.setVisible(true);
            treeFrame.toFront();
            treeFrame.repaint();
        }
    }

    private class PositionButtonListener implements ActionListener {
        private int position;

        public PositionButtonListener(int position) {
            this.position = position;
        }

        public void actionPerformed(ActionEvent event) {
            if (firstSelected == -1) {
                // First position selected
                firstSelected = position;
                positionButtons[position].setEnabled(false);
            } else if (secondSelected == -1 && position == firstSelected + 1) {
                // Second position selected, must be adjacent to first
                secondSelected = position;
                positionButtons[position].setEnabled(false);
                moveButton.setEnabled(true);
            }
        }
    }

    private class MoveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            if (firstSelected != -1 && secondSelected != -1 || model.getCurrentPlayer() == 2) {
                if (model.getCurrentPlayer() == 2) {
                    int move = computerPlayer.getMove(model);
                    firstSelected = move;
                    secondSelected = move + 1;

                }
                int player = model.getCurrentPlayer();
                boolean moved = model.update(firstSelected, secondSelected);

                if (moved) {
                    for (int i = 0; i < model.getLength(); i++) {
                        positionButtons[i].setText(model.getPosition(i));
                        positionButtons[i].setEnabled(true);
                    }
                    //positionButtons[model.getLength()].setEnabled(false);
                    positionPanel.remove(model.getLength());
                    //moveButton.setEnabled(false);
                    statusLabel.setText("Move made by player " + player + " in the positions " + firstSelected + " and " + secondSelected + ". ");
                    firstSelected = -1;
                    secondSelected = -1;

                    player1Score.setText("Player 1 Score: " + model.getPlayer1Score());
                    player2Score.setText("Computer Score: " + model.getPlayer2Score());
                } else {
                    statusLabel.setText("Game finished. ");
                    player1Score.setText("Player 1 Score: " + model.getPlayer1Score());
                    player2Score.setText("Computer Score: " + model.getPlayer2Score());
                    ;
                    for (int i = 0; i < model.getLength(); i++) {
                        positionButtons[i].setEnabled(false);
                    }
                    moveButton.setEnabled(true);
                }
            }
        }
    }


}