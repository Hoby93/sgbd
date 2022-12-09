package base;

import sql.*;
import java.util.*;
import java.lang.*;

public class Sql {
    AccesRegister file = new AccesRegister();

    public Sql() {

    }

    public void DisplayResult(Vector<Table> R) throws Exception {
        int row = 0;
        String trait = "";
        String col = " ";

        if(R.size() == 0) {
            throw new Exception("** Aucun element pour l'instant **");
        }

        for(int i = 0; i < R.get(0).getAllNom().length; i++) {
            String next = R.get(0).getAllNom()[i];
            col = col +  next;
            int z = 16 - next.toCharArray().length;
            
            for(int x = 0;x < z; x++ ) {
                col = col + " ";
            }
            trait = trait + "------------------";
            col = col + "| ";
        }

        System.out.println(trait);
        System.out.println(col);
        System.out.println(trait);


        for(int i=0; i < R.size(); i++) {
            col = " ";
            for(int x = 0; x < R.get(i).getAllNom().length; x++) {
                String next = R.get(i).getAllVal()[x].toString();
                col = col +  next;
                int z = 16 - next.toCharArray().length;
                for(int n = 0; n < z; n++ ) {
                    col = col + " ";
                }
                col = col + "| ";
            }
            System.out.println(col);
            row++;
        }
        System.out.println(trait);
        System.out.println("0" + row + " ligne(s)");
    }

    public String[] stringArray(Vector<String> vc) {
        String[] ans = new String[vc.size()];

        for(int i = 0; i < vc.size(); i++) {
            ans[i] = vc.get(i);
        }

        return ans;
    }

    public String[] sameCol(Vector<Table> R1, Vector<Table> R2) throws Exception {
        String[] col1 = R1.get(0).getAllNom();
        String[] col2 = R2.get(0).getAllNom();

        Vector<String> ans = new Vector<String>();

        for(int x = 0; x < col1.length; x++) {
            for(int i = 0; i < col2.length; i++) {
                //System.out.println(col1[x] + " - " + col2[i]);
                if(col1[x].equalsIgnoreCase(col2[i])) {
                    ans.add(col1[x]);
                }
            }
        }

        return stringArray(ans);
    }

    public String[] notSame(String[] same, Vector<Table> R) {
        Vector<String> notSame = new Vector<String>();
        String[] col2 = R.get(0).getAllNom();

        boolean isOn = false;
        for(int x = 0; x < col2.length; x++) {
            for(int i = 0; i < same.length; i++) {
                //System.out.println(col2[x] + " - " + same[i]);
                if(col2[x].equalsIgnoreCase(same[i])) {
                    isOn = true;
                }
            }
            if(!isOn) {
                notSame.add(col2[x]);
            }
            isOn = false;
        }
        
        return stringArray(notSame);
    }

    public Vector<Table> Projection(Vector<Table> R, String nomCol[]) throws Exception {
        if(nomCol[0].equalsIgnoreCase("*")) {
            return R;
        }
        Vector<Table> ans = new Vector<Table>();
        for(int i = 0; i < R.size(); i++) {
            try {
                Object[] valCol = new Object[nomCol.length];
                int id = 0;
                for(int x = 0; x < nomCol.length; x++) {
                    valCol[id] = R.get(i).get(nomCol[x]);
                    id++;
                }
                Table add = new Table(nomCol, valCol);
                if(!add.isOn(ans)) {
                    //System.out.println("Here");
                    ans.add(add);
                }
                //break;
            } catch (Exception e) {
                //e.printStackTrace();
                throw new Exception("** Nom de colonne Invalide **");
            }
        }

       return ans;
    }   

    public Vector<Table> Division(Vector<Table> R1, Vector<Table> R2) throws Exception {
        String[] same = sameCol(R1, R2);

        Vector<Table> R3 = Projection(R1, same);
        Vector<Table> R4 = Projection(R2, notSame(same, R2));
        Vector<Table> R5 = ProduitCartesien(R4, R3);
        Vector<Table> R6 = Difference(R2, R5);
        Vector<Table> R7 = Projection(R6, notSame(same, R2));
        Vector<Table> R8 = Difference(R7, R4);

        return R8;
    }

