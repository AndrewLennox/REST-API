/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import Model.Properties;
import org.springframework.data.repository.CrudRepository;
import Model.Propertytypes;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 *
 * @author Andrew
 */
@Repository
public interface ProptypeRepo extends CrudRepository<Propertytypes, Long> {
    @Query("SELECT b FROM Propertytypes b WHERE b.p_Type LIKE :searchterm")
public Propertytypes getname(String searchterm);
}
