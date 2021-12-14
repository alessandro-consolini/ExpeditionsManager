/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tipidato;

import java.text.SimpleDateFormat;
import java.util.Date;
import gestionefinestre.FileSpedizioni;

/**
 * Rappresenta una spedizione non assicurata
 * @author Alessandro Consolini
 */
public class SpedizioneNormale{
    public static final String DATE_PATTERN = "dd/MM/yyyy HH:mm";
    
    protected String utente;
    protected String codice;
    protected String destinazione;
    protected float peso;
    protected Date dataImmissione;
    protected StatoSpedizione stato;
    
    /**
     * Costruttore che inizializza gli attributi
     * @param user nome utente di chi sta effettuando l'ordine di spedizione
     * @param n numero associato a questa spedizione
     * @param dest destinazione
     * @param p peso dell'oggetto spedito
     * @param s stato della spedizione
     */
    public SpedizioneNormale(String user,int n, String dest, float p, StatoSpedizione s){
        this.utente = user;
        this.destinazione = dest;
        this.peso = p;
        dataImmissione = new Date();
        this.codice = user + n + getDataImmissione();
        this. stato = s;
    }
    /**
     * 
     * @param user nome utente di chi sta effettuando l'ordine di spedizione
     * @param cod codice associato ad una spedizione
     * @param dest destinazione
     * @param data data in cui è stata effettuata la spedizione
     * @param p peso dell'oggetto spedito
     * @param s stato della spedizione
     */
    public SpedizioneNormale(String user, String cod, String dest, Date data, float p, StatoSpedizione s){
        this.utente = user;
        this.destinazione = dest;
        this.peso = p;
        dataImmissione = data;
        this.codice = cod;
        this. stato = s;
    }

    /**
     * Restituisce il nome utente
     * @return il nome utente
     */
    public String getUtente() {
        return utente;
    }

    /**
     * Restituisce il codice della spedizione
     * @return il codice della spedizione
     */
    public String getCodice() {
        return codice;
    }

    /**
     * Restituisce la destinazione della spedizione
     * @return la destinazione della spedizione
     */
    public String getDestinazione() {
        return destinazione;
    }

    /**
     * Restituisce il peso della spedizione
     * @return il peso della spedizione
     */
    public float getPeso() {
        return peso;
    }

    /**
     * Restituisce la data in cui è stata effettuata la spedizione
     * @return data in cui è stata effettuata la spedizione
     */
    public String getDataImmissione() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        return sdf.format(dataImmissione);
    }

    /**
     * Restituisce lo stato della spedizione
     * @return lo stato della spedizione
     */
    public StatoSpedizione getStato() {
        return stato;
    }
    
    /**
     * Assegna allo stato della spedizione lo stato fornito in input
     * @param s valore da assegnare allo stato della spedizione
     */
    public void setStato(StatoSpedizione s){
        stato = s;
    }

    /**
     * Restituisce vero se la spedizione è ad uno stato finale
     * @return vero se la spedizione è ad uno stato finale
     */
    public boolean isStatoFinale(){
        return stato == StatoSpedizione.FALLITA || stato == StatoSpedizione.RICEVUTA;
    }
    
    /**
     * Funzione che copia l'oggetto, creato al posto di fare l'overriding della funzone clone perchè la funzione clone
     * a tempo di compilazione generava un warning: unchecked or unsafe operations.
     * @return l'oggeta spedizione clonata
     */
    public SpedizioneNormale cloneObject() {
        String usr = new String(this.utente);
        String cod = new String(this.codice);
        String dest = new String(this.destinazione);
        StatoSpedizione s;
        switch(this.stato){
            case PREPARAZIONE:
                s = StatoSpedizione.PREPARAZIONE;
                break;
            case TRANSITO:
                s = StatoSpedizione.TRANSITO;
                break;
            case RICEVUTA:
                s = StatoSpedizione.RICEVUTA;
                break;
            case FALLITA:
                s = StatoSpedizione.FALLITA;
                break;
            default:
                s=null;
        }
        
        SpedizioneNormale cloned = new SpedizioneNormale(usr,cod,dest,(Date)this.dataImmissione.clone(),this.peso, s);
        return cloned;
    }
    
    /**
     * Restituisce una stringa rappresentante la spedizione con gli attributi separati dal carattere separatore
     * @return una stringa rappresentante la spedizione con gli attributi separati dal carattere separatore
     */
    public String toStringWithSeparator() {
        return utente + FileSpedizioni.SEPARATORE +
               "N" + FileSpedizioni.SEPARATORE +
               codice + FileSpedizioni.SEPARATORE +
               destinazione + FileSpedizioni.SEPARATORE +
               peso + FileSpedizioni.SEPARATORE +
               getDataImmissione() + FileSpedizioni.SEPARATORE +
               stato.name();
    }
}
