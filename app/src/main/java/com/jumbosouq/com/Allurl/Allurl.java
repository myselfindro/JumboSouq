package com.jumbosouq.com.Allurl;

public class Allurl {

    public static String baseUrl;
    public static String ForgotPassword;
    public static String Weeklydeals;
    public static String AdminLogin;
    public static String Productdetailsl;
    public static String Searh;
    public static String Category;
    public static String Addtocart;
    public static String CreateCart;
    public static String EditCart;
    public static String DeleteCart;
    public static String CartList;
    public static String Subscribe;





    static {
        baseUrl = "https://www.jumbosouq.com";
        ForgotPassword = baseUrl +"/rest/default/V1/customers/password";
        Weeklydeals = baseUrl+"/rest/default/V1/products?";
        AdminLogin = baseUrl+"/rest/default/V1/integration/admin/token";
        Productdetailsl = baseUrl+"/rest/default/V1/products/";
        Searh = baseUrl+"/rest/default/V1/products?";
        Category = baseUrl+"/rest/default/V1/categories";
        Addtocart = baseUrl+"/rest/default/V1/carts/mine/items";
        CreateCart = baseUrl+"/rest/default/V1/carts/mine";
        EditCart = baseUrl+"/rest/default/V1/carts/mine/items/";
        DeleteCart = baseUrl+"/rest/default/V1/carts/mine/items/";
        CartList = baseUrl+"/rest/default/V1/carts/mine/items";
        Subscribe = baseUrl+"/rest/default/V1/customers/me";



    }

}
