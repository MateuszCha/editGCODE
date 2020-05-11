package model;

import java.util.ArrayList;
import java.util.List;

public class ModelCreateNewLine {
    private List<String> list;
    private String str;

//////////////////////// Constructors //////////////////////////
    public ModelCreateNewLine(){
        list = new ArrayList<>(10);
    }


////////////////////////METHODS ///////////////////////////////////
    public void addToList(int value,String str){
        if(this.list.size() <= value) value = list.size();
        if(value < 0) value = 0;
        this.list.add(value,str);
    }


    public void addToList(int value,String[] strTab)
    {
        StringBuilder strBuilder = new StringBuilder();
        for(int i = 0 ; i<strTab.length; i++){
            strBuilder.append(strTab[i]);
            strBuilder.append(" ");
        }
        strBuilder.append("\n");
        this.addToList(value,strBuilder.toString());

    }

    public String deleteElementList(int value)
    {
        String str;
        if(value >= this.list.size() || value < 0) str = "Program not work correctly :(";
        else str = "was remove: " + list.remove(value);
        return str;
    }

    public void showList(){
        int i = 1;
        for(String list: this.list){
            System.out.print(i + ". " +  list);
            i++;
        }
    }

///////////////////////OVERRIDE METHODS//////////////////////////////

////////////////////////////SETTER ANG GETTER ////////////////////

    public List<String> getList() {
        return list;
    }

    public String getStr() {
        StringBuilder strBuilder = new StringBuilder();
        for(String str :this.list){
            strBuilder.append(str);
        }
        return strBuilder.toString();
    }
    ////////////////////////////INNER & NESTED CLASS ////////////////////////////////////////////////////

    public static void  main(String[] arg){
        ModelCreateNewLine modelNewLine = new ModelCreateNewLine();
        String[] tab = {"sdasd","ssssss","ttttt"};
        String[] tab1 = {"sdasd","xxxxx","ttttt"};
        String[] tab2 = {"sdasd","zzzz","ttttt"};

        modelNewLine.addToList(0,tab);
        modelNewLine.addToList(3,tab1);
        modelNewLine.addToList(1,tab2);
        System.out.print(modelNewLine.getStr());

        modelNewLine.showList();
    }
}

