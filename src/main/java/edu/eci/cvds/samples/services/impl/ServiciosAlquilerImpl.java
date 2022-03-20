package edu.eci.cvds.samples.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.eci.cvds.sampleprj.dao.ClienteDAO;
import edu.eci.cvds.sampleprj.dao.ItemDAO;

import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.entities.TipoItem;
import edu.eci.cvds.samples.services.ServiciosAlquiler;
import edu.eci.cvds.samples.services.excepciones.ExcepcionServiciosAlquiler;
import org.apache.ibatis.exceptions.PersistenceException;

import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ServiciosAlquilerImpl implements ServiciosAlquiler {

   @Inject
   private ItemDAO itemDAO;

   @Inject
   private ClienteDAO clienteDAO;

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
       throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public List<TipoItem> consultarTiposItem() throws ExcepcionServiciosAlquiler {
       throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void registrarAlquilerCliente(Date date, long docu, Item item, int numdias) throws ExcepcionServiciosAlquiler {
       throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void registrarCliente(Cliente c) throws ExcepcionServiciosAlquiler {
       throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public long consultarCostoAlquiler(int iditem, int numdias) throws ExcepcionServiciosAlquiler {
       throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void actualizarTarifaItem(int id, long tarifa) throws ExcepcionServiciosAlquiler {
       throw new UnsupportedOperationException("Not supported yet.");
   }
   @Override
   public void registrarItem(Item i) throws ExcepcionServiciosAlquiler {
       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void vetarCliente(long docu, boolean estado) throws ExcepcionServiciosAlquiler {
       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
}