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
    public JScrollPane canvasPane;



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
        // canvasPane.setSize(200, 200);
        this.add(canvasPane, BorderLayout.CENTER);
    }


    public void injectCanvas(Canvas canvas) {
        canvasPane.setViewportView(canvas);
    }


    private JScrollPane createCanvasPane() {
        canvasPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        canvasPane.setViewportBorder(BorderFactory.createLineBorder(Color.GRAY));
        canvasPane.getVerticalScrollBar().setUnitIncrement(12);
        canvasPane.getHorizontalScrollBar().setUnitIncrement(12);
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

    //{buttonId, tooltipText}
    public Object[][] fileButtons = new Object[][]{
        {"create_new", "new"},
        {"open", "open"},
        {"save", "save"},
        {"exit", "exit"}
    };

    public Object[][] modeButtons = new Object[][]{
        {"select_mode", "select"},
        {"place_mode", "Place"},
        {"transition_mode", "Transition"},
        {"arc_mode", "Arc"}
    };

    ButtonBar() {
        super(JToolBar.HORIZONTAL);

        //set display parameter for each block of buttons
        Object[] blockParams;

        this.setFloatable(false);
        this.setAlignmentX(0);
        this.setBackground(Color.DARK_GRAY);
        this.setMargin(new Insets(3,5,5,5));

        blockParams = new Object[]{"file", 40};
        addButtonBlock(fileButtons, blockParams);

        Separator jSeparator = new Separator();
        this.add(jSeparator);

        blockParams = new Object[]{"mode", 60};
        addButtonBlock(modeButtons, blockParams);

        // this.setRollover(true);
    }

    private void addButtonBlock(Object[][] block, Object[] blockParams) {
        for (Object[] button_info : block) {
           this.addSeparator(new Dimension(8, 1));
           this.add(createButton(button_info, blockParams));
        }
    }

    private JButton createButton(Object[] button_info, Object[] blockParams) {
        ImageIcon icon = new ImageIcon(this.getClass().getResource("images/" + button_info[0] + ".png"));
        JButton button = new JButton(icon);
        final Object[] final_button_info = button_info;

        button.setToolTipText((String)button_info[1]);

        if ((String)blockParams[0] == "file") {
            button.setBorder(new LineBorder(Color.RED, 2));
            button.setBorderPainted(true);
            button.setBackground(new Color( 255,0,0,120 ));
            button.setContentAreaFilled(true);
        }

        button.setOpaque(true);

        button.setMaximumSize(new Dimension((int)blockParams[1], 30));
        button.setPreferredSize(new Dimension((int)blockParams[1], 30));

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                MainWindowController.executeButtonBarAction((String)final_button_info[0]);
            }
        });

        return button;
    }

}


