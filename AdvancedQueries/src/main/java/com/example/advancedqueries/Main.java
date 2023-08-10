package com.example.advancedqueries;

import com.example.advancedqueries.entities.Ingredient;
import com.example.advancedqueries.entities.Shampoo;
import com.example.advancedqueries.services.IngredientService;
import com.example.advancedqueries.services.ShampooService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class Main implements CommandLineRunner {

    private final ShampooService shampooService;
    private final IngredientService ingredientService;

    public Main(ShampooService shampooService, IngredientService ingredientService) {
        this.shampooService = shampooService;
        this.ingredientService = ingredientService;
    }

    @Override
    public void run(String... args) throws Exception {

//        findShampooByBrand();
//        findBySize();
//        findBySizeOrLabel();
//        findByPriceHigherThanGiven();
//        findIngredientsByNameStartingWith();
//        findIngredientsByName();
//        findCountOfShampoosByPriceLowerThanGiven();
//        getAllShampoosWithIngredientsGiven();
//        countOfShampooIngredientsLessThanGivenCount();
//        deleteIngredientByName();
//        updateIngredientsPrices();
//        updatePriceOfIngredientsWithGivenNames();
    }

    private void updatePriceOfIngredientsWithGivenNames() {

        Scanner scanner = new Scanner(System.in);

        String name = scanner.nextLine();

        List<String> names = new ArrayList<>();

        while (!name.isBlank()) {
           names.add(name);

           name = scanner.nextLine();
        }

        ingredientService.updatePriceWithNamesGiven(names);
    }

    private void updateIngredientsPrices() {

        ingredientService.updateIngredientsPrices();
    }

    private void deleteIngredientByName() {

        Scanner scanner = new Scanner(System.in);

        String name = scanner.nextLine();

        ingredientService.deleteIngredientByName(name);
    }

    private void countOfShampooIngredientsLessThanGivenCount() {

        Scanner scanner = new Scanner(System.in);

        int givenCount = Integer.parseInt(scanner.nextLine());

        for (Shampoo shampoo :  shampooService.countShampooIngredientsLessThan(givenCount)) {
            System.out.println(shampoo.getBrand());
        }

    }

    private void getAllShampoosWithIngredientsGiven() {

        Scanner scanner = new Scanner(System.in);

        String name = scanner.nextLine();

        Set<String> ingredients = new HashSet<>();

        while (!name.isBlank()) {
            ingredients.add(name);

            name = scanner.nextLine();
        }

        for (Shampoo shampoo : shampooService.getShampoosWithIngredients(ingredients)) {
            System.out.println(shampoo.getBrand());
        }

    }

    private void findCountOfShampoosByPriceLowerThanGiven() {

        Scanner scanner = new Scanner(System.in);

        BigDecimal price = new BigDecimal(scanner.nextLine());

        System.out.println(shampooService.findCountByPriceLowerThanGiven(price));
    }

    private void findIngredientsByName() {

        Scanner scanner = new Scanner(System.in);

        String name = scanner.nextLine();

        List<String> names = new ArrayList<>();

        while (!name.isBlank()) {
            names.add(name);

            name = scanner.nextLine();
        }

        for (Ingredient ingredient : ingredientService.findByName(names)) {
            System.out.println(ingredient.getName());
        }

    }

    private void findIngredientsByNameStartingWith() {

        Scanner scanner = new Scanner(System.in);

        String startingWith = scanner.nextLine();

        for (Ingredient ingredient : ingredientService.findByNameStartingWith(startingWith)) {
            System.out.println(ingredient.getName());
        }
    }

    private void findByPriceHigherThanGiven() {

        Scanner scanner = new Scanner(System.in);

        BigDecimal price = new BigDecimal(scanner.nextLine());

        for (Shampoo shampoo : shampooService.findByPriceHigherThanGiven(price)) {
            System.out.printf("%s %s %.2flv.%n", shampoo.getBrand(), shampoo.getSize(), shampoo.getPrice());
        }

    }

    private void findBySizeOrLabel() {

        Scanner scanner = new Scanner(System.in);

        String size = scanner.nextLine();
        Long labelId = Long.parseLong(scanner.nextLine());

        for (Shampoo shampoo : shampooService.findBySizeOrLabel(size, labelId)) {
            System.out.printf("%s %s %.2flv.%n", shampoo.getBrand(), shampoo.getSize(), shampoo.getPrice());
        }

    }

    private void findBySize() {

        Scanner scanner = new Scanner(System.in);

        String size = scanner.nextLine();

        for (Shampoo shampoo : shampooService.findBySize(size.toUpperCase())) {
            System.out.printf("%s %s %.2flv.%n", shampoo.getBrand(), size, shampoo.getPrice());
        }
    }

    private void findShampooByBrand() {
        for (Shampoo shampoo : shampooService.findByBrand("Silk Comb")) {
            System.out.println(shampoo.getId());
        }
    }
}
