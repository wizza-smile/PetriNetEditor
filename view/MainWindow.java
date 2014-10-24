package view;

import controller.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.JToolBar.*;


public class MainWindow extends JFrame {//implements Scrollable

    Color editorBackgroundColor = new Color(224, 224, 255);

    JPanel panel;
    JLabel statusbar;
    Canvas canvas;
    JScrollPane canvasPane;



    public void init() {
        this.setTitle("PetriNetEditor");
        this.setSize(800, 600);
        this.setMinimumSize(new Dimension(400, 400));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


        JMenuBar menubar = createMenuBar();
        this.setJMenuBar(menubar);

        JToolBar toolbar = new ButtonBar();// createToolBar();
        this.add(toolbar, BorderLayout.NORTH);

        statusbar = createStatusBar();
        this.add(statusbar, BorderLayout.SOUTH);

        canvasPane = createCanvasPane();
        this.add(canvasPane, BorderLayout.CENTER);
    }


    public void injectCanvas(Canvas canvas) {
        canvasPane.setViewportView(canvas);
    }


    private JScrollPane createCanvasPane() {
        canvasPane = new JScrollPane(canvas, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        canvasPane.setViewportBorder(BorderFactory.createLineBorder(Color.GRAY));
        canvasPane.getVerticalScrollBar().setUnitIncrement(12);
        canvasPane.getViewport().setBackground(editorBackgroundColor);

        return canvasPane;
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



}





class ButtonBar extends JToolBar {

    //{buttonId, setBorderPainted}
    public Object[][] fileButtons = new Object[][]{
        {"create_new", "new", false, 30},
        {"open", "open", false, 40},
        {"save", "save", false, 40},
        {"exit", "exit", false, 40}
    };

    public Object[][] modeButtons = new Object[][]{
        {"select_mode", "select", true, 60},
        {"place_mode", "Place", true, 60},
        {"transition_mode", "Transition", true, 60},
        {"arc_mode", "Arc", true, 60}
    };

    ButtonBar() {
        super(JToolBar.HORIZONTAL);

        this.setFloatable(false);
        this.setAlignmentX(0);
        this.setBackground(Color.DARK_GRAY);
        this.setMargin(new Insets(3,5,5,5));
        // this.setLayout(null);

        addButtonBlock(fileButtons, false);
        Separator jSeparator = new Separator();
        this.add(jSeparator);
        addButtonBlock(modeButtons, true);


    }

    private void addButtonBlock(Object[][] block, Boolean seperator) {
        for (Object[] button_info : block) {
           if (seperator) {
                this.addSeparator(new Dimension(13, 1));
           }
           this.add(createButton(button_info));
        }
    }


    private JButton createButton(Object[] button_info) {
        ImageIcon icon = new ImageIcon(this.getClass().getResource("images/" + button_info[0] + ".png"));
        JButton button = new JButton(icon);
        button.setToolTipText((String)button_info[1]);
        button.setBorderPainted((Boolean)button_info[2]);

        // button.setContentAreaFilled(true);
        // button.setBackground(Color.RED);

        button.setMaximumSize(new Dimension((int)button_info[3], 30));
        button.setPreferredSize(new Dimension((int)button_info[3], 30));

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                MainWindowController.executeButtonBarAction((String) button_info[0]);
            }
        });

        return button;
    }

    // private JButton createButton(String button_id) {
    //     ImageIcon icon = new ImageIcon(this.getClass().getResource("images/" + button_id + ".png"));
    //     JButton button = new JButton(icon);
    //     button.setBorderPainted(false);

    //     button.addActionListener(new ActionListener() {
    //         public void actionPerformed(ActionEvent event) {
    //             MainWindowController.executeButtonBarAction(button_id);
    //         }
    //     });

    //     return button;
    // }


}


