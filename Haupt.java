import view.*;

import javax.swing.*;

class Haupt {

    public static void main(String[] args) {
        System.out.println("START");

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PetriNetEditor pne = new PetriNetEditor();
                pne.setVisible(true);
            }
        });
    }


}


