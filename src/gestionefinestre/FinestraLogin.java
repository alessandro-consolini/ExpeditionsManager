/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionefinestre;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import tipidato.TipoUtente;

/**
 * Finestra di login
 * @author Alessandro Consolini
 */
public class FinestraLogin extends JFrame implements ActionListener{
    public static final String FILE_UTENTI = "users.csv";
    private TipoUtente tipoLogin;
    
    private JLabel err;
    private JTextField usrInput;
    private JPasswordField pwdInput;
    private JButton loginBtn;
    private JButton annullaBtn;
    
    /**
     * Costruttore che imposta la finestra
     * @param tipo tipo di utente che puo essere cliente o amministratore
     * @param f la finestra precedente
     */
    public FinestraLogin(TipoUtente tipo, JFrame f){
        tipoLogin = tipo;
        JLabel title = new JLabel();
        
        switch(tipoLogin){
            case ADMIN:
                title.setText("login Amministratore");
                break;
            case CLIENTE:
                title.setText("login Cliente");
                break;
        }
        
        err = new JLabel(" ");
        JLabel usr = new JLabel("Username");
        JLabel pwd = new JLabel("Password");
        
        usrInput = new JTextField(20);
        pwdInput = new JPasswordField(20);
        loginBtn = new JButton("Login");
        loginBtn.addActionListener(this);
        JButton registratiBtn;
        
        annullaBtn = new JButton("Annulla");
        annullaBtn.addActionListener(new LogoutAnnullaListener(this));
        
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        p1.add(usr);
        p1.add(usrInput);
        p2.add(pwd);
        p2.add(pwdInput);
        p3.add(loginBtn);
        p3.add(annullaBtn);
        
        if(tipoLogin.equals(TipoUtente.CLIENTE)){
            registratiBtn = new JButton("Registrati");
            registratiBtn.addActionListener(this);
            p3.add(registratiBtn);
        }
        this.setLayout(new FlowLayout());
        this.add(title);
        this.add(err);
        this.add(p1);
        this.add(p2);
        this.add(p3);
        
        this.setSize(500, 220);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(f);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent we) {
                usrInput.setText("");
                pwdInput.setText("");
                f.setVisible(false);
            }
            @Override
            public void windowClosing(WindowEvent event) {
                f.setLocationRelativeTo((JFrame)event.getSource());
                f.setVisible(true);
                dispose();
            }
        });
    }
    
    /**
     * Prova ad eseguire l'autenticazione dell'amministratore
     * @return vero se l'autenticazione avviene, falso altrimenti
     */
    private boolean authenticateAdmin(){
        // hardcoded username and password
        String p = new String(pwdInput.getPassword());        
        if(usrInput.getText().equals("admin") && p.equals("admin")){
            return true;
        }
        return false;
    }
    
    /**
     * Prova ad eseguire l'autenticazione del cliente
     * @return vero se l'autenticazione avviene, falso altrimenti
     */
    private boolean authenticateUser(){
        String pwd = new String(pwdInput.getPassword());
        String usr = usrInput.getText().trim();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_UTENTI))){
            String line;
            while((line = reader.readLine()) != null){
                String[] array = line.split(FileSpedizioni.SEPARATORE);
                if(array[0].equals(usr) && array[1].equals(pwd)){
                    return true;
                }
            }
        } catch (IOException e) {
            String error = Arrays.toString(e.getStackTrace());
            JOptionPane.showMessageDialog(this, "errore nell'apertura del file utenti.");
            System.err.println(error);
        }
        return false;
    }
    
    /**
     * Rende visibile l'errore di username o password errati
     */
    private void displayErr(){
        err.setForeground(Color.RED);
        err.setText("ERRORE, USERNAME O PASSWORD ERRATI");
    }
    
    /**
     * Nasconde l'errore di username o password errati
     */
    private void hideErr(){
        err.setText("");
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if(tipoLogin.equals(TipoUtente.ADMIN)){
            if(!authenticateAdmin()){
                displayErr();
            }else{
                JFrame fAdmin = new FinestraAmministratoreAutenticato(this);
                fAdmin.setLocationRelativeTo(this);
                hideErr();
            }
        }else if(((JButton)ae.getSource()).getText().equals("Registrati")){
            JFrame registrationFrame = new FinestraRegistrazione(this);
            registrationFrame.setLocationRelativeTo(this);            
        }else{
            if(!authenticateUser()){
                displayErr();
            }else{
                JFrame fCliente = new FinestraClienteAutenticato(this, usrInput.getText().trim());
                fCliente.setLocationRelativeTo(this);
                hideErr();
            }
        }
    }
}

