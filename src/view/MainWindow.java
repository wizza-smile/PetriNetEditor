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


/**
 * Main Window - contains the Toolbar, the MenuBar and the statusBar,
 * as well as, of course, the Canvas in its ScrollPane.
 */
public class MainWindow extends JFrame {

    /** the background color of the canvasPane */
    Color editorBackgroundColor = new Color(224, 224, 255);

    /** the scrollpane that contains the canvas */
    protected JScrollPane canvasPane;

    /** the canvas that the petrinet is drawn upon */
    protected Canvas canvas;

    /** the statusbar on the bottom of the window */
    protected JLabel statusbar;


    /**
     * the File related functionality
     * (displayed as buttons and menu entries)
     * encoded as Object:
     * {id, tooltipText}
     * the id will also be used to bind the functionality
     */
    public Object[][] fileFunctions = new Object[][]{
        {"create_new", "New"},
        {"open", "Open"},
        {"save", "Save"},
        {"exit", "Exit"}
    };

    // same as above for options
    public Object[][] optionsFunctions = new Object[][]{
        {"toggle_opacity", "toggle opacity"}
    };

    /**
     * the ActionMode related functionality
     * (displayed as buttons)
     * encoded as Object:
     * {id, tooltipText}
     * the id will also be used to bind the functionality
     */
    public Object[][] modeFunctions = new Object[][]{
        {"select_mode", "Select"},
        {"place_mode", "Place"},
        {"transition_mode", "Transition"},
        {"arc_mode", "Arc"}
    };


    /**
     * a resize Listener to call the Canvas-Resizing whenever the mainWindow size changes
     */
    private class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            CanvasController.cleanUpCanvas();
        }
    }

    /**
     * initializes the MainWindow
     * sets default values and creates all the parts:
     * menubar/toolbar/canvasPane/statusbar
     * sets a resizeListener
     */
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

    /**
     * injects the canvas object into the canvasPane
     * @param canvas the canvas object
     */
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

    /**
     * returns the canvasPane
     * @return the canvasPane
     */
    public JScrollPane getCanvasPane() {
        return canvasPane;
    }

    /**
     * set a text to the statusbar
     * @param s the text to be displayed in the statusbar
     */
    public void setStatusBarText(String s) {
        statusbar.setText(s);
    }

    private JLabel createStatusBar() {
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusLabel.setBackground(Color.WHITE);
        statusLabel.setOpaque(true);

        return statusLabel;
    }


    /**
     * the MenuBar.
     * contains the "File" and the "Options" menu
     */
    private class MenuBar extends JMenuBar {

        MenuBar() {
            //File Menu
            JMenu file = new JMenu("File");
            this.addMenuBlock(file, fileFunctions);

            //Options Menu
            JMenu options = new JMenu("Options");
            this.addMenuBlock(options, optionsFunctions);
        }

        /**
         * creates and adds menuentries to a menu
         * @param menu  the menu.
         * @param block the parameter which defines the menuentries
         */
        private void addMenuBlock(JMenu menu, Object[][] block) {
            for (Object[] button_info : block) {
                final Object[] final_button_info = button_info;
                JMenuItem eMenuItem = new JMenuItem((String)button_info[1]);
                eMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        MainWindowController.executeMenuOrButtonBarAction((String)final_button_info[0]);
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

            /** will take display parameter for each block of buttons */
            Object[] displayParams;

            this.setFloatable(false);
            this.setAlignmentX(0);
            this.setBackground(Color.DARK_GRAY);
            this.setMargin(new Insets(3,5,5,5));

            // add the FileMenu related buttons
            displayParams = new Object[]{"file", 40};
            addButtonGroup(fileFunctions, displayParams);

            Separator jSeparator = new Separator();
            this.add(jSeparator);

            // add the ActionMode buttons
            displayParams = new Object[]{"mode", 60};
            addButtonGroup(modeFunctions, displayParams);

            Separator jSeparator2 = new Separator();
            this.add(jSeparator2);

            // add the slider that controls the display size of various elements
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

        /**
         * creates and adds a group of buttons to the buttonbar
         * @param block         contains the params that defines the buttons
         * @param displayParams contains params to alter the appearance
         */
        private void addButtonGroup(Object[][] block, Object[] displayParams) {
            ButtonGroup grp = new ButtonGroup();
            for (Object[] button_info : block) {
                this.addSeparator(new Dimension(8, 1));
                JButton btn = createButton(button_info, displayParams, grp);
                grp.add(btn);
                this.add(btn);
            }
        }

        /**
         * create a single button and add it to the related buttonGroup
         * @param  button_info   contains the params that define the button
         * @param  displayParams defines the appearance of the button
         * @param  grp           the ButtonGroup this button will be added to
         * @return               [description]
         */
        private JButton createButton(Object[] button_info, Object[] displayParams, ButtonGroup grp) {
            ImageIcon icon = new ImageIcon(this.getClass().getResource("images/" + button_info[0] + ".png"));
            final JButton button = new JButton(icon);
            final Object[] final_button_info = button_info;
            final ButtonGroup group = grp;

            button.setToolTipText((String)button_info[1]);

            if ((String)displayParams[0] == "file") {
                button.setBorder(new LineBorder(Color.RED, 2));
                button.setBorderPainted(true);
                button.setBackground(new Color( 255,0,0,120 ));
                button.setContentAreaFilled(true);
            }

            button.setOpaque(true);

            button.setMaximumSize(new Dimension((int)displayParams[1], 30));
            button.setPreferredSize(new Dimension((int)displayParams[1], 30));

            //this actionListener will ensure that only one button of a buttonGroup appears as "selected/active" at a time
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
                    MainWindowController.executeMenuOrButtonBarAction((String)final_button_info[0]);
                }
            });

            return button;
        }

    }

}


