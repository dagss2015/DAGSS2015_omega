/*
 Proyecto Java EE, DAGSS-2013
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.CitaDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicamentoDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicoDAO;
import es.uvigo.esei.dagss.dominio.daos.TratamientoDAO;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.EstadoCita;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import es.uvigo.esei.dagss.dominio.entidades.Tratamiento;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author ribadas
 */

@Named(value = "medicoControlador")
@SessionScoped
public class MedicoControlador implements Serializable {

    private Medico medicoActual;
    private String dni;
    private String numeroColegiado;
    private String password;
    private Cita citaActual;
    private String textoBusqueda;
    private List<Medicamento> medicamentos; 
    private Date fechaInicio;
    private Date fechaFin;
    
    @Inject
    private AutenticacionControlador autenticacionControlador;
    

    @EJB
    private MedicamentoDAO medicamentoDAO;
    @EJB
    private MedicoDAO medicoDAO;

    @EJB
    private TratamientoDAO tratamientoDAO;
    
    @EJB
    private CitaDAO citaDAO;
    /**
     * Creates a new instance of AdministradorControlador
     */
    public MedicoControlador() {
        this.medicamentos= new LinkedList<>();
    }

    public List<Medicamento> getMedicamentos(){
        return medicamentos;
    }
    public Date getFechaInicio(){
        return this.fechaInicio;
    }
    public void setFechaInicio(Date fecha){
        this.fechaInicio=fecha;
    }
     public Date getFechaFin(){
        return this.fechaFin;
    }
    public void setFechaFin(Date fecha){
        this.fechaFin=fecha;
    }
    
    public String getDni() {
        return dni;
    }
    public void setTextoBusqueda(String aux){
        this.textoBusqueda=aux;
    }
    public String getTextoBusqueda(){
        return this.textoBusqueda;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }
    public void doBuscarMedicamento(){
        
        medicamentos= medicamentoDAO.buscarPorPrincipioActivo(textoBusqueda);
        if(medicamentos.isEmpty()){
            medicamentos= medicamentoDAO.buscarPorNombre(textoBusqueda);
        }      
    }
    
    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Medico getMedicoActual() {
        return medicoActual;
    }

    public void setMedicoActual(Medico medicoActual) {
        this.medicoActual = medicoActual;
    }
 public Cita getCitaActual() {
        return citaActual;
    }

    public void setCitaActual(Cita medicoActual) {
        this.citaActual = medicoActual;
    }
    private boolean parametrosAccesoInvalidos() {
        return (((dni == null) && (numeroColegiado == null)) || (password == null));
    }

    private Medico recuperarDatosMedico() {
        Medico medico = null;
        if (dni != null) {
            medico = medicoDAO.buscarPorDNI(dni);
        }
        if ((medico == null) && (numeroColegiado != null)) {
            medico = medicoDAO.buscarPorNumeroColegiado(numeroColegiado);
        }
        return medico;
    }
    public String doCrearTratamiento(){
        //.citaActual=cita;
        
        return "/medico/privado/crearTratamiento";
        /*
        List<Tratamiento> lista=this.tratamientoDAO.getTratamiento(this.citaActual.getPaciente().getId());
        for (Tratamiento tratamiento : lista) {long idMedicamento
            tratamiento.
        }*/
        //return null;
    }
    
    public void doAddTratamiento(){
        Tratamiento aux=new Tratamiento(citaActual.getPaciente(), medicoActual, "comentario", fechaInicio, fechaFin);
         tratamientoDAO.crear(aux);
         
         
          
    }
    public List<Cita> verCitasHoy(){
        String DATE_FORMAT = "yyyyMMdd";
        SimpleDateFormat sdf =new SimpleDateFormat(DATE_FORMAT);
        Date hoy = Calendar.getInstance().getTime();
          return  citaDAO.buscarCitasMedico(medicoActual.getId(),hoy);
    }
    public String doLogin() {
        String destino = null;
        if (parametrosAccesoInvalidos()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha indicado un DNI ó un número de colegiado o una contraseña", ""));
        } else {
            Medico medico = recuperarDatosMedico();
            if (medico == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe ningún médico con los datos indicados", ""));
            } else {
                if (autenticacionControlador.autenticarUsuario(medico.getId(), password,
                        TipoUsuario.MEDICO.getEtiqueta().toLowerCase())) {
                    medicoActual = medico;
                    destino = "privado/index";
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Credenciales de acceso incorrectas", ""));
                }
            }
        }
        return destino;
    }

    //Acciones
    public String doShowCita(Cita cita) {
        this.citaActual=cita;
        String destino = null;
        destino="verCita";
        return destino;
    }
    
    public void marcarAusente(){
        this.citaActual.setEstado(EstadoCita.AUSENTE);
        this.citaActual = this.citaDAO.actualizar(this.citaActual);
    }
    
    public void marcarCompletada(){
        this.citaActual.setEstado(EstadoCita.COMPLETADA);
        this.citaActual =this.citaDAO.actualizar(this.citaActual);
    }
}
