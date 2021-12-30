package project.ui;

import icai.dtc.isw.client.Client;
import icai.dtc.isw.domain.Contenido;
import icai.dtc.isw.domain.Usuario;
import project.misc.CheckBoxRenderer;
import project.misc.EstadoRenderer;
import project.misc.ImageRenderer;
import project.misc.StringRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interfaz del programa, será la clase encargada de comunicar al usuario con la base de datos. Contiene el código relacionado
 * con la parte gráfica y las llamadas a la clase "fachada".
 */
public class Ventana extends JFrame {

    public static void main(String args[])
    {
        new Ventana();
    }

    //Inicializamos componentes de la ventana para uso en ActionListeners y métodos
    private Usuario user;
    private JLayeredPane panelLayer = new JLayeredPane();
    private JPanel panelBuscador = new JPanel(null);
    private JTextField txtPelicula = new JTextField(20);
    private JButton btnBusqueda = new JButton("Realizar Busqueda");
    private JButton btnBuscaN = new JButton("Busqueda Por Nombre");
    private JButton btnFiltro = new JButton("Actualizar Servicios");
    private JButton btnEstado = new JButton("Estado de Visualizacion");
    private JButton btnBuscaF = new JButton("Busqueda Por Servicios");
    private JButton btnCatalogo = new JButton("Mostrar Catalogo");
    private JButton btnCambio = new JButton("Buscar en: Peliculas");
    private JButton btnVolver = new JButton("Volver");
    private JButton btnVolverEstado = new JButton("Volver");
    private JButton btnAyuda = new JButton("Ayuda");
    private JButton btnModEstado = new JButton("Modificar Estado");
    private JCheckBox ckbNetflix = new JCheckBox();
    private JCheckBox ckbHBO = new JCheckBox();
    private JCheckBox ckbPrime = new JCheckBox();
    private JCheckBox ckbDisney = new JCheckBox();
    private JCheckBox ckbCrunchy = new JCheckBox();
    private DefaultTableModel modeloTabla;
    private Color moradoClaro = new Color(139,113,227);
    private Color moradoFondo = new Color(192,188,213);
    private Color moradoDark = new Color(90,73,146);
    private Font fuente = new Font("Arial Rounded MT Bold", Font.PLAIN,13);
    private Font fuenteBtn = new Font("Arial Rounded MT Bold", Font.PLAIN,13);
    private Font fuenteBtnBig = new Font("Arial Rounded MT Bold", Font.PLAIN,14);
    private Font fuenteBtnSmall = new Font("Arial Rounded MT Bold", Font.PLAIN,11);
    private Font fuenteBtnMini = new Font("Arial Rounded MT Bold", Font.PLAIN,8);
    private JTable tablaPeliculas;


    private Boolean netflixNew;
    private Boolean hboNew;
    private Boolean primeNew;
    private Boolean disneyNew;
    private Boolean crunchyNew;
    private String catalogo = "P";
    private boolean firstPress = true;

    //guardarán los servicios definidos como favoritos en el momento de abrir la ventana de establecer servicios
    //se guardan en caso de que se cambien los checkboxes pero no se apliquen los cambios
    private ArrayList<Boolean> inicial = new ArrayList<>();
    private Boolean netflixInicial;
    private Boolean hboInicial;
    private Boolean primeInicial;
    private Boolean disneyInicial;
    private Boolean crunchyInicial;

