package myapp.model;

import com.google.gson.annotations.SerializedName;

public enum ProductStatus {

    @SerializedName("0")
    out_of_stock,
    ///AWAITED, // (0),

    @SerializedName("1")
    awaited, // (1),

    @SerializedName("2")
    released; // (2);

    public static int value;

    public String status_name;


    public String ProductStatus(int value) {
        return status_name;
    }

    // private final String status_name;

    public static int getValue() {
        return value;
    }

   /* ProductStatus(int value) {
        this.value = value;
    //    status_name = toString(ProductStatus);
    //    status_name = valueOf(ProductStatus);
    }*/

    public static ProductStatus enumOfValue(String value) {
        for (ProductStatus e : values()) {
            if (e.value==Integer.valueOf(value)) {
                return e;
            }
        }
        return null;
    }
}
