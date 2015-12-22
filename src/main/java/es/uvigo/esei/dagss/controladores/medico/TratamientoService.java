/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.CitaDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicamentoDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicoDAO;
import es.uvigo.esei.dagss.dominio.daos.RecetaDAO;
import es.uvigo.esei.dagss.dominio.daos.TratamientoDAO;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.EstadoCita;
import es.uvigo.esei.dagss.dominio.entidades.EstadoReceta;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Receta;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import es.uvigo.esei.dagss.dominio.entidades.Tratamiento;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Gabriel
 */
@Named(value = "tratamientoService")
@Singleton
public class TratamientoService implements Serializable {

   @EJB
   TratamientoDAO tratamientoDAO;
   
   @EJB
   RecetaDAO recetaDAO;
    
    public TratamientoService() {       
    }
    
     public void doAddTratamiento(Medicamento medicamento, Cita citaActual,Medico medicoActual, Date fechaInicio, Date fechaFin,String comentario, int dosis){
        //System.out.println(medicamento.toString());
        Tratamiento t=new Tratamiento(citaActual.getPaciente(), medicoActual, "not used", fechaInicio, fechaFin);
        Prescripcion p=new Prescripcion(comentario, t, medicamento, dosis);
        //aux.setPrescipciones(Arrays.asList(p));
    t.getPrescipciones().add(p);
    int cantidadBote= medicamento.getNumeroDosis();
         //System.out.println(cantidadBote);
         LocalDate initdate = fechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
         LocalDate finishdate = fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
         Period duracion= Period.between(initdate, finishdate);
         float numTotalDosis=(duracion.getDays()*dosis);
         int numRecetas=(int) Math.ceil(numTotalDosis / medicamento.getNumeroDosis() );
         System.out.println(numRecetas);
//Receta r =new Receta(p, Integer cantidad, fechaInicio, fechaFin, EstadoReceta.GENERADA);
    tratamientoDAO.crear(t); 
    
             Receta receta=new Receta(p, numRecetas, fechaFin, fechaFin, EstadoReceta.GENERADA);
         
     recetaDAO.crear(receta);
     }
         
    
    

}

