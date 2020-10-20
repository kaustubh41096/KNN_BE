/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knnclassification;

import java.util.Scanner;


/**
 *
 * @author kaustubhrajpathak
 */
public class Main {
   
    public static void main(String[] args) {
        
        KNN knn = new KNN();
        knn.initDatabase();
        knn.populateTable();
        System.out.println("Enter x-coord :-");
        Scanner c = new Scanner(System.in);
        double xcoord = c.nextDouble();
        System.out.println("Enter y-coord :-");
        double ycoord = c.nextDouble();
        int cl = knn.classify(xcoord,ycoord);
        knn.insertTable(xcoord, ycoord, cl);
    }
    
}
