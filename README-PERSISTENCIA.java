/*VAMOS A LISTAR EL CURSO DE ACCIÓN PARA PERSISTIR EN UN ARRAY:*/

//1) CREAMOS NUESTRA CARPETA 'persistencia'
//	a) Dentro creamos la interfaz PersistenciaModel
//	que servirá como tipo de dato para TODOS NUESTROS OBJETOS


		package persistencia;
		public interface PersistenciaModel {}

// 2) CREAMOS EN LA CARPETA 'persistencia' LA INTERFAZ PersistenciaDAO
// que será implementada luego por la clase OBJETO (Mascota) con el nombre array en una carpeta llamada array (MascotaArray).


	package persistencia;

	import excepciones.DataAccessException;
	import excepciones.EncontradoException;
	import excepciones.NoEncontradoException;
	import java.util.Collection;

	public interface PersistenciaDAO {
    	public void eliminar(String pk) throws DataAccessException, 	NoEncontradoException;
    	public PersistenciaModel buscarPorClavePrimaria(String pk) throws DataAccessException, NoEncontradoException;
    	public Collection buscarTodos() throws DataAccessException;
    	public void insertar(PersistenciaModel objetoAPersistir) throws DataAccessException, EncontradoException;
    	public void actualizar(PersistenciaModel objetoAPersistir) throws DataAccessException,NoEncontradoException;
}


// 3) El tercer archivo que crearemos también irá dentro de la carpeta 'persistencia'.
// Y se llamará "BaseDeDatosArray". Dentro de ella crearemos una función
// que devolverá una Lista de Listas (cada una de ellas representará a un
// objeto de nuestro modelado. En este caso Mascota).

	package persistencia;

	import java.util.ArrayList;
	import java.util.List;

	public class BaseDeDatosArray {    
    	protected static List<ArrayList<PersistenciaModel>> instance = null; 
    	public static  ArrayList<PersistenciaModel> getInstance(int indice) {
        if (instance == null) {
            instance = new ArrayList<ArrayList<PersistenciaModel>>();
            instance.add(new ArrayList<PersistenciaModel>());
            instance.add(new ArrayList<PersistenciaModel>());
            instance.add(new ArrayList<PersistenciaModel>());
        }                
        return instance.get(indice);
    }
    public enum Tablas
    {
        CLIENTE, PRODUCTO,CONDICION_IVA
    }    
}



// 4) DENTRO DE LA CARPETA 'array', que estará dentro de 'persistencia',
// colocaremos cada clase que represente a nuestro modelado seguido del
// nombre array: MascotaArray. Esta implementará la interfaz PersistenciaDAO
// que creamos en el punto 2


	package persistencia.array;

	import excepciones.DataAccessException;
	import excepciones.EncontradoException;
	import excepciones.NoEncontradoException;
	import java.util.ArrayList;
	import java.util.Collection;
	import modelo.Cliente;
	import persistencia.BaseDeDatosArray;
	import persistencia.PersistenciaDAO;
	import persistencia.PersistenciaModel;

	public class ClienteArray implements PersistenciaDAO {
    @Override
    public void eliminar(String pk) throws DataAccessException, NoEncontradoException {
        Cliente cliente = (Cliente)buscarPorClavePrimaria(pk);
        ArrayList<PersistenciaModel> base = BaseDeDatosArray.getInstance(BaseDeDatosArray.Tablas.CLIENTE.ordinal());
        base.remove(cliente);

    }
    	public boolean existeCliente(String pk) {
        ArrayList<PersistenciaModel> base = BaseDeDatosArray.getInstance(BaseDeDatosArray.Tablas.CLIENTE.ordinal());
        for (PersistenciaModel var : base) {
            Cliente cliente = (Cliente)var;
            if (cliente.getCuil().equals(pk)) {
                return true;
            }
        }
        return false;
    }    
    @Override
    	public PersistenciaModel buscarPorClavePrimaria(String pk) throws DataAccessException, NoEncontradoException {
        ArrayList<PersistenciaModel> base = BaseDeDatosArray.getInstance(BaseDeDatosArray.Tablas.CLIENTE.ordinal());
        Cliente cliente = null;
            
        for (PersistenciaModel var : base) {
            Cliente clienteBase = (Cliente)var;
            if (clienteBase.getCuil().equals(pk)) {
                cliente = clienteBase;
                break;
            }
        }
        if (cliente == null) {
            throw new NoEncontradoException("No se encuentra el Cliente");
        }                
        return cliente;
    }
    @Override
    	public Collection buscarTodos() throws DataAccessException{       
        ArrayList<PersistenciaModel> base = BaseDeDatosArray.getInstance(BaseDeDatosArray.Tablas.CLIENTE.ordinal());
        return base;
    }        
    @Override
    	public void insertar(PersistenciaModel objetoAPersistir) throws DataAccessException, EncontradoException {
        Cliente cliente=(Cliente)objetoAPersistir;
        if (existeCliente(cliente.getCuil())==false) {
            ArrayList<PersistenciaModel> base = BaseDeDatosArray.getInstance(BaseDeDatosArray.Tablas.CLIENTE.ordinal());
            base.add(cliente);            
        } else {
            throw new EncontradoException("Cliente existente en ClienteDAO");
        }
    }
    @Override
    	public void actualizar(PersistenciaModel objetoAPersistir) throws DataAccessException,NoEncontradoException {
        Cliente cliente=(Cliente)objetoAPersistir;
        Cliente clienteEncontrado = (Cliente)buscarPorClavePrimaria(cliente.getCuil());
        ArrayList<PersistenciaModel> base = BaseDeDatosArray.getInstance(BaseDeDatosArray.Tablas.CLIENTE.ordinal());
        base.remove(clienteEncontrado);        
        base.add(cliente);
    }    
}




