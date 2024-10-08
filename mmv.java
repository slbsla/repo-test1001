import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MouseMover {

    public static void main(String[] args) {
        try {
            Robot robot = new Robot();
            Random random = new Random();

            // Loop to move the mouse every 1 minute (60 seconds)
            while (true) {
                // Get screen size
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int screenWidth = (int) screenSize.getWidth();
                int screenHeight = (int) screenSize.getHeight();

                // Generate random x and y coordinates
                int x = random.nextInt(screenWidth);
                int y = random.nextInt(screenHeight);

                // Move the mouse to the random position
                robot.mouseMove(x, y);
                System.out.println("Mouse moved to: (" + x + ", " + y + ")");

                // Wait for 1 minute (60 seconds)
                TimeUnit.MINUTES.sleep(1);
            }
        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}