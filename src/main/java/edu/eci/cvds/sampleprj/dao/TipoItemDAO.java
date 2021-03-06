package edu.eci.cvds.sampleprj.dao;

import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.TipoItem;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

public interface TipoItemDAO {

    public void save(TipoItem tit) throws PersistenceException;

    public TipoItem load(int tit) throws PersistenceException;

    public List<TipoItem> loadAll() throws PersistenceException;

}