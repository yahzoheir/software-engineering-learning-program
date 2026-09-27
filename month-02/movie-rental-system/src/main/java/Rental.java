

public class Rental {
    private final int id;
    private final Customer customer;
    private final Movie movie;
    private boolean returned;

    public Rental(int id, Customer customer, Movie movie) {
        this.id = id;
        this.customer = customer;
        this.movie = movie;
        this.returned = false;
    }

    public int getId() {
        return this.id;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public Movie getMovie() {
        return this.movie;
    }

    public boolean isReturned() {
        return this.returned;
    }

    public void markReturned() {
        if (this.returned == true) {
            throw new IllegalStateException("Rental is already returned");
        }
        this.returned = true;
    }
}