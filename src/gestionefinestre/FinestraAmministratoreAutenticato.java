/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionefinestre;

import tabella.SpedizioniTableModel;
import tabella.TabellaSpedizioniCellRenderer;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Semaphore;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import tipidato.StatoSpedizione;

/**
 * Finestra mostrata all'amministaratore autenticato
 * @author Alessandro Consolini
 */
public class FinestraAmministratoreAutenticato extends JFrame implements ActionListener {
    private JLabel title;
    private JButton logoutBtn;
    private JTable tabella;
    private SpedizioniTableModel tm;
    private JScrollPane spedizioniScrollPane;
    private JPanel northPanel;
    private BorderLayout borderLayout;
    private JPopupMenu popupMenu;
    private JMenuItem cancellaMenuItem;
    
    private ThreadModificaStato threadModificatore;
    private Semaphore semaforo;
    
    /**
     * Costruttore che imposta la finestra
     * @param f la finestra precedente
     */
    public FinestraAmministratoreAutenticato(FinestraLogin f) {
        title = new JLabel();
        title.setText("Autenticato come Amministratore");
        
        title.setHorizontalAlignment(JLabel.CENTER);
        logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(new LogoutAnnullaListener(this));
        
        tm = new SpedizioniTableModel();
        tabella = new JTable(tm);
        tabella.setDefaultRenderer(Object.class, new TabellaSpedizioniCellRenderer());
        tabella.setFillsViewportHeight(true);
        //tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabella.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        aggiornaTabella();
        
        cancellaMenuItem = new JMenuItem("Cancella spedizione");
        popupMenu = new JPopupMenu();
        popupMenu.add(cancellaMenuItem);

        tabella.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    int row = tabella.rowAtPoint(new Point(e.getX(),e.getY()));
                    if(row >= 0) {
                        tabella.setRowSelectionInterval(row, row);
                        StatoSpedizione stato = (StatoSpedizione)tabella.getValueAt(row, 6);
                        if(stato.equals(StatoSpedizione.RICEVUTA) || stato.equals(StatoSpedizione.RIMBORSO_EROGATO) ||
                          (stato.equals(StatoSpedizione.FALLITA) && tabella.getValueAt(row, 4).equals("Non assicurata"))) {
                            
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }else {
                        tabella.clearSelection();
                    }
                }else if(SwingUtilities.isLeftMouseButton(e)) {
                    int row = tabella.rowAtPoint(new Point(e.getX(),e.getY()));
                    if(row < 0)
                        tabella.clearSelection();
                }
                repaint();
            }
        });
        
        spedizioniScrollPane = new JScrollPane(tabella);
        spedizioniScrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        northPanel = new JPanel(new GridLayout(2,3));
        borderLayout = new BorderLayout();
        
        this.setLayout(borderLayout);
		
        this.add(northPanel, BorderLayout.NORTH);
        this.add(spedizioniScrollPane, BorderLayout.CENTER);

        northPanel.add(new JPanel());
        northPanel.add(title);
        northPanel.add(new JPanel());
        northPanel.add(new JPanel());
        northPanel.add(new JPanel());
        northPanel.add(logoutBtn);

        this.setVisible(true);
        this.setResizable(true);
        this.pack();
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent we) {
                f.setVisible(false);
            }
            @Override
            public void windowClosing(WindowEvent event) {
                threadModificatore.interrupt();
                FileSpedizioni.salva();
                f.setLocationRelativeTo((JFrame)event.getSource());
                f.setVisible(true);
                dispose();
            }
        });
        
        semaforo = new Semaphore(1);
        threadModificatore = new ThreadModificaStato(this, semaforo);
        
        cancellaMenuItem.addActionListener(this);
    }
    
    /**
     * Aggiorna i dati mostrati dalla tabella
     */
    public void aggiornaTabella(){
        tm.setInformation(FileSpedizioni.getSpedizioni());
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            semaforo.acquire();
            repaint();
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null,"ERRORE - THREAD DELLA GRAFICA INTERROTTO");
        }
        
        int row = tabella.getSelectedRow();
        FileSpedizioni.rimuoviSpedizione(row);
        aggiornaTabella();
        threadModificatore.caricaSpedizioni();
        semaforo.release();
    }
}
