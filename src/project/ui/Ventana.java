package project.ui;

import icai.dtc.isw.client.Client;
import icai.dtc.isw.domain.Contenido;
import icai.dtc.isw.domain.Usuario;
import project.misc.CheckBoxRenderer;
import project.misc.StringRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
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
    private JPanel btnBusqBG = new JPanel();
    private JButton btnBuscaN = new JButton("Busqueda Por Nombre");
    private JButton btnFiltro = new JButton("Actualizar Servicios");
    private JPanel btnFiltroBG = new JPanel();
    private JButton btnBuscaF = new JButton("Busqueda Por Servicios");
    private JButton btnCatalogo = new JButton("Mostrar Catalogo");
    private JButton btnCambio = new JButton("Buscar en: Peliculas");
    private JButton btnVolver = new JButton("Volver");
    private JCheckBox ckbNetflix = new JCheckBox();
    private JCheckBox ckbHBO = new JCheckBox();
    private JCheckBox ckbPrime = new JCheckBox();
    private JCheckBox ckbDisney = new JCheckBox();
    private JCheckBox ckbCrunchy = new JCheckBox();
    private DefaultTableModel modeloTabla;
    private Color moradoClaro = new Color(139,113,227);
    private Color moradoFondo = new Color(197,192,216);
    private Color moradoDark = new Color(90,73,146);
    private Font fuente = new Font("Arial Rounded MT Bold", Font.PLAIN,13);
    private Font fuenteBtn = new Font("Arial Rounded MT Bold", Font.PLAIN,13);
    private Font fuenteBtn2 = new Font("Arial Rounded MT Bold", Font.PLAIN,14);
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
        JPanel btnLoginBG = new JPanel();
        JPanel btnRegBG = new JPanel();
        addBoton(btnLogin,btnLoginBG,355,453,80,38,fuenteBtn);
        addBoton(btnReg,btnRegBG,445,453,110,38,fuenteBtn);
        lblUsuario.setBounds(245,370,80,40);
        lblPassword.setBounds(245,410,80,40);
        txtUsuario.setBounds(355,370,200,38);
        txtPassword.setBounds(355,410,200,38);
        btnReg.setBounds(445,453,110,38);
        btnRegBG.setBounds(445,453,110,38);
        lblUsuario.setFont(fuente);
        lblPassword.setFont(fuente);
        txtUsuario.setFont(fuente);
        txtPassword.setFont(fuente);
        JButton btnCancelarReg = new JButton("Cancelar");
        JPanel btnCanRegBG = new JPanel();
        addBoton(btnCancelarReg,btnCanRegBG,btnLogin.getX(),btnLogin.getY(),btnLogin.getWidth(),btnLogin.getHeight(),fuenteBtn);
        btnCancelarReg.setVisible(false);
        btnCanRegBG.setVisible(false);
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


        //menu seleccion servicios
        panelBuscador.setSize(800,600);
        panelBuscador.setBackground(moradoFondo);
        panelLayer.setLocation(0,0);
        panelLayer.setBounds(0,0,800,600);
        panelLayer.setPreferredSize(new Dimension(800,600));
        panelLayer.setBackground(moradoFondo);

        JPanel panelServicios = new JPanel(new BorderLayout());
        panelServicios.setLocation(0,0);
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

        panelServicios.add(infoServicios,BorderLayout.NORTH);
        panelServicios.add(optServicios,BorderLayout.CENTER);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnAplicar);
        panelServicios.add(panelBotones,BorderLayout.SOUTH);

        JPanel pnlBuscaInicio = new JPanel(null);
        pnlBuscaInicio.setBounds(0,0,800,100);
        pnlBuscaInicio.setBackground(moradoFondo);
        JPanel pnlBusquedas = new JPanel(null);
        pnlBusquedas.setBounds(0,0,800,100);
        pnlBusquedas.setBackground(moradoFondo);

        addBoton(btnFiltro,btnFiltroBG,450,31,200,38,fuenteBtn2);
        addBoton(btnBusqueda,btnBusqBG,150,31,200,38,fuenteBtn2);
        txtPelicula.setBounds(10,31,180,40);
        txtPelicula.setFont(fuente);
        JPanel btnBuscaNBG = new JPanel();
        addBoton(btnBuscaN,btnBuscaNBG,210,31,180,38,fuenteBtn);
        JPanel btnBuscaFBG = new JPanel();
        addBoton(btnBuscaF,btnBuscaFBG,410,31,180,38,fuenteBtn);
        JPanel btnCatalogoBG = new JPanel();
        addBoton(btnCatalogo,btnCatalogoBG,610,31,180,38,fuenteBtn);
        JPanel btnCambioBG = new JPanel();
        addBoton(btnCambio,btnCambioBG,610,2,100,14,fuenteBtnMini);
        btnCambioBG.setBackground(moradoDark);
        JPanel btnVolverBG = new JPanel();
        addBoton(btnVolver,btnVolverBG,730,2,60,14,fuenteBtnMini);
        btnVolverBG.setBackground(moradoDark);

        //registro nuevo user
        btnReg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(firstPress){
                    btnLogin.setVisible(false);
                    btnLoginBG.setVisible(false);
                    btnCancelarReg.setVisible(true);
                    btnCanRegBG.setVisible(true);
                    lblRegistro.setVisible(true);
                    firstPress = false;
                    txtUsuario.setText("");
                    txtPassword.setText("");
                } else {
                    String nombre = txtUsuario.getText();
                    String password =  txtPassword.getText();
                    Client client = new Client();
                    HashMap<String, Object> session = new HashMap<>();
                    session.put("nombre", nombre);
                    session.put("password", password);
                    client.startClient("/userRegistro",session);
                    int regOk = (int) session.get("registro");
                    if(regOk == 1){
                        JOptionPane.showMessageDialog(Ventana.this, "Registro realizado correctamente");
                        btnLogin.setVisible(true);
                        btnLoginBG.setVisible(true);
                        btnCancelarReg.setVisible(false);
                        btnCanRegBG.setVisible(false);
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
                btnLoginBG.setVisible(true);
                btnCancelarReg.setVisible(false);
                btnCanRegBG.setVisible(false);
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
        String[] nombreColumnas = {"", "Titulo", "Director", "", "", "", "", ""};
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
                } else {
                    return new CheckBoxRenderer();
                }
            }
            public Class getColumnClass(int column) {
                if (column == 0) {
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
        tablaPeliculas.setSelectionForeground(Color.WHITE);
        tablaPeliculas.setRowHeight(100);
        tablaPeliculas.getColumnModel().getColumn(0).setPreferredWidth(57);
        tablaPeliculas.getColumnModel().getColumn(1).setPreferredWidth(298);
        tablaPeliculas.getColumnModel().getColumn(2).setPreferredWidth(200);
        tablaPeliculas.getColumnModel().getColumn(3).setPreferredWidth(45);
        tablaPeliculas.getColumnModel().getColumn(4).setPreferredWidth(45);
        tablaPeliculas.getColumnModel().getColumn(5).setPreferredWidth(45);
        tablaPeliculas.getColumnModel().getColumn(6).setPreferredWidth(45);
        tablaPeliculas.getColumnModel().getColumn(7).setPreferredWidth(45);
        tablaPeliculas.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());

        JScrollPane panelScroll = new JScrollPane(tablaPeliculas);
        panelScroll.setLocation(10,100);
        panelScroll.setBounds(10,100,780,460);
        panelScroll.setPreferredSize(new Dimension(780,460));

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
            }
        });

        //cambio a menu inicial
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlBuscaInicio.setVisible(true);
                pnlBusquedas.setVisible(false);
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
                        JOptionPane.showMessageDialog(Ventana.this, "No hay películas disponibles que coincidan con su búsqueda");
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
        panelLogin.add(btnLoginBG);
        panelLogin.add(btnReg);
        panelLogin.add(btnRegBG);
        panelLogin.add(btnCancelarReg);
        panelLogin.add(btnCanRegBG);
        panelLogin.add(lblRegistro);
        panelLogin.add(fotoInicio);
        pnlBusquedas.add(btnBuscaF);
        pnlBusquedas.add(btnBuscaFBG);
        pnlBusquedas.add(btnBuscaN);
        pnlBusquedas.add(btnBuscaNBG);
        pnlBusquedas.add(btnCatalogo);
        pnlBusquedas.add(btnCatalogoBG);
        pnlBusquedas.add(txtPelicula);
        pnlBusquedas.add(btnVolver);
        pnlBusquedas.add(btnVolverBG);
        pnlBusquedas.add(btnCambio);
        pnlBusquedas.add(btnCambioBG);
        pnlBuscaInicio.add(btnFiltro);
        pnlBuscaInicio.add(btnFiltroBG);
        pnlBuscaInicio.add(btnBusqueda);
        pnlBuscaInicio.add(btnBusqBG);
        panelBuscador.add(panelScroll);
        panelBuscador.add(pnlBuscaInicio);
        panelBuscador.add(pnlBusquedas);
        pnlBusquedas.setVisible(false);
        panelLayer.add(panelTranslucid);
        panelLayer.add(panelServicios);
        panelLayer.moveToFront(panelServicios);
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
        modelo.addRow(new Object[]{portada,titulo,director,netflix,hbo,prime,disney,crunchy});
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
        btnFiltroBG.setEnabled(false);
        btnFiltroBG.setOpaque(false);
        btnBusqueda.setEnabled(false);
        btnBusqBG.setEnabled(false);
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
        btnFiltroBG.setEnabled(true);
        btnFiltroBG.setOpaque(true);
        btnBusqueda.setEnabled(true);
        btnBusqBG.setEnabled(true);
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

    private void addBoton(JButton btn, JPanel fondo, int x, int y, int w, int h, Font fuente){
        btn.setBounds(x,y,w,h);
        fondo.setBounds(x,y,w,h);
        fondo.setBackground(moradoClaro);
        btn.setFont(fuente);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setMargin(new Insets(0,-12,0,-12));
    }

    /**
     * Método para añadir imagenes
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
        JLabel label1 = new JLabel(new ImageIcon(image),
                JLabel.CENTER);
        label1.setBorder(headerBorder);
        //set the renderer
        TableCellRenderer renderer = new JTableCellRenderer();
        //define column model
        TableColumnModel model = tablaPeliculas.getColumnModel();
        TableColumn col = model.getColumn(index);
        col.setHeaderRenderer(renderer);
        col.setHeaderValue(label1);
    }

    /**
     * Creado a partir de la clase descrita aquí: https://mdsaputra.wordpress.com/2011/06/13/swing-hack-show-image-in-jtable/
     * Clase usada como CellRenderer para mostrar imágenes en la celda, convirtiendo la ruta de cada imagen (String) a ImageIcon
     */
    private class ImageRenderer extends DefaultTableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected,boolean hasFocus, int row, int column)
        {
            this.setHorizontalAlignment(JLabel.CENTER);
            setIcon((Icon)value);
            return this;
        }
    }
}
