import java.awt.*;

public class Note extends Rectangle {

    // middle note x positions
    final int MID_LEFT = 100;
    final int MID_RIGHT = 200;

    // note colors
    final Color MID_COLOR = Color.cyan;
    final Color MAIN_COLOR = Color.white;
    int yVelocity;

    Note(int x, int y, int width, int height, int yVelocity) {
        super(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.yVelocity = yVelocity;
    }

    public int getYPos() { return y; }

    public int getXPos() { return x; }

    public void move() { y += yVelocity; }

    public void draw(Graphics g) {
        g.drawRect(x, y, width, height);
        g.setColor(MAIN_COLOR);
        if(getXPos() == MID_LEFT || getXPos() == MID_RIGHT) {
            g.setColor(MID_COLOR);
        }
        g.fillRect(x, y, width, height);
    }
}
