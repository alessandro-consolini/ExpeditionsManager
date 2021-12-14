/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionefinestre;

import tipidato.SpedizioneNormale;
import tipidato.StatoSpedizione;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Thread che modfica gli stati delle spedizioni in maniera casuale
 * @author Alessandro Consolini
 */
class ThreadModificaStato extends Thread{
    private static final double PROBABILITA_FALLIMENTO = 0.3;
    private static final double PROBABILITA_RIMANERE_STESSO_STATO = 0.4;
    private static final int INTERVALLO_AGGIORNAMENTI = 5000;
    
    private FinestraAmministratoreAutenticato finestra;
    private Semaphore semaforo;
    private ArrayList<SpedizioneNormale> spedizioni;
    
    /**
     * Costruttore che inizializza e fa partire il thread
     * @param f la finestra precedente
     * @param s il semaforo per regolare l'utilizzo della classe FileSpedizioni
     */
    public ThreadModificaStato(FinestraAmministratoreAutenticato f, Semaphore s) {
        this.finestra=f;
        this.semaforo = s;
        caricaSpedizioni();
        this.start();
    }
    
    /**
     * Carica le spedizioni dalla classe FileSpedizioni
     */
    public void caricaSpedizioni(){
        spedizioni = FileSpedizioni.getSpedizioni();
    }
    
    @Override
    public void run() {
        boolean stop = spedizioni.isEmpty();
        while(!isInterrupted() && !stop) {
            try {
                semaforo.acquire();
                int indiceSpedizione;
                do{
                    stop = spedizioni.isEmpty();
                    indiceSpedizione = (int)(Math.random() * (spedizioni.size()));
                    if(stop || FileSpedizioni.seTutteSpedizioniStatoFinale()){
                        this.interrupt();
                    }
                }while(!isInterrupted() && spedizioni.get(indiceSpedizione).isStatoFinale());

                SpedizioneNormale sped = spedizioni.get(indiceSpedizione);
                StatoSpedizione statoSped = sped.getStato();
                if(Math.random() > PROBABILITA_RIMANERE_STESSO_STATO){
                    switch (statoSped) {
                        case PREPARAZIONE:
                            if(Math.random() < PROBABILITA_FALLIMENTO){
                                FileSpedizioni.modificaStatoSpedizioneIndice(indiceSpedizione, StatoSpedizione.FALLITA);
                                sped.setStato(StatoSpedizione.FALLITA);
                            }else{
                                FileSpedizioni.modificaStatoSpedizioneIndice(indiceSpedizione, StatoSpedizione.TRANSITO);
                                sped.setStato(StatoSpedizione.TRANSITO);
                            }   break;
                        case TRANSITO:
                            if(Math.random() < PROBABILITA_FALLIMENTO){
                                FileSpedizioni.modificaStatoSpedizioneIndice(indiceSpedizione, StatoSpedizione.FALLITA);
                                sped.setStato(StatoSpedizione.FALLITA);
                            }else{
                                FileSpedizioni.modificaStatoSpedizioneIndice(indiceSpedizione, StatoSpedizione.RICEVUTA);
                                sped.setStato(StatoSpedizione.RICEVUTA);
                            }   break;
                        case RIMBORSO_RICHIESTO:
                            FileSpedizioni.modificaStatoSpedizioneIndice(indiceSpedizione, StatoSpedizione.RIMBORSO_EROGATO);
                            sped.setStato(StatoSpedizione.RIMBORSO_EROGATO);
                            break;
                        default:
                            break;
                    }
                }

                finestra.aggiornaTabella();
                finestra.repaint();
                semaforo.release();
                sleep(INTERVALLO_AGGIORNAMENTI);
                stop = spedizioni.isEmpty();
            } catch (InterruptedException e) {
                stop = true;
            }finally{
                semaforo.release();
            }
        }
    }
}
