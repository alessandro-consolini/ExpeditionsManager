/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tipidato;

import java.util.Date;
import gestionefinestre.FileSpedizioni;

/**
 * Rappresenta una spedizione assicurata
 * @author Alessandro Consolini
 */
public class SpedizioneAssicurata extends SpedizioneNormale {
    public static final float MIN_VAL = 100.0f;
    private float valoreAssicurato;
    
    /**
     * Costruttore
     * @param user nome utente di chi sta effettuando l'ordine di spedizione
     * @param n numero associato a questa spedizione
     * @param dest destinazione
     * @param p peso dell'oggetto spedito
     * @param s stato della spedizione
     * @param va valore assicurato
     */
    public SpedizioneAssicurata(String user,int n, String dest, float p, StatoSpedizione s,float va){
        super(user, n, dest, p, s);
        this.valoreAssicurato = va;
    }
    
    /**
     * 
     * @param user nome utente di chi sta effettuando l'ordine di spedizione
     * @param cod codice associato alla spedizione
     * @param dest destinazione
     * @param data data in cui è stata effettuata la spedizione
     * @param p peso dell'oggetto spedito
     * @param s stato della spedizione
     * @param va valore assicurato
     */
    public SpedizioneAssicurata(String user,String cod, String dest, Date data, float p, StatoSpedizione s,float va){
        super(user, cod, dest, data, p, s);
        this.valoreAssicurato = va;
    }

    /**
     * Restituisce il valore assicurato della merce
     * @return il valore assicurato della merce
     */
    public float getValoreAssicurato() {
        return valoreAssicurato;
    }

    @Override
    public boolean isStatoFinale() {
        return  this.stato == StatoSpedizione.FALLITA || 
                this.stato == StatoSpedizione.RICEVUTA || 
                this.stato == StatoSpedizione.RIMBORSO_EROGATO;    
    }
    
    @Override
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
            case RIMBORSO_RICHIESTO:
                s = StatoSpedizione.RIMBORSO_RICHIESTO;
                break;
            case RIMBORSO_EROGATO:
                s = StatoSpedizione.RIMBORSO_EROGATO;
                break;
            default:
                s=null;
        }
        
        SpedizioneAssicurata cloned = new SpedizioneAssicurata(usr,cod,dest,(Date)this.dataImmissione.clone(),this.peso, s, this.valoreAssicurato);
        return cloned;
    }
    
    @Override
    public String toStringWithSeparator() {
        return this.utente + FileSpedizioni.SEPARATORE +
               "A" + FileSpedizioni.SEPARATORE +
               this.codice + FileSpedizioni.SEPARATORE +
               this.destinazione + FileSpedizioni.SEPARATORE +
               this.getPeso() + FileSpedizioni.SEPARATORE +
               getDataImmissione() + FileSpedizioni.SEPARATORE +
               this.stato.name() + FileSpedizioni.SEPARATORE +
               this.valoreAssicurato;
    }
}
