package gestionefinestre;

import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * LA classe main del programma
 * @author Alessandro Consolini
 */
public class Main {
    /**
     * La funzione main del prgramma
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        setUIFont(new javax.swing.plaf.FontUIResource("Arial",Font.PLAIN,20));
        
        JFrame frame = new FinestraAvvio();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
    * Funzione che imposta il font dell'interfaccia utente
    * @param f il carattere da usare
    */
    public static void setUIFont(javax.swing.plaf.FontUIResource f){
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while(keys.hasMoreElements()){
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if(value instanceof javax.swing.plaf.FontUIResource){
                UIManager.put(key,f);
            }
        }
    }
}