    public Vector<Table> Selection(Vector<Table> R, String nomCol, String valCol) throws Exception {
        Vector<Table> ans = new Vector<Table>();
        for(int i=0;i<R.size();i++) {
            try {
                if(R.get(i).get(nomCol).equals(valCol)) {
                    ans.add(R.get(i));
                }
            } catch (Exception e) {
                throw new Exception("** Nom Colonne '" + nomCol + "' invalide **");
            }
        }
       return ans;
    }

    public Vector<Table> Union(Vector<Table> R1, Vector<Table> R2) {
        Vector<Table> ans = new Vector<Table>();
        ans = R1;
        for(int i=0;i<R2.size();i++) {
            if(!R2.get(i).isOn(ans)) {
                ans.add(R2.get(i));
            }
        }

        return ans;
    }
    
    public Vector<Table> Intersection(Vector<Table> R1, Vector<Table> R2) {
        Vector<Table> ans = new Vector<Table>();
     
        for(int i=0;i<R2.size();i++) {
            if(R2.get(i).isOn(R1)) {
                ans.add(R2.get(i));
            }
        }

        return ans;
    }

    public String[] add(String[] str1, String[] str2) {
        String[] ans = new String[str1.length + str2.length];
        int x = 0;
        for(int i = 0; i < str1.length; i++) {
            ans[x] = str1[i];
            x++;
        }
        for(int i = 0; i < str2.length; i++) {
            ans[x] = str2[i];
            x++;
        }

        return ans;
    }

    public Object[] add(Object[] obj1, Object[] obj2) {
        Object[] ans = new Object[obj1.length + obj2.length];
        int x = 0;

        for(int i = 0; i < obj1.length; i++) {
            ans[x] = obj1[i];
            x++;
            //System.out.println("1>"+obj1[i]);
        }
        for(int i = 0; i < obj2.length; i++) {
            ans[x] = obj2[i];
            x++;
            //System.out.println("2>"+obj2[i]);
        }

        return ans;
    }

    public Vector<Table> ProduitCartesien(Vector<Table> R1, Vector<Table> R2) {
        Vector<Table> ans = new Vector<Table>();
        for(int x = 0; x < R1.size() ; x++) {
            for(int i = 0; i < R2.size(); i++) {
                String[] col = add(R1.get(x).getAllNom(), R2.get(i).getAllNom());
                Object[] val = add(R1.get(x).getAllVal(), R2.get(i).getAllVal());
                ans.add(new Table(col, val));
            }
        }

        return ans;
    }

    public Vector<Table> Difference(Vector<Table> tb1, Vector<Table> tb2) {
        Vector<Table> R1 = tb1;
        Vector<Table> R2 = tb2;

        if(tb1.size() > tb2.size()) {
            R1 = tb2;
            R2 = tb1;
        }
        
        Vector<Table> ans = new Vector<Table>();
     
        for(int i = 0; i < R2.size(); i++) {
            if(!R2.get(i).isOn(R1)) {
                ans.add(R2.get(i));
            }
        }

        return ans;
    }

    public Vector<Table> Join(Vector<Table> R1, Vector<Table> R2, String[] keys) {
        Vector<Table> cart = ProduitCartesien(R1, R2);
        Vector<Table> ans = new Vector<Table>();

        for(int x = 0; x < cart.size(); x++) {
            String[] col = cart.get(x).getAllNom();
            Object[] val = cart.get(x).getAllVal();
            for(int i = 0; i < col.length; i++) {
                for(int n = i+1; n < col.length; n++) {
                    if(col[i].equalsIgnoreCase(keys[0]) && col[n].equalsIgnoreCase(keys[1])) {
                        if(val[i].equals(val[n])) {
                            ans.add(cart.get(x));
                        } 
                        //System.out.println(val[i] + " = " + val[n]);
                        i = col.length;
                        break;
                    }
                }
            }
        }
        
        return ans;
    } 

    public String[] Colonne(String[] col, String chx) throws Exception {
        String[] show = chx.split(",");
        boolean isCol = false;
       
        for(int i = 0; i < show.length; i++) {
            for(int x = 0; x < col.length; x++) {
                if(show[i].equalsIgnoreCase(col[x]) && !show[i].equals("*")) {
                    isCol = true;
                }
            }
            if(!isCol && !show[0].equals("*")) {
                throw new Exception("** Nom Colonne '" + show[i] + "' invalide **");
            }
        }

        return show;
    }

