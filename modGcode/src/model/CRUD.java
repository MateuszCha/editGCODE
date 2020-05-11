package model;

import model.setters.SetterCRUD;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CRUD {
    private String oneElement;
    private final Path path;
//////////////////////// Constructors //////////////////////////

    public CRUD(){
        this.oneElement = "";
        this.path = Paths.get(SetterCRUD.PATH);
    }
////////////////////////METHODS ///////////////////////////////////
    public void create(String index, String values, String subscribe){
        StringBuilder strBuilder = new StringBuilder();
        if(index == null || index.isEmpty()) return;
        if(values == null) values = "";
        if(subscribe == null) subscribe = "";

        strBuilder.append(SetterCRUD.INDEX + index);
        if(!index.contains("\n"))
            strBuilder.append(SetterCRUD.NEW_LINE);
        strBuilder.append(SetterCRUD.VALUES + values);
        if(!values.contains("\n"))
           strBuilder.append(SetterCRUD.NEW_LINE);
        strBuilder.append(SetterCRUD.SUBSCRIBE + subscribe);
        strBuilder.append(SetterCRUD.NEW_LINE);
        strBuilder.append(SetterCRUD.PAUSE_LINE);
        strBuilder.append(SetterCRUD.NEW_LINE);

        this.oneElement = strBuilder.toString();
        this.addToFile(0);
    }

    public void update(String index, String values,String subscribe){
        String[] str = this.get(index);
        if(str[0].isEmpty()) this.create(index,values,subscribe);
        else
        {
            if(values == null || values.isEmpty()) values = str[1];
            if(subscribe == null || subscribe.isEmpty()) subscribe = str[2];
            create(index,values,subscribe);
        }
    }

    public String[] get(String index) { // synchronize for path
        checkTheFile();
        String[] tabStr = new String[3];
        synchronized (this.path)
        {
            try {
                BufferedReader reader = Files.newBufferedReader(path);
                while (reader.ready()) {
                    String str = reader.readLine();
                    if (str == null) break;
                    if (str.charAt(0) == ';' && str.toUpperCase().contains(SetterCRUD.INDEX))  // index:3 == index:30
                    {
                        if (!(index.equals(str.substring(str.indexOf(':')))))
                            continue; // index:3 == index:30 ..but 03 != 3 :( Parse to int solvet this problem

                        tabStr[0] = index;
                        while (reader.ready()) {
                            str = reader.readLine();
                            if (str == null || str.contains(SetterCRUD.PAUSE_LINE) || str.isEmpty())
                                break;
                            if (str.contains(SetterCRUD.VALUES)) {
                                tabStr[1] = str.substring(str.indexOf(":"));
                                continue;
                            }
                            if (str.contains(SetterCRUD.SUBSCRIBE)) {
                                tabStr[2] = str.substring(str.indexOf(":"));
                                continue;
                            } else tabStr[2] += str;
                        }
                    }
                }
                reader.close();
            } catch (IOException err) {
                System.out.println("Problem: z otwarciem pliku lub cos z plikiem txt");
            }
        }
        if(tabStr[0] == null) tabStr[0] ="";
        if(tabStr[1] == null) tabStr[1] ="";
        if(tabStr[2] == null) tabStr[2] ="";
        return tabStr;
    }

    public List<HelperGCODE> getAll(){
        checkTheFile();
        List<HelperGCODE> list = new ArrayList<>(100);
        synchronized (this.path)
        {
            try ( BufferedReader reader = Files.newBufferedReader(path))
            {
                String[] sTemp = {"","",""};
               while(reader.ready())
               {
                   String str = reader.readLine();
                   if (str == null) break;
                   if(str.contains(SetterCRUD.INDEX))
                   {
                       sTemp[0] = str.substring(str.indexOf(":")+1);
                       continue;
                   }
                   if(str.contains(SetterCRUD.VALUES)){
                       sTemp[1] = str.substring(str.indexOf(":")+1);
                       continue;
                   }
                   if(str.contains(SetterCRUD.SUBSCRIBE))
                   {
                       StringBuilder strBuilder = new StringBuilder();
                       strBuilder.append(str.substring(str.indexOf(":")+1));
                       strBuilder.append(SetterCRUD.NEW_LINE);
                       while(reader.ready())
                       {
                           str = reader.readLine();
                           if(str == null || str.equals(SetterCRUD.PAUSE_LINE)) break;
                           strBuilder.append(str);
                           strBuilder.append(SetterCRUD.NEW_LINE);
                       }
                       list.add(new HelperGCODE(sTemp[0],sTemp[1],strBuilder.toString()));
                   }
               }

            }catch (IOException err){

            }
        }
        ((ArrayList)list).trimToSize();
        return list;
    }

    public void delete(String index) {
        synchronized(this.path){
            try
            {
                BufferedReader reader = Files.newBufferedReader(path);
                Path pathToWriteTemp = Files.createTempFile(Paths.get(path.getParent().toString()),null,".txt");
                BufferedWriter writer = Files.newBufferedWriter(pathToWriteTemp);
                while(reader.ready()){
                    String str = reader.readLine();
                    if(str == null)break;
                    if(str.charAt(0) == ';' && str.contains(SetterCRUD.INDEX)){
                        System.out.println(str + " " + str.substring(str.indexOf(':')));
                        if(str.substring(str.indexOf(':')+1).equals(index))
                        {
                            while(reader.ready()) {
                                str = reader.readLine();
                                if(str == null || str.equals(SetterCRUD.PAUSE_LINE)) break;
                            }
                            continue;
                        }
                    }
                    writer.write(str);
                    writer.write(SetterCRUD.NEW_LINE);
                }
                reader.close();
                writer.close();
                Files.move(pathToWriteTemp,path, StandardCopyOption.REPLACE_EXISTING);
            }catch (IOException err){
                System.out.println("delete file error");
                err.printStackTrace();
            }

        }
    }

    private void checkTheFile(){
        synchronized (this.path) {
            if (Files.exists(this.path)) {
                String str = path.getFileName().toString();
                str = str.substring(str.lastIndexOf('.'));
                if (str.equals(SetterCRUD.PREFIX)) System.out.println("jest notatnikiem");
            } else {
                try {
                    Files.createDirectories(path.getParent());
                    Files.createFile(path);
                } catch (IOException err) {
                    System.out.println("niemozna stworzyc biblioteki lub pliku");
                }
            }
        }
    }

    private void addToFile(int index){
        checkTheFile();
        System.out.println(this.oneElement);
        synchronized (this.path){
            try {
                BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND);
                writer.write(this.oneElement);
                writer.close();
            }catch (IOException err){
                System.out.println("Problem z czyms");
            }
        }
    }


///////////////////////OVERRIDE METHODS//////////////////////////////

////////////////////////////SETTER ANG GETTER ////////////////////
////////////////////////////INNER & NESTED CLASS ////////////////////////////////////////////////////
    public static void main(String[] args){
        CRUD crude = new CRUD();
   //  for(int i = 0 ; i<100; i++) {
   //          crude.create("G"+i, " Y"+(i*5) + " X"+ (i*10), "null "+ (i*732137));
   //     }
        List<HelperGCODE> list = crude.getAll();
        //crude.delete("G1");
        for(HelperGCODE listForOF : list){
            System.out.print(listForOF);
        }
    }
}
