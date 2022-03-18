/*
 * Copyright (C) 2015 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.cvds.sampleprj.jdbc.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class JDBCExample {
    
    public static void main(String args[]){
        try {
            String url="jdbc:mysql://desarrollo.is.escuelaing.edu.co:3306/bdprueba";
            String driver="com.mysql.jdbc.Driver";
            String user="bdprueba";
            String pwd="prueba2019";
                        
            Class.forName(driver);
            Connection con=DriverManager.getConnection(url,user,pwd);
            con.setAutoCommit(false);

            System.out.println("Valor total pedido 1:"+valorTotalPedido(con, 1));
            
            List<String> prodsPedido=nombresProductosPedido(con, 1);

            System.out.println("Productos del pedido 1:");
            System.out.println("-----------------------");
            for (String nomprod:prodsPedido){
                System.out.println(nomprod);
            }
            System.out.println("-----------------------");

            int suCodigoECI=2168129;
            registrarNuevoProducto(con, suCodigoECI, "SU NOMBRE", 99999999);            
            con.commit();
            con.close();
                                   
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(JDBCExample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Consultar los nombres de los productos asociados a un pedido
     * @param con la conexión JDBC
     * @param codigoPedido el código del pedido
     * @return
     */
    public static List<String> nombresProductosPedido(Connection con, int codigoPedido) throws SQLException{
        List<String> n = new LinkedList<>();
        String consulta = "SELECT nombre FROM ORD_DETALLE_PEDIDO dp , ORD_PRODUCTOS p WHERE dp.producto_fk = p.codigo AND dp.pedido_fk = ?";
        //Crear Prepared Statement
        PreparedStatement productosPedido = con.prepareStatement(consulta);
        //Asignar parámetros
        productosPedido.setInt(1, codigoPedido);
        //ExecuteQuery
        ResultSet rS = productosPedido.executeQuery();
        while (rS.next()) {
            String nombre = rS.getString("nombre");
            n.add(nombre);
        }
        return n;
    }

    /**
     * Calcular el costo total de un pedido
     * @param con
     * @param codigoPedido código del pedido cuyo total se calculará
     * @return el costo total del pedido (suma de: cantidades*precios)
     */
    public static int valorTotalPedido(Connection con, int codigoPedido)throws SQLException{
        String consulta = "SELECT SUM(cantidad*op.precio) FROM ORD_DETALLE_PEDIDO odp , ORD_PRODUCTOS op WHERE producto_fk = op.codigo && pedido_fk = ?";
        PreparedStatement totalPedido = null;
        //Crear prepared statement
        totalPedido = con.prepareStatement(consulta);
        //asignar parámetros
        totalPedido.setInt(1, codigoPedido);
        //usar executeQuery
        ResultSet rS = totalPedido.executeQuery();
        //Sacar resultado del ResultSet
        int costoTotal = 0;
        while(rS.next()) {
            costoTotal = rS.getInt(1);
        }
        return costoTotal;
    }

    /**
     * Agregar un nuevo producto con los parámetros dados
     * @param con la conexión JDBC
     * @param codigo
     * @param nombre
     * @param precio
     * @throws SQLException
     */
    public static void registrarNuevoProducto(Connection con, int codigo, String nombre,int precio) throws SQLException{
        String consulta = "INSERT INTO ORD_PRODUCTOS (codigo,nombre,precio) values (?, ?, ?)";
        //Crear preparedStatement
        PreparedStatement agregarProducto = con.prepareStatement(consulta);
        //Asignar parámetros
        agregarProducto.setInt(1, codigo);
        agregarProducto.setString(2, nombre);
        agregarProducto.setInt(3, precio);
        //usar 'execute'
        agregarProducto.execute();
        con.commit();
    }

}