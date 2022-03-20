package edu.eci.cvds.test;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import com.google.inject.Inject;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.entities.TipoItem;
import edu.eci.cvds.samples.services.ServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquilerFactory;
import edu.eci.cvds.samples.services.excepciones.ExcepcionServiciosAlquiler;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.*;

public class ServiciosAlquilerTest {

    @Inject
    private SqlSession sqlSession;

    ServiciosAlquiler serviciosAlquiler;

    public ServiciosAlquilerTest() {
        serviciosAlquiler = ServiciosAlquilerFactory.getInstance().getServiciosAlquilerTesting();
    }

    @Before
    public void setUp() {
    }

    @Test
    public void emptyDB() {
        for(int i = 0; i < 100; i += 10) {
            boolean r = false;
            try {
                long documento = 100;
                Cliente cliente = serviciosAlquiler.consultarCliente(documento);
            } catch(ExcepcionServiciosAlquiler e) {
                r = true;
            } catch(IndexOutOfBoundsException e) {
                r = true;
            }
            // Validate no Client was found;
            Assert.assertTrue(r);
        };
    }

    @Test
    public void deberiaConsultarCliente(){
        try {
            //Agregamos un cliente
            ArrayList<ItemRentado> itemRentados = new ArrayList<ItemRentado>();
            Cliente cliente = new Cliente("Prueba",11,"telefono","direccion","email",false,itemRentados);
            serviciosAlquiler.registrarCliente(cliente);
            //Consultamos el cliente
            assertEquals("Prueba", serviciosAlquiler.consultarCliente(11).getNombre());
        } catch (Exception e) {
            fail("Error. Consulto mal cliente");
        }
    }

    @Test
    public void deberiaConsultarItem(){
        try {
            //Agregamos Item
            Item item = new Item(new TipoItem(1, "Prueba" ),1,
                    "Prueba", "Prueba Test", new SimpleDateFormat("yyyy/MM/dd").parse("2020/12/01"),
                    100,"Prueba","Prueba");
            serviciosAlquiler.registrarItem(item);
            //Consultamos Item
            assertEquals(1, serviciosAlquiler.consultarItem(1).getId());
        } catch (Exception e) {
            fail("Error. Consulto mal item");
        }
    }

    @Test
    public void deberiaConsultarTipoItem(){
        try {
            //Agregamos Item
            Item item = new Item(new TipoItem(55, "Prueba2" ),1,
                    "Prueba2", "Prueba Test2", new SimpleDateFormat("yyyy/MM/dd").parse("2021/12/01"),
                    100,"Prueba2","Prueba2");
            serviciosAlquiler.registrarItem(item);
            //Consultamos Tipo Item del Item agregado
            assertEquals(55, serviciosAlquiler.consultarItem(1).getTipo());
        } catch (Exception e) {
            fail("Error. Consulto mal Tipo item");
        }
    }

    @Test
    public void deberiaRegistrarCliente(){
        try {
            //Registramos un cliente
            ArrayList<ItemRentado> itemRentados = new ArrayList<ItemRentado>();
            Cliente cliente = new Cliente("Prueba3",11,"telefono","direccion","email",false,itemRentados);
            serviciosAlquiler.registrarCliente(cliente);
            //Consultamos el cliente Registrado
            assertEquals("Prueba3", serviciosAlquiler.consultarCliente(11).getNombre());
        } catch (Exception e) {
            fail("Error. Registro mal el cliente");
        }
    }

    @Test
    public void deberiRegistrarItem() throws ExcepcionServiciosAlquiler {
        try {
            //Agregamos Item
            Item item = new Item(new TipoItem(10, "Prueba4" ),1010,
                    "Prueba4", "Prueba4", new SimpleDateFormat("yyyy/MM/dd").parse("2023/10/04"),
                    100,"Prueba4","Prueba4");
            serviciosAlquiler.registrarItem(item);
            //Consultamos Item Agregado
            assertEquals(10, serviciosAlquiler.consultarItem(10).getId());
        } catch (Exception e) {
            fail("Error. Registro mal el Item");
        }
    }

