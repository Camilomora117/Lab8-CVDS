package edu.eci.cvds.sampleprj.dao.mybatis.mappers;

import java.util.Date;
import java.util.List;

import edu.eci.cvds.samples.entities.ItemRentado;
import org.apache.ibatis.annotations.Param;

import edu.eci.cvds.samples.entities.Cliente;

/**
 *
 * @author 2106913
 */
public interface ClienteMapper {

    public void insertarCliente(@Param("documento") long id, @Param("nombre") String nombre, @Param("telefono") String telefono
            , @Param("direccion") String direccion,@Param("email") String email, @Param("vetado") Boolean vetado);
    
    public Cliente consultarCliente(@Param("idcli") long id);
    
    /**
     * Registrar un nuevo item rentado asociado al cliente identificado
     * con 'idc' y relacionado con el item identificado con 'idi'
     * @param id
     * @param idit
     * @param fechainicio
     * @param fechafin
     */
    public void agregarItemRentadoACliente(@Param("idcli")int id,
            @Param("idit") int idit,
            @Param("fechai") Date fechainicio,
            @Param("fechaf") Date fechafin);

    /**
     * Consultar todos los clientes
     * @return Clientes
     */
    public List<Cliente> consultarClientes();

    public void vetarCliente(@Param("idcli") int id, @Param("estado") int estado);

}
