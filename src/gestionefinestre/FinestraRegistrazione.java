/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionefinestre;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Finestra per la registrazione di un nuovo utente
 * @author Alessandro Consolini
 */
public class FinestraRegistrazione extends JFrame implements ActionListener{
    private JTextField userInput;
    private JPasswordField pwInput1;
    private JPasswordField pwInput2;
    private JTextField addressInput;
    
    private JButton accettaBtn;
    private JButton annullaBtn;
    
    private JLabel title;
    private JLabel userLabel;
    private JLabel pwLabell;
    private JLabel pwLabel2;
    private JLabel addressLabel;
    
    private JLabel errUsr;
    private JLabel errPwd;
    private JLabel errAddr;
    
    private JPanel panelInput;
    private JPanel panelButton;
    
    private FinestraLogin prec;
    
    public static final int COD_PWD_OK = 0;
    public static final int COD_PWD_ERR1 = 1;
    public static final int COD_PWD_ERR2 = 2;

    /**
     * Costruttore che imposta la finestra
     * @param f la finestra precedente
     */
    public FinestraRegistrazione(FinestraLogin f){
        prec = f;
        title = new JLabel("Registrazione");
        userLabel = new JLabel("User");
        userInput = new JTextField(20);
        pwLabell = new JLabel("Password");
        pwInput1 = new JPasswordField(20);
        pwLabel2 = new JLabel("Ripeti Password");
        pwInput2 = new JPasswordField(20);
        addressLabel = new JLabel("Indirizzo");
        addressInput = new JTextField(20);
        
        accettaBtn = new JButton("Accetta");
        accettaBtn.addActionListener(this);
        annullaBtn = new JButton("Annulla");
        annullaBtn.addActionListener(new LogoutAnnullaListener(this));
        
        errUsr = new JLabel("Errore, username vuoto o già in uso");
        errUsr.setForeground(Color.RED);
        hideErrUsr();
        
        errPwd = new JLabel("");
        errPwd.setForeground(Color.RED);
        hideErrPwd();
        
        errAddr = new JLabel("Errore, campo obbligatorio");
        errAddr.setForeground(Color.RED);
        hideErrAddr();
        
        
        panelInput = new JPanel(new GridLayout(7,2,5,5));
        
        panelInput.add(new JLabel());
        panelInput.add(errUsr);
        panelInput.add(userLabel);
        panelInput.add(userInput);
        panelInput.add(pwLabell);
        panelInput.add(pwInput1);
        
        panelInput.add(new JLabel());
        panelInput.add(errPwd);
        panelInput.add(pwLabel2);
        panelInput.add(pwInput2);
        
        panelInput.add(new JLabel());
        panelInput.add(errAddr);
        panelInput.add(addressLabel);
        panelInput.add(addressInput);
        
        panelButton = new JPanel();
        panelButton.add(accettaBtn);
        panelButton.add(annullaBtn);
        
        this.setLayout(new BorderLayout());
        this.add(title, BorderLayout.NORTH);
        this.add(panelInput, BorderLayout.CENTER);
        this.add(panelButton, BorderLayout.SOUTH);
        this.add(new JPanel(), BorderLayout.EAST);
        this.add(new JPanel(), BorderLayout.WEST);
        this.setVisible(true);
        this.setResizable(false);
        this.pack();
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent we) {
                prec.setVisible(false);
            }
            @Override
            public void windowClosing(WindowEvent event) {
                prec.setLocationRelativeTo((JFrame)event.getSource());
                prec.setVisible(true);
                dispose();
            }
        });  
    }
    
    /**
     * Mostra l'errore di utente vuoto o gia esistente
     */
    private void displayErrUsr(){
        errUsr.setVisible(true);
    }
    /**
     * Nasconde l'errore di utente vuoto o gia esistente
     */
    private void hideErrUsr(){
        errUsr.setVisible(false);
    }
    /**
     * Mostra l'errore delle password non coincidenti
     */
    private void displayErr1Pwd(){
        errPwd.setText("Errore, le password non coincidono");
        errPwd.setVisible(true);
    }
    /**
     * Mostra l'errore della password vuota
     */
    private void displayErr2Pwd(){
        errPwd.setText("Errore, la password non può essere vuota");
        errPwd.setVisible(true);
    }
    /**
     * Nasconde l'errore associato alla password
     */
    private void hideErrPwd(){
        errPwd.setVisible(false);
    }
    /**
     * Mostra l'errore di indirizzo vuoto
     */
    private void displayErrAddr(){
        errAddr.setVisible(true);
    }
    /**
     * asconde l'errore associato all'indirizzo
     */
    private void hideErrAddr(){
        errAddr.setVisible(false);
    }

    /**
     * Controlla che le due password inserite siano coincidenti
     * @return vero se le due password coincidono, flaso altrimenti
     */
    private int checkPassword(){
        String pw1 = new String(pwInput1.getPassword());
        String pw2 = new String(pwInput2.getPassword());
        
        if(pw1.isEmpty())
            return COD_PWD_ERR2;
        
        if(!pw1.equals(pw2))
            return COD_PWD_ERR1;
        
        return COD_PWD_OK;
    }
    
    /**
     * 
     * @param user lo username che devo controllare
     * @return true se lo user esiste, false altrimenti
     */
    private boolean checkUser(String user){
        //ricerca nel file se è presente lo username
        try (BufferedReader reader = new BufferedReader(new FileReader(FinestraLogin.FILE_UTENTI))){
            String line;
            while((line = reader.readLine()) != null){
                String[] array = line.split(FileSpedizioni.SEPARATORE);
                if(array[0].equals(user)){
                    return true;
                }
            }
        } catch (IOException e) {
            String error = Arrays.toString(e.getStackTrace());
            JOptionPane.showMessageDialog(this, "errore nell'apertura del file.");
            System.err.println(error);
        }
        return false;
    }
    
    /**
     * Regisstra un nuovo utente
     * @return vero se è riuscito a scrivere il nuovo utente sul file, falso altrimenti
     */
    private boolean registraUtente(){
        String riga = "";
        riga += userInput.getText().trim() + FileSpedizioni.SEPARATORE;
        riga += new String(pwInput1.getPassword()) + FileSpedizioni.SEPARATORE;
        riga += addressInput.getText().trim() + FileSpedizioni.SEPARATORE;
        
        try (FileWriter writer = new FileWriter(FinestraLogin.FILE_UTENTI,true)){ //il true per fare append
            writer.append(riga);
            writer.append(System.lineSeparator());
            writer.close();
        } catch (Exception e) {
            String error = Arrays.toString(e.getStackTrace());
            JOptionPane.showMessageDialog(null,"Ci sono stati problemi nel salvataggio delle spedizioni");
            System.err.println(error);
            return false;
        }
        return true;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        boolean usrCheck = checkUser(userInput.getText().trim());
        int pwdCheck = checkPassword();
        boolean addressCheck = !addressInput.getText().isEmpty();

        if(!addressCheck){
            displayErrAddr();
        }else{
            hideErrAddr();
        }

        if(usrCheck){
            displayErrUsr();
        }else{
            hideErrUsr();
            switch(pwdCheck){
                case COD_PWD_OK:
                    hideErrPwd();
                    if(addressCheck){
                        if(registraUtente()){
                            JOptionPane.showMessageDialog(this, "Utente " + userInput.getText().trim()+ " registrato");
                        }
                        prec.setVisible(true);
                        dispose();
                    }
                    break;
                case COD_PWD_ERR1:
                    displayErr1Pwd();
                    break;
                case COD_PWD_ERR2:
                    displayErr2Pwd();
                    break;
                default:
                    System.err.println("Errore valore ritorno funzione controllo password");
                    prec.setVisible(true);
                    dispose();
            }
        }
    }
}
