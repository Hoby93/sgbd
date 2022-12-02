package front;

import base.*;
import sql.*;
import java.util.*;
import java.io.*;

public class Main {

    public static void main(String args[]) {

        String[] colIng = {"idIng", "Nom"}; 
        
        //Object[] valIng = null;

        Vector<Table> Ingredient = new Vector<Table>();
            Object[] valIng1 = {1, "Oeuf"};
            Ingredient.add(new Table(colIng, valIng1));
            Object[] valIng2 = {2, "Huile"};
            Ingredient.add(new Table(colIng, valIng2));
            //Object[] valIng = {3, "Sel"};
            //Ingredient.add(new Table(colIng, valIng));

        String[] colRec = {"idRec", "idIng"}; 
        
        Object[] valRec = null;

        Vector<Table> Recette = new Vector<Table>();
            Object[] valRec1 = {1, 1};
            Recette.add(new Table(colRec, valRec1));
            Object[] valRec2 = {1, 2};
            Recette.add(new Table(colRec, valRec2));
            Object[] valRec3 = {2, 1};
            Recette.add(new Table(colRec, valRec3));
            Object[] valRec4 = {3, 1};
            Recette.add(new Table(colRec, valRec4));
            Object[] valRec5 = {3, 2};
            Recette.add(new Table(colRec, valRec5));
            

        try {
            Sql rqt = new Sql();

            String[] show = {"*"};
            AccesRegister reg = new AccesRegister();

            //Vector<Table> vc = reg.getTableContent("Employer");
           
            //Vector<Table> filtre = rqt.ProduitCartesien(data1, data2);
            //Recette = rqt.Projecter(Recette, show);
            Vector<Table> Dv = rqt.Division(Ingredient, Recette);
            //rqt.DisplayResult(Dv, show);

            //reg.insert("Departement", "Ville,Laand");

            rqt.UPDATE("update Employer set Post=Pdg,Nom=Succes,Salaire=123 where Nom=Update or Post=Pass");

            String[][] updt = {{"Nom","RAKOTO"}};
            String[][] cond = {{"Nom","Hoby"}};

            //reg.update("Employer", updt, cond);
            //System.out.println(vc.size());

            //rqt.executeQuery("select * from employer");
        }
        catch(Exception ex) {
            ex.printStackTrace();
            //System.out.println(ex.getMessage());
        }//*/
    }
}