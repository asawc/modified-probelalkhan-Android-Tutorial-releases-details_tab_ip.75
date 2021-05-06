package myapp.api;

import net.simplifiedcoding.simplifiedcoding.User;

import java.util.List;

import myapp.model.Employee;
import myapp.model.Product;
import myapp.model.Release;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static myapp.api.URLs.URL_ADD_EMPLOYEE;
import static myapp.api.URLs.URL_ADD_PRODUCT;
import static myapp.api.URLs.URL_ADD_RELEASE;
import static myapp.api.URLs.URL_GET_ALL_EMPLOYEES;
import static myapp.api.URLs.URL_GET_ALL_PRODUCTS;
import static myapp.api.URLs.URL_GET_ALL_RELEASES;
import static myapp.api.URLs.URL_GET_EMPLOYEE;
import static myapp.api.URLs.URL_GET_PRODUCT;
import static myapp.api.URLs.URL_GET_RELEASE;
import static myapp.api.URLs.URL_LOGIN;
import static myapp.api.URLs.URL_REGISTER;

public interface RetrofitApi {

    @POST(URL_REGISTER)
    Call<ResponseContainer<User>> addUser(@Body User user);

    @GET(URL_LOGIN)
    Call<ResponseContainer<User>> getUser(@Query("id") int id);

    @GET(URL_GET_ALL_EMPLOYEES)
    Call<ResponseContainer<List<Employee>>> getEmployees();

    @GET(URL_GET_EMPLOYEE)
    Call<ResponseContainer<Employee>> getEmployee(@Query("symbol") String symbol);

    @POST(URL_ADD_EMPLOYEE)
    Call<ResponseContainer<Employee>> addEmployee(@Body Employee employee);

    @GET(URL_GET_ALL_PRODUCTS)
    Call<ResponseContainer<List<Product>>> getProducts();

    @GET(URL_GET_PRODUCT)
    Call<ResponseContainer<Product>> getProduct(@Query("symbol") String symbol);

    @POST(URL_ADD_PRODUCT)
    Call<ResponseContainer<Product>> addProduct(@Body Product product);

    @GET(URL_GET_ALL_RELEASES)
    Call<ResponseContainer<List<Release>>> getReleases();

    @POST(URL_ADD_RELEASE)
    Call<ResponseContainer<Release>> addRelease(@Body Release release);

    @GET(URL_GET_RELEASE)
    Call<ResponseContainer<Release>> getRelease(@Query("id") int id);

    // @Query("apicall") String apiCall
    /*
    @GET("group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    @POST("users/new")
    Call<User> createUser(@Body User user);
    */
}
