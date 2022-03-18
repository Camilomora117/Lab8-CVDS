package edu.eci.cvds.sampleprj.dao.mybatis.mappers;


import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import edu.eci.cvds.samples.entities.Item;

/**
 *
 * @author 2106913
 */
public interface ItemMapper {
    
    
    public List<Item> consultarItems();        
    
    public Item consultarItem(@Param("item") int id);
    
    public void insertarItem(@Param("id")int id,
                             @Param("nombre") String nombre,
                             @Param("descripcion") String descripcion,
                             @Param("fechal") Date fechal,
                             @Param("tarifa") int tarifa,
                             @Param("renta") String renta,
                             @Param("genero") String genero,
                             @Param("tipoItem") int tipoItem);

}
