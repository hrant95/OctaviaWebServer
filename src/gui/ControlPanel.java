package gui;

import local_server.LocalServer;
import sun.jdbc.odbc.JdbcOdbcBatchUpdateException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static java.awt.Font.PLAIN;

/**
 * Created by hrant on 11/14/15.
 */
public class ControlPanel extends JFrame implements ActionListener {

    // Octavia
    private JPanel octaviaPanel = new JPanel();
    private JLabel octaviaLabel = new JLabel("Octavia local server control panel");

    // Path choser
    private JPanel choserPanel = new JPanel();
    private JLabel pathLabel = new JLabel("Sites Directory Path");
    private JTextField sitesDirPath = new JTextField(30);
    private JButton choseSitesDir = new JButton("Chose");

    // Port
    private JPanel portPanel = new JPanel();
    private JLabel portLabel = new JLabel("Port for localhost");
    private JTextField portField = new JTextField("1786", 5);

    // Start Server
    private JPanel startServerPanel = new JPanel();
    private JButton startServerButton = new JButton("Start server");
    private JButton stopServerButton = new JButton("Stop server");

    private String SITES_DIR_PATH;
    private int PORT = 1786;
    private Thread server = null;
    private LocalServer localServer;

    public ControlPanel() {
        super("ControlPanel");
        setSize(new Dimension(700, 500));
        setVisible(true);
        setLayout(new FlowLayout());

        // Octavia
        octaviaPanel.setLayout(new FlowLayout());
        octaviaPanel.setPreferredSize(new Dimension(600, 45));
        octaviaPanel.setBackground(Color.cyan);
        octaviaLabel.setFont(new Font("Serif", Font.BOLD, 30));
        octaviaPanel.add(octaviaLabel);
        add(octaviaPanel);

        // Choser
        choserPanel.setLayout(new FlowLayout());
        choserPanel.setPreferredSize(new Dimension(600, 35));
        choserPanel.setBackground(Color.ORANGE);
        choserPanel.add(pathLabel);
        choserPanel.add(sitesDirPath);
        choserPanel.add(choseSitesDir);
        add(choserPanel);
        choseSitesDir.addActionListener(this);

        // Port
        portPanel.setLayout(new FlowLayout());
        portPanel.setPreferredSize(new Dimension(600, 35));
        portPanel.setBackground(Color.ORANGE);
        portPanel.add(portLabel);
        portPanel.add(portField);
        add(portPanel);

        // Start Server
        startServerPanel.setLayout(new FlowLayout());
        startServerPanel.setPreferredSize(new Dimension(600, 35));
        startServerPanel.setBackground(Color.ORANGE);
        startServerPanel.add(startServerButton);
        startServerPanel.add(stopServerButton);
        add(startServerPanel);
        startServerButton.addActionListener(this);
        stopServerButton.addActionListener(this);
        stopServerButton.setEnabled(false);

    }

    public String getSITES_DIR_PATH() {
        return SITES_DIR_PATH;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == choseSitesDir) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setApproveButtonText("Chose dir");
            chooser.showOpenDialog(null);
            SITES_DIR_PATH = chooser.getSelectedFile().getAbsolutePath();
            sitesDirPath.setText(SITES_DIR_PATH);
        }

        if (e.getSource() == startServerButton) {
            try {
                PORT = Integer.parseInt(portField.getText());
                localServer = new LocalServer(PORT, SITES_DIR_PATH);
                server = new Thread(localServer);
                server.start();

                sitesDirPath.setEnabled(false);
                choseSitesDir.setEnabled(false);
                portField.setEnabled(false);
                startServerButton.setEnabled(false);
                stopServerButton.setEnabled(true);
            } catch (java.lang.NumberFormatException ne) {
                JOptionPane.showMessageDialog(null, "Please input number.");
            }

        }

        if(e.getSource() == stopServerButton){
            localServer.closeServerSocket();
            server.stop();
            sitesDirPath.setEnabled(true);
            choseSitesDir.setEnabled(true);
            portField.setEnabled(true);
            startServerButton.setEnabled(true);
            stopServerButton.setEnabled(false);
        }
    }
}
