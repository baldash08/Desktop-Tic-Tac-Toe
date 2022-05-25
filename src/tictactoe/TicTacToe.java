package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class TicTacToe extends JFrame {

    private final JButton[][] buttons = new JButton[Game.FIELD_LENGTH][Game.FIELD_LENGTH];
    private final JButton buttonStartReset = new JButton(Game.RESET);
    private JButton player1;
    private JButton player2;
    private final JLabel labelStatus = new JLabel(Game.GAME_IS_NOT_STARTED);
    private boolean isGameOver = false;

    private final String[] text = {Game.X, Game.O, Game.WS};
    private final Player[] players = {new HumanPlayer(text[0]), new HumanPlayer(text[1])};
    private int turn = 0;


    // Constructor
    public TicTacToe() {
        super(Game.GAME_NAME);
        init();        // initialization

        // Menu bar
        JMenuBar jMenuBar = new JMenuBar();
        setJMenuBar(jMenuBar);
        JMenu menuGame = new JMenu("Game");
        menuGame.setName("MenuGame");

        // Menu Items
        JMenuItem HVH = new JMenuItem("Human vs Human");
        HVH.setName("MenuHumanHuman");
        JMenuItem RVH = new JMenuItem("Robot vs Human");
        RVH.setName("MenuRobotHuman");
        JMenuItem HVR = new JMenuItem("Human vs Robot");
        HVR.setName("MenuHumanRobot");
        JMenuItem RVR = new JMenuItem("Robot vs Robot");
        RVR.setName("MenuRobotRobot");
        JMenuItem exit = new JMenuItem("Exit");
        exit.setName("MenuExit");

        // Actions
        HVH.addActionListener(event -> {
            player1.setText(Game.HUMAN);
            player2.setText(Game.HUMAN);

            players[0] = new HumanPlayer(text[0]);
            players[1] = new HumanPlayer(text[1]);
            start();
        });

        RVH.addActionListener(event -> {
            player1.setText(Game.ROBOT);
            player2.setText(Game.HUMAN);

            players[0] = new ComputerPlayer(text[0]);
            players[1] = new HumanPlayer(text[1]);
            start();
        });

        HVR.addActionListener(event -> {
            player1.setText(Game.HUMAN);
            player2.setText(Game.ROBOT);

            players[0] = new HumanPlayer(text[0]);
            players[1] = new ComputerPlayer(text[1]);
            start();
        });

        RVR.addActionListener(event -> {
            player1.setText(Game.ROBOT);
            player2.setText(Game.ROBOT);

            players[0] = new ComputerPlayer(text[0]);
            players[1] = new ComputerPlayer(text[1]);
            start();
        });
        exit.addActionListener(event -> System.exit(0));

        menuGame.add(HVH);
        menuGame.add(HVR);
        menuGame.add(RVH);
        menuGame.add(RVR);
        menuGame.addSeparator();
        menuGame.add(exit);

        jMenuBar.add(menuGame);


        startResetGame();

    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(500, 500);

        JPanel panelHead = createPanelHead();
        JPanel panel = createGameField();
        JPanel panelStatus = createPanelStatus();

        setLayout(new BorderLayout(0, 0));

        add(panel, BorderLayout.CENTER);
        add(panelStatus, BorderLayout.SOUTH);
        add(panelHead, BorderLayout.NORTH);
        setVisible(true);
    }

    private JButton createButton(String text, String name) {
        JButton button = new JButton(text);
        button.setName(name);
        button.setFocusPainted(false);
        return button;
    }

    private JPanel createPanelHead() {
        JPanel panelHead = new JPanel();
        panelHead.setSize(300, 30);
        panelHead.setLayout(new GridLayout(1, 3));
        panelHead.setPreferredSize(new Dimension(300, 30));

        this.buttonStartReset.setName(Game.START_RESET);

        player1 = createButton(Game.HUMAN, Game.PLAYER_1_NAME);
        player2 = createButton(Game.HUMAN, Game.PLAYER_2_NAME);

        buttonStartReset.addActionListener(e -> startResetGame());
        player1.addActionListener(e -> togglePlayerType(player1, 0));
        player2.addActionListener(e -> togglePlayerType(player2, 1));

        panelHead.add(player1);
        panelHead.add(buttonStartReset);
        panelHead.add(player2);


        return panelHead;
    }

    private JPanel createGameField() {
        JPanel gameField = new JPanel();
        gameField.setLayout(new GridLayout(3, 3));

        int rows = 0;
        int cols = 0;
        for (int i = 3; i > 0; i--) {
            for (char c : new char[]{'A', 'B', 'C'}) {
                buttons[rows][cols] = createButton(Game.WS, "Button" + c + i);
                buttons[rows][cols].setFont(new Font("Arial", Font.BOLD, 40));
                buttons[rows][cols].setEnabled(false);
                buttons[rows][cols].addActionListener(e -> endTurn((JButton) e.getSource()));
                gameField.add(buttons[rows][cols]);
                cols++;
            }
            cols = 0;
            rows++;
        }

        return gameField;
    }

    private JPanel createPanelStatus() {
        JPanel panelStatus = new JPanel();
        panelStatus.setLayout(new BorderLayout());
        panelStatus.setSize(300, 40);
        panelStatus.setPreferredSize(new Dimension(300, 35));

        this.labelStatus.setName(Game.LABEL_STATUS_NAME);

        panelStatus.add(labelStatus, BorderLayout.WEST);

        return panelStatus;
    }

    private void nextPlayer() {
        turn = (turn + 1) % 2;
        players[turn].makeMove(this);
    }

    private void endTurn(JButton button) {
        if (!isGameOver && button.getText().equals(" ")) {
            button.setText(players[turn].getIcon());
            labelStatus.setText("Game in progress");
            isGameOver = status();
            nextPlayer();
        }
    }

    private void togglePlayerType(JButton playerTypeButton, int playerNumber) {
        playerTypeButton.setText(playerTypeButton.getText().equals("Human") ? "Robot" : "Human");
        if (playerTypeButton.getText().equals("Human")) {
            players[playerNumber] = new HumanPlayer(text[playerNumber]);
        } else {
            players[playerNumber] = new ComputerPlayer(text[playerNumber]);
        }
    }

    private void toggleGameBoardButtons(boolean enabled) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                buttons[y][x].setText(" ");
                buttons[y][x].setEnabled(enabled);
            }
        }
    }

    private void start() {
        if (buttonStartReset.getText().equals(Game.START)) {
            buttonStartReset.doClick();
        } else {
            buttonStartReset.doClick();
            buttonStartReset.doClick();
        }

    }

    private void startResetGame() {
        toggleGameBoardButtons(buttonStartReset.getText().equals(Game.START));
        if (buttonStartReset.getText().equals(Game.START)) {
            buttonStartReset.setText(Game.RESET);
            player1.setEnabled(false);
            player2.setEnabled(false);
            isGameOver = false;
            turn = 1;
            nextPlayer();
        } else {
            buttonStartReset.setText(Game.START);
            player1.setEnabled(true);
            player2.setEnabled(true);
            labelStatus.setText(Game.GAME_IS_NOT_STARTED);
            isGameOver = false;
            turn = 1;
        }
    }

    private boolean status() {
        int counter = 0;
        int[] winSums = { 7, 56, 448, 73, 146, 292, 273, 84 };

        int sumX = 0;
        int sumO = 0;
        int index = 0;

        for (JButton[] button : buttons) {
            for (JButton btn : button) {
                if (!btn.getText().equals(Game.WS)) {
                    counter++;

                    if (Objects.equals(btn.getText(), Game.X)) {
                        sumX += Math.pow(2, index);
                        //System.out.println("X sum: " + sumX);
                    } else if (Game.O.equals(btn.getText())) {
                        sumO += Math.pow(2, index);
                        //System.out.println("O sum: " + sumO);
                    }
                }
                index++;
            }
        }

        for (int win : winSums) {
            final int NINE = 9;
            if ((sumX & win) == win) {
                labelStatus.setText(String.format("The %s Player (%s) wins", player1.getText(), Game.X));
                System.out.println(sumX & win);
                return true;
            } else if ((sumO & win) == win) {
                labelStatus.setText(String.format("The %s Player (%s) wins", player2.getText(), Game.O));
                return true;
            } else if (counter == NINE) {
                labelStatus.setText(Game.DRAW);
                return true;
            }
        }

        return false;
    }

    interface Player {
        void makeMove(TicTacToe game);
        String getIcon();
    }

    static abstract class AbstractPlayer implements Player {
        private final String icon;

        AbstractPlayer(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return icon;
        }
    }

    static class HumanPlayer extends AbstractPlayer {
        HumanPlayer(String icon) {
            super(icon);
        }

        @Override
        public void makeMove(TicTacToe game) {
            if (!game.isGameOver)
                game.labelStatus.setText(String.format("The turn of Human Player (%s)", getIcon()));
        }
    }

    static class ComputerPlayer extends AbstractPlayer {
        ComputerPlayer(String icon) {
            super(icon);
        }

        @Override
        public synchronized void makeMove(TicTacToe game) {
            java.util.Random rng = new java.util.Random();
            if (!game.isGameOver)
                game.labelStatus.setText(String.format("The turn of Robot Player (%s)", getIcon()));

            int row;
            int col;

            do {
                row = rng.nextInt(3);
                col = rng.nextInt(3);
            } while (!game.buttons[row][col].getText().equals(" "));
            game.endTurn(game.buttons[row][col]);
        }
    }
}