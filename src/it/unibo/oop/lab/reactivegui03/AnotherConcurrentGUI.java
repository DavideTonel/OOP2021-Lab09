package it.unibo.oop.lab.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * A reactive GUI.
 */
public final class AnotherConcurrentGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final double WIDTH_REDUX = 0.2;
    private static final double HEIGHT_REDUX = 0.1;
    private static final long WAITING_TIME = TimeUnit.SECONDS.toMillis(10);
    private final JLabel display = new JLabel();

    /**
     * Builds a new GUI.
     */
    public AnotherConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_REDUX), (int) (screenSize.getHeight() * HEIGHT_REDUX));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();
        panel.add(display);
        final JButton up = new JButton("Up");
        final JButton down = new JButton("Down");
        final JButton stop = new JButton("Stop");
        panel.add(up);
        panel.add(down);
        panel.add(stop);

        final Agent agent = new Agent();
        up.addActionListener(e -> agent.countUp());
        down.addActionListener(e -> agent.countDown());
        stop.addActionListener(e -> {
            agent.stopCount();
            up.setEnabled(false);
            down.setEnabled(false);
            stop.setEnabled(false);
        });
        this.getContentPane().add(panel);
        this.setVisible(true);

        new Thread(agent).start();
        new Thread(() -> {
            try {
                Thread.sleep(WAITING_TIME);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            agent.stopCount();
        }).start();
    }

    /**
     * The counter agent.
     */
    private class Agent implements Runnable {

        private volatile boolean stop;
        private volatile int count;
        private volatile boolean up = true;

        @Override
        public void run() {
            while (!this.stop) {
                try {
                    count += up ? 1 : -1;

                    SwingUtilities.invokeLater(() -> display.setText(Integer.toString(count)));
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        /**
         * Command to count up.
         */
        public void countUp() {
            this.up = true;
        }

        /**
         * Command to count down.
         */
        public void countDown() {
            this.up = false;
        }

        /**
         * Command to stop count.
         */
        public void stopCount() {
            this.stop = true;
        }
    }
}
