package tabella;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import tipidato.StatoSpedizione;

/**
 * Gestisce i colori usati nella tabella associandoli al tipo di spedizione
 * @author Alessandro Consolini
 */
public class TabellaSpedizioniCellRenderer extends JLabel implements TableCellRenderer {
    public static final String COLORE_PREPARAZIONE = "#7FE5F0";
    public static final String COLORE_TRANSITO = "#FFF68F";
    public static final String COLORE_RICEVUTA = "#71CC71";
    public static final String COLORE_FALLITA = "#FF0909";
    public static final String COLORE_RIMBORSO_RICHIESTO = "#ff6733";
    public static final String COLORE_RIMBORSO_EROGATO = "#6ce36c";
    
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        StatoSpedizione stato = (StatoSpedizione)jtable.getValueAt(row, 6);
        Color c = Color.white;
        switch (stato) {
            case PREPARAZIONE:			
                c = Color.decode(COLORE_PREPARAZIONE);
                break;
            case TRANSITO:			
                c = Color.decode(COLORE_TRANSITO);
                break;
            case RICEVUTA:
                c = Color.decode(COLORE_RICEVUTA);
                break;
            case FALLITA:			
                c = Color.decode(COLORE_FALLITA);
                break;
            case RIMBORSO_RICHIESTO:			
                c = Color.decode(COLORE_RIMBORSO_RICHIESTO);
                break;
            case RIMBORSO_EROGATO:
                c = Color.decode(COLORE_RIMBORSO_EROGATO);
                break;
            default:
                System.err.println("ERRORE - STATO NON SUPPORTATO");
        }
        
        if(isSelected) {
            setBackground(Color.blue);
            setForeground(Color.white);
        }else {
            setBackground(c);
            setForeground(Color.black);
        }

        this.setText(value.toString());
        this.setFont(new Font(getFont().getFontName(), Font.PLAIN, 14));
        this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        this.setOpaque(true);

        return this;
    }
}