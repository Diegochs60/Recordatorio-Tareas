package task;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class InputFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = -7292098804316941558L;
    private JTextField texto = new JTextField();
    private JTextField proyecto = new JTextField();
    private TrayIcon trayIcon;
    private SystemTray tray;
    private InputFrame inputFrame;
            
    public InputFrame() throws HeadlessException {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setTitle("Registro de Horas");
        setLocation(300, 300);
        
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                createAndShowSysTray();
//            }
//        });
        createAndShowSysTray();
        add(new JLabel("¿Qué estás haciendo?"));
        inputFrame = this;
        texto.setColumns(30);
        texto.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 27) {
                    inputFrame.setVisible(false);
                }
                if (e.getKeyChar() == 10) {
                    if (texto.getText() != null && !"".equals(texto.getText().trim())) {
                        saveComment(texto.getText(), proyecto.getText());
                    }
                    inputFrame.setVisible(false);
                }
            }
        });
        add(texto);

        add(new JLabel("Proyecto"));
        proyecto.setColumns(20);
        proyecto.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 27) {
                    inputFrame.setVisible(false);
                }
                if (e.getKeyChar() == 10) {
                    if (proyecto.getText() != null && !"".equals(proyecto.getText().trim())) {
                        saveComment(texto.getText(), proyecto.getText());
                    }
                    inputFrame.setVisible(false);
                }
            }
        });
        add(proyecto);

        JButton boton = new JButton("Guardar");
        boton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (texto.getText() != null && !"".equals(texto.getText().trim())) {
                    saveComment(texto.getText(), proyecto.getText());
                     
                }
                inputFrame.setVisible(false);
            }
        });
        add(boton);

        JButton boton3 = new JButton("Cerrar");
        boton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               inputFrame.setVisible(false);
            }
        });
        add(boton3);
        
        JButton boton2 = new JButton("Salir");
        boton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               salir();
            }
        });
        add(boton2);

        setFocusableWindowState(true);
        pack();
        setVisible(true);
        TriggerThread.setDisplay(true);
    }

    public void saveComment(String comentario, String proyecto) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        File logs=new File("registro");
        if(!logs.exists()){
            logs.mkdir();
        }
        File f = new File("registro/log" + df.format(new Date()) + ".csv");
        try {
            TimeZone.setDefault(new SimpleTimeZone(-18000000, "Mexico/General"));


            FileWriter fw = new FileWriter(f, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, new Locale("es", "MX")).format(new Date())
                    + "," + comentario
                    + "," + proyecto + "\r\n");
            bw.flush();
            bw.close();
            fw.close();
            trayIcon.displayMessage("Recordatorios",
                            "La tarea ha sido registrada", TrayIcon.MessageType.INFO);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            e.printStackTrace();
        }
    }

    public void setVisible(boolean b) {
        TriggerThread.setDisplay(b);
        super.setVisible(b);
        texto.setFocusable(true);
        texto.requestFocus();
        texto.select(0, texto.getText() != null ? texto.getText().length() : 0);
    }

    @Override
    public void dispose() {
        TriggerThread.setDisplay(false);
        super.dispose();

    }
    private void createAndShowSysTray() {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        PopupMenu popup = new PopupMenu();
        trayIcon =
                new TrayIcon(createImage("/images/jwm.gif", "JWM Solutions"));
        tray = SystemTray.getSystemTray();
        trayIcon.setToolTip("Recordatorios - JWM");
        trayIcon.setImageAutoSize(true);
        
        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("Acerca de");
        MenuItem exitItem = new MenuItem("Salir");
        
        //Add components to popup menu
        popup.add(aboutItem);
        //popup.addSeparator();
        popup.add(exitItem);
        
        trayIcon.setPopupMenu(popup);
        
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }
        
        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputFrame.setVisible(true);
            }
        });
        
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Recordatorios para registrar tareas\n JWM Solutions - 2013","Recordatorios JWM",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        
//        ActionListener listener = new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                MenuItem item = (MenuItem)e.getSource();
//                //TrayIcon.MessageType type = null;
//                System.out.println(item.getLabel());
//                if ("Error".equals(item.getLabel())) {
//                    //type = TrayIcon.MessageType.ERROR;
//                    trayIcon.displayMessage("Sun TrayIcon Demo",
//                            "This is an error message", TrayIcon.MessageType.ERROR);
//                    
//                } else if ("Warning".equals(item.getLabel())) {
//                    //type = TrayIcon.MessageType.WARNING;
//                    trayIcon.displayMessage("Sun TrayIcon Demo",
//                            "This is a warning message", TrayIcon.MessageType.WARNING);
//                    
//                } else if ("Info".equals(item.getLabel())) {
//                    //type = TrayIcon.MessageType.INFO;
//                    trayIcon.displayMessage("Sun TrayIcon Demo",
//                            "This is an info message", TrayIcon.MessageType.INFO);
//                    
//                } else if ("None".equals(item.getLabel())) {
//                    //type = TrayIcon.MessageType.NONE;
//                    trayIcon.displayMessage("Sun TrayIcon Demo",
//                            "This is an ordinary message", TrayIcon.MessageType.NONE);
//                }
//            }
//        };
        
        
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               salir();
               // System.exit(0);
            }
        });
    }
    private void salir(){
         TriggerThread.setExit(true);
         this.dispose();
          tray.remove(trayIcon);
    }
    //Obtain the image URL
    protected  Image createImage(String path, String description) {
        URL imageURL = InputFrame.class.getResource(path);
        
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}
