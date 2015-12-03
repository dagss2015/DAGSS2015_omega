/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Cita;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;


@Stateless
@LocalBean
public class CitaDAO  extends GenericoDAO<Cita>{    

    public List<Cita> buscarCitasMedico(long medicoid,Date fecha){
         Query q = em.createQuery("SELECT c FROM Cita AS c "
                + "  WHERE (c.medico.id = :idmedico) AND "
                + "        (c.fecha = :fecha)");
        q.setParameter("idmedico",medicoid);
        q.setParameter("fecha",fecha);        
        return q.getResultList();
        
    }
    // Completar aqui
}
