/*
 Proyecto Java EE, DAGSS-2014
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
@LocalBean
public class MedicamentoDAO extends GenericoDAO<Medicamento> {
    
    public List<Medicamento> buscarPorPrincipioActivo(String textoBusqueda){
        Query q= em.createQuery("SELECT m FROM Medicamento AS m WHERE m.principioActivo LIKE :patron");
        q.setParameter("patron","%"+textoBusqueda+"%");
        return q.getResultList();
    }
    public List<Medicamento> buscarPorNombre(String textoBusqueda){
        Query q= em.createQuery("SELECT m FROM Medicamento AS m WHERE m.nombre LIKE :patron");
        q.setParameter("patron","%"+textoBusqueda+"%");
        return q.getResultList();
    }
    
    // Completar aqui
}
