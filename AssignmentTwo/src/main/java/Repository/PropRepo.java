/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

/**
 *
 * @author Andrew
 */
import java.util.List;
import Model.Properties;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropRepo extends CrudRepository<Properties, Long> {
    
@Query("SELECT b FROM Properties b WHERE b.agent_Id = :searchterm")
public List<Properties> getAgentsProps(int searchterm);

@Query("SELECT b FROM Properties b WHERE b.type_Id = :searchterm")
public List<Properties> getPTProps(int searchterm);

@Query("SELECT b FROM Properties b WHERE b.bedrooms = :searchterm AND bathrooms = :two")
public List<Properties> BB(int searchterm, float two);
    
}
