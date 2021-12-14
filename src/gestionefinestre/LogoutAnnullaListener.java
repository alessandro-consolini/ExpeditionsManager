/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionefinestre;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * Ascoltatore usato per annullare un'azione o eseguire il logout
 * @author Alessandro Consolini
 */
public class LogoutAnnullaListener implements ActionListener{

    private JFrame finestra;
    /**
     * inizializza l'ascoltatore
     * @param f la finestra da cui far parire l'evento di chiusura
     */
    public LogoutAnnullaListener(JFrame f){
        this.finestra = f;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        finestra.dispatchEvent(new WindowEvent(finestra, WindowEvent.WINDOW_CLOSING));
    }
    
}
