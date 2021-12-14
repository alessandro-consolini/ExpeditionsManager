/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tipidato;

/**
 * Tipo enumeratio che indica i vari stati che puo assumere una spedizione
 * @author Alessandro Consolini
 */
public enum StatoSpedizione {
    PREPARAZIONE,
    TRANSITO,
    RICEVUTA,
    FALLITA,
    RIMBORSO_RICHIESTO,
    RIMBORSO_EROGATO;
}