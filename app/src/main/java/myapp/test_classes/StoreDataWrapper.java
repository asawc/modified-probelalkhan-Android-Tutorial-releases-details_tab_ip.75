package myapp.test_classes;

import java.util.List;

public class StoreDataWrapper {
    private List<StoreData> coupons;

    public List<StoreData> getCoupons() {
        return coupons;
    }

    public void getCoupons(List<StoreData> coupons) {
        this.coupons = coupons;
    }
}