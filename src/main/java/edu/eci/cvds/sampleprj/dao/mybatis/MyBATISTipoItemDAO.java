package edu.eci.cvds.sampleprj.dao.mybatis;

import com.google.inject.Inject;
import edu.eci.cvds.sampleprj.dao.TipoItemDAO;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.TipoItemMapper;
import edu.eci.cvds.samples.entities.TipoItem;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

public class MyBATISTipoItemDAO implements TipoItemDAO {

    @Inject
    private TipoItemMapper tipoItemMapper;


    @Override
    public void save(TipoItem tit) throws PersistenceException {
        try{
            String descripcion = tit.getDescripcion();
            tipoItemMapper.addTipoItem(descripcion);
        }
        catch(org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al registrar el tipo item "+tit,e);
        }
    }

    @Override
    public TipoItem load(int tit) throws PersistenceException {
        return tipoItemMapper.getTipoItem(tit);
    }

    @Override
    public List<TipoItem> loadAll() throws PersistenceException {
        return tipoItemMapper.getTiposItems();
    }
}