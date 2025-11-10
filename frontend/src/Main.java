
import main.ui.EventListFrame;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new EventListFrame().setVisible(true);
        });
    }
}
