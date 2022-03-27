package edu.eci.cvds.sampleprj.dao.mybatis;

import com.google.inject.Inject;
import edu.eci.cvds.sampleprj.dao.ClienteDAO;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ItemMapper;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.TipoItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;

import java.sql.Date;
import java.util.List;

public class MyBATISClienteDAO implements ClienteDAO {

    @Inject
    private ClienteMapper clienteMapper;

    @Override
    public void save(Cliente cli) throws PersistenceException {
        try {
            int id = (int) cli.getDocumento();
            String nombre = cli.getNombre();
            String telefono = cli.getTelefono();
            String direccion = cli.getDireccion();
            String email = cli.getEmail();
            Boolean vetado = cli.isVetado();
            clienteMapper.insertarCliente(id,nombre,telefono,direccion,email,vetado);
        }
        catch(org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al registrar el cliente"+ cli,e);
        }
    }

    @Override
    public Cliente load(long documento) throws PersistenceException {
        try{
            return clienteMapper.consultarCliente(documento);
        }
        catch(org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al consultar el cliente "+documento,e);
        }

    }

    @Override
    public List<Cliente> loadAll() throws PersistenceException {
        try{
            return clienteMapper.consultarClientes();
        }
        catch(org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al consultar los clientes ",e);
        }
    }

    @Override
    public void saveItemRentadoCliente(long docu, int id, Date date, Date date1) {
        try{
            clienteMapper.agregarItemRentadoACliente((int) docu,id,date,date1);
        }
        catch(org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al consultar los clientes ",e);
        }
    }
}