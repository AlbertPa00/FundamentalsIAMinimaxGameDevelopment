import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MinimaxPlayer {

    private int depth;
    private boolean maximizingPlayer;

    public MinimaxPlayer(int depth, boolean maximizingPlayer) {
        this.depth = depth;
        this.maximizingPlayer = maximizingPlayer;
    }

    public int getMove(TwoPlayerGameModel model) {

// Build the game tree
        TreeNode root = new TreeNode(model, -1);
        buildGameTree(root, depth, maximizingPlayer);
        //drawGameTree(g,200,150,root, 0);
        int bestMove = -2;
        int bestValue = -1;

        for (TreeNode child : root.getChildren()) {
            if (child.getValue() > bestValue) {
                bestMove = child.getMove();
                bestValue = child.getValue();
            }
        }
        return bestMove;
    }

    private int minimax(TreeNode node, int depth, boolean maximizingPlayer) {
        if (depth == 0 || node.getChildren().isEmpty()) {
            int value = evaluate(node.getModel());
            node.setValue(value);
            return value;
        }


        int bestValue;
        if (maximizingPlayer) {
            bestValue = -999;
            for (TreeNode child : node.getChildren()) {
                int value = minimax(child, depth - 1, false);
                bestValue = Math.max(bestValue, value);
            }
        } else {
            bestValue = 999;
            for (TreeNode child : node.getChildren()) {
                int value = minimax(child, depth - 1, true);
                bestValue = Math.min(bestValue, value);
            }
        }
        node.setValue(bestValue);
        return bestValue;
    }
    private void buildGameTree(TreeNode parent, int depth, boolean maximizingPlayer) {
        TwoPlayerGameModel model = parent.getModel();
        for (int i = 0; i < model.getLength() - 1; i++) {
            TwoPlayerGameModel newModel = new TwoPlayerGameModel(model.getInput());
            if (newModel.update(i, i + 1)) {
                TreeNode child = new TreeNode(newModel, i);
                parent.addChild(child);
                buildGameTree(child, depth - 1, !maximizingPlayer);
            }
        }

        minimax(parent, depth, maximizingPlayer);
    }

    private int evaluate(TwoPlayerGameModel model) {
        // return 1 if the computer wins,
        //-1 if the human wins and zero if the game is a draw.
        int value = 0;
        int result = model.getPlayer1Score() - model.getPlayer2Score();
        if (result > 1) {
            return -1;
        }
        if (result < 0) {
            return 1;
        } else {
            return 0;
        }
    }

    // function to draw the game tree built

    public BufferedImage draw(TwoPlayerGameModel model) {
        TreeNode root = new TreeNode(model, -1);
        buildGameTree(root, depth, maximizingPlayer);

        return drawGameTree(root, 3);
    }

    public BufferedImage drawGameTree(TreeNode root, int depth) {
        int count = 0;
        BufferedImage image = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        // Increase node size and font size, and use a consistent distance between nodes
        int nodeSize = 40;
        int nodeSpacing = 40;
        Font font = new Font("Arial", Font.PLAIN, 14);
        g.setFont(font);
        g.setColor(new Color(255, 228, 196));
        g.fillOval(50, 50, nodeSize, nodeSize);
        g.setColor(new Color(165, 42, 42));
        g.drawString(root.getModel().getInput() + "", 50 + nodeSize/2 - g.getFontMetrics().stringWidth(root.getModel().getInput() + "")/2, 50 + nodeSize/2 + g.getFontMetrics().getHeight()/4);

        for (int i = 0; i < root.getChildren().size(); i++) {
            TreeNode child = root.getChildren().get(i);
            int childX = 25 + nodeSpacing * (i+1);
            int childY = 25 + nodeSpacing * (depth+1);

            // Use a consistent line thickness and a different color for the lines
            g.setStroke(new BasicStroke(2));
            g.setColor(Color.DARK_GRAY);
            g.drawLine(50 + nodeSize/2, 50 + nodeSize, childX + nodeSize/2, childY);
            g.drawLine(childX + nodeSize/2, childY, childX + nodeSize/2 - nodeSize/5, childY - nodeSize/5);
            g.drawLine(childX + nodeSize/2, childY, childX + nodeSize/2 + nodeSize/5, childY - nodeSize/5);

            g.setColor(new Color(165, 42, 42));
            g.fillOval(childX, childY, nodeSize, nodeSize);
            g.setColor(Color.WHITE);
            g.drawString(child.getModel().getInput() + "", childX + nodeSize/2 - g.getFontMetrics().stringWidth(child.getModel().getInput() + "")/2, childY + nodeSize/2 + g.getFontMetrics().getHeight()/4);

            BufferedImage childImage = drawGameTree(child, depth + 1);
            g.drawImage(childImage, childX - nodeSpacing/2, childY + nodeSpacing/2, null);
        }
        g.dispose();
        return image;
    }


    private static class TreeNode {
        private TwoPlayerGameModel model;
        private int move;
        private int value;
        private List<TreeNode> children;

        public TreeNode(TwoPlayerGameModel model, Integer move) {
            this.model = model;
            this.move = move;
            this.children = new ArrayList<>();
        }

        public TwoPlayerGameModel getModel() {
            return model;
        }

        public int getMove() {
            return move;
        }

        public List<TreeNode> getChildren() {
            return children;
        }

        public void addChild(TreeNode child) {
            children.add(child);
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}