    @Test
    public void deberiaActualizarTarifaItem(){
        try {
            Item item = new Item(new TipoItem(1, "prueba5" ),2,
                    "prueba5", "prueba5", new SimpleDateFormat("yyyy/MM/dd").parse("2027/11/4"),
                    100,"prueba5","prueba5");
            serviciosAlquiler.registrarItem(item);
            serviciosAlquiler.actualizarTarifaItem(2, 150);
            assertEquals(150, serviciosAlquiler.consultarItem(2).getTarifaxDia());
        } catch (Exception e) {
            fail("Error. Actualizo mal la tarifa del Item");
        }
    }

    @Test
    public void deberiaVetarCliente(){
        try {
            //Registramos un cliente
            ArrayList<ItemRentado> itemRentados = new ArrayList<ItemRentado>();
            Cliente cliente = new Cliente("Prueba6",111,"telefono","direccion","email",false,itemRentados);
            serviciosAlquiler.registrarCliente(cliente);
            //Vetamos cliente
            serviciosAlquiler.vetarCliente(111,true);
            //Consultamos que se halla vetado
            assertEquals(true, serviciosAlquiler.consultarCliente(111).isVetado());
        } catch (Exception e) {
            fail("Error. Actualizo mal la tarifa del Item");
        }
    }

    @Test
    public void deberiaConsultarCostoAlquiler() throws ExcepcionServiciosAlquiler{
        try {
            //Agregamos Item
            Item it = new Item(new TipoItem(1, "prueba"), 2222,
                    "prueba", "prueba", new SimpleDateFormat("yyyy/MM/dd").parse("2019/09/28"),
                    65, "prueba", "prueba");
            serviciosAlquiler.registrarItem(it);
            //Consultamos costo del Alquiler
            assertEquals(195, serviciosAlquiler.consultarCostoAlquiler(2222,3));
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void noDeberiaRegistrarAlquilerDeClienteQueNoExiste(){
        try {
            Item item = new Item(new TipoItem(8, "prueba" ),10,
                    "prueba", "prueba", new SimpleDateFormat("yyyy/MM/dd").parse("2020/10/04"),
                    100,"prueba","prueba");
            serviciosAlquiler.registrarAlquilerCliente(Date.valueOf(LocalDate.parse("2020-10-04")) , serviciosAlquiler.consultarCliente(98989898).getDocumento() , item , 4 );
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void deberiaConsultarValorMultaRetrasoPorDia() throws ParseException, ExcepcionServiciosAlquiler {
        try {
            /* Cliente cliente = new Cliente("Nombre",111,"telefono","direccion","email",false,null); */
            Item item = new Item(new TipoItem(1, "prueba" ),6,
                    "prueba", "prueba Pa", new SimpleDateFormat("yyyy/MM/dd").parse("2020/10/4"),
                    100,"prueba","prueba");
            serviciosAlquiler.registrarItem(item);
            //Consultamos valor
            assertEquals(100, serviciosAlquiler.valorMultaRetrasoxDia(6));
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void noDeberiaConsultarElCostoDeUnItemDesconocido() throws ExcepcionServiciosAlquiler{
        try {
            assertEquals(101010101, serviciosAlquiler.consultarCostoAlquiler(91919191, 818181));
            fail("No entro a la excepcion");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void deberiaDeConsultarElCostoDeUnItemAgregado() throws ExcepcionServiciosAlquiler{
        try {
            //Registramos el Item
            Item item = new Item(new TipoItem(1, "prueba" ),6,
                    "prueba", "prueba Pa", new SimpleDateFormat("yyyy/MM/dd").parse("2020/10/4"),
                    100,"prueba","prueba");
            serviciosAlquiler.registrarItem(item);
            //Verificamos el costo
            assertEquals(100*5, serviciosAlquiler.consultarCostoAlquiler(6, 5));
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}