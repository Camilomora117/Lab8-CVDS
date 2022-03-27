package edu.eci.cvds.samples.services;

import com.google.inject.Injector;
import edu.eci.cvds.sampleprj.dao.ClienteDAO;
import edu.eci.cvds.sampleprj.dao.ItemDAO;
import edu.eci.cvds.sampleprj.dao.TipoItemDAO;
import edu.eci.cvds.sampleprj.dao.mybatis.MyBATISClienteDAO;
import edu.eci.cvds.sampleprj.dao.mybatis.MyBATISItemDAO;
import edu.eci.cvds.sampleprj.dao.mybatis.MyBATISTipoItemDAO;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ItemMapper;
import edu.eci.cvds.sampleprj.dao.mybatis.session.MyBATISSessionAlquiler;
import edu.eci.cvds.samples.services.impl.ServiciosAlquilerImpl;
import edu.eci.cvds.samples.services.impl.ServiciosAlquilerItemsStub;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.guice.XMLMyBatisModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static com.google.inject.Guice.createInjector;

public class ServiciosAlquilerFactory {

    private static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }

    private static SqlSession SessionStarter(){
        return getSqlSessionFactory().openSession();
    }


   private static ServiciosAlquilerFactory instance = new ServiciosAlquilerFactory();

   private static Optional<Injector> optInjector;

    private Injector myBatisInjector(String env, String pathResource) {

       return createInjector(new XMLMyBatisModule() {
           @Override
           protected void initialize() {
               setEnvironmentId(env);
               setClassPathResource(pathResource);
               bind(ItemDAO.class).to(MyBATISItemDAO.class);
               bind(ClienteDAO.class).to(MyBATISClienteDAO.class);
               bind(TipoItemDAO.class).to(MyBATISTipoItemDAO.class);
               bind(ServiciosAlquiler.class).to(ServiciosAlquilerItemsStub.class);

           }
       });
   }

   private ServiciosAlquilerFactory(){
       optInjector = Optional.empty();
   }

   public ServiciosAlquiler getServiciosAlquiler(){
       if (!optInjector.isPresent()) {
           optInjector = Optional.of(myBatisInjector("development","mybatis-config.xml"));
       }

       return optInjector.get().getInstance(ServiciosAlquiler.class);
   }


   public ServiciosAlquiler getServiciosAlquilerTesting(){
       if (!optInjector.isPresent()) {
           optInjector = Optional.of(myBatisInjector("test","mybatis-config-h2.xml"));
       }

       return optInjector.get().getInstance(ServiciosAlquiler.class);
   }


   public static ServiciosAlquilerFactory getInstance(){
       return instance;
   }

}