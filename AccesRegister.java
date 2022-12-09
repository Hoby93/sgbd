package sql;

import base.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.lang.reflect.*;

public class AccesRegister {
    String dbName = "Defaut";
    String fileName = "./bd/BasketBall.txt";

    public AccesRegister() {
        
    }

    public String[] getEachLine() throws Exception {
        File fl = new File(fileName);
        FileReader file = new FileReader(fl);
        BufferedReader readLines = new BufferedReader(file); 
        Object[] lines = readLines.lines().toArray();  
        String[] ans = new String[lines.length];
        
        for(int i=0;i<lines.length;i++) {
            ans[i]=String.valueOf(lines[i]);
        }

        return ans;
    }

    public Object[] prepareValues(String row) {
        char[] chrs = row.toCharArray();
        char[] ans = new char[chrs.length - 1];
        for(int i = 1; i < chrs.length ; i++) {
            ans[i-1] = chrs[i];
        }

        String rep = new String(ans);
        //System.out.println(rep);

        return (Object[]) rep.split(",");
    }

    public String[] prepareColonne(String[] spl) {
        String[] ans = new String[spl.length - 1];

        for(int i = 1; i < spl.length ; i++) {
            ans[i-1] = spl[i];
            //System.out.println(ans[i - 1]);
        }

        return ans;
    }

    public Vector<Table> getTableContent(String table) throws Exception {
        Vector<Table> ans = new Vector<Table>();
        String[] lines = getEachLine();
        char cote = ">".toCharArray()[0];
        char space = " ".toCharArray()[0];

        boolean isExiste = false;

        for(int i = 0; i < lines.length; i++) {
            
            char[] first = lines[i].toCharArray();
            try {
                if(first[0] != cote && first[0] != space) {
                    String[] desc = lines[i].split(",");
                    if(desc[0].equalsIgnoreCase(table)) {
                        isExiste = true;
                        String[] cls = prepareColonne(desc);
                        i++;
                        while(i < lines.length) {
                            try {
                                if(lines[i].toCharArray()[0] == cote) {
                                    ans.add(new Table(cls, prepareValues(lines[i])));
                                }
                                i++;
                            } catch(Exception e) {
                                break;
                            }
                        }
                        break;
                    }

                }
            } catch(Exception e) {
                //e.printStackTrace();
            }
        }

        if(!isExiste) {
            throw new Exception("** Aucun table '" + table + "' correspondant **");
        }

        return ans;
    }

    public void CleanFile() throws Exception {
        //System.out.println("ARE YOU SURE TO DELETE ?");

        File fl = new File(fileName);
        PrintWriter writer=new PrintWriter(fl);
        writer.print("");
        writer.close();

    }

    public void insert(String TName, String valCol) throws Exception {
        File fl = new File(fileName);
        FileOutputStream out = new FileOutputStream(fl,true);
        PrintWriter filOut = new PrintWriter(out,true);

        String[] values = valCol.split(",");
        String[] save = getEachLine();

        try {
            if(getTableContent(TName).get(0).getAllNom().length != values.length) {
                throw new Exception("** Erreur lie(s) au(x) nombre(s) d'argument(s) **");
            }
        } catch(Exception ex) {
            //ex.printStackTrace();
        }

        char cote = ">".toCharArray()[0];
        char space = " ".toCharArray()[0];
  
        int limit = getTableContent(TName).size();
        CleanFile();
        int count = 0;
        boolean addHere = false;
        for(int x = 0; x < save.length; x++) {
            char[] first = save[x].toCharArray();
            try {
                if(first[0] != cote && first[0] != space) {
                    String[] desc = save[x].split(",");
                    if(desc[0].equalsIgnoreCase(TName)) {
                        addHere = true;
                    }
                }
                filOut.println(save[x]);
                if(addHere) {
                    count++;
                    if(count == limit + 1) filOut.println(">" + valCol);
                }
            } catch(Exception e) {
                filOut.println("");
                addHere = false;
            }
        }

        throw new Exception("** 01 Ligne  cree **");
    }

