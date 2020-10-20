/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knnclassification;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kaustubhrajpathak
 */
public class KNN {
    
    Connection con;
    Statement stmt,stmt1,stmt2,stmt3;
    double dist[] = new double[20];
    double maxdist[][] = new double[20][2];
    int classes[] = new int[5];
    
    static final String jdbc = "com.mysql.jdbc.Driver";
    static final String db = "jdbc:mysql://localhost/knn";
    static final String user = "root";
    static final String pass = "root";
    
    public void initDatabase(){
        try {
            Class.forName(jdbc);
            con = (Connection) DriverManager.getConnection(db, user, pass);
            stmt = (Statement) con.createStatement();
            stmt1 = (Statement) con.createStatement();
            stmt2 = (Statement) con.createStatement();
            stmt3 = (Statement) con.createStatement();
            String query = "create table nodes(id int auto_increment, x double, y double, class int,primary key(id));";
            stmt.execute(query);
            String query2 = "create table maxtable(dist double,class int);";
            stmt.execute(query2);
            System.out.println("Created tables Successfully!\n");
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void populateTable(){
        try {
            String query = "insert into nodes(x,y,class) values (1.0,2.0,1),(1.5,2.0,2),(2.0,1.5,2),(2.0,2.0,2),(2.5,2.0,3),(2.5,2.5,3);";
            stmt.execute(query);
            System.out.println("Populated Table!");
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }
    
    public int classify(double x,double y){
        int c = 1;
        int count = -1;
        double distance = 0.0;
        int count1 = 0;
        String query = "select x,y from nodes;";
        try {
            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            while(rs.next()){
                count++;
                distance = calcDistance(rs.getDouble(1),rs.getDouble(2),x,y);
                System.out.println(distance);
                dist[count] = distance;
            }
           rs.close();
            
            try{
                ResultSet rs1 = null;
                for(int i=1;i<=count;i++){
                    String query1 = "select class from nodes where id = "+i+";";
                    rs1 = stmt1.executeQuery(query1);
                    System.out.println(query1);
                    while(rs1.next()){
                        String query3 = "insert into maxtable(dist,class) values("+dist[i]+","+rs1.getInt(1)+");";
                        System.out.println(query3);
                        stmt3.execute(query3);
                    }

                }
                rs1.close();
                
            }
            catch(SQLException e){
                e.printStackTrace();
            }
            
            String query3 = "select class from maxtable order by dist limit 5;";
            ResultSet rs2 = stmt2.executeQuery(query3);
            System.out.println(query3);
            while(rs2.next()){
                classes[count1] = rs2.getInt(1);
                count1++;
            }
            rs2.close();
            c = getClass(classes);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return c;
    }
    
    public void insertTable(double x,double y,int cl){
        try {
            System.out.println("Point classified into class :"+cl);
            String query = "insert into nodes(x,y,class) values("+x+","+y+","+cl+");";
            stmt.execute(query);
            System.out.print(query);
        } catch (SQLException ex) {
            Logger.getLogger(KNN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public double calcDistance(double x,double y,double xp,double yp){
        double dis = 0.0;
        double res = Math.pow((x-xp), 2)+Math.pow((y-yp), 2);
        dis = Math.sqrt(res); 
        return dis;
    }
    
    public int getClass(int[] classes){
        int cls = 1;
        int class1=0,class2=0,class3 = 0;
        for(int i=0;i<5;i++){
            switch (classes[i]) {
                case 1:
                    class1++;
                    break;
                case 2:
                    class2++;
                    break;
                default:
                    class3++;
                    break;
            }
        }
        System.out.println(class1+","+class2+","+class3);
        cls = Math.max(class1,Math.max(class2, class3));
        if(cls==class1)
            return 1;
        else if(cls==class2)
            return 2;
        else
            return 3;
    }
}