    public Ventana()
    {
        //Inicializacion de la ventana
        super("Watchalog");
        this.setPreferredSize(new Dimension(800,600));
        this.setSize(800,600);
        this.setLayout(null);

        //diseño login
        JPanel panelLogin = new JPanel();
        panelLogin.setLayout(null);
        panelLogin.setSize(800,600);
        JTextField txtUsuario = new JTextField(20);
        JTextField txtPassword = new JTextField(20);
        JLabel lblUsuario = new JLabel("Usuario:");
        JLabel lblPassword = new JLabel("Contraseña:");
        JButton btnLogin = new JButton("Login");
        JButton btnReg = new JButton("Registrarse");
        addBoton(btnLogin,355,453,80,38,moradoClaro,Color.WHITE,fuenteBtn);
        addBoton(btnReg,445,453,110,38,moradoClaro,Color.WHITE,fuenteBtn);
        lblUsuario.setBounds(245,370,80,40);
        lblPassword.setBounds(245,410,80,40);
        txtUsuario.setBounds(355,370,200,38);
        txtPassword.setBounds(355,410,200,38);
        btnReg.setBounds(445,453,110,38);
        lblUsuario.setFont(fuente);
        lblPassword.setFont(fuente);
        txtUsuario.setFont(fuente);
        txtPassword.setFont(fuente);
        JButton btnCancelarReg = new JButton("Cancelar");
        addBoton(btnCancelarReg,btnLogin.getX(),btnLogin.getY(),btnLogin.getWidth(),btnLogin.getHeight(),moradoClaro,Color.WHITE,fuenteBtn);
        btnCancelarReg.setVisible(false);
        JLabel lblRegistro = new JLabel("Introduce tus datos para completar tu registro:");
        lblRegistro.setFont(fuente);
        lblRegistro.setBounds(245,326,330,38);
        lblRegistro.setVisible(false);

        JLabel fotoInicio = new JLabel();
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResourceAsStream("/img/inicio.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon imgFondo = new ImageIcon(img.getScaledInstance(800, 600, Image.SCALE_SMOOTH));
        fotoInicio.setIcon(imgFondo);
        fotoInicio.setBounds(0,0,800,600);

        panelBuscador.setSize(800,600);
        panelBuscador.setBackground(moradoFondo);

        //menu seleccion servicios
        panelLayer.setLocation(0,0);
        panelLayer.setBounds(0,0,800,600);
        panelLayer.setPreferredSize(new Dimension(800,600));
        panelLayer.setBackground(moradoFondo);

        JPanel panelServicios = new JPanel(new BorderLayout());
        panelServicios.setBounds(295,150,210,350);
        panelServicios.setPreferredSize(new Dimension(210,350));
        panelServicios.setBackground(Color.WHITE);
        panelServicios.setBorder(BorderFactory.createMatteBorder(2,2,2,2,moradoClaro));
        JPanel optServicios = new JPanel(new GridLayout(5,2));
        optServicios.setBackground(Color.WHITE);
        JLabel infoServicios = new JLabel("Escoja sus servicios favoritos:");
        infoServicios.setFont(fuente);
        infoServicios.setHorizontalAlignment(JLabel.CENTER);
        JPanel panelTranslucid = new JPanel();
        JLabel lblNetflix = new JLabel();
        lblNetflix.setHorizontalAlignment(JLabel.CENTER);
        setImagen(lblNetflix, "/img/netflix.png",60,45);
        JLabel lblHBO = new JLabel();
        setImagen(lblHBO, "/img/hbo.png",55,40);
        lblHBO.setHorizontalAlignment(JLabel.CENTER);
        JLabel lblPrime = new JLabel();
        setImagen(lblPrime, "/img/prime.png",60,52);
        lblPrime.setHorizontalAlignment(JLabel.CENTER);
        JLabel lblDisney = new JLabel();
        setImagen(lblDisney, "/img/disney+.png",60,40);
        lblDisney.setHorizontalAlignment(JLabel.CENTER);
        JLabel lblCrunchy = new JLabel();
        setImagen(lblCrunchy, "/img/crunchy.png",60,45);
        lblCrunchy.setHorizontalAlignment(JLabel.CENTER);
        ckbNetflix.setHorizontalAlignment(JLabel.CENTER);
        ckbHBO.setHorizontalAlignment(JLabel.CENTER);
        ckbPrime.setHorizontalAlignment(JLabel.CENTER);
        ckbDisney.setHorizontalAlignment(JLabel.CENTER);
        ckbCrunchy.setHorizontalAlignment(JLabel.CENTER);
        optServicios.add(lblNetflix);
        optServicios.add(ckbNetflix);
        optServicios.add(lblHBO);
        optServicios.add(ckbHBO);
        optServicios.add(lblPrime);
        optServicios.add(ckbPrime);
        optServicios.add(lblDisney);
        optServicios.add(ckbDisney);
        optServicios.add(lblCrunchy);
        optServicios.add(ckbCrunchy);
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnAplicar = new JButton("Aplicar");
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 16,8));
        panelBotones.setBackground(Color.WHITE);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBackground(moradoClaro);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setOpaque(true);
        btnCancelar.setFont(fuente);
        btnCancelar.setMargin(new Insets(0,-12,0,-12));
        btnAplicar.setForeground(Color.WHITE);
        btnAplicar.setBackground(moradoClaro);
        btnAplicar.setBorderPainted(false);
        btnAplicar.setContentAreaFilled(false);
        btnAplicar.setOpaque(true);
        btnAplicar.setFont(fuente);
        btnAplicar.setMargin(new Insets(0,-12,0,-12));
        panelTranslucid.setBackground(new Color(0,0,0,200));
        panelTranslucid.setLocation(0,0);
        panelTranslucid.setBounds(0,0,800,600);
        panelTranslucid.setPreferredSize(new Dimension(800,600));

