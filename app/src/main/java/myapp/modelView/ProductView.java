package myapp.modelView;

public class ProductView {
    private int id;
    private String symbol;
    private String name;
    private int quantity;

    public ProductView(String symbol, String name, int quantity) {
        //this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.quantity = quantity;
    }

/*    public int getId() {
        return id;
    }*/

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
