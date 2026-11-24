
import java.util.*;

// ================= PRODUCT CLASS =================

class Product {

    private final int id;
    private String name;
    private String category;
    private double price;
    private int stock;

    public Product(int id, String name, String category,
                   double price, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return String.format(
            "%-6d %-22s %-14s %10.2f %6d",
            id, name, category, price, stock
        );
    }
}

// ================= PRODUCT MANAGER CLASS =================

class ProductManager {

    private final Map<Integer, Product> productMap = new HashMap<>();

    private final Map<String, List<Product>> categoryMap = new HashMap<>();

    // Add Product
    public boolean addProduct(Product p) {

        if (productMap.containsKey(p.getId())) {
            return false;
        }

        productMap.put(p.getId(), p);

        categoryMap.computeIfAbsent(
            p.getCategory().toLowerCase(),
            k -> new ArrayList<>()
        ).add(p);

        return true;
    }

    // Search Product by ID using HashMap
    public Product getProductById(int id) {
        return productMap.get(id);
    }

    // Delete Product
    public boolean deleteProduct(int id) {

        Product p = productMap.remove(id);

        if (p == null) {
            return false;
        }

        String category = p.getCategory().toLowerCase();

        List<Product> list = categoryMap.get(category);

        if (list != null) {
            list.remove(p);

            if (list.isEmpty()) {
                categoryMap.remove(category);
            }
        }

        return true;
    }

    // Update Price
    public boolean updatePrice(int id, double newPrice) {

        Product p = productMap.get(id);

        if (p == null) {
            return false;
        }

        p.setPrice(newPrice);
        return true;
    }

    // Update Stock
    public boolean updateStock(int id, int newStock) {

        Product p = productMap.get(id);

        if (p == null) {
            return false;
        }

        p.setStock(newStock);
        return true;
    }

    // Get All Products
    public List<Product> getAllProducts() {
        return new ArrayList<>(productMap.values());
    }

    // Linear Search by Name
    public List<Product> searchByName(String keyword) {

        List<Product> result = new ArrayList<>();

        String key = keyword.toLowerCase();

        for (Product p : productMap.values()) {

            if (p.getName().toLowerCase().contains(key)) {
                result.add(p);
            }
        }

        return result;
    }

