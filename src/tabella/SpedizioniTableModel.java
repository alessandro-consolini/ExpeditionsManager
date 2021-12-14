/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabella;

import tipidato.SpedizioneAssicurata;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import tipidato.SpedizioneNormale;

/**
 * motello della tabella che visualizza le spedizioni
 * @author Alessandro Consolini
 */
public class SpedizioniTableModel extends AbstractTableModel{
    private static final String[] HEADER = {"Utente", "Codice", "Destinazione", "Peso(kg)", "Valore(Euro)", "Data", "Stato"};
    
    /**
     * Default serial version
     */
    private ArrayList<SpedizioneNormale> spedizioni;
    private SimpleDateFormat sdf;
    
    /**
     * Instanzia un nuovo modello
     */
    public SpedizioniTableModel() {
        spedizioni = null;
        sdf = new SimpleDateFormat("dd/MM/yyyy");
    }

    /**
     * Imposta i dati della tabella
     * @param spedizioni Dati
     */
    public void setInformation(ArrayList<SpedizioneNormale> spedizioni) {
        this.spedizioni = spedizioni;
    }
    
    @Override
    public String getColumnName(int column) {
        return HEADER[column];
    }
    
    @Override
    public int getColumnCount() {
        return HEADER.length;
    }

    @Override
    public int getRowCount() {
        if(spedizioni == null)
            return 0;
        return spedizioni.size();
    }
    
    @Override
    public Object getValueAt(int row, int col){
        //String s = null;
        if(spedizioni == null || row >= getRowCount() || col >= getColumnCount())
            return "";
        switch (col){
            case 0: return spedizioni.get(row).getUtente();
            case 1: return spedizioni.get(row).getCodice();
            case 2: return spedizioni.get(row).getDestinazione();
            case 3: return spedizioni.get(row).getPeso();
            case 4: 
                if(spedizioni.get(row) instanceof SpedizioneAssicurata)
                    return ((SpedizioneAssicurata)spedizioni.get(row)).getValoreAssicurato();
                return "Non assicurata";
            case 5: return spedizioni.get(row).getDataImmissione();
            case 6: return spedizioni.get(row).getStato();
        }
        return "";
    }
    
    @Override
    public Class getColumnClass(int col) {
        if(col < 0 || col >= getColumnCount()) {
            return null;
        }else {
            if(col == 3 || col == 4)
                return float.class;
            return getValueAt(0, col).getClass();
        }
    }   
}