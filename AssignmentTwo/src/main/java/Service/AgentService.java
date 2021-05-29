/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Model.Agents;
import Repository.AgentRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Andrew
 */
@Service
public class AgentService {
    
    @Autowired
    private AgentRepo agentRepo;

    public Agents findOne(Long id) {
        return agentRepo.findById(id).get();
    }

    public List<Agents> findAll() {
        return (List<Agents>) agentRepo.findAll();
    }

    public long count() {
        return agentRepo.count();
    }

    public void deleteByID(long authorID) {
        agentRepo.deleteById(authorID);
    }

    public void saveAuthor(Agents a) {
        agentRepo.save(a);
    }  
    
}
