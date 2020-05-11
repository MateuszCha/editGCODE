package model;

public class HelperGCODE {
    private String index;
    private String values;
    private String subscribe;

    public HelperGCODE(String index, String values, String subscribe) {
        this.index = index;
        this.values = values;
        this.subscribe = subscribe;
    }

    public String getIndex() {
        return index;
    }

    public String getValues() {
        return values;
    }
    public String[] getTabValues(){
        return values.split(" ");
    }

    public String getSubscribe() {
        return subscribe;
    }

    @Override
    public String toString() {
        return "Index: " + this.index + "\n"
                + "values: " + this.values + " \n"
                + "subscribe: " + this.subscribe;
    }
    /////////////////////////////////

}
