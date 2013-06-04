package task;

import static task.Utileria.*;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.Image;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import task.basecamp.Account;
import task.basecamp.BasecampAbstract;
import task.basecamp.DTOAbstract;
import task.basecamp.ProjectDTO;
import task.basecamp.Projects;
import task.basecamp.TimeEntryDTO;
import task.basecamp.ToDoItems;
import task.basecamp.ToDoList;
import task.basecamp.TodoItemDTO;
import task.basecamp.TodoListDTO;

public class InputFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = -7292098804316941558L;
    private JTextField texto = new JTextField();
    private JTextField proyecto = new JTextField();
    private JFormattedTextField horas;
    private TrayIcon trayIcon;
    private SystemTray tray;
    private InputFrame inputFrame;
    private JComboBox cmbPrj;
    private JComboBox todoList = new JComboBox();
    private JComboBox todoItems = new JComboBox();
    //******** basecamp
    Account bcProfile = new Account();
    Projects bcProjects = new Projects();
    ToDoList bcTodoList = new ToDoList();
    ToDoItems bcTodoItems = new ToDoItems();
    
    public InputFrame() throws HeadlessException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InputFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(InputFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(InputFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(InputFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        setIconImage(createImage("/images/jwm.gif", "JWM Solutions"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setTitle("Registro de Horas");
        setLocation(200, 300);

        //***************** Primer panel
        JPanel descripcion = new JPanel();

//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                createAndShowSysTray();
//            }
//        });
        createAndShowSysTray();
        descripcion.add(new JLabel("¿Qué estás haciendo?"));
        inputFrame = this;
        texto.setColumns(30);
        texto.addKeyListener(new EnterKeyAdapter(texto.getText()));
        descripcion.add(texto);
        
        descripcion.add(new JLabel("Horas"));
        NumberFormat nfdec = NumberFormat.getNumberInstance();
        nfdec.setMinimumFractionDigits(0);
        nfdec.setMaximumFractionDigits(1);
        horas = new JFormattedTextField(nfdec);
        horas.setColumns(4);
        horas.addKeyListener(new EnterKeyAdapter(horas.getText()));
        descripcion.add(horas);
        
        getContentPane().add(descripcion);

        //*************** Segundo panel
        final JPanel registro = new JPanel();
        setSubFrameStyle("Registro en Archivo", registro);
        registro.add(new JLabel("Proyecto"));
        proyecto.setColumns(20);
        proyecto.addKeyListener(new EnterKeyAdapter(proyecto.getText()));
        registro.add(proyecto);
        
        JButton boton = new JButton("Guardar");
        boton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (valido(texto.getText()) && valido(horas.getText()) && valido(proyecto.getText())) {
                    saveComment(false);
                    inputFrame.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(registro, "Verifique los datos capturados (Descripcion, Horas y Proyecto)");
                }
            }
        });
        registro.add(boton);
        getContentPane().add(registro);
        checkIfBasecamp(this);

        //************ Tercer Panel- Botones
        JPanel buttonPane = new JPanel();
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton boton3 = new JButton("Cerrar");
        boton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputFrame.setVisible(false);
            }
        });
        buttonPane.add(boton3);
        
        JButton boton2 = new JButton("Salir");
        boton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salir();
            }
        });
        buttonPane.add(boton2);
        
        
        getContentPane().add(buttonPane);
        
        setFocusableWindowState(true);
        pack();
        setVisible(true);
        TriggerThread.setDisplay(true);
    }
    
    private void checkIfBasecamp(JFrame frame) {
        String username = Parameters.getInstance().getString("basecamp.username");
        String password = Parameters.getInstance().getString("basecamp.password");
        JPanel conImagen = new JPanel(new BorderLayout(20, 20));
        final JPanel pan = new JPanel();
        pan.setBackground(Color.WHITE);
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
        setSubFrameStyle("Registro en Basecamp", conImagen);
        
        if (username == null || username.length() == 0) {
            pan.add(new JLabel("Para utilizar Basecamp, necesita configurar el archivo config.properties, \nsi no existe, créelo en donde ejecuta esta aplicación"), BorderLayout.WEST);
        } else {
            BasecampAbstract.username = username;
            BasecampAbstract.password = password;
            
            if (bcProfile.getAccountInfo() == null) {
                pan.add(new JLabel("No se puede cargra Basecamp, ocurrió un error con la configuración del archivo config.properties"), BorderLayout.WEST);                
            } else {
                
                
                cmbPrj = new JComboBox(bcProjects.getProjectList());
                cmbPrj.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            long pid = ((DTOAbstract) cmbPrj.getSelectedItem()).getId();
                            System.out.println("Seleccionado project" + pid);
                            
                            todoList.removeAllItems();
                            if (pid > 0) {
                                TodoListDTO[] tdla = bcTodoList.getTodoListArray(pid);
                                for (TodoListDTO d : tdla) {
                                    todoList.addItem(d);
                                }
                            }
                        }
                    }
                });
                
                todoList.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            long tlid = ((DTOAbstract) todoList.getSelectedItem()).getId();
                            System.out.println("Seleccionado List " + tlid);
                            todoItems.removeAllItems();
                            if (tlid > 0) {
                                TodoItemDTO[] tdla = bcTodoItems.getTodoItemsArray(tlid);
                                for (TodoItemDTO d : tdla) {
                                    todoItems.addItem(d);
                                }
                            }
                        }
                    }
                });
                
                todoItems.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {                            
                            long tlid = ((DTOAbstract) todoItems.getSelectedItem()).getId();
                            System.out.println("Seleccionado Item " + tlid);
                        }
                    }
                });
                
                
                pan.add(new JLabel("Proyecto de Basecamp"));
                pan.add(cmbPrj);
                pan.add(new JLabel("ToDo List"));
                pan.add(todoList);
                pan.add(new JLabel("ToDo Items"));
                pan.add(todoItems);
                pan.add(new JLabel("Horas"));
                JButton boton = new JButton("Registrar");
                boton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        
                        if (todoItems.getSelectedIndex() <= 0 || !valido(texto.getText())
                                || !valido(horas.getText())) {
                            JOptionPane.showMessageDialog(pan, "Verifique los datos capturados (ToDo Item, descripción y Horas)");
                            return;
                        }
                        
                        try {
                            TimeEntryDTO dto = new TimeEntryDTO(bcProfile.getData().getJSONObject("person").getJSONObject("id").getLong("content"),
                                    new Date(), Float.parseFloat(horas.getText()), texto.getText());
                            long tlid = ((DTOAbstract) todoItems.getSelectedItem()).getId();
                            
                            if (!bcTodoItems.newTimeEntry(tlid, dto)) {
                                trayIcon.displayMessage("Recordatorios",
                                        "La tarea ha sido registrada en Basecamp", TrayIcon.MessageType.INFO);
                            } else {
                                trayIcon.displayMessage("Recordatorios",
                                        "No se pudo registrar en Basecamp", TrayIcon.MessageType.ERROR);
                            }
                            saveComment(true);
                            inputFrame.setVisible(false);
                        } catch (Exception exc) {
                            Logger.getLogger(this.getClass().toString()).log(Level.INFO, exc.getMessage());
                        }
                    }
                });
                pan.add(boton);
            }
        }

        //*** Cargar Avatar
        try {
            URL imgURL = new URL(bcProfile.getData().getJSONObject("person").getString("avatar-url"));
            ImageIcon img = new ImageIcon(ImageIO.read(imgURL));
            JLabel test = new JLabel();
            test.setIcon(img);
            conImagen.add(test, BorderLayout.WEST);
            
            conImagen.add(pan, BorderLayout.CENTER);
        } catch (Exception e) {
            conImagen = pan;
        }
        
        
        
        frame.getContentPane().add(conImagen);
        
    }
    
    private void setSubFrameStyle(String message, JPanel panel) {
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
                message, TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
                null, Color.BLUE));
        panel.setBackground(Color.WHITE);
    }
    
    public void saveComment(boolean isBC) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        File logs = new File("registro");
        if (!logs.exists()) {
            logs.mkdir();
        }
        File f = new File("registro/log" + df.format(new Date()) + ".csv");
        try {
            TimeZone.setDefault(new SimpleTimeZone(-18000000, "Mexico/General"));
            
            
            FileWriter fw = new FileWriter(f, true);
            BufferedWriter bw = new BufferedWriter(fw);
            if (isBC) {
                bw.write(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, new Locale("es", "MX")).format(new Date())
                        + ",\"" + texto.getText() + "\""
                        + ",\"" + cmbPrj.getSelectedItem() + "\""
                        + "," + horas.getText()
                        + ",\"" + todoList.getSelectedItem() + "\""
                        + ",\"" + todoItems.getSelectedItem() + "\""
                        + "\r\n");
            } else {
                bw.write(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, new Locale("es", "MX")).format(new Date())
                        + ",\"" + texto.getText() + "\""
                        + ",\"" + proyecto.getText() + "\""
                        + "," + horas.getText()
                        + "\r\n");                
            }
            bw.flush();
            bw.close();
            fw.close();
            trayIcon.displayMessage("Recordatorios",
                    "La tarea ha sido registrada en archivo "+f.getName(), TrayIcon.MessageType.INFO);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            e.printStackTrace();
            trayIcon.displayMessage("Recordatorios",
                    "No se pudo registrar en archivo "+f.getName(), TrayIcon.MessageType.ERROR);
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
                JPanel panCR=new JPanel();
                panCR.setLayout(new BoxLayout(panCR, BoxLayout.Y_AXIS));
                JButton button = new JButton();
                button.setText("<HTML>Proyecto de código abierto, <FONT color=\"#000099\"><U>https://github.com/haestrada/Recordatorio-Tareas</U></FONT>"
                     + " </HTML>");
                button.setHorizontalAlignment(SwingConstants.LEFT);
                button.setBorderPainted(false);
                button.setOpaque(false);
                
                button.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            URI uri = new URI("https://github.com/haestrada/Recordatorio-Tareas");
                            if(Desktop.isDesktopSupported()){
                                Desktop.getDesktop().browse(uri);                                
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(InputFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                JLabel cr=new JLabel("Recordatorios para registrar tareas\n JWM Solutions - 2013");
                
                panCR.add(cr);
                panCR.add(button);
                JOptionPane.showMessageDialog(null,
                        panCR,
                        "Recordatorios JWM", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salir();
                // System.exit(0);
            }
        });
    }
    
    private void salir() {
        TriggerThread.setExit(true);
        this.dispose();
        tray.remove(trayIcon);
    }
    //Obtain the image URL

    protected Image createImage(String path, String description) {
        URL imageURL = InputFrame.class.getResource(path);
        
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
    
    private class EnterKeyAdapter extends KeyAdapter {
        
        private String text;
        
        public EnterKeyAdapter(String txt) {
            text = txt;
        }
        
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == 27) {
                inputFrame.setVisible(false);
            }
            if (e.getKeyChar() == 10) {
                if (text != null && !"".equals(text.trim())) {
                    saveComment(false);
                }
                inputFrame.setVisible(false);
            }
        }
    }
}
