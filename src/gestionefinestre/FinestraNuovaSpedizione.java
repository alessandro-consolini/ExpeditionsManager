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
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import tipidato.SpedizioneAssicurata;
import tipidato.SpedizioneNormale;
import tipidato.StatoSpedizione;

/**
 * Finestra usata da un utente per creare una nuova spedizione
 * @author Alessandro Consolini
 */
public class FinestraNuovaSpedizione extends JFrame implements ActionListener{
    public static final int ERR1_ID = 0;
    public static final int ERR2_ID = 1;
    public static final String ERR1_MSG = "Valore non accettato.";
    public static final String ERR2_MSG = "Campo necessario.";
    
    private JTextField destinazioneInput;
    private JTextField cittaInput;
    private JTextField pesoInput;
    private JTextField valoreInput;
    
    private JLabel destLabel;
    private JLabel cittaLabel;
    private JLabel pesoLabel;
    private JLabel valoreLabel;
    
    private JLabel errLabel1;
    private JLabel errLabel2;
    private JLabel errLabel3;
    private JLabel errLabel4;
    
    private JLabel title;
    
    private JPanel panelInput;
    private JPanel panelButton;
    
    private JButton annullaBtn;
    private JButton accettaBtn;
    private JCheckBox spedizioneAssicurata;
    
    private FinestraClienteAutenticato prec;
    private String user;
    private int nOrdine;
    private boolean assicurato;
    
