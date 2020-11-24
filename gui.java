/* PoeAn Lu
   Hackathon
   GUI for project 
   v1 */
   
import java.awt.EventQueue;
import javax.swing.JFrame;

public class gui extends JFrame {

    public gui() {

        initUI();
    }

    private void initUI() {
        
        setTitle("Simple example");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new gui();
            ex.setVisible(true);
        });
    }
}