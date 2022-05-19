import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Problem01 extends JFrame {
    private final static int BOX_SIZE = 64;

    private final Model model = new Model();

    private final JLabel whichLevel = new JLabel((model.getLev()) + " from 4");

    JLabel movesName = new JLabel("Moves");
    private final JLabel moves = new JLabel(String.valueOf(model.getMoves()));

    private int[][] desk = model.getLevel();
    private int[][] ground = model.getGround();

    private final Image boyD = new ImageIcon("images/RobotD.png").getImage();
    private final Image boyR = new ImageIcon("images/RobotR.png").getImage();
    private final Image boyL = new ImageIcon("images/RobotL.png").getImage();
    private final Image boyU = new ImageIcon("images/RobotU.png").getImage();
    private final Image wall = new ImageIcon("images/Wall.png").getImage();
    private final Image goal = new ImageIcon("images/Goal.png").getImage();
    private final Image red = new ImageIcon("images/BoxRed.png").getImage();
    private final Image blue = new ImageIcon("images/BoxBlue.png").getImage();
    private final Image grass = new ImageIcon("images/Ground.png").getImage();

    Problem01() {
        setTitle("Sokoban");
        setSize(800,800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //for drawing the whole map
        MainPanel mainPanel = new MainPanel();
        mainPanel.setBackground(Color.BLACK);
        mainPanel.requestFocus();
        mainPanel.setFocusable(true);
        add(mainPanel,  BorderLayout.CENTER);

        //to manage the game;
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.GRAY);
        controlPanel.setLayout(new GridLayout(20, 1));
        add(controlPanel, BorderLayout.EAST);

        JLabel levelName = new JLabel("Level");
        levelName.setHorizontalAlignment(JLabel.LEFT);
        levelName.setVerticalAlignment(JLabel.BOTTOM);
        controlPanel.add(levelName);

        JLabel level = new JLabel("MiniCosmos");
        level.setHorizontalAlignment(JLabel.CENTER);
        level.setVerticalAlignment(JLabel.CENTER);
        level.setBackground(Color.CYAN);
        level.setOpaque(true);
        controlPanel.add(level);

        JLabel puzzleName = new JLabel("Puzzle");
        puzzleName.setVerticalAlignment(JLabel.BOTTOM);
        puzzleName.setHorizontalAlignment(JLabel.LEFT);
        controlPanel.add(puzzleName);

        whichLevel.setHorizontalAlignment(JLabel.CENTER);
        whichLevel.setVerticalAlignment(JLabel.CENTER);
        whichLevel.setBackground(Color.CYAN);
        whichLevel.setOpaque(true);
        controlPanel.add(whichLevel);

        JButton next = new JButton(">>");
        controlPanel.add(next);

        JButton goBack = new JButton("<<");
        controlPanel.add(goBack);

        movesName.setVerticalAlignment(JLabel.BOTTOM);
        movesName.setHorizontalAlignment(JLabel.LEFT);
        controlPanel.add(movesName);

        moves.setVerticalAlignment(JLabel.CENTER);
        moves.setHorizontalAlignment(JLabel.CENTER);
        moves.setBackground(Color.CYAN);
        moves.setOpaque(true);
        controlPanel.add(moves);

        JButton reset = new JButton("Reset(ESC");
        reset.addActionListener(e -> {
            model.reset();
            desk = model.getLevel();
            ground = model.getGround();
            moves.setText(String.valueOf(model.getMoves()));
            repaint();
        });
        controlPanel.add(reset);

        next.addActionListener(e -> {
            model.nextLevel();
            desk = model.getLevel();
            ground = model.getGround();
            whichLevel.setText((model.getLev()) + " from 4");
            moves.setText(String.valueOf(model.getMoves()));
            repaint();
        });

        goBack.addActionListener(e -> {
            model.levelBefore();
            desk = model.getLevel();
            ground = model.getGround();
            whichLevel.setText((model.getLev()) + " from 4");
            moves.setText(String.valueOf(model.getMoves()));
            repaint();
        });

        goBack.setFocusable(false);
        reset.setFocusable(false);
        next.setFocusable(false);

        mainPanel.addKeyListener(new CanvasKeyListener());
    }

    public static void main(String[] args) {
        new Problem01().setVisible(true);
    }

    class MainPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int x = BOX_SIZE;
            int y = BOX_SIZE;
            int width = BOX_SIZE;
            int height = BOX_SIZE;

            for (int[] value : ground) {//value = row, i = column
                for (int i : value) {
                    if (i == 1) {
                        g.drawImage(grass, x, y, null);
                    } else if (i == 7) {
                        g.drawImage(grass, x, y, null);
                        g.drawImage(goal, x + 16, y + 16, null);
                    }
                    x += width;
                }
                x = BOX_SIZE;
                y += height;
            }
            y = BOX_SIZE;

            for (int[] ints : desk) {
                for (int anInt : ints) {
                    switch (anInt) {
                        case 3:
                            g.drawImage(wall, x, y, null);
                            break;
                        case 2:
                            switch (model.getBoyDirection()) {
                                case 0:
                                    g.drawImage(boyU, x + 16, y, null);
                                    break;
                                case 1:
                                    g.drawImage(boyR, x + 16, y, null);
                                    break;
                                case 2:
                                    g.drawImage(boyD, x + 16, y, null);
                                    break;
                                case 3:
                                    g.drawImage(boyL, x + 16, y, null);
                                    break;
                            }
                            break;
                        case 4:
                            if (model.check()) {
                                g.drawImage(red, x, y, null);
                            } else {
                                g.drawImage(blue, x, y, null);
                            }
                            break;
                    }
                    x += width;
                }
                x = BOX_SIZE;
                y += height;
            }
        }
    }

    class CanvasKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            model.getData();
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    model.moveRight();
                    break;
                case KeyEvent.VK_LEFT:
                    model.moveLeft();
                    break;
                case KeyEvent.VK_UP:
                    model.moveUp();
                    break;
                case KeyEvent.VK_DOWN:
                    model.moveDown();
                    break;
                case KeyEvent.VK_ESCAPE:
                    model.reset();
                    desk = model.getLevel();
                    ground = model.getGround();
                    break;
            }
            moves.setText(Integer.toString(model.getMoves()));
            repaint();
            if (model.check()) {
                JOptionPane.showMessageDialog(null, "You won puzzle " + model.getLev() + ". Moves: " + model.getMoves());
                model.nextLevel();
                whichLevel.setText((model.getLev()) + " from 4");
                moves.setText(String.valueOf(model.getMoves()));
                desk = model.getLevel();
                ground = model.getGround();
                repaint();
            }
        }
    }
}
