/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Receta;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
@LocalBean
public class RecetaDAO extends GenericoDAO<Receta>{
 
     public List<Receta> buscarPorNTS(String numeroTarjetaSanitaria){
        Query q = em.createQuery("SELECT receta FROM Receta AS receta "
                + "  WHERE (receta.prescripcion.tratamiento.paciente.numeroTarjetaSanitaria = :numeroTarjetaSanitaria)");
        q.setParameter("numeroTarjetaSanitaria",numeroTarjetaSanitaria);
                
        return q.getResultList();
    }
    // Completar aqui
}
