package edu.eci.cvds.sampleprj.dao;

import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import jdk.internal.ref.Cleaner;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

public interface ClienteDAO {

    public void save(Cliente cli) throws PersistenceException;

    public Cliente load(long documento) throws PersistenceException;

    public List<Cliente> loadAll() throws PersistenceException;

}