// 5) Ahora trabajaremos con quienes se sirven de esta base de datos: los REPOSITORIOS, que son las colecciones de objetos. Pero antes
//  debemos crear el modelo del comercio, que instansciará todo:


// (Te preguntarás de dónde sale ClienteDAO y todos esos archivos. En realidad no son clases, al menos no con ese nombre
// son nombres que referencian a clases. Puntualmente las clases Array que creamos dentro de persistencia. Pero que
// a través de un archivo llamado config.properties en el src declaramos:

ClienteDAO = persistencia.array.ClienteArray
CondicionIVADAO = persistencia.array.CondicionIVAArray
ProductoDAO = persistencia.array.ProductoArray

//)

//FIN DE NOTA 

package modelo;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.PersistenciaDAO;
import repositorio.CatalogoProducto;
import repositorio.FicheroCliente;

public class Comercio {

    private CatalogoProducto catalogoProducto;
    private FicheroCliente ficheroCliente;  
    
    private final Properties configuracion;
    
    private static Comercio comercio;
    
    private Comercio(Properties prop){       
        this.configuracion = prop;
    }
   
    public static Comercio instancia(Properties prop){
        if (comercio==null){
            comercio=new Comercio(prop);
            comercio.catalogoProducto = new CatalogoProducto(comercio);
            comercio.ficheroCliente = new FicheroCliente(comercio);
        }
        return comercio;
    }

    public static Comercio instancia(){
        return comercio;
    }

    
    public FicheroCliente getFicheroCliente() {
        return ficheroCliente;
    }
    
    public CatalogoProducto getCatalogoProducto() {
        return catalogoProducto;
    }

    public PersistenciaDAO getClienteDAO() {
        return getModelDAO("ClienteDAO");
    }

    public PersistenciaDAO getCondicionIVADAO() {
        return getModelDAO("CondicionIVADAO");
    }

    public PersistenciaDAO getProductoDAO() {
        return getModelDAO("ProductoDAO");

    }

    public PersistenciaDAO getModelDAO(String clase) {
        String nombreClase = configuracion.getProperty(clase);
        PersistenciaDAO modelo = null;
        try {
            modelo = (PersistenciaDAO)Class.forName(nombreClase).getConstructor().newInstance();
        } catch (Exception ex) {
            Logger.getLogger(Comercio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return modelo;
    }
    
}




// 6) OFICIALMENTE PASAMOS A LOS REPOSITORIOS, NUESTRAS COLECCIONES. 
// Donde crearemos el archivo respectivo para la colección. Que reccibe
// nuestro comercio:


package repositorio;

import excepciones.DataAccessException;
import excepciones.EncontradoException;
import excepciones.NoEncontradoException;
import java.util.ArrayList;
import modelo.Cliente;
import modelo.Comercio;
import modelo.CondicionIVA;
import persistencia.PersistenciaDAO;

public class FicheroCliente {

    Comercio comercio;
    
    private PersistenciaDAO clienteDAO;
    private PersistenciaDAO condicionIVADAO;
    
    public FicheroCliente(Comercio comercio){
        this.comercio=comercio;
        this.clienteDAO = comercio.getClienteDAO();
        this.condicionIVADAO = comercio.getCondicionIVADAO();
    }
    
    
    public void agregarCliente(Cliente cliente) throws DataAccessException, EncontradoException{
        clienteDAO.insertar(cliente);
    }
    public void agregarCondicionIva(CondicionIVA condicionIva) throws DataAccessException, EncontradoException{
        condicionIVADAO.insertar(condicionIva);
    }
    public void removerCondicionIva(String condicion) throws DataAccessException, NoEncontradoException{
        condicionIVADAO.eliminar(condicion);
    }
    
    public ArrayList<Cliente> clientes() throws DataAccessException{
        return (ArrayList)clienteDAO.buscarTodos();
    }
    public ArrayList<CondicionIVA> condicionesIVA() throws DataAccessException{
        return (ArrayList)condicionIVADAO.buscarTodos();
    }
    
}

// 7) FINALMENTE NUESTRO MAIN CARGA TODO:

package comercio;

import ui.Principal;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.JFrame;
import modelo.Comercio;

public class Main {

    public static void main(String[] args) {
        
        String propertiesFilename = "config.properties"; 
        Properties prop = loadProperties(propertiesFilename);
        Comercio comercio = Comercio.instancia(prop);
        
        JFrame aplicacion = new Principal("Gestion de Comercio");
        aplicacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        
        Dimension dim_pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dim_cuadro = aplicacion.getSize();
        aplicacion.setLocation((dim_pantalla.width-dim_cuadro.width)/2,(dim_pantalla.height-dim_cuadro.height)/2);        
        
        aplicacion.setVisible(true); 
        
    }
    
   private static Properties loadProperties(String propertiesFilename)
    {
        Properties prop = new Properties();
        ClassLoader loader = Main.class.getClassLoader(); 
        try (InputStream stream = loader.getResourceAsStream(propertiesFilename))
        {
            if (stream == null) {
                throw new FileNotFoundException();
            }
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }    
}

