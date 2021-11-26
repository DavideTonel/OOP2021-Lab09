package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 
 * This is a reactive GUI.
 */
public final class ConcurrentGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final double WIDTH_REDUX = 0.2;
    private static final double HEIGHT_REDUX = 0.1;
    private final JLabel display = new JLabel();


    /**
     * Builds a new CGUI.
     */
    public ConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_REDUX), (int) (screenSize.getHeight() * HEIGHT_REDUX));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();
        panel.add(display);
        // Up button
        final JButton up = new JButton("up");
        panel.add(up);
        // Down button
        final JButton down = new JButton("down");
        panel.add(down);
        // Stop button
        final JButton stop = new JButton("stop");
        panel.add(stop);
        // Handlers
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
    }

    /** 
     * The counter agent.
     */
    private class Agent implements Runnable {

        private volatile boolean stop;
        private volatile boolean up = true;
        private volatile int counter;

        @Override
        public void run() {
            while (!this.stop) {
                try {

                    counter += up ? 1 : -1;

                    SwingUtilities.invokeLater(() -> display.setText(Integer.toString(this.counter)));
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
         * Command to stop counting.
         */
        public void stopCount() {
            this.stop = true;
        }
    }

}
