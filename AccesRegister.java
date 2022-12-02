package sql;

import base.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.lang.reflect.*;

public class AccesRegister {
    String fileName = "Register.txt";

    public AccesRegister() {
        
    }

    public String[] getEachLine() throws Exception {
        File fl = new File("Register.txt");
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

        for(int i = 0; i < lines.length; i++) {
            
            char[] first = lines[i].toCharArray();
            try {
                if(first[0] != cote && first[0] != space) {
                    String[] desc = lines[i].split(",");
                    if(desc[0].equalsIgnoreCase(table)) {
                        String[] cls = prepareColonne(desc);
                        i++;
                        while(i < lines.length) {
                            try {
                                if(lines[i].toCharArray()[0] == cote) {
                                    //System.out.println(lines[i]);
                                    ans.add(new Table(cls, prepareValues(lines[i])));
                                }
                                i++;
                            } catch(Exception e) {
                                break;
                            }
                        }
                        break;
                    }//*/
                    //System.out.println(first[0]);
                }
            } catch(Exception e) {
                //break;
            }
        }

        if(ans.size() == 0) {
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

        if(getTableContent(TName).get(0).getAllNom().length != values.length) {
            throw new Exception("** Erreur lie(s) au(x) nombre(s) d'argument(s) **");
        }

        char cote = ">".toCharArray()[0];
        char space = " ".toCharArray()[0];
        //System.out.println("LAST-SIZE>>"+splitLine().length);
        //getTableContent(TName);
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

        for(int i = 0; i < lines.length; i++) {     
            char[] first = lines[i].toCharArray();
            try {
                if(isTable) {
                    if(tables.get(index).isOn(condition)) {
                        tables.get(index).setColValues(update);
                        filOut.println(tables.get(index).fileFormat());
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
    }

}