        //panel links externos simulados
        JPanel panelLinks = new JPanel(new BorderLayout());
        panelLinks.setBounds(190,225,420,200);
        panelLinks.setPreferredSize(new Dimension(420,200));
        panelLinks.setBackground(Color.WHITE);
        panelLinks.setBorder(BorderFactory.createMatteBorder(2,2,2,2,moradoClaro));
        JPanel optLinks = new JPanel(new GridLayout(2,5));
        optLinks.setBackground(Color.WHITE);
        JLabel infoLinks = new JLabel("Escoge la página a la que quieres acceder:");
        infoLinks.setFont(fuente);
        infoLinks.setHorizontalAlignment(JLabel.CENTER);
        JLabel lblNetflix2 = new JLabel();
        lblNetflix2.setHorizontalAlignment(JLabel.CENTER);
        lblNetflix2.setIcon(lblNetflix.getIcon());
        JLabel lblHBO2 = new JLabel();
        lblHBO2.setHorizontalAlignment(JLabel.CENTER);
        lblHBO2.setIcon(lblHBO.getIcon());
        JLabel lblPrime2 = new JLabel();
        lblPrime2.setHorizontalAlignment(JLabel.CENTER);
        lblPrime2.setIcon(lblPrime.getIcon());
        JLabel lblDisney2 = new JLabel();
        lblDisney2.setHorizontalAlignment(JLabel.CENTER);
        lblDisney2.setIcon(lblDisney.getIcon());
        JLabel lblCrunchy2 = new JLabel();
        lblCrunchy2.setHorizontalAlignment(JLabel.CENTER);
        lblCrunchy2.setIcon(lblCrunchy.getIcon());
        JToggleButton linkNetflix = new JToggleButton("Netflix");
        JToggleButton linkHBO = new JToggleButton("HBO");
        JToggleButton linkPrime = new JToggleButton("Prime Video");
        JToggleButton linkDisney = new JToggleButton("Disney+");
        JToggleButton linkCrunchy = new JToggleButton("Crunchyroll");
        addBotonToggle(linkNetflix,50,50,50,50,moradoDark,Color.WHITE,fuenteBtnSmall);
        addBotonToggle(linkHBO,50,50,50,50,moradoDark,Color.WHITE,fuenteBtnSmall);
        addBotonToggle(linkPrime,50,50,50,50,moradoDark,Color.WHITE,fuenteBtnSmall);
        addBotonToggle(linkDisney,50,50,50,50,moradoDark,Color.WHITE,fuenteBtnSmall);
        addBotonToggle(linkCrunchy,50,50,50,50,moradoDark,Color.WHITE,fuenteBtnSmall);
        optLinks.add(lblNetflix2);
        optLinks.add(lblHBO2);
        optLinks.add(lblPrime2);
        optLinks.add(lblDisney2);
        optLinks.add(lblCrunchy2);
        optLinks.add(linkNetflix);
        optLinks.add(linkHBO);
        optLinks.add(linkPrime);
        optLinks.add(linkDisney);
        optLinks.add(linkCrunchy);
        linkNetflix.setEnabled(false);
        linkHBO.setEnabled(false);
        linkPrime.setEnabled(false);
        linkDisney.setEnabled(false);
        linkCrunchy.setEnabled(false);
        //solo puede haber un boton elegido
        linkNetflix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                linkNetflix.setSelected(true);
                linkHBO.setSelected(false);
                linkPrime.setSelected(false);
                linkDisney.setSelected(false);
                linkCrunchy.setSelected(false);
            }
        });
        linkHBO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                linkNetflix.setSelected(false);
                linkHBO.setSelected(true);
                linkPrime.setSelected(false);
                linkDisney.setSelected(false);
                linkCrunchy.setSelected(false);
            }
        });
        linkPrime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                linkNetflix.setSelected(false);
                linkHBO.setSelected(false);
                linkPrime.setSelected(true);
                linkDisney.setSelected(false);
                linkCrunchy.setSelected(false);
            }
        });
        linkDisney.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                linkNetflix.setSelected(false);
                linkHBO.setSelected(false);
                linkPrime.setSelected(false);
                linkDisney.setSelected(true);
                linkCrunchy.setSelected(false);
            }
        });
        linkCrunchy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                linkNetflix.setSelected(false);
                linkHBO.setSelected(false);
                linkPrime.setSelected(false);
                linkDisney.setSelected(false);
                linkCrunchy.setSelected(true);
            }
        });

        JButton btnVolverLinks = new JButton("Volver atras");
        JButton btnWeb = new JButton("Ir al sitio web");
        JPanel panelBotones2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 16,8));
        panelBotones2.setBackground(Color.WHITE);
        btnVolverLinks.setForeground(Color.WHITE);
        btnVolverLinks.setBackground(moradoClaro);
        btnVolverLinks.setBorderPainted(false);
        btnVolverLinks.setContentAreaFilled(false);
        btnVolverLinks.setOpaque(true);
        btnVolverLinks.setFont(fuente);
        btnVolverLinks.setMargin(new Insets(0,-12,0,-12));
        btnWeb.setForeground(Color.WHITE);
        btnWeb.setBackground(moradoClaro);
        btnWeb.setBorderPainted(false);
        btnWeb.setContentAreaFilled(false);
        btnWeb.setOpaque(true);
        btnWeb.setFont(fuente);
        btnWeb.setMargin(new Insets(0,-12,0,-12));
        panelLinks.setVisible(false);

        //paneles inicial, busquedas, links y estados
        panelServicios.add(infoServicios,BorderLayout.NORTH);
        panelServicios.add(optServicios,BorderLayout.CENTER);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnAplicar);
        panelBotones2.add(btnVolverLinks);
        panelBotones2.add(btnWeb);
        panelServicios.add(panelBotones,BorderLayout.SOUTH);
        panelLinks.add(infoLinks,BorderLayout.NORTH);
        panelLinks.add(optLinks,BorderLayout.CENTER);
        panelLinks.add(panelBotones2,BorderLayout.SOUTH);

        JPanel pnlBuscaInicio = new JPanel(null);
        pnlBuscaInicio.setBounds(0,0,800,100);
        pnlBuscaInicio.setBackground(moradoFondo);
        JPanel pnlBusquedas = new JPanel(null);
        pnlBusquedas.setBounds(0,0,800,100);
        pnlBusquedas.setBackground(moradoFondo);
        JPanel pnlEstado = new JPanel(null);
        pnlEstado.setBounds(0,0,800,100);
        pnlEstado.setBackground(moradoFondo);

        addBoton(btnFiltro,300,31,200,38,moradoClaro,Color.WHITE, fuenteBtnBig);
        addBoton(btnBusqueda,30,31,200,38,moradoClaro,Color.WHITE, fuenteBtnBig);
        addBoton(btnEstado,570,31,200,38,moradoClaro,Color.WHITE, fuenteBtnBig);
        txtPelicula.setBounds(10,31,180,40);
        txtPelicula.setFont(fuente);
        addBoton(btnBuscaN,210,31,180,38,moradoClaro,Color.WHITE,fuenteBtn);
        addBoton(btnBuscaF,410,31,180,38,moradoClaro,Color.WHITE,fuenteBtn);
        addBoton(btnCatalogo,610,31,180,38,moradoClaro,Color.WHITE,fuenteBtn);
        addBoton(btnCambio,610,2,100,14,moradoDark,Color.WHITE,fuenteBtnMini);
        addBoton(btnVolver,730,2,60,14,moradoDark,Color.WHITE,fuenteBtnMini);
        JButton btnVer = new JButton("Ver contenido");
        addBoton(btnVer,325,540,150,30,moradoClaro,Color.WHITE,fuenteBtn);
        btnVer.setVisible(false);

        //menu estados
        ImageIcon viendoIcon = new ImageIcon(new ImageIcon(getClass().getResource("/img/viendoBig.png")).getImage().getScaledInstance(54,31,Image.SCALE_SMOOTH));
        ImageIcon vistoIcon = new ImageIcon(new ImageIcon(getClass().getResource("/img/vistoBig.png")).getImage().getScaledInstance(55,35,Image.SCALE_SMOOTH));
        ImageIcon listaIcon = new ImageIcon(new ImageIcon(getClass().getResource("/img/listaBig.png")).getImage().getScaledInstance(40,42,Image.SCALE_SMOOTH));
        JLabel imgViendo = new JLabel(viendoIcon);
        JLabel imgVisto = new JLabel(vistoIcon);
        JLabel imgLista = new JLabel(listaIcon);
        JToggleButton btnViendo = new JToggleButton("Viendo");
        JToggleButton btnVisto = new JToggleButton("Visto");
        JToggleButton btnLista = new JToggleButton("En mi lista");
        JButton btnQuitar = new JButton("Quitar Estado");
        //solo puede haber un boton elegido
        btnViendo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnViendo.setSelected(true);
                btnVisto.setSelected(false);
                btnLista.setSelected(false);
            }
        });
        btnVisto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnVisto.setSelected(true);
                btnViendo.setSelected(false);
                btnLista.setSelected(false);
            }
        });
        btnLista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLista.setSelected(true);
                btnVisto.setSelected(false);
                btnViendo.setSelected(false);
            }
        });
        imgLista.setBounds(60,15,40,45);
        addBotonToggle(btnLista,40,65,80,20,moradoClaro,Color.WHITE,fuenteBtnSmall);
        imgViendo.setBounds(195,22,55,31);
        addBotonToggle(btnViendo,182,65,80,20,moradoClaro,Color.WHITE,fuenteBtnSmall);
        imgVisto.setBounds(340,22,55,35);
        addBotonToggle(btnVisto,328,65,80,20,moradoClaro,Color.WHITE,fuenteBtnSmall);
        addBoton(btnQuitar,450,31,150,38,moradoClaro,Color.WHITE,fuenteBtn);
        addBoton(btnVolverEstado,730,2,60,14,moradoDark,Color.WHITE,fuenteBtnMini);
        addBoton(btnAyuda,660,2,60,14,moradoDark,Color.WHITE,fuenteBtnMini);
        addBoton(btnModEstado,630,31,160,38,moradoClaro,Color.WHITE,fuenteBtn);


        //registro nuevo user
        btnReg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(firstPress){
                    btnLogin.setVisible(false);
                    btnCancelarReg.setVisible(true);
                    lblRegistro.setVisible(true);
                    firstPress = false;
                    txtUsuario.setText("");
                    txtPassword.setText("");
                } else {
                    String nombre = txtUsuario.getText();
                    String password =  txtPassword.getText();
                    if(nombre.isEmpty() || password.isEmpty() || nombre.isBlank() || password.isBlank()) {
                        JOptionPane.showMessageDialog(Ventana.this, "Por favor, rellena todos los campos obligatorios");
                        return;
                    }
                    Client client = new Client();
                    HashMap<String, Object> session = new HashMap<>();
                    session.put("nombre", nombre);
                    session.put("password", password);
                    client.startClient("/userRegistro",session);
                    int regOk = (int) session.get("registro");
                    if(regOk == 1){
                        JOptionPane.showMessageDialog(Ventana.this, "Registro realizado correctamente");
                        btnLogin.setVisible(true);
                        btnCancelarReg.setVisible(false);
                        lblRegistro.setVisible(false);
                        firstPress = true;
                        txtUsuario.setText("");
                        txtPassword.setText("");
                    } else if (regOk == -1) {
                        JOptionPane.showMessageDialog(Ventana.this, "Su nombre de usuario ya está registrado:" +
                                " por favor use un nombre distinto");
                    } else {
                        JOptionPane.showMessageDialog(Ventana.this, "Lo sentimos, ha habido un error en " +
                                "el proceso de registro");
                    }
                }
            }
        });

        btnCancelarReg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLogin.setVisible(true);
                btnCancelarReg.setVisible(false);
                lblRegistro.setVisible(false);
                firstPress = true;
                txtUsuario.setText("");
                txtPassword.setText("");
            }
        });

        //código para el inicio de sesión
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = txtUsuario.getText();
                String password =  txtPassword.getText();
                user = new Usuario();
                Client client = new Client();
                HashMap<String, Object> session = new HashMap<>();
                session.put("nombre", nombre);
                session.put("password", password);
                session.put("user",user);
                client.startClient("/userLogin",session);
                boolean respuesta = (boolean) session.get("loginStatus");
                user = (Usuario)session.get("user");
                ArrayList<String> serviciosIniciales = user.getServicios();
                if (respuesta) {
                    JOptionPane.showMessageDialog(Ventana.this, "Login OK");
                    panelBuscador.setVisible(true);
                    panelLogin.setVisible(false);
                    //user = nombre;
                    for (String serviciosIni : serviciosIniciales) {
                        switch (serviciosIni) {
                            case "Netflix":
                                ckbNetflix.setSelected(true);
                                break;
                            case "HBO":
                                ckbHBO.setSelected(true);
                                break;
                            case "Prime Video":
                                ckbPrime.setSelected(true);
                                break;
                            case "Disney+":
                                ckbDisney.setSelected(true);
                                break;
                            case "Crunchyroll":
                                ckbCrunchy.setSelected(true);
                                break;
                        }
                        almacenarServicios();
                    }
                } else {
                    JOptionPane.showMessageDialog(Ventana.this, "ERROR: nombre o password incorrecto");
                }
            }
        });

        //Logica de la lista
        String[] nombreColumnas = {"", "Titulo", "Director", "", "", "", "", "",""};
        Object[][] data = {};
        modeloTabla = new DefaultTableModel(data, nombreColumnas);
        tablaPeliculas = new JTable(modeloTabla){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            //renderización alternativa de las celdas de la lista
            public TableCellRenderer getCellRenderer(int row, int col){
                //Wrap text in a specific column
                if (col == 1 || col == 2) {
                    StringRenderer sr = new StringRenderer();
                    sr.setFont(fuente);
                    return sr;
                } else if (col == 0){
                    return new ImageRenderer();
                } else if (col == 8){
                    return new EstadoRenderer();
                } else {
                    return new CheckBoxRenderer();
                }
            }
            public Class getColumnClass(int column) {
                if (column == 0 || column == 8) {
                    return Icon.class;
                }else if (column == 1 || column == 2) {
                    return String.class;
                }else {
                    return Boolean.class;
                }
            }
        };

        //características tabla
        tablaPeliculas.getTableHeader().setReorderingAllowed(false);
        tablaPeliculas.getTableHeader().setResizingAllowed(false);
        tablaPeliculas.getTableHeader().setFont(fuente);
        tablaPeliculas.setAutoCreateRowSorter(true);
        tablaPeliculas.setEnabled(false);
        tablaPeliculas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPeliculas.setSelectionForeground(Color.WHITE);
        tablaPeliculas.setSelectionBackground(new Color(135,114,220,80));
        tablaPeliculas.setRowHeight(100);
        tablaPeliculas.getColumnModel().getColumn(0).setPreferredWidth(57);
        tablaPeliculas.getColumnModel().getColumn(1).setPreferredWidth(265);
        tablaPeliculas.getColumnModel().getColumn(2).setPreferredWidth(188);
        tablaPeliculas.getColumnModel().getColumn(3).setPreferredWidth(45);
        tablaPeliculas.getColumnModel().getColumn(4).setPreferredWidth(45);
        tablaPeliculas.getColumnModel().getColumn(5).setPreferredWidth(45);
        tablaPeliculas.getColumnModel().getColumn(6).setPreferredWidth(45);
        tablaPeliculas.getColumnModel().getColumn(7).setPreferredWidth(45);
        tablaPeliculas.getColumnModel().getColumn(8).setPreferredWidth(45);
        tablaPeliculas.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());

        JScrollPane panelScroll = new JScrollPane(tablaPeliculas);
        panelScroll.setLocation(10,100);
        panelScroll.setBounds(10,100,780,435);
        panelScroll.setPreferredSize(new Dimension(780,435));

        columnImage(tablaPeliculas,"/img/netflix.png",3,40,20);
        columnImage(tablaPeliculas,"/img/hbo.png",4,36,18);
        columnImage(tablaPeliculas,"/img/prime.png",5,37,25);
        columnImage(tablaPeliculas,"/img/disney+.png",6,40,18);
        columnImage(tablaPeliculas,"/img/crunchy.png",7,40,20);

        //cambio a menu busquedas
        btnBusqueda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlBuscaInicio.setVisible(false);
                pnlBusquedas.setVisible(true);
                btnVer.setVisible(true);
                tablaPeliculas.setEnabled(true);
            }
        });

        //cambio a menu estados
        btnEstado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlBuscaInicio.setVisible(false);
                pnlEstado.setVisible(true);
                tablaPeliculas.setEnabled(true);
            }
        });

        //cambio a menu inicial desde busquedas
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlBuscaInicio.setVisible(true);
                pnlBusquedas.setVisible(false);
                btnVer.setVisible(false);
                tablaPeliculas.setEnabled(false);
                tablaPeliculas.clearSelection();
            }
        });

        //cambio a menu inicial desde estado
        btnVolverEstado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlBuscaInicio.setVisible(true);
                pnlEstado.setVisible(false);
                tablaPeliculas.setEnabled(false);
                tablaPeliculas.clearSelection();
            }
        });

        btnAyuda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Ventana.this,"Desde este menú tienes la opción de marcar el estado de visua" +
                        "lizacion de tu contenido favorito.\nPrimero, selecciona el contenido que desees marcar\nDespués, elige entre las" +
                        " opciones disponibles: EN LISTA, VIENDO o VISTO\nFinalmente confirma tu seleccion. De esta manera, el estado quedara" +
                        " modificado en todas tus futuras busquedas");
            }
        });

        //cambio de estado
        btnModEstado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String estado = "";
                if (btnLista.isSelected()) {
                    estado = "LISTA";
                } else if (btnViendo.isSelected()){
                    estado = "VIENDO";
                } else if (btnVisto.isSelected()){
                    estado = "VISTO";
                } else {
                    JOptionPane.showMessageDialog(Ventana.this,"Elige uno de los estados");
                    return;
                }
                int row = tablaPeliculas.getSelectedRow();
                if(row == -1) {
                    JOptionPane.showMessageDialog(Ventana.this,"Elige un contenido para marcar");
                    return;
                }
                String c = (String)tablaPeliculas.getValueAt(row,1);
                Client client = new Client();
                HashMap<String, Object> session = new HashMap<>();
                session.put("usuario",user.getId());
                session.put("titulo",c);
                session.put("estado",estado);
                client.startClient("/cambioEstado",session);
                int cambioOk = (int) session.get("cambio");
                if(cambioOk == 1){
                    JOptionPane.showMessageDialog(Ventana.this, "El nuevo estado ha quedado actualizado para futuras busquedas");
                    btnLista.setSelected(false);
                    btnVisto.setSelected(false);
                    btnViendo.setSelected(false);
                } else {
                    JOptionPane.showMessageDialog(Ventana.this, "Lo sentimos, ha habido un error en " +
                            "el proceso de cambio de estado");
                }
            }
        });

        //quitar estado
        btnQuitar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String estado = "BORRAR";
                int row = tablaPeliculas.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(Ventana.this,"Elige un contenido para marcar");
                    return;
                }
                Object c_estado = tablaPeliculas.getValueAt(row,8);
                boolean nulo = false;
                try {
                    c_estado.equals(null);
                } catch (NullPointerException ne) {
                    nulo = true;
                }
                if (nulo) {
                    JOptionPane.showMessageDialog(Ventana.this,"El contenido elegido no tiene ningun estado asignado");
                    return;
                }
                String c = (String)tablaPeliculas.getValueAt(row,1);
                Client client = new Client();
                HashMap<String, Object> session = new HashMap<>();
                session.put("usuario",user.getId());
                session.put("titulo",c);
                session.put("estado",estado);
                client.startClient("/cambioEstado",session);
                int cambioOk = (int) session.get("cambio");
                if(cambioOk == 1){
                    JOptionPane.showMessageDialog(Ventana.this, "El nuevo estado ha quedado actualizado para futuras busquedas");
                    btnLista.setSelected(false);
                    btnVisto.setSelected(false);
                    btnViendo.setSelected(false);
                } else {
                    JOptionPane.showMessageDialog(Ventana.this, "Lo sentimos, ha habido un error en " +
                            "el proceso de cambio de estado");
                }
            }
        });

        //ver contenido en web externa
        btnVer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tablaPeliculas.getSelectedRow();
                if(row == -1) {
                    JOptionPane.showMessageDialog(Ventana.this,"Elige un contenido para ver");
                    return;
                }
                panelLayer.setVisible(true);
                panelLinks.setVisible(true);
                panelServicios.setVisible(false);
                btnCatalogo.setEnabled(false);
                btnBuscaF.setEnabled(false);
                btnBuscaN.setEnabled(false);
                txtPelicula.setEnabled(false);
                tablaPeliculas.getTableHeader().setEnabled(false);
                panelScroll.setEnabled(false);
                btnVer.setEnabled(false);

                linkNetflix.setSelected(false);
                linkHBO.setSelected(false);
                linkPrime.setSelected(false);
                linkDisney.setSelected(false);
                linkCrunchy.setSelected(false);

                if ((boolean)tablaPeliculas.getValueAt(row,3)) {
                    linkNetflix.setEnabled(true);
                } else {
                    linkNetflix.setEnabled(false);
                }

                if ((boolean)tablaPeliculas.getValueAt(row,4)) {
                    linkHBO.setEnabled(true);
                } else {
                    linkHBO.setEnabled(false);
                }

                if ((boolean)tablaPeliculas.getValueAt(row,5)) {
                    linkPrime.setEnabled(true);
                } else {
                    linkPrime.setEnabled(false);
                }

                if ((boolean)tablaPeliculas.getValueAt(row,6)) {
                    linkDisney.setEnabled(true);
                } else {
                    linkDisney.setEnabled(false);
                }

                if ((boolean)tablaPeliculas.getValueAt(row,7)) {
                    linkCrunchy.setEnabled(true);
                } else {
                    linkCrunchy.setEnabled(false);
                }
            }
        });

        btnVolverLinks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelLayer.setVisible(false);
                panelLinks.setVisible(false);
                panelServicios.setVisible(true);
                btnCatalogo.setEnabled(true);
                btnBuscaF.setEnabled(true);
                btnBuscaN.setEnabled(true);
                txtPelicula.setEnabled(true);
                tablaPeliculas.getTableHeader().setEnabled(true);
                panelScroll.setEnabled(true);
                btnVer.setEnabled(true);
            }
        });

        btnWeb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Ventana.this,"Saliendo de la aplicación, espere mientras le redirigimos" +
                        " al servicio de streaming deseado...\n\n(Funcionalidad simulada. Puede cerrar esta ventana y volvera al menu de busqueda)");
                panelLayer.setVisible(false);
                panelLinks.setVisible(false);
                panelServicios.setVisible(true);
                btnCatalogo.setEnabled(true);
                btnBuscaF.setEnabled(true);
                btnBuscaN.setEnabled(true);
                txtPelicula.setEnabled(true);
                tablaPeliculas.getTableHeader().setEnabled(true);
                panelScroll.setEnabled(true);
                linkNetflix.setEnabled(false);
                linkHBO.setEnabled(false);
                linkPrime.setEnabled(false);
                linkDisney.setEnabled(false);
                linkCrunchy.setEnabled(false);
                btnVer.setEnabled(true);
            }
        });

        //seleccion de catalogo
        btnCambio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnCambio.getText().equals("Buscar en: Peliculas")){
                    catalogo = "S";
                    btnCambio.setText("Buscar en: Series");
                } else if (btnCambio.getText().equals("Buscar en: Series")){
                    catalogo = "A";
                    btnCambio.setText("Buscar en: Ambos");
                } else {
                    catalogo = "P";
                    btnCambio.setText("Buscar en: Peliculas");
                }
            }
        });

        //busqueda catalogo completo
        btnCatalogo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarTabla();
                Client client = new Client();
                HashMap<String, Object> session = new HashMap<>();
                session.put("catalogo",catalogo);
                session.put("usuario",user.getId());
                client.startClient("/buscaFull",session);
                ArrayList<Contenido> listaContenido = (ArrayList<Contenido>) session.get("listaFull");
                for(Contenido contenido : listaContenido) {
                    RellenarFila(contenido,modeloTabla);
                }
            }
        });

        //Busqueda filtrada por servicios
        btnBuscaF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarTabla();
                Client client = new Client();
                HashMap<String, Object> session = new HashMap<>();
                session.put("catalogo",catalogo);
                session.put("usuario",user.getId());
                //generamos el string para hacer la query
                ArrayList<String> servicios = new ArrayList<>();
                for(int i = 0; i<inicial.size(); i++){
                    switch (i) {
                        case 0:
                            if (inicial.get(0)) {
                                servicios.add("Netflix");
                            }
                        case 1:
                            if (inicial.get(1)) {
                                servicios.add("HBO");
                            }
                        case 2:
                            if (inicial.get(2)) {
                                servicios.add("Prime Video");
                            }
                        case 3:
                            if (inicial.get(3)) {
                                servicios.add("Disney+");
                            }
                        case 4:
                            if (inicial.get(4)) {
                                servicios.add("Crunchyroll");
                            }
                    }
                }
                String aux = "";
                for(int i = 0; i<servicios.size(); i++){
                    if(i == servicios.size()-1)
                    {
                        aux = aux + servicios.get(i);
                    } else {
                        aux = aux + servicios.get(i) + ",";
                    }
                }
                session.put("serviciosQuery", aux);
                client.startClient("/buscaF",session);
                ArrayList<Contenido> listaContenido = (ArrayList<Contenido>) session.get("listaPeliculas");
                if (listaContenido.size() == 0){
                    JOptionPane.showMessageDialog(Ventana.this, "Elige tus servicios preferidos para obtener resultados");
                } else {
                    for(Contenido contenido : listaContenido) {
                        RellenarFila(contenido,modeloTabla);
                    }
                }
            }
        });

        //busqueda por nombre
        btnBuscaN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = txtPelicula.getText().trim();
                boolean cero = true;
                if (nombre.isBlank() || nombre.isBlank()){
                    JOptionPane.showMessageDialog(Ventana.this, "Por favor, introduzca texto antes de buscar");
                } else {
                    limpiarTabla();
                    Client client = new Client();
                    HashMap<String, Object> session = new HashMap<>();
                    session.put("catalogo",catalogo);
                    session.put("usuario",user.getId());
                    session.put("nombre", nombre);
                    client.startClient("/buscaN",session);
                    ArrayList<Contenido> listaContenido = (ArrayList<Contenido>) session.get("listaNombre");
                    try {
                        for(Contenido contenido : listaContenido) {
                            RellenarFila(contenido, modeloTabla);
                            cero = false;
                        }
                    } catch(NullPointerException ex){
                    }
                    if(cero){
                        JOptionPane.showMessageDialog(Ventana.this, "No hay contenido disponible que coincida con su búsqueda");
                    }
                }
            }
        });

        //menu seleccion servicios
        btnFiltro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                almacenarServicios();
                mostrarMenuFiltros(tablaPeliculas, panelScroll);
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //usamos el array inicial para reestablecer los valores
                for(int i = 0; i<inicial.size(); i++){
                    switch (i) {
                        case 0:
                            if(inicial.get(0)){
                                ckbNetflix.setSelected(true);
                            }else{
                                ckbNetflix.setSelected(false);
                            }
                            break;
                        case 1:
                            if(inicial.get(1)){
                                ckbHBO.setSelected(true);
                            }else{
                                ckbHBO.setSelected(false);
                            }
                            break;
                        case 2:
                            if(inicial.get(2)){
                                ckbPrime.setSelected(true);
                            }else{
                                ckbPrime.setSelected(false);
                            }
                            break;
                        case 3:
                            if(inicial.get(3)){
                                ckbDisney.setSelected(true);
                            }else{
                                ckbDisney.setSelected(false);
                            }
                            break;
                        case 4:
                            if(inicial.get(4)){
                                ckbCrunchy.setSelected(true);
                            }else{
                                ckbCrunchy.setSelected(false);
                            }
                            break;
                    }
                }
                ocultarMenuFiltros(tablaPeliculas, panelScroll);
            }
        });

        btnAplicar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //cogemos los servicios para actualizar
                netflixNew = ckbNetflix.isSelected();
                hboNew = ckbHBO.isSelected();
                primeNew = ckbPrime.isSelected();
                disneyNew = ckbDisney.isSelected();
                crunchyNew = ckbCrunchy.isSelected();
                //cambiamos los servicios iniciales para tenerlos guardados
                netflixInicial = ckbNetflix.isSelected();
                hboInicial = ckbHBO.isSelected();
                primeInicial = ckbPrime.isSelected();
                disneyInicial = ckbDisney.isSelected();
                crunchyInicial = ckbCrunchy.isSelected();
                Client client = new Client();
                HashMap<String, Object> session = new HashMap<>();
                session.put("netflix", netflixNew);
                session.put("hbo", hboNew);
                session.put("prime", primeNew);
                session.put("disney", disneyNew);
                session.put("crunchy", crunchyNew);
                session.put("usuario", user.getName());
                client.startClient("/aplicarServicios",session);
                ocultarMenuFiltros(tablaPeliculas, panelScroll);
                almacenarServicios();
                JOptionPane.showMessageDialog(Ventana.this, "Servicios preferidos actualizados");
            }
        });

        //añadir paneles
        panelBuscador.setVisible(false);
        panelLayer.setVisible(false);
        panelLogin.setVisible(true);
        panelLogin.add(txtUsuario);
        panelLogin.add(txtPassword);
        panelLogin.add(lblUsuario);
        panelLogin.add(lblPassword);
        panelLogin.add(btnLogin);
        panelLogin.add(btnReg);
        panelLogin.add(btnCancelarReg);
        panelLogin.add(lblRegistro);
        panelLogin.add(fotoInicio);
        pnlBusquedas.add(btnBuscaF);
        pnlBusquedas.add(btnBuscaN);
        pnlBusquedas.add(btnCatalogo);
        pnlBusquedas.add(txtPelicula);
        pnlBusquedas.add(btnVolver);
        pnlBusquedas.add(btnCambio);
        pnlBuscaInicio.add(btnFiltro);
        pnlBuscaInicio.add(btnBusqueda);
        pnlBuscaInicio.add(btnEstado);
        pnlEstado.add(imgVisto);
        pnlEstado.add(imgLista);
        pnlEstado.add(imgViendo);
        pnlEstado.add(btnLista);
        pnlEstado.add(btnViendo);
        pnlEstado.add(btnVisto);
        pnlEstado.add(btnQuitar);
        pnlEstado.add(btnAyuda);
        pnlEstado.add(btnVolverEstado);
        pnlEstado.add(btnModEstado);
        panelBuscador.add(panelScroll);
        panelBuscador.add(pnlBuscaInicio);
        panelBuscador.add(pnlBusquedas);
        panelBuscador.add(pnlEstado);
        panelBuscador.add(btnVer);
        pnlBusquedas.setVisible(false);
        pnlEstado.setVisible(false);
        panelLayer.add(panelTranslucid);
        panelLayer.add(panelServicios);
        panelLayer.add(panelLinks);
        panelLayer.moveToBack(panelTranslucid);
        this.add(panelLayer);
        this.setLayout(new BorderLayout());
        this.add(panelLogin);
        this.add(panelBuscador);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Método para rellenar cada fila de la lista una vez realizada la búsqueda en la base de datos
     * @param contenido Objeto cuyos datos introducimos en la fila
     * @param modelo Modelo de la tabla donde introducimos los datos
     */
    private void RellenarFila(Contenido contenido, DefaultTableModel modelo){
        String titulo = contenido.getTitulo();
        String director = contenido.getDirector();
        boolean netflix = false;
        boolean hbo = false;
        boolean prime = false;
        boolean disney = false;
        boolean crunchy = false;
        BufferedImage img;
        ImageIcon portada = null;
        try {
            img = ImageIO.read(getClass().getResource(contenido.getPortada()));
            Image imagen = getImagen(img,img.getWidth(),img.getHeight());
            portada = new ImageIcon(imagen.getScaledInstance(57, 100, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (contenido.getServicios().contains("Netflix")){
            netflix = true;
        }
        if (contenido.getServicios().contains("HBO")){
            hbo = true;
        }
        if (contenido.getServicios().contains("Prime Video")){
            prime = true;
        }
        if (contenido.getServicios().contains("Disney+")){
            disney = true;
        }
        if (contenido.getServicios().contains("Crunchyroll")){
            crunchy = true;
        }
        modelo.addRow(new Object[]{portada,titulo,director,netflix,hbo,prime,disney,crunchy,contenido.getEstado()});
    }

    /**
     * Método para mostrar el menú estilo Pop-Up de selección de filtros preferidos. Bloquea la interacción con el resto de elementos
     * de la ventana.
     * @param tablaPeliculas Tabla donde se muestran los resultados de la búsqueda.
     * @param panelScroll Panel donde se ubica la tabla.
     */
    private void mostrarMenuFiltros(JTable tablaPeliculas, JScrollPane panelScroll){
        panelLayer.setVisible(true);
        btnFiltro.setEnabled(false);
        btnBusqueda.setEnabled(false);
        btnEstado.setEnabled(false);
        tablaPeliculas.getTableHeader().setEnabled(false);
        panelScroll.setEnabled(false);
    }

    /**
     * Método para ocultar el menú estilo Pop-Up de selección de filtros preferidos. Rehabilita la interacción con el resto de elementos
     * de la ventana.
     * @param tablaPeliculas Tabla donde se muestran los resultados de la búsqueda.
     * @param panelScroll Panel donde se ubica la tabla.
     */
    private void ocultarMenuFiltros(JTable tablaPeliculas, JScrollPane panelScroll){
        panelLayer.setVisible(false);
        btnFiltro.setEnabled(true);
        btnBusqueda.setEnabled(true);
        btnEstado.setEnabled(true);
        tablaPeliculas.getTableHeader().setEnabled(true);
        panelScroll.setEnabled(true);
    }

    /**
     * Método para almacenar los servicios favoritos elegidos por el usuario autenticado. Este método permite almacenar
     * en cualquier momento la versión actualizada de los servicios elegidos por el usuario, y por tanto ahorra tener que hacer
     * la query en la base de datos.
     */
    private void almacenarServicios(){
        //almacenamos los servicios marcados como favoritos
        netflixInicial = ckbNetflix.isSelected();
        hboInicial = ckbHBO.isSelected();
        primeInicial = ckbPrime.isSelected();
        disneyInicial = ckbDisney.isSelected();
        crunchyInicial = ckbCrunchy.isSelected();
        //guardamos estos en un array
        inicial.add(0,netflixInicial);
        inicial.add(1,hboInicial);
        inicial.add(2,primeInicial);
        inicial.add(3,disneyInicial);
        inicial.add(4,crunchyInicial);
    }

    private void limpiarTabla(){
        modeloTabla.getDataVector().removeAllElements();
        modeloTabla.fireTableDataChanged();
    }

    /**
     * Método para añadir botones al interfaz.
     * @param btn boton a añadir
     * @param x posicion x del boton
     * @param y posicion y del boton
     * @param w anchura del boton
     * @param h altura del boton
     * @param fondo color del boton
     * @param frente color de las letras del boton
     * @param fuente fuente de las letras del boton
     */
    private void addBoton(JButton btn, int x, int y, int w, int h, Color fondo, Color frente, Font fuente){
        btn.setBounds(x,y,w,h);
        btn.setForeground(frente);
        btn.setBackground(fondo);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setFont(fuente);
        btn.setMargin(new Insets(0,-12,0,-12));
        btn.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    btn.setBackground(new Color(128,86,202,80));
                } else {
                    btn.setBackground(fondo);
                }
            }
        });
    }

    /**
     * Método para añadir botones tipo toggle al interfaz.
     * @param btn boton a añadir
     * @param x posicion x del boton
     * @param y posicion y del boton
     * @param w anchura del boton
     * @param h altura del boton
     * @param fondo color del boton
     * @param frente color de las letras del boton
     * @param fuente fuente de las letras del boton
     */
    private void addBotonToggle(JToggleButton btn, int x, int y, int w, int h, Color fondo, Color frente, Font fuente){
        btn.setBounds(x,y,w,h);
        btn.setForeground(frente);
        btn.setBackground(fondo);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setFont(fuente);
        btn.setMargin(new Insets(0,-12,0,-12));
        btn.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isSelected()) {
                    btn.setBackground(moradoDark);
                } else {
                    btn.setBackground(fondo);
                }
            }
        });
    }

    /**
     * Método para añadir imagenes.
     * @param lbl JLabel que guardara la imagen
     * @param file ruta de la imagen
     * @param x posicion x de la imagen
     * @param y posicion y de la imagen
     */
    private void setImagen(JLabel lbl, String file, int x, int y){
        BufferedImage img = null;
        try {
            img = ImageIO.read(getClass().getResourceAsStream(file));
            Image logo = getImagen(img,img.getWidth(),img.getHeight());
            lbl.setIcon(new ImageIcon(logo.getScaledInstance(x,y,Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image getImagen(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    //renderer class: https://stackhowto.com/how-to-print-a-jtable-with-image-in-header/
    class JTableCellRenderer implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object
                value, boolean isSelected, boolean hasFocus, int row, int column) {
            return (JComponent) value;
        }
    }

    private void columnImage(JTable tablaPeliculas, String img, int index, int x, int y){
        //creates a border
        Border headerBorder = UIManager.getBorder("TableHeader.cellBorder");
        //creates two labels with a border
        Image image = new ImageIcon(getClass().getResource(img)).getImage().getScaledInstance(x,y,Image.SCALE_SMOOTH);
        JLabel label1 = new JLabel(new ImageIcon(image), JLabel.CENTER);
        label1.setBorder(headerBorder);
        //set the renderer
        TableCellRenderer renderer = new JTableCellRenderer();
        //define column model
        TableColumnModel model = tablaPeliculas.getColumnModel();
        TableColumn col = model.getColumn(index);
        col.setHeaderRenderer(renderer);
        col.setHeaderValue(label1);
    }
}
