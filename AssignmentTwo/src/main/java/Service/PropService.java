/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

/**
 *
 * @author Andrew
 */

import Model.Properties;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Repository.PropRepo;


@Service
public class PropService {

    @Autowired
    private PropRepo propRepo;

    public Properties findOne(Long id) {
        return propRepo.findById(id).get();
    }

    public List<Properties> findAll() {
        return (List<Properties>) propRepo.findAll();
    }

    public long count() {
        return propRepo.count();
    }

    public void deleteByID(long authorID) {
        propRepo.deleteById(authorID);
    }

    public void saveAuthor(Properties a) {
        propRepo.save(a);
    }  
    
    public List byAgent(int title) {
        return (List<Properties>) propRepo.getAgentsProps(title);
    }  
    
       public List byType(int title) {
        return (List<Properties>) propRepo.getPTProps(title);
    }  
       
       

       
              public List BB(int title,float bath) {
        return (List<Properties>) propRepo.BB(title,bath);
    }  
}//end class
