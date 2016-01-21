/*
 Proyecto Java EE, DAGSS-2013
 */
package es.uvigo.esei.dagss.controladores.farmacia;

import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.FarmaciaDAO;
import es.uvigo.esei.dagss.dominio.daos.RecetaDAO;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import static es.uvigo.esei.dagss.dominio.entidades.EstadoReceta.ANULADA;
import static es.uvigo.esei.dagss.dominio.entidades.EstadoReceta.GENERADA;
import static es.uvigo.esei.dagss.dominio.entidades.EstadoReceta.SERVIDA;
import es.uvigo.esei.dagss.dominio.entidades.Farmacia;
import es.uvigo.esei.dagss.dominio.entidades.Receta;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author ribadas
 */
@Named(value = "farmaciaControlador")
@SessionScoped
public class FarmaciaControlador implements Serializable {

    private Receta recetaActual;
    private List<Receta> recetas;
    private Farmacia farmaciaActual;
    private String nif;
    private String password;
    private String textoBusqueda;
    
    @Inject
    private AutenticacionControlador autenticacionControlador;

    @EJB
    private FarmaciaDAO farmaciaDAO;

    @EJB
    private RecetaDAO recetaDAO;
    /**
     * Creates a new instance of AdministradorControlador
     */
    public FarmaciaControlador() {
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }

    public String getTextoBusqueda() {
        return textoBusqueda;
    }

    public void setTextoBusqueda(String textoBusqueda) {
        this.textoBusqueda = textoBusqueda;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Receta getRecetaActual() {
        return recetaActual;
    }

    public void setRecetaActual(Receta recetaActual) {
        this.recetaActual = recetaActual;
    }

    public Farmacia getFarmaciaActual() {
        return farmaciaActual;
    }

    public void setFarmaciaActual(Farmacia farmaciaActual) {
        this.farmaciaActual = farmaciaActual;
    }

    private boolean parametrosAccesoInvalidos() {
        return ((nif == null) || (password == null));
    }

    public void doBuscarReceta(){
        
        recetas= recetaDAO.buscarPorNTS(textoBusqueda);
        List<Receta> aux=new LinkedList();
        for (Receta receta : recetas) {
            if(receta.getFinValidez().after(new Date())){
                aux.add(receta);
                //recetas.remove(receta);
            }
        }
        recetas=aux;
   
        
    }
    public void doSuministrarReceta(){
        
        if((this.recetaActual.getEstado()==GENERADA) && (this.recetaActual.getFinValidez().after(new Date()))){
        this.recetaActual.setEstado(SERVIDA);
        recetaActual=recetaDAO.actualizar(recetaActual);
        }
    }
    public void doAnuladaReceta(){
        this.recetaActual.setEstado(ANULADA);
        recetaActual=recetaDAO.actualizar(recetaActual);
    }
    
    
    public String doShowReceta(Receta receta) {
        this.recetaActual=receta;
        String destino = null;
        destino="verReceta";
        return destino;
    }
    
    public String doLogin() {
        String destino = null;
        if (parametrosAccesoInvalidos()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha indicado un nif o una contraseña", ""));
        } else {
            Farmacia farmacia = farmaciaDAO.buscarPorNIF(nif);
            if (farmacia == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe una farmacia con el NIF " + nif, ""));
            } else {
                if (autenticacionControlador.autenticarUsuario(farmacia.getId(), password,
                        TipoUsuario.FARMACIA.getEtiqueta().toLowerCase())) {
                    farmaciaActual = farmacia;
                    destino = "privado/index";
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Credenciales de acceso incorrectas", ""));
                }

            }
        }
        return destino;
    }
}