    // Search by Category using HashMap
    public List<Product> searchByCategory(String category) {

        List<Product> list = categoryMap.get(
            category.toLowerCase()
        );

        if (list == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(list);
    }

    // Binary Search by Price Range
    public List<Product> searchByPriceRange(double min, double max) {

        List<Product> sorted = sortByPrice();

        int start = lowerBound(sorted, min);

        List<Product> result = new ArrayList<>();

        for (int i = start;
             i < sorted.size() && sorted.get(i).getPrice() <= max;
             i++) {

            result.add(sorted.get(i));
        }

        return result;
    }

    // Lower Bound for Binary Search
    private int lowerBound(List<Product> sorted, double target) {

        int low = 0;
        int high = sorted.size();

        while (low < high) {

            int mid = (low + high) / 2;

            if (sorted.get(mid).getPrice() < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        return low;
    }

    // ================= MERGE SORT =================

    // Sort by Price (Ascending)
    public List<Product> sortByPrice() {

        List<Product> list = getAllProducts();

        mergeSort(list);

        return list;
    }

    private void mergeSort(List<Product> list) {

        if (list.size() < 2) {
            return;
        }

        int mid = list.size() / 2;

        List<Product> left = new ArrayList<>(
            list.subList(0, mid)
        );

        List<Product> right = new ArrayList<>(
            list.subList(mid, list.size())
        );

        mergeSort(left);
        mergeSort(right);

        int i = 0;
        int j = 0;
        int k = 0;

        while (i < left.size() && j < right.size()) {

            if (left.get(i).getPrice() <= right.get(j).getPrice()) {
                list.set(k++, left.get(i++));
            } else {
                list.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            list.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            list.set(k++, right.get(j++));
        }
    }

    // ================= QUICK SORT =================

    // Sort by Name (A-Z)
    public List<Product> sortByName() {

        List<Product> list = getAllProducts();

        quickSort(list, 0, list.size() - 1);

        return list;
    }

    private void quickSort(List<Product> list, int low, int high) {

        if (low >= high) {
            return;
        }

        String pivot = list.get(high).getName().toLowerCase();

        int i = low - 1;

        for (int j = low; j < high; j++) {

            if (list.get(j).getName().toLowerCase().compareTo(pivot) <= 0) {

                i++;

                Collections.swap(list, i, j);
            }
        }

        Collections.swap(list, i + 1, high);

        int p = i + 1;

        quickSort(list, low, p - 1);
        quickSort(list, p + 1, high);
    }

    // ================= INSERTION SORT =================

    // Sort by Stock (Ascending)
    public List<Product> sortByStock() {

        List<Product> list = getAllProducts();

        for (int i = 1; i < list.size(); i++) {

            Product key = list.get(i);

            int j = i - 1;

            while (j >= 0 &&
                   list.get(j).getStock() > key.getStock()) {

                list.set(j + 1, list.get(j));

                j--;
            }

            list.set(j + 1, key);
        }

        return list;
    }

    // Low Stock Products
    public List<Product> lowStockProducts(int threshold) {

        List<Product> result = new ArrayList<>();

        for (Product p : sortByStock()) {

            if (p.getStock() < threshold) {
                result.add(p);
            } else {
                break;
            }
        }

        return result;
    }

    public int size() {
        return productMap.size();
    }
}

// ================= MAIN CLASS =================

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    private static final ProductManager manager = new ProductManager();

    public static void main(String[] args) {

        loadSampleData();

        while (true) {

            printMenu();

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    addProduct();
                    break;

                case 2:
                    display(manager.getAllProducts(), "All Products");
                    break;

                case 3:
                    searchById();
                    break;

                case 4:
                    searchByName();
                    break;

                case 5:
                    searchByCategory();
                    break;

                case 6:
                    searchByPriceRange();
                    break;

                case 7:
                    display(manager.sortByPrice(), "Sorted by Price (Merge Sort)");
                    break;

                case 8:
                    display(manager.sortByName(), "Sorted by Name (Quick Sort)");
                    break;

                case 9:
                    display(manager.sortByStock(), "Sorted by Stock (Insertion Sort)");
                    break;

                case 10:
                    updatePrice();
                    break;

                case 11:
                    updateStock();
                    break;

                case 12:
                    deleteProduct();
                    break;

                case 13:
                    int threshold = readInt("Stock threshold: ");
                    display(
                        manager.lowStockProducts(threshold),
                        "Low Stock Products"
                    );
                    break;

                case 0:
                    System.out.println("Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // Print Menu
    private static void printMenu() {

        System.out.println("\n==============================================");
        System.out.println("     E-COMMERCE PRODUCT MANAGEMENT SYSTEM");
        System.out.println("==============================================");

        System.out.println("1.  Add Product");
        System.out.println("2.  View All Products");
        System.out.println("3.  Search by ID (HashMap)");
        System.out.println("4.  Search by Name (Linear Search)");
        System.out.println("5.  Search by Category (HashMap)");
        System.out.println("6.  Search by Price Range (Binary Search)");
        System.out.println("7.  Sort by Price (Merge Sort)");
        System.out.println("8.  Sort by Name (Quick Sort)");
        System.out.println("9.  Sort by Stock (Insertion Sort)");
        System.out.println("10. Update Price");
        System.out.println("11. Update Stock");
        System.out.println("12. Delete Product");
        System.out.println("13. Low Stock Report");
        System.out.println("0.  Exit");

        System.out.println("==============================================");
    }

    // Add Product
    private static void addProduct() {

        int id = readInt("Enter Product ID: ");

        System.out.print("Enter Product Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter Category: ");
        String category = sc.nextLine().trim();

        double price = readDouble("Enter Price: ");

        int stock = readInt("Enter Stock: ");

        if (id <= 0 || name.isEmpty() || category.isEmpty()
                || price < 0 || stock < 0) {

            System.out.println("Invalid product details!");
            return;
        }

        Product p = new Product(id, name, category, price, stock);

        if (manager.addProduct(p)) {
            System.out.println("Product Added Successfully!");
        } else {
            System.out.println("Product ID Already Exists!");
        }
    }

    // Search by ID
    private static void searchById() {

        int id = readInt("Enter Product ID: ");

        Product p = manager.getProductById(id);

        if (p == null) {
            System.out.println("Product Not Found!");
        } else {
            display(List.of(p), "Search Result");
        }
    }

    // Search by Name
    private static void searchByName() {

        System.out.print("Enter Product Name: ");

        String name = sc.nextLine().trim();

        display(manager.searchByName(name), "Name Search Results");
    }

    // Search by Category
    private static void searchByCategory() {

        System.out.print("Enter Category: ");

        String category = sc.nextLine().trim();

        display(
            manager.searchByCategory(category),
            "Category Search Results"
        );
    }

    // Search by Price Range
    private static void searchByPriceRange() {

        double min = readDouble("Enter Minimum Price: ");

        double max = readDouble("Enter Maximum Price: ");

        if (min < 0 || max < min) {
            System.out.println("Invalid price range!");
            return;
        }

        display(
            manager.searchByPriceRange(min, max),
            "Price Range Results"
        );
    }

    // Update Price
    private static void updatePrice() {

        int id = readInt("Enter Product ID: ");

        double price = readDouble("Enter New Price: ");

        if (price < 0) {
            System.out.println("Price cannot be negative!");
            return;
        }

        if (manager.updatePrice(id, price)) {
            System.out.println("Price Updated Successfully!");
        } else {
            System.out.println("Product Not Found!");
        }
    }

    // Update Stock
    private static void updateStock() {

        int id = readInt("Enter Product ID: ");

        int stock = readInt("Enter New Stock: ");

        if (stock < 0) {
            System.out.println("Stock cannot be negative!");
            return;
        }

        if (manager.updateStock(id, stock)) {
            System.out.println("Stock Updated Successfully!");
        } else {
            System.out.println("Product Not Found!");
        }
    }

    // Delete Product
    private static void deleteProduct() {

        int id = readInt("Enter Product ID: ");

        if (manager.deleteProduct(id)) {
            System.out.println("Product Deleted Successfully!");
        } else {
            System.out.println("Product Not Found!");
        }
    }

    // Display Products
    private static void display(List<Product> list, String title) {

        System.out.println("\n--- " + title + " (" + list.size() + ") ---");

        if (list.isEmpty()) {
            System.out.println("No Products Found!");
            return;
        }

        System.out.printf(
            "%-6s %-22s %-14s %10s %6s%n",
            "ID", "Name", "Category", "Price", "Stock"
        );

        System.out.println("-".repeat(62));

        for (Product p : list) {
            System.out.println(p);
        }
    }

    // Read Integer Safely
    private static int readInt(String prompt) {

        while (true) {

            System.out.print(prompt);

            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please Enter a Valid Integer!");
            }
        }
    }

    // Read Double Safely
    private static double readDouble(String prompt) {

        while (true) {

            System.out.print(prompt);

            try {
                double value = Double.parseDouble(sc.nextLine().trim());

                if (Double.isFinite(value)) {
                    return value;
                }

                System.out.println("Please Enter a Finite Number!");

            } catch (NumberFormatException e) {
                System.out.println("Please Enter a Valid Number!");
            }
        }
    }

    // Load Sample Products
    private static void loadSampleData() {

        manager.addProduct(
            new Product(101, "iPhone 15", "Mobile", 79999, 15)
        );

        manager.addProduct(
            new Product(102, "Samsung Galaxy S24", "Mobile", 74999, 8)
        );

        manager.addProduct(
            new Product(103, "Dell XPS 13", "Laptop", 109999, 5)
        );

        manager.addProduct(
            new Product(104, "HP Pavilion", "Laptop", 58999, 20)
        );

        manager.addProduct(
            new Product(105, "Sony WH-1000XM5", "Audio", 29999, 12)
        );

        manager.addProduct(
            new Product(106, "boAt Rockerz 450", "Audio", 1999, 50)
        );

        manager.addProduct(
            new Product(107, "Nike Air Max", "Footwear", 8999, 3)
        );

        manager.addProduct(
            new Product(108, "Levis Jeans", "Clothing", 2999, 40)
        );

        System.out.println("Sample Products Loaded Successfully!");
    }
}
