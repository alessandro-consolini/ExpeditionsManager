/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionefinestre;


import tipidato.SpedizioneNormale;
import tipidato.StatoSpedizione;
import tipidato.SpedizioneAssicurata;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 * Classe che gestisce tutte le operazioni sul file delle spedizioni e la lista di spedizioni
 * @author Alessandro Consolini
 */

public class FileSpedizioni {
    public static final String FILE_SPEDIZOINI = "spedizioni.csv";
    public static final String SEPARATORE = ";";
    
    private static ArrayList<SpedizioneNormale> spedizioni;
    
    /**
     * 
     * Carica i dati letti da file nell'attributo delle spedizioni
     */
    public static void carica(){
        spedizioni = new ArrayList<SpedizioneNormale>();
        File fileSpedizioni = new File(FILE_SPEDIZOINI);
        if(!fileSpedizioni.exists()){
            try {
                fileSpedizioni.createNewFile();
            } catch (IOException e) {
                String error = Arrays.toString(e.getStackTrace());
                JOptionPane.showMessageDialog(null,"Ci sono stati problemi con la creazione del file delle spedizioni");
                System.err.println(error);
            }
        }else{
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_SPEDIZOINI))){
                ArrayList<String[]> list = new ArrayList<String[]>();
                String line;
                String user;
                String tipoSpedizioneLetto;
                String codice;
                String destinazione;
                float peso;
                Date data;
                StatoSpedizione stato;
                float valore;
                
                while((line = reader.readLine()) != null){
                    String[] spedizioniUtente = line.split(SEPARATORE);
                    user = spedizioniUtente[0];
                    tipoSpedizioneLetto = spedizioniUtente[1];                                                  //sarà "N" o "A"
                    codice = spedizioniUtente[2];
                    destinazione = spedizioniUtente[3];
                    peso = Float.parseFloat(spedizioniUtente[4]);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SpedizioneNormale.DATE_PATTERN);
                    data = simpleDateFormat.parse(spedizioniUtente[5]);
                    stato = StatoSpedizione.valueOf(spedizioniUtente[6]);                                       //value of usa il nome della cosatante enum

                    SpedizioneNormale sped;
                    if(tipoSpedizioneLetto.equals("N")){
                        sped = new SpedizioneNormale(user, codice, destinazione,data, peso, stato);
                    }else{
                        valore = Float.parseFloat(spedizioniUtente[7]);
                        sped = new SpedizioneAssicurata(user, codice, destinazione,data, peso, stato, valore);
                    }
                    aggiungiSpedizione(sped);
                }
            }catch(IllegalArgumentException | IOException | ParseException e){
                String error = Arrays.toString(e.getStackTrace());
                JOptionPane.showMessageDialog(null,"Ci sono stati problemi con la lettura delle spedizioni");
                System.err.println(error);
            }
        }
    }
    
    /**
     * 
     * Salva su file i dati delle spedizioni che possono essere state aggiunte , modificate o eliminalte
     */
    public static void salva(){
        ArrayList<String> spedizioniScrittura = new ArrayList<String>();
        for (int i=0; i<spedizioni.size();i++) {
            spedizioniScrittura.add(spedizioni.get(i).toStringWithSeparator());
        }
        try (FileWriter writer = new FileWriter(FILE_SPEDIZOINI)){ //il true per fare append
            for (String s : spedizioniScrittura) {
                writer.append(s);
                writer.append(System.lineSeparator());
            }
            writer.close();
        } catch (Exception e) {
            String error = Arrays.toString(e.getStackTrace());
            JOptionPane.showMessageDialog(null,"Ci sono stati problemi nel salvataggio delle spedizioni");
            System.err.println(error);
        }
    }
    
    /**
     * 
     * Restituisce una copia dell' attributo statico spedizioni
     * @return una copia dell' attributo statico spedizioni
     */
    public static ArrayList<SpedizioneNormale> getSpedizioni(){
        ArrayList<SpedizioneNormale> copied = new ArrayList<SpedizioneNormale>();
        for(int i=0; i<spedizioni.size(); i++){
            copied.add(spedizioni.get(i).cloneObject());
        }
        
        return copied;
    }
    
    /**
     * 
     * Restituisce l'indice della spedizione con codice dato in input
     * @param c codice della spedizione da cercae
     * @return l'indice della spedizione con codice dato in input
     */
    public static int findSpedizione(String c){
        for(int i=0; i<spedizioni.size(); i++){
            if(spedizioni.get(i).getCodice().equals(c)){
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Restituisce un ArrayList contenente le spedizioni dell'utente fornito in input
     * @param utente nome utente per il quale vengono cercate le spedizioni
     * @return un ArrayList contenente le spedizioni dell'utente fornito in input
     */
    public static ArrayList<SpedizioneNormale> getSpedizioniPerUtente(String utente){
        ArrayList<SpedizioneNormale> app = new ArrayList<SpedizioneNormale>();
        for(int i=0; i<spedizioni.size();i++){
            if(spedizioni.get(i).getUtente().equals(utente)){
                app.add(spedizioni.get(i));
            }
        }
        return app;
    }
    
    /**
     * 
     * sostituisce lo stato s allo stato della spedizione di indice index, controllando che lo stato sia compatibile con il tipo di spedizione
     * @param index indice della spedizione indicata
     * @param s nuovo stato della spedizione
     */
    public static void modificaStatoSpedizioneIndice(int index, StatoSpedizione s){
        if(controllaCompatibilitaStato(index, s))
            spedizioni.get(index).setStato(s);
    }
    
    /**
     * 
     * @param index indice della spedizone da controllare
     * @param s lo stato che si vuole assegnare
     * @return vero se lo setato è compatibile, falso altrimenti
     */
    private static boolean controllaCompatibilitaStato(int index, StatoSpedizione s){
        if(!(spedizioni.get(index) instanceof SpedizioneAssicurata)  && 
            (s == StatoSpedizione.RIMBORSO_RICHIESTO ||
            s == StatoSpedizione.RIMBORSO_EROGATO))
                return false;
        return true;
    }
    
    /**
     * restituisce vero se tutte le spedizioni hanno uno stato finale, falso altrimenti
     * @return vero se tutte le spedizioni hanno uno stato finale, falso altrimenti
     */
    public static boolean seTutteSpedizioniStatoFinale(){
        for(int i=0; i<spedizioni.size(); i++){
            if(!spedizioni.get(i).isStatoFinale()){
                return false;
            }
        }
        return true;
    }
    
    /**
     * restituisce il numero totale di spedizioni inserite
     * @return numero totale di spedizioni inserite
     */
    public static int getNumSpedizioni(){
        return spedizioni.size();
    }
    
    /**
     * 
     * Aggiunge la spedizione data in input alla lista di spedizioni
     * @param s spedizione da aggiungere
     */
    public static void aggiungiSpedizione(SpedizioneNormale s){
        if(spedizioni == null){
            carica();
        }
        spedizioni.add(s);
    }
    
    /**
     * 
     * rimuove la spedizione di indice dato in input
     * @param index indice della spedizione da eliminare
     */
    public static void rimuoviSpedizione(int index){
        if(spedizioni != null){
            spedizioni.remove(index);
        }
    }
}
