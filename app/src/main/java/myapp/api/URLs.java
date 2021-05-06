package myapp.api;

public class URLs {
    public static final String BASE_URL = "http://10.0.5.75:81/Praca/";

    private static final String ROOT_URL = BASE_URL + "Api.php?apicall="; // 10.0.5.137 10.0.5.75 /AndroidMySQLCrudTutorial/HeroApi/v1/

    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN = ROOT_URL + "login";
    public static final String URL_GET_PRODUCT = ROOT_URL + "get_product";
    public static final String URL_GET_EMPLOYEE = ROOT_URL + "get_employee";
    public static final String URL_ADD_PRODUCT = ROOT_URL + "add_product";
    public static final String URL_GET_ALL_PRODUCTS = ROOT_URL + "get_all_products";
    public static final String URL_GET_ALL_EMPLOYEES = ROOT_URL + "get_all_employees";
    public static final String URL_GET_ALL_RELEASES = ROOT_URL + "get_all_releases";
    public static final String URL_ADD_EMPLOYEE= ROOT_URL + "add_employee";
    public static final String URL_ADD_RELEASE= ROOT_URL + "add_release";
    public static final String URL_GET_RELEASE= ROOT_URL + "get_release";
    public static final String URL_GET_PRODUCTS_ORDERS= ROOT_URL + "get_products_order";

    //public static final String URL_ADD_QUANTITY = ROOT_URL + "add_quantity";

}