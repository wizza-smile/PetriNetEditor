package view;

import model.*;
import view.figures.*;
import controller.*;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.JToolBar.*;


public class MainWindow extends JFrame {

    Color editorBackgroundColor = new Color(224, 224, 255);

    JPanel panel;
    JLabel statusbar;
    Canvas canvas;
    public JScrollPane canvasPane;

    //{buttonId, tooltipText}
    public Object[][] fileButtons = new Object[][]{
        {"create_new", "New"},
        {"open", "Open"},
        {"save", "Save"},
        {"exit", "Exit"}
    };

    public Object[][] modeButtons = new Object[][]{
        {"select_mode", "Select"},
        {"place_mode", "Place"},
        {"transition_mode", "Transition"},
        {"arc_mode", "Arc"}
    };


    // a resize Listener to call the Canvas-Resizing whenever the mainWindow size changes
    private class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            CanvasController.cleanUpCanvas();
        }
    }

    public void init() {
        this.addComponentListener(new ResizeListener());
        this.setTitle(GlobalController.applicationTitle);
        this.setSize(657, 500);
        this.setMinimumSize(new Dimension(400, 400));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //create an instance of inner class MenuBar
        JMenuBar menubar = new MenuBar();
        this.setJMenuBar(menubar);

        //create an instance of inner class ButtonBar
        JToolBar toolbar = new ButtonBar();
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
        canvasPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        canvasPane.setViewportBorder(BorderFactory.createLineBorder(Color.GRAY));
        canvasPane.getVerticalScrollBar().setUnitIncrement(12);
        canvasPane.getHorizontalScrollBar().setUnitIncrement(12);
        canvasPane.getViewport().setBackground(editorBackgroundColor);

        return canvasPane;
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


    private class MenuBar extends JMenuBar {

        MenuBar() {
            // ImageIcon icon = new ImageIcon("exit.png");

            //File Menu
            JMenu file = new JMenu("File");
            this.addMenuBlock(file, fileButtons);

            // //ActionMode Menu
            // JMenu actionMode = new JMenu("Action Mode");
            // this.addMenuBlock(actionMode, modeButtons);

            //Options Menu
            JMenu options = new JMenu("Options");
            JMenuItem opacityMenuItem = new JMenuItem("toggle opacity");
            opacityMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    GlobalController.toggleOpacity();
                }
            });
            options.add(opacityMenuItem);
            this.add(options);
        }

        private void addMenuBlock(JMenu menu, Object[][] block) {
            for (Object[] button_info : block) {
                final Object[] final_button_info = button_info;
                JMenuItem eMenuItem = new JMenuItem((String)button_info[1]);
                eMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        MainWindowController.executeButtonBarAction((String)final_button_info[0]);
                    }
                });
                menu.add(eMenuItem);
            }
            this.add(menu);
        }
    }

    /**
     * the ButtonBar
     */
    private class ButtonBar extends JToolBar {

        ButtonBar() {
            super(JToolBar.HORIZONTAL);

            //will take display parameter for each block of buttons
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

            Separator jSeparator2 = new Separator();
            this.add(jSeparator2);

            JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 20);
                slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    Double size = 0.7 + (((JSlider)e.getSource()).getValue() * (0.8/100));
                    GlobalController.setSize(size);
                    CanvasController.repaintCanvas();
                }
            });

            slider.setMaximumSize(new Dimension(150, 30));
            slider.setPreferredSize(new Dimension(150, 30));

            // //Create the label.
            // JLabel sliderLabel = new JLabel("Size:", JLabel.CENTER);
            // sliderLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
            // sliderLabel.setForeground(Color.white);
            // this.add(sliderLabel);

            this.add(slider);
        }

        private void addButtonBlock(Object[][] block, Object[] blockParams) {
            ButtonGroup grp = new ButtonGroup();
            for (Object[] button_info : block) {
                this.addSeparator(new Dimension(8, 1));
                JButton btn = createButton(button_info, blockParams, grp);
                grp.add(btn);
                this.add(btn);
            }
        }


        private JButton createButton(Object[] button_info, Object[] blockParams, ButtonGroup grp) {
            ImageIcon icon = new ImageIcon(this.getClass().getResource("images/" + button_info[0] + ".png"));
            final JButton button = new JButton(icon);
            final Object[] final_button_info = button_info;
            final ButtonGroup group = grp;

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
                    //unselect all other buttons from related group
                    Enumeration<AbstractButton> button_enum = group.getElements();
                    while(button_enum.hasMoreElements()){
                        JButton jb = (JButton) button_enum.nextElement();
                        jb.setSelected(false);
                    }
                    //select this button and execute action
                    button.setSelected(true);
                    MainWindowController.executeButtonBarAction((String)final_button_info[0]);
                }
            });

            return button;
        }

    }

}