    public String onlyOneSpace(String str) {
        char[] chr = str.toCharArray();

        int x = -1;
        int count = 0;
        char space = ' ';
        while(x<(chr.length-1)) {
            x++;
            if(chr[x]==space && chr[x+1]==space) {
                continue;
            }
            count ++;
        }

        char[] ans = new char[count];
        int id = 0;
        x = -1;
        while(x<(chr.length-1)) {
            x++;
            if(chr[x]==space && chr[x+1]==space) {
                continue;    
                //System.out.print(""+ls[i]);
            }
            ans[id] = chr[x];
            id++;
        }

        return new String(ans);
    }

    public String noSpace(String str) {
        char[] chr = str.toCharArray();

        int x = -1;
        int count = 0;
        char space = ' ';
        while(x<(chr.length-1)) {
            x++;
            if(chr[x]==space) {
                continue;
            }
            count ++;
        }

        char[] ans = new char[count];
        int id = 0;
        x = -1;
        while(x<(chr.length-1)) {
            x++;
            if(chr[x]==space) {
                continue;    
            }
            ans[id] = chr[x];
            id++;
        }

        return new String(ans);
    }

    public Vector<Table> allSelection(String TName, String rqt) throws Exception {
        String[] and =  rqt.split(" ");
        //String ns = noSpace(myRQT[5]);
    
        Vector<Table> allCond = new Vector<Table>();

        String[] cond = and[0].split("=");

        try {
            allCond = Selection(file.getTableContent(TName), cond[0], cond[1]);
        } catch (Exception ex) {
            //ex.printStackTrace();
            throw new Exception("** Syntax Invalide **");
        }

        for(int i = 0; i < and.length; i++) {
            if(and[i].equalsIgnoreCase("AND")) {
                cond = and[i + 1].split("=");
                allCond = Intersection(allCond, Selection(file.getTableContent(TName), cond[0], cond[1]));
            } else if(and[i].equalsIgnoreCase("OR")) {
                cond = and[i + 1].split("=");
                allCond = Union(allCond, Selection(file.getTableContent(TName), cond[0], cond[1]));
            }      
        } 

        return allCond;
    }

    public Vector<Table> SELECT(String rqt) throws Exception {
        rqt = onlyOneSpace(rqt);
        String[] myRQT = rqt.split(" ");
        String[] show = Colonne(file.getTableContent(myRQT[3]).get(0).getAllNom(), myRQT[1]);

        if(!myRQT[2].equalsIgnoreCase("FROM")) {
            throw new Exception("** Commande '" + myRQT[2] + "' inconnue **");
        }
        
        if(myRQT.length == 4) {
            //System.out.println("\n-> " + rqt + "\n");
                Vector<Table> R = file.getTableContent(myRQT[3]);

                return Projection(R, show);
                //DisplayResult(Projection(R, show));
        } else {
            myRQT = rqt.split(" ",6);
            if(myRQT[4].equalsIgnoreCase("WHERE")) {

                Vector<Table> allCond = allSelection(myRQT[3], myRQT[5]);
                //System.out.println("\n-> " + rqt + "\n");
                
                return Projection(allCond, show);
                //DisplayResult(Projection(allCond, show));

            } else if(myRQT[4].equalsIgnoreCase("JOIN")) {
                String[] forJoin = myRQT[5].split(" ");
                if(forJoin.length < 3) {
                    throw new Exception("** Syntax Invalide **");
                }
                if(!forJoin[1].equalsIgnoreCase("ON")) {
                    throw new Exception("** Syntax '" + forJoin[1] + "' Invalide **");
                }
                //System.out.println("--> " + myRQT[5]);
                String[] on = forJoin[2].split("=");
                Vector<Table> joined = Join(file.getTableContent(myRQT[3]), file.getTableContent(forJoin[0]), on);
                if(joined.size() == 0) {
                    throw new Exception("** Echec du Jointure **");
                }

                //System.out.println("\n-> " + rqt + "\n");

                return Projection(joined, show);
                //DisplayResult(Projection(joined, show));
            } else {
                throw new Exception("** Commande '" + myRQT[4] + "' inconnue **");
            }
        }//*/
    }

