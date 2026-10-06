import java.util.HashMap;
import java.util.Map;

public class RentalService {
    private final Map<Integer, Customer> customers = new HashMap<>();
    private final Map<Integer, Movie> movies = new HashMap<>();
    private final Map<Integer, Rental> rentals = new HashMap<>();
    private int nextCustomerId = 1;
    private int nextMovieId = 1;
    private int nextRentalId = 1;

    public Customer registerCustomer(String name) {
        Customer newCustomer = new Customer(nextCustomerId, name);
        this.customers.put(nextCustomerId, newCustomer);
        nextCustomerId++;
        return newCustomer;
    }

    public Customer getCustomerById(int id) {
        if (!this.customers.containsKey(id)) {
            throw new IllegalArgumentException("There is no registered customer with this id");
        }
        return this.customers.get(id);
    }

}
