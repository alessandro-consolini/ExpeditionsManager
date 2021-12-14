/*
gestionefinestre.Main * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionefinestre;

import tipidato.TipoUtente;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Finestra Principale del programma
 * @author Alessandro Consolini
 */
public class FinestraAvvio extends JFrame implements ActionListener{
    private JLabel titolo;
    private JButton logCliente;
    private JButton logAdmin;
    
    /**
     * Costruttore che imposta la finestra
     */
    public FinestraAvvio() {
        FileSpedizioni.carica();
        titolo = new JLabel("Gestione Spedizioni");
        logCliente = new JButton("Login cliente");
        logAdmin = new JButton("Login amministratore");
        Dimension btnDim = new Dimension(350,40);
        logCliente.setPreferredSize(btnDim);
        logAdmin.setPreferredSize(btnDim);
        
        logCliente.addActionListener(this);
        logAdmin.addActionListener(this);
        
        File fileUsers = new File(FinestraLogin.FILE_UTENTI);
        try{
            if(!fileUsers.exists())
                fileUsers.createNewFile();
        }catch(IOException e){
            String error = Arrays.toString(e.getStackTrace());
            JOptionPane.showMessageDialog(null, "Errore nella creazioe del file utenti.");
            System.err.println(error);
        }
        
        this.setSize(500, 200);
        this.setLayout(new FlowLayout());
        this.add(titolo);
        this.add(logAdmin);
        this.add(logCliente);
    }
    
    /**
     * sgestisce gli eventi generati dai bottoni di questa finestra
     * @param ae evento
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        String s = ((JButton)ae.getSource()).getText();
        JFrame login;
        
        if(s.equals("Login cliente")){
            login = new FinestraLogin(TipoUtente.CLIENTE,this);
        }else{
            login = new FinestraLogin(TipoUtente.ADMIN,this);
        }  
    }
}