    public Vector<Table> executeQuery(String rqt) throws Exception {
        rqt = onlyOneSpace(rqt);

        //System.out.println("\n-> " + rqt + "\n");

        String[] myRQT = rqt.split(" ");

        switch(myRQT[0].toLowerCase()) {
            case "use" :
                SETDB(rqt);
                break;
            case "select" : 
                return SELECT(rqt);
            case "insert" :
                INSERT(rqt);
                break;
            case "update" :
                UPDATE(rqt);
                break;
            case "delete" :
                DELETE(rqt);
                break;
            case "show" :
                if(myRQT[1].equalsIgnoreCase("TABLE")) {
                    return SHOWTABLE();
                } else {
                    return SHOWDB();
                } 
            case "create" :
                if(myRQT[1].equalsIgnoreCase("TABLE"))
                    CREATETABLE(rqt);
                else 
                    CREATEDB(rqt);
                break;
        }

        throw  new Exception("** Commande '" + myRQT[0] + "' Inconnue **");
    }

    public String insertValues(String rqt) {
        String ans = rqt.substring(7, endIndex(rqt));

        return ans;
    }

    public int endIndex(String str) {
        char[] chr = str.toCharArray();

        return chr.length-1;
    }

    public void INSERT(String rqt) throws Exception {
        String[] myRQT = rqt.split(" ", 4);
        if(!myRQT[1].equalsIgnoreCase("INTO")) {
            throw new Exception("** Commande '" + myRQT[1] + "' inconnue **");
        }
        myRQT[3] = noSpace(myRQT[3]);
        String exc = myRQT[3].substring(0,6);
        if(!exc.equalsIgnoreCase("VALUES")) {
            throw new Exception("** Commande 'values' requise **");
        }

        file.insert(myRQT[2], insertValues(myRQT[3]));
    }

    public String[][] updateValues(String val) {
        String[] splt = val.split(",");
        String[][] ans = new String[splt.length][];

        for(int i = 0; i < splt.length; i++) {
            ans[i] = splt[i].split("=");
        }

        return ans;
    }

    public void UPDATE(String rqt) throws Exception {
        String[] myRQT = rqt.split(" ", 6);
        if(!myRQT[2].equalsIgnoreCase("SET")) {
            throw new Exception("** Commande '" + myRQT[2] + "' inconnue **");
        }
        if(!myRQT[4].equalsIgnoreCase("WHERE")) {
            throw new Exception("** Commande '" + myRQT[4] + "' inconnue **");
        }

        file.update(myRQT[1], updateValues(myRQT[3]), allSelection(myRQT[1], myRQT[5]));
    }

    public void DELETE(String rqt) throws Exception {
        String[] myRQT = rqt.split(" ", 4);

        if(!myRQT[2].equalsIgnoreCase("WHERE")) {
            throw new Exception("** Commande '" + myRQT[2] + "' inconnue **");
        }

        file.update(myRQT[1], null, allSelection(myRQT[1], myRQT[3]));
    }

    public void CREATETABLE(String rqt) throws Exception {
        String[] myRQT = rqt.split(" ", 4);
        if(!myRQT[1].equalsIgnoreCase("TABLE")) {
            throw new Exception("** Syntax '" + myRQT[1] + "' Error **");
        }
        myRQT[3] = noSpace(myRQT[3]);

        String col = myRQT[3].substring(1, endIndex(myRQT[3]));

        file.createTable(myRQT[2], col);
    }

    public void CREATEDB(String rqt) throws Exception {
        String[] myRQT = rqt.split(" ");
        if(!myRQT[1].equalsIgnoreCase("DATABASE")) {
            throw new Exception("** Syntax '" + myRQT[1] + "' Error **");
        }

        file.createDB(myRQT[2]);
    }

    public Vector<Table> SHOWTABLE() throws Exception {
        Vector<Table> tables = file.showTable();
        String[] show = {"*"};

        return Projection(tables, show);
        //DisplayResult(Projection(tables, show));
    }

    public Vector<Table> SHOWDB() throws Exception {
        Vector<Table> db = file.showDB();
        String[] show = {"*"};

        return Projection(db, show);
        //DisplayResult(Projection(db, show));
    }

    public void SETDB(String rqt) throws Exception {
        String[] myRQT = rqt.split(" ");

        file.setDB(myRQT[1]);
    }

    public String dbName() {
        return file.dbName();
    }
}