package myapp.modelView;

import com.google.gson.annotations.SerializedName;

public class ReleaseProductsView {

    private int id;

    @SerializedName("prod_order_status")
    private String status;

    private String name;

    @SerializedName("requested_quantity")
    private int reqQuantity;

    public ReleaseProductsView(int id, String status, String name, int reqQuantity) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.reqQuantity = reqQuantity;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public int getReqQuantity() {
        return reqQuantity;
    }
}