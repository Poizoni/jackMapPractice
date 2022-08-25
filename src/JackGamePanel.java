import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class JackGamePanel extends JPanel implements Runnable {

    final int NOTE_WIDTH = 100;
    final int NOTE_HEIGHT = 20;

    int noteY = 0;

    // scroll speed
    int yVelocity = 20;

    // note x positions
    final int LEFT = 0;
    final int MIDLEFT = 100;
    final int MIDRIGHT = 200;
    final int RIGHT = 300;
    final int BLANK = 400;

    // note positions in notes array
    final int NOTE_1 = 0;
    final int NOTE_2 = 1;
    final int NOTE_3 = 2;
    final int NOTE_4 = 3;

    // hit dot settings
    final Color DOT_COLOR = Color.yellow;
    final int DOT_SIZE = 50;
    final int DOT_Y = 530;
    boolean leftDot = false;
    boolean midLeftDot = false;
    boolean midRightDot = false;
    boolean rightDot = false;

    // score settings
    final Font SCORE_FONT = new Font("Arial", Font.PLAIN, 60);
    final Color SCORE_COLOR = Color.white;
    final int SCORE_X = 170;
    final int SCORE_Y = 100;

    final Font SPEED_FONT = new Font("Arial", Font.PLAIN, 20);
    final int SPEED_X = 10;
    final int SPEED_Y = 30;

    static final int GAME_WIDTH = 400;
    static final int GAME_HEIGHT = 600;
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);

    final int yTopHitPos = 490;
    final int yBottomHitPos = 570;

    int score = 0;
    boolean pauseScreen = false;

    ArrayList<Note> notes = new ArrayList<>(4);
    Thread gameThread;
    Graphics graphics;
    Random random;
    Image image;

    JackGamePanel() {
        start();
        random = new Random();

        this.addKeyListener(new AL());
        this.setFocusable(true);
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void start() {
        notes.add(0, newNote(LEFT));
        notes.add(1, newNote(MIDLEFT));
        notes.add(2, newNote(MIDRIGHT));
        notes.add(3, newNote(RIGHT));
    }


    public Note newNote(int x) {
        return new Note(x, noteY, NOTE_WIDTH, NOTE_HEIGHT, yVelocity);
    }

    public Note blankNote() {
        return new Note(BLANK, noteY, NOTE_WIDTH, NOTE_HEIGHT, yVelocity);
    }

    public void checkScore() {
        // resets score if they pass hit area
        for(Note note : notes) {
            if (note.getYPos() > yBottomHitPos && note.getXPos() != BLANK) {
                score = 0;
                break;
            }
        }
    }

    public void checkCollision() {
        for(Note note : notes) {
            if(note.getYPos() >= 600) {
                shuffle();
                note.y = 0;
            }
        }
    }

    public void incScrollSpeed() {
        if(yVelocity<50) {
            yVelocity++;
        }
    }

    public void decScrollSpeed() {
        if(yVelocity>1) {
            yVelocity--;
        }
    }

    public void shuffle() {
        // randomly chooses 0 & 1 for each note and if its 0 the note will be blank
        int first = random.nextInt(2);
        if(first == 0) {
            notes.set(NOTE_1, blankNote());
        } else {
            notes.set(NOTE_1, newNote(LEFT));
        }

        int second = random.nextInt(2);
        if(second == 0) {
            notes.set(NOTE_2, blankNote());
        } else {
            notes.set(NOTE_2, newNote(MIDLEFT));
        }

        int third = random.nextInt(2);
        if(third == 0) {
            notes.set(NOTE_3, blankNote());
        } else {
            notes.set(NOTE_3, newNote(MIDRIGHT));
        }

        int fourth = random.nextInt(2);
        if(fourth == 0) {
            notes.set(NOTE_4, blankNote());
        } else {
            notes.set(NOTE_4, newNote(RIGHT));
        }
    }

    public void run() {
        // game loop idk how this works
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while(true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1) {
                move();
                checkCollision();
                checkScore();
                repaint();
                delta--;
            }
        }
    }

    public void move() {
        if(!pauseScreen) {
            for (Note note : notes) {
                note.move();
            }
        }
    }

    public void draw(Graphics g) {
        for(Note note : notes) {
            note.draw(g);
        }

        // hitline
        g.setColor(Color.white);
        g.drawLine(0,560,400,560);

        // hit dots
        if(leftDot) {
            g.drawOval(20, DOT_Y, DOT_SIZE, DOT_SIZE);
            g.setColor(DOT_COLOR);
            g.fillOval(20, DOT_Y, DOT_SIZE, DOT_SIZE);
        }
        if(midLeftDot) {
            g.drawOval(120, DOT_Y, DOT_SIZE, DOT_SIZE);
            g.setColor(DOT_COLOR);
            g.fillOval(120, DOT_Y, DOT_SIZE, DOT_SIZE);
        }
        if(midRightDot) {
            g.drawOval(220, DOT_Y, DOT_SIZE, DOT_SIZE);
            g.setColor(DOT_COLOR);
            g.fillOval(220, DOT_Y, DOT_SIZE, DOT_SIZE);
        }
        if(rightDot) {
            g.drawOval(320, DOT_Y, DOT_SIZE, DOT_SIZE);
            g.setColor(DOT_COLOR);
            g.fillOval(320, DOT_Y, DOT_SIZE, DOT_SIZE);
        }

        // score
        g.setColor(SCORE_COLOR);
        g.setFont(SCORE_FONT);
        g.drawString(String.valueOf(score), SCORE_X, SCORE_Y);

        // speed (WIP)
        g.setFont(SPEED_FONT);
        g.drawString(String.valueOf(yVelocity), SPEED_X, SPEED_Y);

        // idk what this does but apparently its good
        Toolkit.getDefaultToolkit().sync();
    }

    public void paint(Graphics g) {
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
    }

    public class AL extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            // hides notes and increments score if hit properly
            // also shows hit dots
            if(e.getKeyCode() == KeyEvent.VK_A) {
                leftDot = true;
                if(notes.get(NOTE_1).getYPos() > yTopHitPos
                   && notes.get(NOTE_1).getYPos() < yBottomHitPos
                   && notes.get(NOTE_1).getXPos() != BLANK ) {
                        score++;
                        notes.set(NOTE_1, blankNote());
                }
            }
            if(e.getKeyCode() == KeyEvent.VK_S) {
                midLeftDot = true;
                if(notes.get(NOTE_2).getYPos() > yTopHitPos
                   && notes.get(NOTE_2).getYPos() < yBottomHitPos
                   && notes.get(NOTE_2).getXPos() != BLANK ) {
                        score++;
                        notes.set(NOTE_2, blankNote());
                }
            }
            if(e.getKeyCode() == KeyEvent.VK_K) {
                midRightDot = true;
                if(notes.get(NOTE_3).getYPos() > yTopHitPos
                   && notes.get(NOTE_3).getYPos() < yBottomHitPos
                   && notes.get(NOTE_3).getXPos() != BLANK ) {
                        score++;
                        notes.set(NOTE_3, blankNote());
                }
            }
            if(e.getKeyCode() == KeyEvent.VK_L) {
                rightDot = true;
                if(notes.get(NOTE_4).getYPos() > yTopHitPos
                   && notes.get(NOTE_4).getYPos() < yBottomHitPos
                   && notes.get(NOTE_4).getXPos() != BLANK ) {
                        score++;
                        notes.set(NOTE_4, blankNote());
                }
            }
            if(e.getKeyCode() == KeyEvent.VK_UP) {
                incScrollSpeed();
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                decScrollSpeed();
            }
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                pauseScreen = !pauseScreen;
            }
        }

        public void keyReleased(KeyEvent e) {
            // hides hit dots
            if(e.getKeyCode() == KeyEvent.VK_A){
                leftDot = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_S){
                midLeftDot = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_K){
                midRightDot = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_L){
                rightDot = false;
            }
        }

    }

}
