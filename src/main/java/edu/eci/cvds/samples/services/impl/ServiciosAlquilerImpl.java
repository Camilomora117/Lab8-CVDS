package edu.eci.cvds.samples.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.eci.cvds.sampleprj.dao.ClienteDAO;
import edu.eci.cvds.sampleprj.dao.ItemDAO;

import edu.eci.cvds.sampleprj.dao.TipoItemDAO;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.entities.TipoItem;
import edu.eci.cvds.samples.services.ServiciosAlquiler;
import edu.eci.cvds.samples.services.excepciones.ExcepcionServiciosAlquiler;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.guice.transactional.Transactional;

import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Singleton
public class ServiciosAlquilerImpl implements ServiciosAlquiler {

   @Inject
   private ItemDAO itemDAO;

   @Inject
   private ClienteDAO clienteDAO;

   @Inject
   private TipoItemDAO tipoItemDAO;

   @Override
   public int valorMultaRetrasoxDia(int itemId) throws ExcepcionServiciosAlquiler {
       try {
           return (int) itemDAO.load(itemId).getTarifaxDia();
       }
       catch (PersistenceException ex){
           throw new ExcepcionServiciosAlquiler("Error al consultar el valor de multa del item"+itemId);
       }
   }

   @Override
   public Cliente consultarCliente(long docu) throws ExcepcionServiciosAlquiler {
       try{
           return clienteDAO.load(docu);
       }
       catch (PersistenceException ex) {
           throw new ExcepcionServiciosAlquiler("Error al consultar el cliente"+docu);
       }
   }

   @Override
   public List<ItemRentado> consultarItemsCliente(long idcliente) throws ExcepcionServiciosAlquiler {
       try{
           return clienteDAO.load(idcliente).getRentados();
       }
       catch (PersistenceException ex) {
           throw new ExcepcionServiciosAlquiler("Error al consultar los items rentados de el cliente"+idcliente);
       }
   }

   @Override
   public List<Cliente> consultarClientes() throws ExcepcionServiciosAlquiler {
       try{
           return clienteDAO.loadAll();
       }
       catch (PersistenceException ex) {
           throw new ExcepcionServiciosAlquiler("Error al consultar los clientes");
       }
   }

   @Override
   public Item consultarItem(int id) throws ExcepcionServiciosAlquiler {
       try {
           return itemDAO.load(id);
       } catch (PersistenceException ex) {
           throw new ExcepcionServiciosAlquiler("Error al consultar el item "+id);
       }
   }

   @Override
   public List<Item> consultarItemsDisponibles() throws ExcepcionServiciosAlquiler {
       try {
           return itemDAO.loadAll();
       } catch (PersistenceException ex) {
           throw new ExcepcionServiciosAlquiler("Error al consultar los items");
       }
   }

   @Override
   public long consultarMultaAlquiler(int iditem, Date fechaDevolucion) throws ExcepcionServiciosAlquiler {
       try {
           List<Cliente> clientes = consultarClientes();
           for (int i=0 ; i<clientes.size() ; i++) {
               ArrayList<ItemRentado> rentados = clientes.get(i).getRentados();
               for (int j=0 ; j<rentados.size() ; j++) {
                   if(null != rentados.get(j).getItem()){
                       if (rentados.get(j).getItem().getId() == iditem) {
                           long diasRetraso = ChronoUnit.DAYS.between(rentados.get(j).getFechafinrenta().toLocalDate(), fechaDevolucion.toLocalDate());
                           if (diasRetraso < 0) {
                               return 0;
                           }
                           return diasRetraso * valorMultaRetrasoxDia(rentados.get(j).getId());
                       }
                   }
               }
           }
       } catch (Exception e) {
           throw  new ExcepcionServiciosAlquiler("Error al consultar multa de item con id: "+iditem);
       }
       return iditem;
   }

   @Override
   public TipoItem consultarTipoItem(int id) throws ExcepcionServiciosAlquiler {
       try{
           return tipoItemDAO.load(id);
       }
       catch(Exception e){
           throw new UnsupportedOperationException("Error al consultar el Tipo Item con id: "+id);
       }
   }

   @Override
   public List<TipoItem> consultarTiposItem() throws ExcepcionServiciosAlquiler {
       try{
           return tipoItemDAO.loadAll();
       } catch(Exception e){
           throw new UnsupportedOperationException("Error al consultar los Tipos Items");
       }
   }

   @Override
   public void registrarAlquilerCliente(Date date, long docu, Item item, int numdias) throws ExcepcionServiciosAlquiler {
       try{
           if(clienteDAO.load(docu)==null){
               throw new ExcepcionServiciosAlquiler("El cliente es null Pa") ;
           }
           Calendar calendar = Calendar.getInstance();
           calendar.setTime(date);
           calendar.add(Calendar.DAY_OF_YEAR, numdias);
           clienteDAO.saveItemRentadoCliente(docu,item.getId(),date,new java.sql.Date(calendar.getTime().getTime()));
       }  catch (PersistenceException ex) {
           throw new ExcepcionServiciosAlquiler("Error al agregar item rentado al cliente con id: " + docu);
       }
   }

   @Transactional
   @Override
   public void registrarCliente(Cliente c) throws ExcepcionServiciosAlquiler {
       try {
           clienteDAO.save(c);
       } catch (Exception e) {
           throw new UnsupportedOperationException(e.toString());
       }
   }

   @Override
   public long consultarCostoAlquiler(int iditem, int numdias) throws ExcepcionServiciosAlquiler {
       try {
           return numdias * itemDAO.load(iditem).getTarifaxDia();
       } catch (Exception e) {
           throw new UnsupportedOperationException("Error al consultar costo alquiler del item con id: "+iditem);
       }
   }

   @Transactional
   @Override
   public void actualizarTarifaItem(int id, long tarifa) throws ExcepcionServiciosAlquiler {
       try {
           itemDAO.actualizarTarifaItem(id, tarifa);
       } catch (Exception e) {
           throw new UnsupportedOperationException("Error al actualizar tarifa item");
       }
   }
   @Transactional
   @Override
   public void registrarItem(Item i) throws ExcepcionServiciosAlquiler {
       try {
           itemDAO.save(i);
       } catch (Exception e) {
           throw new UnsupportedOperationException("Error al registrar el Item");
       }
   }

   @Transactional
   @Override
   public void vetarCliente(long docu, boolean estado) throws ExcepcionServiciosAlquiler {
       try {
           int estadoInt;
           if(clienteDAO.load(docu)==null){
               throw new UnsupportedOperationException("Cliente nulo");
           }
           if (estado){ estadoInt = 1; } else estadoInt = 0;
           clienteDAO.vetarCliente((int) docu, estadoInt);
       } catch (Exception e) {
           throw new UnsupportedOperationException("No se pudo cambiar el estado de el cliente"+docu+" a "+estado);
       }
   }
}