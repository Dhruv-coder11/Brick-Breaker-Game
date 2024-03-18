import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BrickBreakerGame extends JFrame implements ActionListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int BALL_DIAMETER = 20;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 20;
    private static final int PADDLE_Y_POS = 550;
    private static final int BRICK_ROWS = 5;
    private static final int BRICK_COLS = 10;
    private static final int BRICK_WIDTH = 75;
    private static final int BRICK_HEIGHT = 20;
    private static final int BRICK_GAP = 5;

    private Timer timer;
    private boolean gameRunning = true;
    private int ballX = WIDTH / 2 - BALL_DIAMETER / 2;
    private int ballY = HEIGHT / 2 - BALL_DIAMETER / 2;
    private int ballXSpeed = 3;
    private int ballYSpeed = 3;
    private int paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
    private boolean leftKeyPressed = false;
    private boolean rightKeyPressed = false;
    private boolean[][] bricks = new boolean[BRICK_ROWS][BRICK_COLS];

    public BrickBreakerGame() {
        setTitle("Brick Breaker Game");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    leftKeyPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    rightKeyPressed = true;
                }
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    leftKeyPressed = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    rightKeyPressed = false;
                }
            }
        });
        initBricks();
        timer = new Timer(10, this);
        timer.start();
    }

    public void initBricks() {
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                bricks[i][j] = true;
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        moveBall();
        movePaddle();
        checkCollision();
        repaint();
    }

    public void moveBall() {
        ballX += ballXSpeed;
        ballY += ballYSpeed;
        if (ballX <= 0 || ballX >= WIDTH - BALL_DIAMETER) {
            ballXSpeed = -ballXSpeed;
        }
        if (ballY <= 0) {
            ballYSpeed = -ballYSpeed;
        }
        if (ballY >= HEIGHT - BALL_DIAMETER) {
            gameRunning = false;
        }
    }

    public void movePaddle() {
        if (leftKeyPressed && paddleX > 0) {
            paddleX -= 5;
        }
        if (rightKeyPressed && paddleX < WIDTH - PADDLE_WIDTH) {
            paddleX += 5;
        }
    }

    public void checkCollision() {
        Rectangle ballRect = new Rectangle(ballX, ballY, BALL_DIAMETER, BALL_DIAMETER);
        Rectangle paddleRect = new Rectangle(paddleX, PADDLE_Y_POS, PADDLE_WIDTH, PADDLE_HEIGHT);
        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLS; j++) {
                if (bricks[i][j]) {
                    Rectangle brickRect = new Rectangle(j * (BRICK_WIDTH + BRICK_GAP), i * (BRICK_HEIGHT + BRICK_GAP), BRICK_WIDTH, BRICK_HEIGHT);
                    if (ballRect.intersects(brickRect)) {
                        bricks[i][j] = false;
                        ballYSpeed = -ballYSpeed;
                    }
                }
            }
        }
        if (ballRect.intersects(paddleRect)) {
            ballYSpeed = -ballYSpeed;
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (gameRunning) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.WHITE);
            g.fillOval(ballX, ballY, BALL_DIAMETER, BALL_DIAMETER);
            g.fillRect(paddleX, PADDLE_Y_POS, PADDLE_WIDTH, PADDLE_HEIGHT);
            for (int i = 0; i < BRICK_ROWS; i++) {
                for (int j = 0; j < BRICK_COLS; j++) {
                    if (bricks[i][j]) {
                        g.setColor(Color.GREEN);
                        g.fillRect(j * (BRICK_WIDTH + BRICK_GAP), i * (BRICK_HEIGHT + BRICK_GAP), BRICK_WIDTH, BRICK_HEIGHT);
                    }
                }
            }
        } else {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", WIDTH / 2 - 100, HEIGHT / 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BrickBreakerGame().setVisible(true);
            }
        });
    }
}
