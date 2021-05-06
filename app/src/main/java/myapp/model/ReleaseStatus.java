package myapp.model;

import com.google.gson.annotations.SerializedName;

public enum ReleaseStatus {

    @SerializedName("0")
    pending (0),

    @SerializedName("1")
    in_progress (1),

    @SerializedName("2")
    done (2);

    private final int value;

    public int getValue() {
        return value;
    }

    ReleaseStatus(int value) {
        this.value = value;
    }

    public static ReleaseStatus enumOfValue(String value) {
        for (ReleaseStatus e : values()) {
            if (e.value==Integer.valueOf(value)) {
                return e;
            }
        }
        return null;
    }
}
