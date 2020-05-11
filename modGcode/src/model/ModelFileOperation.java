package model;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ModelFileOperation {
    private String path;
    private int layers;
    private float layersHeight;
    private int totalTime;
    private int layerOfPause;
    private long countLinesToPause;
    private String[] strBeforeLine;
    private String[] strAfterLine;
    //private final int VALUE_LINE_IN_TAB = 10;

//////////////////////// Constructors //////////////////////////
    private ModelFileOperation(){
        this.strBeforeLine = new String[SetterValues.VALUE_LINE_IN_TAB];
        this.strAfterLine = new String[SetterValues.VALUE_LINE_IN_TAB];
        for(int i= 0; i<SetterValues.VALUE_LINE_IN_TAB;i++){
            strBeforeLine[i] = "";
            strAfterLine[i] = "";
        }
    }
    public ModelFileOperation(String path,int layerOfPause){
        this();
        this.path = path;
        this.layerOfPause= layerOfPause;
        checkFileExists();
    }

////////////////////////METHODS ///////////////////////////////////
    private void showLine(String[] tab){
        for(int i = 0; i< tab.length; i++){
            System.out.println(tab[i]);
        }
    }
    public boolean checkFileExists(){
        Path path = Paths.get(this.path);
        if(Files.exists(path) && Files.isRegularFile(path)){
            String str = path.getFileName().toString().toLowerCase();
            str = str.substring(str.lastIndexOf("."));
            return str.equals(".gcode");
        }
        return false;
    }

    private void setValuesFromFile(){
        long layerCount = 0;
        int indexTemp = SetterValues.VALUE_LINE_IN_TAB -1;
        String[] strTab = new String[SetterValues.VALUE_LINE_IN_TAB];
        List<String> listStr = new ArrayList<>(100);
        for(int i =0; i<SetterValues.VALUE_LINE_IN_TAB;i++) strTab[i] ="";


        try
        {
            Path path = Paths.get(this.path);
            if(Files.exists(path) && Files.isRegularFile(path))
            {
                BufferedReader reader = new BufferedReader(new FileReader(this.path));
                while (reader.ready())
                {
                    String str = reader.readLine();
                    layerCount++;
                    if (str.charAt(0) == ';')
                    {
                        listStr.add(str.toLowerCase());
                        if(str.toLowerCase().contains(SetterValues.LAYER+this.layerOfPause))
                        {
                            this.countLinesToPause = layerCount;
                            for(int i =0; i < SetterValues.VALUE_LINE_IN_TAB; i++){
                                str = reader.readLine();
                                if(str != null) this.strAfterLine[i] = str;
                            }
                            break;
                    }   }
                    strTab[indexTemp] = str;
                    indexTemp--;
                    if(indexTemp < 0) indexTemp = SetterValues.VALUE_LINE_IN_TAB-1;
                }
                reader.close();
            }
        }catch (InvalidPathException err) {
            System.out.println("blad w sicezce");
            return;
        }catch (SecurityException err){
            System.out.println("blad podczas sprawdzanie sciezki");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        indexTemp+=1;
        for(int i = SetterValues.VALUE_LINE_IN_TAB-1; i>=0; i--){
            this.strBeforeLine[i] = strTab[indexTemp];
            indexTemp++;
            if(indexTemp >= SetterValues.VALUE_LINE_IN_TAB) indexTemp = 0;
        }
        strTab = null;

        byte value = 0;
        for(String strr : listStr)
        {
            if(value >= 3 ) break;
            if(strr.contains(SetterValues.TIME))
            {
                int index = strr.indexOf(":");
                this.totalTime = Integer.parseInt(strr.substring(index+1));
                value+=1;
                continue;
            }
            if(strr.contains(SetterValues.LAYER_HEIGHT))
            {
                int index = strr.indexOf(":");
                this.layersHeight = Float.parseFloat(strr.substring(index+1));
                value+=1;
                continue;
            }
            if(strr.contains(SetterValues.LAYER_COUNT)){
                int index = strr.indexOf(":");
                this.layers = Integer.parseInt(strr.substring(index+1));
                value+=1;
            }
        }
    }



///////////////////////OVERRIDE METHODS//////////////////////////////

////////////////////////////SETTER ANG GETTER ////////////////////

    public String getPath() {
        return path;
    }

    public int getLayers() {
        return layers;
    }

    public float getLayersHeight() {
        return layersHeight;
    }

    public int getTotalTime() {
        return totalTime;
    }
    public int getLayerOfPause() {
        return this.layerOfPause;
    }

    public long getCountLinesToPause() {
        return countLinesToPause;
    }
    public String[] getStrBeforeLine() {
        return strBeforeLine;
    }

    public String[] getStrAfterLine() {
        return strAfterLine;
    }


    ////////////////////////////INNER & NESTED CLASS ////////////////////////////////////////////////////


    public static void main(String[] arg){
        ModelFileOperation modelFileOperation = new ModelFileOperation("C:\\Users\\Media\\Desktop\\druki3d\\CFDMP_oddish-body.gcode",0);
        modelFileOperation.setValuesFromFile();
    }

}
