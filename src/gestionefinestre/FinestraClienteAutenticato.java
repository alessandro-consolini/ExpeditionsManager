/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionefinestre;

import tipidato.StatoSpedizione;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import tabella.SpedizioniTableModel;
import tabella.TabellaSpedizioniCellRenderer;

/**
 * Finestra del Cliente una volta autenticato
 * @author Alessandro Consolini
 */
public class FinestraClienteAutenticato extends JFrame implements ActionListener{
    private JLabel title;
    private JButton logoutBtn;
    private JButton aggiungiSpedizioneBtn;
    private JTable tabella;
    private SpedizioniTableModel tm;
    private JPopupMenu popupMenu;
    private JMenuItem rhichiediImborsoMenuItem;
    private JScrollPane spedizioniScrollPane;
    private JPanel northPanel;
    private BorderLayout borderLayout;
    private String user;
    private int nOrdiniSessione;
    
    /**
     * Costruttore che imposta la finestra
     * @param f la finestra precedente
     * @param username lo username dell'utente autenticato
     */
    public FinestraClienteAutenticato(FinestraLogin f, String username){
        this.user = username;
        nOrdiniSessione = 0;
        title = new JLabel();
        title.setText("Autenticato come " + user);
        
        title.setHorizontalAlignment(JLabel.CENTER);
        logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(new LogoutAnnullaListener(this));
        aggiungiSpedizioneBtn = new JButton("Aggiungi Spedizione");
        aggiungiSpedizioneBtn.addActionListener(this);
        
        tm = new SpedizioniTableModel();
        aggiornaTabella();
        tabella = new JTable(tm);
        tabella.setDefaultRenderer(Object.class, new TabellaSpedizioniCellRenderer());
        tabella.setFillsViewportHeight(true);
        //tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabella.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        rhichiediImborsoMenuItem = new JMenuItem("Richiedi Rimborso");
        popupMenu = new JPopupMenu();
        popupMenu.add(rhichiediImborsoMenuItem);

        rhichiediImborsoMenuItem.addActionListener(new ActionListener() {	
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tabella.getSelectedRow();
                int index = FileSpedizioni.findSpedizione((String)tabella.getValueAt(row, 1));
                FileSpedizioni.modificaStatoSpedizioneIndice(index, StatoSpedizione.RIMBORSO_RICHIESTO);
                aggiornaTabella();
            }
        });

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
                        if(stato.equals(StatoSpedizione.FALLITA) && !tabella.getValueAt(row, 4).equals("Non assicurata")){
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }else{
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
        spedizioniScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        northPanel = new JPanel(new GridLayout(2,3));
        borderLayout = new BorderLayout();

        this.setLayout(borderLayout);
		
        this.add(northPanel, BorderLayout.NORTH);
        this.add(spedizioniScrollPane, BorderLayout.CENTER);

        northPanel.add(new JPanel());
        northPanel.add(title);
        northPanel.add(new JPanel());
        northPanel.add(new JPanel());
        northPanel.add(aggiungiSpedizioneBtn);
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
                FileSpedizioni.salva();
                f.setLocationRelativeTo((JFrame)event.getSource());
                f.setVisible(true);
                dispose();
            }
        });
    }
    
    /**
     * Incrementa di un'unità il contatore di ordini eseguiti dall'utente in questa sessione
     */
    public void incrementaOrdiniEseguiti(){
        nOrdiniSessione++;
    }
    
    /**
     * Aggiorna i dati della tabella mostrata all'utente
     */
    public void aggiornaTabella(){
        tm.setInformation(FileSpedizioni.getSpedizioniPerUtente(user));
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        FinestraNuovaSpedizione fns = new FinestraNuovaSpedizione(this, user, nOrdiniSessione);
        fns.setLocationRelativeTo(this);
    }
}
