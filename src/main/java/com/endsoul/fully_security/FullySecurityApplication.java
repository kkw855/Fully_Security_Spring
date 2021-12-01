package com.endsoul.fully_security;

import com.endsoul.fully_security.jwt.JsonAuthorities;
import com.endsoul.fully_security.jwt.JsonRole;
import com.fasterxml.jackson.databind.Module;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import io.vavr.jackson.datatype.VavrModule;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@AllArgsConstructor
class UserSimple {
  final String name;
  final String email;
  final int age;
  final boolean isDeveloper;
}

@AllArgsConstructor
class UserNested {
  final String name;
  final String email;
  final int age;
  final boolean isDeveloper;

  // new, see below!
  UserAddress userAddress;
}

@AllArgsConstructor
class UserAddress {
  String street;
  String houseNumber;
  String city;
  String country;
}

@AllArgsConstructor
class RestaurantWithMenu {
  String name;

  List<RestaurantMenuItem> menu;
  // RestaurantMenuItem[] menu; // alternative, either one is fine
}

@AllArgsConstructor
class RestaurantMenuItem {
  String description;
  float price;
}

@AllArgsConstructor
class Founder {
  String name;
  int flowerCount;
}

@AllArgsConstructor
class GeneralInfo {
  private final String name;
  private final String website;
  private final List<Founder> founders;
}

class AmountWithCurrency {
  private String currency;
  private int amount;
}

class MyAmountWithCurrency {
  String name;
  HashMap<String, AmountWithCurrency> amountWithCurrency;
}

class AuthoritiesDeserializer implements JsonDeserializer<JsonAuthorities> {

  @Override
  public JsonAuthorities deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {

    final JsonArray roles = json.getAsJsonObject().get("authorities").getAsJsonArray();
    java.util.List<JsonRole> role = new ArrayList<JsonRole>();
    for (JsonElement e : roles) {
      final JsonObject obj = e.getAsJsonObject();
      role.add(new JsonRole(obj.get("role").getAsString()));
    }

    JsonAuthorities authorities = new JsonAuthorities(role);
    return authorities;
  }
}

@SpringBootApplication
public class FullySecurityApplication {

  public static void main(String[] args) {
    UserSimple userObject = new UserSimple("Norman", "norman@futurestud.io", 26, true);

    Gson gson = new Gson();
    gson.newBuilder()
        .registerTypeAdapter(JsonAuthorities.class, new AuthoritiesDeserializer())
        .create();

    final String userJson = gson.toJson(userObject);
    final UserSimple userObject2 =
        gson.fromJson(
            "{'age':26,'email':'norman@futurestud.io','isDeveloper':true,'name':'Norman'}",
            UserSimple.class);

    UserAddress userAddress = new UserAddress("Main Street", "42A", "Magdeburg", "Germany");

    UserNested userNestedObject =
        new UserNested("Norman", "norman@futurestud.io", 26, true, userAddress);

    final String userWithAddressJson = gson.toJson(userNestedObject);

    List<RestaurantMenuItem> menu = new ArrayList<>();
    menu.add(new RestaurantMenuItem("Spaghetti", 7.99f));
    menu.add(new RestaurantMenuItem("Steak", 12.99f));
    menu.add(new RestaurantMenuItem("Salad", 5.99f));

    RestaurantWithMenu restaurant = new RestaurantWithMenu("Future Studio Steak House", menu);

    String restaurantJson = gson.toJson(restaurant);
    String menuJson = gson.toJson(menu);

    String founderJson =
        "[{'name': 'Christian','flowerCount': 1}, {'name': 'Marcus', 'flowerCount': 3}, {'name': 'Norman', 'flowerCount': 2}]";
    Founder[] founderArray = gson.fromJson(founderJson, Founder[].class);

    final Type founderListType = new TypeToken<ArrayList<Founder>>() {}.getType();

    List<Founder> founderList = gson.fromJson(founderJson, founderListType);

    String generalInfoJson =
        "{'name': 'Future Studio Dev Team', 'website': 'https://futurestud.io', 'founders': [{'name': 'Christian', 'flowerCount': 1 }, {'name': 'Marcus','flowerCount': 3 }, {'name': 'Norman','flowerCount': 2 }]}";
    GeneralInfo generalInfoObject = gson.fromJson(generalInfoJson, GeneralInfo.class);

    HashMap<String, List<String>> employees = new HashMap<>();
    employees.put("A", Arrays.asList("Andreas", "Arnold", "Aden"));
    employees.put("C", Arrays.asList("Christian", "Carter"));
    employees.put("M", Arrays.asList("Marcus", "Mary"));
    String employeeJson = gson.toJson(employees);

    String dollarJson =
        "{ '1$': { 'amount': 1, 'currency': 'Dollar'}, '2$': { 'amount': 2, 'currency': 'Dollar'}, '3€': { 'amount': 3, 'currency': 'Euro'} }";
    Type amountCurrencyType = new TypeToken<HashMap<String, AmountWithCurrency>>() {}.getType();
    HashMap<String, AmountWithCurrency> amountCurrency =
        gson.fromJson(dollarJson, amountCurrencyType);

    String myDollarJson =
        "{ 'name': 'myName', 'amountWithCurrency': { '1$': { 'amount': 1, 'currency': 'Dollar'}, '2$': { 'amount': 2, 'currency': 'Dollar'}, '3€': { 'amount': 3, 'currency': 'Euro'} } }";
    final MyAmountWithCurrency myAmountWithCurrency =
        gson.fromJson(myDollarJson, MyAmountWithCurrency.class);

    SpringApplication.run(FullySecurityApplication.class, args);
  }

  // @RestController 에서 HTTP 요청과 응답에 Vavr 자료형을 사용했을 때 정상적으로 converting 해준다.
  @Bean
  Module vavrModule() {
    return new VavrModule();
  }
}
