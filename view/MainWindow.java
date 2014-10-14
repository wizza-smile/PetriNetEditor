package view;

import controller.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JToolBar.*;


public class MainWindow extends JFrame {//implements Scrollable

    GlobalController global_controller;

    Color editorBackgroundColor = new Color(224, 224, 255);

    JPanel panel;
    JLabel statusbar;
    Canvas canvas;
    JScrollPane jScrollPane;

    public MainWindow(GlobalController gctrl) {
        this.global_controller = gctrl;
        initview();
    }

    private void initview() {
        this.setTitle("PetriNetEditor");
        this.setSize(800, 600);
        this.setMinimumSize(new Dimension(400, 400));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


        JMenuBar menubar = createMenuBar();
        this.setJMenuBar(menubar);

        JToolBar toolbar = createToolBar();
        this.add(toolbar, BorderLayout.NORTH);

        statusbar = createStatusBar();
        this.add(statusbar, BorderLayout.SOUTH);




        canvas = new Canvas(this);
        canvas.setPreferredSize(new Dimension(600, 550));



        jScrollPane = new JScrollPane(canvas, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        jScrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.GRAY));

        jScrollPane.getVerticalScrollBar().setUnitIncrement(12);

        jScrollPane.getViewport().setBackground(editorBackgroundColor);

        this.add(jScrollPane, BorderLayout.CENTER);
    }






    private JMenuBar createMenuBar() {
        JMenuBar menubar = new JMenuBar();

        ImageIcon icon = new ImageIcon("exit.png");

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit", icon);
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        file.add(eMenuItem);

        menubar.add(file);

        return menubar;
    }


    public void setStatusBarText(String s) {
        statusbar.setText(s);
    }

    private JLabel createStatusBar() {
        JLabel statusLabel = new JLabel("status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusLabel.setBackground(Color.WHITE);
        statusLabel.setOpaque(true);

        return statusLabel;
    }


    private JToolBar createToolBar() {

        JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
        toolbar.setBackground(Color.DARK_GRAY);

        ImageIcon newi = new ImageIcon(this.getClass().getResource("images/new.png"));
        ImageIcon open = new ImageIcon(this.getClass().getResource("images/open.png"));
        ImageIcon save = new ImageIcon(this.getClass().getResource("images/save.png"));
        Separator jSeparator = new Separator();
        ImageIcon exit = new ImageIcon(this.getClass().getResource("images/exit.png"));

        JButton newb = new JButton(newi);
        JButton openb = new JButton(open);
        openb.setBorderPainted(false);
        JButton saveb = new JButton(save);
        saveb.setBorderPainted(false);
        JButton exitb = new JButton(exit);
        exitb.setBorderPainted(false);

        toolbar.add(newb);
        toolbar.add(openb);
        toolbar.add(saveb);
        toolbar.add(jSeparator);
        toolbar.add(exitb);
        toolbar.setFloatable(false);
        toolbar.setAlignmentX(0);
        //toolbar.setRollover(true);

        // Dimension tbSize = toolbar.getPreferredSize();
        // tbSize.width = 300; //as you like
        // toolbar.setPreferredSize(tbSize);

        exitb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        return toolbar;
    }


}