    public void update(String TName, String[][] update, Vector<Table> condition) throws Exception {
        File fl = new File(fileName);
        FileOutputStream out = new FileOutputStream(fl,true);
        PrintWriter filOut = new PrintWriter(out,true);

        Vector<Table> tables = getTableContent(TName);
        String[] lines = getEachLine();

        CleanFile();

        char cote = ">".toCharArray()[0];
        char space = " ".toCharArray()[0];

        boolean isTable = false;
        int index = 0;

        int count = 0;

        for(int i = 0; i < lines.length; i++) {     
            char[] first = lines[i].toCharArray();
            try {
                if(isTable) {
                    if(tables.get(index).isOn(condition)) {
                        if(update != null) {
                            tables.get(index).setColValues(update);
                            filOut.println(tables.get(index).fileFormat());
                        }
                        count++;
                    } else {
                        filOut.println(lines[i]);
                    }
                    index++;
                } else {
                    filOut.println(lines[i]);
                }
                if(first[0] != cote && first[0] != space) {
                    String[] desc = lines[i].split(",");
                    if(desc[0].equalsIgnoreCase(TName)) {
                        isTable = true;
                    }
                }
            } catch(Exception e) {
                isTable = false;
                filOut.println();
            }
        }
        throw new Exception("** 0" + count + " Modification(s) effectuee(s) **");
    }

    public void createTable(String TName, String Col) throws Exception {
        File fl = new File(fileName);
        FileOutputStream out = new FileOutputStream(fl,true);
        PrintWriter filOut = new PrintWriter(out, true);

        String[] lines = getEachLine();

        //System.out.println("Il y a " + lines.length + "lignes dans la base " + dbName);

        CleanFile();

        if(lines.length < 2) {
            filOut.println(TName + "," + Col);
        } else {
            char cote = ">".toCharArray()[0];
            char space = " ".toCharArray()[0];

            for(int i = 0; i < lines.length; i++) {     
                try {
                    filOut.println(lines[i]);
                } catch(Exception e) {
                    filOut.println();
                }
            }
            filOut.println();
            filOut.println(TName + "," + Col);
        }

        throw new Exception("** Table cree **");
    }

    public void createDB(String dbName) throws Exception {
        File fl = new File("./bd/" + dbName + ".txt");
        if(!fl.exists()) {
            fl.createNewFile();

            throw new Exception("** DataBase cree **");
        } else {
            throw new Exception("** La base '" + dbName + "' existe deja **");
        } 
    }

    public Vector<Table> showTable() throws Exception {
        Vector<Table> ans = new Vector<Table>();
        String[] lines = getEachLine();
        char cote = ">".toCharArray()[0];
        char space = " ".toCharArray()[0];

        for(int i = 0; i < lines.length; i++) {
            
            char[] first = lines[i].toCharArray();
            try {
                if(first[0] != cote && first[0] != space) {
                    String[] cls = {"All_Tables"};
                    //String[] desc = ;
                    Object[] val = {lines[i].split(",")[0]};
            
                    ans.add(new Table(cls, val));
                }
            } catch(Exception e) {
                //break;
            }
        }

        if(ans.size() == 0) {
            throw new Exception("** Aucun table pour l'instant **");
        }

        return ans;
    }

    public Vector<Table> showDB() throws Exception {
        Vector<Table> ans = new Vector<Table>();

        File currentFile = new File("./bd");
        File[] fileInside = currentFile.listFiles();

        for(int i = 0; i < fileInside.length; i++) {
            String col[] = {"All_DataBase"};
            Object val[] = {fileInside[i].getName().replaceAll(".txt","")};
            
            ans.add(new Table(col, val));
        }
        //System.out.println("Taille : " + fileInside.length);

        return ans;
    }

    public void setDB(String dbname) throws Exception {
        this.fileName = "bd/" + dbname + ".txt";
        this.dbName = dbname;

        //System.out.println("** DataBase modifie **");
        throw new Exception("** DataBase modifiee **");
    }

    public String dbName() {
        return dbName;
    }

}