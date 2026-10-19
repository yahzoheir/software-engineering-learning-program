import java.util.HashMap;
import java.util.Map;

public class RentalService {
    private static final int MAX_ACTIVE_RENTALS = 3;

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

    public Movie addMovie(String title, int availableCopies) {
        Movie movie = new Movie(nextMovieId, title, availableCopies);
        movies.put(nextMovieId, movie);
        nextMovieId++;
        return movie;
    }

    public Movie getMovieById(int id) {
        if(!movies.containsKey(id)) {
            throw new IllegalArgumentException("No movie exists with this id");
        }
        return movies.get(id);
    }

    public Rental rentMovie(int customerId, int movieId) {
        Customer customer = getCustomerById(customerId);
        Movie movie = getMovieById(movieId);

        if (movie.getAvailableCopies() == 0) {
            throw new IllegalStateException("There are no available copies of this movie");
        }

        if (customer.getActiveRentals().size() >= MAX_ACTIVE_RENTALS) {
            throw new IllegalStateException("Customer has reached the maximum number of active rentals");
        }

        Rental rental = new Rental(nextRentalId, customer, movie);
        rentals.put(nextRentalId, rental);
        customer.addActiveRental(rental);
        movie.rentCopy();
        nextRentalId++;

        return rental;
    }



}
