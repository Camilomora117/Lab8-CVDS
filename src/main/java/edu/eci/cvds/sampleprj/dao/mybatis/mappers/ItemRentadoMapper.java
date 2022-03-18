package edu.eci.cvds.sampleprj.dao.mybatis.mappers;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.ItemRentado;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

public interface ItemRentadoMapper {

    /**
     * Consulta un item rentado
     * @param id Id del Item
     * @return ItemRentado
     */
    public List<ItemRentado> consultarItemRentado(@Param("idcli") int id, @Param("iditm") int itm);

}
