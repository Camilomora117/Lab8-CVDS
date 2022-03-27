package edu.eci.cvds.sampleprj.dao.mybatis.session;

import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ItemMapper;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.TipoItemMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBATISSessionAlquiler {
    SqlSession sessionAlquiler;

    private MyBATISSessionAlquiler(){
        SqlSessionFactory sessionfact = getSqlSessionFactory();
        sessionAlquiler = sessionfact.openSession();
    }

    public static SqlSessionFactory getSqlSessionFactory() {
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

    public SqlSession getSessionAlquiler(){
        return sessionAlquiler;
    }

    public ClienteMapper getClienteMapper(){
        return sessionAlquiler.getMapper(ClienteMapper.class);
    }
    public ItemMapper getItemMapper(){
        return sessionAlquiler.getMapper(ItemMapper.class);
    }
    public TipoItemMapper getTipoItemMapper(){
        return sessionAlquiler.getMapper(TipoItemMapper.class);
    }
}
