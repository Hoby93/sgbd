package base;

import java.util.*;
import java.io.Serializable;

public class Table implements Serializable {
    String[] nomCol;
    Object[] valCol;

    public Table(String[] nc, Object[] vc) {
        this.nomCol = nc;
        this.valCol = vc;
    }

    public String[] getAllNom() {
        return nomCol;
    }

    public Object[] getAllVal() {
        return valCol;
    }

    public boolean isEqual(Table tab) {
        for(int i=0;i<valCol.length;i++) {
            if(!valCol[i].equals(tab.getAllVal()[i])) {
                return false;
            }
        }

        return true;
    }

    public boolean isOn(Vector<Table> list) {
        for(int i=0;i<list.size();i++) {
            if(this.isEqual(list.get(i))) {
                return true;
            }
        }
        
        return false;
    }

    public Object get(String col) throws Exception {
        for(int i=0;i<nomCol.length;i++) {
            if(col.equalsIgnoreCase(nomCol[i])) {
                return valCol[i];
            }
        }

        throw new Exception("** Nom de colonne '" + col + "' Invalide **");
    }

    public void setColValue(String[] update) {
        for(int i = 0; i < nomCol.length; i++) {
            if(update[0].equalsIgnoreCase(nomCol[i])) {
                valCol[i] = update[1];
            }
        }
    }

    public void setColValues(String[][] update) {
        for(int i = 0; i < update.length; i++) {
            setColValue(update[i]);
        }
    }

    public boolean isThis(String[][] condition) throws Exception {
        for(int i = 0; i < condition.length; i++) {
            if(!condition[i][1].equals(this.get(condition[i][0]))) {
                return false;
            }
        }
        return true;
    }

    public String fileFormat() {
        String ans = ">";
        for(int i = 0; i < valCol.length; i++) {
            ans = ans + valCol[i].toString();
            if(i != valCol.length - 1) ans = ans + ",";
        }

        return ans;
    }
}