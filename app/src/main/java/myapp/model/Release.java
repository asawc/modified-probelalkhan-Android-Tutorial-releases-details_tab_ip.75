package myapp.model;

import com.google.gson.annotations.SerializedName;

import java.time.ZonedDateTime;
import java.util.List;

public class Release {

    @SerializedName("id")
    private final int id;
    @SerializedName("employee")
    private final Employee employee;
    @SerializedName("status")
    private final ReleaseStatus status;
    @SerializedName("creationDate")
    private final String creationDateTime;
    @SerializedName("realizationDate")
    private final String realizationDateTime;
    @SerializedName("productsRelease")
    private final List<ProductRelease> productsRelease;

    public Release(int id, Employee employee, ReleaseStatus status, List<ProductRelease> productsRelease,
                   String creationDateTime, String realizationDateTime) {
        this.id = id;
        this.employee = employee;
        this.status = status;
        this.productsRelease = productsRelease;
        this.creationDateTime = creationDateTime;
        this.realizationDateTime = realizationDateTime;
    }

    public int getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public ReleaseStatus getStatus() {
        return status;
    }

    public List<ProductRelease> getProductsRelease() {
        return productsRelease;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public String getRealizationDateTime() {
        return realizationDateTime;
    }
}