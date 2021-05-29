/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Model.Properties;
import org.springframework.stereotype.Service;
import Repository.ProptypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import Model.Propertytypes;
import java.util.List;
/**
 *
 * @author Andrew
 */
@Service
public class ProptypeService {
    @Autowired
    private ProptypeRepo proptypeRepo;

    public Propertytypes findOne(Long id) {
        return proptypeRepo.findById(id).get();
    }

    public List<Propertytypes> findAll() {
        return (List<Propertytypes>) proptypeRepo.findAll();
    }

    public long count() {
        return proptypeRepo.count();
    }

    public void deleteByID(long authorID) {
        proptypeRepo.deleteById(authorID);
    }

    public void saveAuthor(Propertytypes a) {
        proptypeRepo.save(a);
    }  
    
    public Propertytypes findName(String id) {
        return proptypeRepo.getname(id);
    }
}