    /**
     * Costruttore che imposta la finestra
     * @param f la finestra precedente
     * @param user nome dell'utente che vuole fare una nuova spedizione
     * @param nOrdine numero di ordine di questo utente nella stessa sessione
     */
    public FinestraNuovaSpedizione(FinestraClienteAutenticato f, String user, int nOrdine){
        this.user = user;
        this.nOrdine = nOrdine;
        this.prec = f;
        assicurato = false;
        title = new JLabel("NUOVA SPEDIZIONE");
        destinazioneInput = new JTextField();
        cittaInput = new JTextField();
        pesoInput = new JTextField();
        valoreInput = new JTextField();
        valoreInput.setEnabled(false);                                          //non abilitato di default perchè la spedizione non è assicurata di default
        
        destLabel = new JLabel("Indirizzo");
        cittaLabel = new JLabel("Citta");
        pesoLabel = new JLabel("Peso(kg)");
        valoreLabel = new JLabel("Valore(min "+ SpedizioneAssicurata.MIN_VAL+" euro)");
        
        errLabel1 = new JLabel("");
        errLabel1.setForeground(Color.RED);
        errLabel2 = new JLabel("");
        errLabel2.setForeground(Color.RED);        
        errLabel3 = new JLabel("");
        errLabel3.setForeground(Color.RED);        
        errLabel4 = new JLabel("");
        errLabel4.setForeground(Color.RED);        
        
        annullaBtn = new JButton("Annulla");
        annullaBtn.addActionListener(new LogoutAnnullaListener(this));
        accettaBtn = new JButton("Accetta");
        accettaBtn.addActionListener(this);
        spedizioneAssicurata = new JCheckBox("Spedizione Assicurata");
        spedizioneAssicurata.addActionListener(this);
        
        panelInput = new JPanel(new GridLayout(9,2,5,5));
        panelInput.add(spedizioneAssicurata);
        panelInput.add(new JLabel());
        panelInput.add(errLabel1);
        panelInput.add(new JLabel());
        panelInput.add(destLabel);
        panelInput.add(destinazioneInput);
        panelInput.add(errLabel2);
        panelInput.add(new JLabel());
        panelInput.add(cittaLabel);
        panelInput.add(cittaInput);
        panelInput.add(errLabel3);
        panelInput.add(new JLabel());
        panelInput.add(pesoLabel);
        panelInput.add(pesoInput);
        panelInput.add(errLabel4);
        panelInput.add(new JLabel());
        panelInput.add(valoreLabel);
        panelInput.add(valoreInput);
        
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
                prec.incrementaOrdiniEseguiti();
                prec.aggiornaTabella();
                prec.setLocationRelativeTo((JFrame)event.getSource());
                prec.setVisible(true);
                dispose();
            }
        });
    }

    /**
     * Mostra l'errore associato all'indirizzo
     */
    public void showErrIndirizzo(){
        errLabel1.setText(ERR2_MSG);
    }
    
    /**
     * Mostra l'errore associato alla citta
     */
    public void showErrCitta(){
        errLabel2.setText(ERR2_MSG);
    }
    
    /**
     * Mostra l'errore associato al peso fornito in input
     * @param errId identificativo dell'errore da mostrare
     */
    public void showErrPeso(int errId){
        switch(errId){
            case ERR1_ID:
                errLabel3.setText(ERR1_MSG);
                break;
            case ERR2_ID:
                errLabel3.setText(ERR2_MSG);
                break;
            default:
                JOptionPane.showMessageDialog(this, "ERRORE INASPETTATO, non è possibile creare una nuova spedizione. - ID_ERR_LABEL_3");
                System.err.println("Probelmi nel mostrare gli errori - errLable1");
        }
    }
    
    /**
     * Mostra l'errore associato al valore fornito in input
     * @param errId identificativo dell'errore da mostrare
     */
    public void showErrValore(int errId){
        switch(errId){
            case ERR1_ID:
                errLabel4.setText(ERR1_MSG);
                break;
            case ERR2_ID:
                errLabel4.setText(ERR2_MSG);
                break;
            default:
                JOptionPane.showMessageDialog(this, "ERRORE INASPETTATO, non è possibile creare una nuova spedizione. - ID_ERR_LABEL_4");
                System.err.println("Probelmi nel mostrare gli errori - errLable2");
        }
    }
    
    /**
     * Nasconde l'errore associato all'indirizzo
     */
    public void hideErrIndirizzo(){
        errLabel1.setText("");
    }
    
    /**
     * Nasconde l'errore associato alla citta
     */
    public void hideErrCitta(){
        errLabel2.setText("");
    }
    
    /**
     * Mostra l'errore associato al peso fornito in input
     */
    public void hideErrPeso(){
        errLabel3.setText("");
    }
    
    /**
     * Mostra l'errore associato al valore fornito in input
     */
    public void hideErrValore(){
        errLabel4.setText("");
    }
    
    /**
     * Controlla la presenza di errori mostrandoli o nascondendoli
     * @return vero se non ci sono errori, falso altrimenti
     */
    public boolean checkErrors(){
        if(cittaInput.getText().trim().isEmpty()){
            showErrIndirizzo();
            return false;
        }else{
            hideErrIndirizzo();
        }
        
        if(destinazioneInput.getText().trim().isEmpty()){
            showErrCitta();
            return false;
        }else{
            hideErrCitta();
        }
        
        try{
            String pesoLetto = pesoInput.getText().trim();
            if(pesoLetto.isEmpty()){
                showErrPeso(ERR2_ID);
                return false;
            }
            
            pesoLetto = pesoLetto.replace(',', '.');
            pesoInput.setText(pesoLetto);
            
            if(Float.parseFloat(pesoLetto) <= 0 || !checkDecimalsPeso(pesoLetto)){
                showErrPeso(ERR1_ID);
                return false;
            }else{
                hideErrPeso();
            }
        }catch(NumberFormatException e){                                        //nel caso ci fossero più punti o non venisse inserito un numero
            showErrPeso(ERR1_ID);
            return false;
        }
        
        try{
            if(spedizioneAssicurata.isSelected()){
                String prezzoLetto = valoreInput.getText().trim();
                if(prezzoLetto.isEmpty()){
                    showErrValore(ERR2_ID);
                    return false;
                }

                prezzoLetto = prezzoLetto.replace(',', '.');
                valoreInput.setText(prezzoLetto);
                if(Float.parseFloat(prezzoLetto) < SpedizioneAssicurata.MIN_VAL || !checkDecimalsPrezzo(prezzoLetto)){
                    showErrValore(ERR1_ID);
                    return false;
                }else{
                    hideErrValore();
                }
            }
        }catch(NumberFormatException e){                                         //nel caso ci fossero più punti o non venisse inserito un numero
            showErrValore(ERR1_ID);
            return false;
        }
        return true;
    }
    
    /**
     * Controlla che il peso non abbia più di tre decimali
     * @param s la stringa che indica il peso
     * @return vero se ci sono al più tre decimali
     * falso altrimenti
     */
    private boolean checkDecimalsPeso(String s){
        if(s.contains(".")){
            String[] arr =s.split("\\.");                                  //vuole una espressione regolare
            if(arr.length == 2){
                if(arr[1].length() > 3 || arr[1].length() == 0){
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Controlla che il valore della merce non abbia più di due decimali
     * @param s la stringa che indica il prezzo
     * @return vero se ci sono al più due decimali
     * falso altrimenti
     */
    private boolean checkDecimalsPrezzo(String s){
        if(s.contains(".")){
            String[] arr =s.split("\\.");                                  //vuole una espressione regolare
            if(arr[1].length() > 2 || arr[1].length() == 0){
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        AbstractButton abstractButton = (AbstractButton) ae.getSource();
        String s = abstractButton.getText();
        
        if (s.equals("Spedizione Assicurata")){
            if(abstractButton.getModel().isSelected()){
                valoreInput.setEnabled(true);
                assicurato = true;
            }else{
                valoreInput.setEnabled(false);
                assicurato = false;
            }
        }else{
            if(checkErrors()){
                String dest =  cittaInput.getText().trim() + ", " + destinazioneInput.getText().trim();
                float peso = Float.parseFloat(pesoInput.getText().trim());

                SpedizioneNormale sped;
                if(assicurato){
                    float valore = Float.parseFloat(valoreInput.getText().trim());                    
                    sped = new SpedizioneAssicurata(user, nOrdine, dest, peso, StatoSpedizione.PREPARAZIONE, valore);    
                }else{
                    sped = new SpedizioneNormale(user, nOrdine, dest, peso, StatoSpedizione.PREPARAZIONE);
                }
                FileSpedizioni.aggiungiSpedizione(sped);

                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        }
    }
}
