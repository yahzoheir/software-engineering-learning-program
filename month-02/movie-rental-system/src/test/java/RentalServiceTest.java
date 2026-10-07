import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class RentalServiceTest {

    @Test 
    void registerCustomerCreatesCustomerWithIdAndName() {
        RentalService service = new RentalService();

        Customer customer = service.registerCustomer("Yahia");

        assertEquals(1, customer.getId());
        assertEquals("Yahia", customer.getName());
    }


    @Test 
    void registerCustomerAssignsConsecutiveIds() {
        RentalService service = new RentalService();
        Customer customer1 = service.registerCustomer("Yahia");
        Customer customer2 = service.registerCustomer("Younes");

        assertEquals(1, customer1.getId());
        assertEquals(2, customer2.getId());
    }

    @Test 
    void getCustomerByIdReturnsRegisteredCustomer() {
        RentalService service = new RentalService();
        Customer customer = service.registerCustomer("Yahia");

        assertSame(customer, service.getCustomerById(1));
    }

    @Test 
    void getCustomerByIdRejectsUnknownId() {
        RentalService service = new RentalService();
        assertThrows(IllegalArgumentException.class, () -> {
            service.getCustomerById(3);
        });
    }

    @Test 
    void registerCustomerRejectsNullOrBlankName() {
        RentalService service = new RentalService();
        
        assertThrows(IllegalArgumentException.class, () -> {
            service.registerCustomer(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            service.registerCustomer("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            service.registerCustomer("  ");
        });
    }

    @Test
    void registerCustomerDoesNotConsumeIdWhenNameIsInvalid() {
    RentalService service = new RentalService();

    assertThrows(IllegalArgumentException.class, () -> {
        service.registerCustomer(null);
    });

    Customer customer = service.registerCustomer("Yahia");

    assertEquals(1, customer.getId());
    }

    @Test 
    void addMovieCreatesMovieWithIdTitleAndAvailableCopies() {
        RentalService service = new RentalService(); 

        Movie movie = service.addMovie("Inception", 2);

        assertEquals(1, movie.getId());
        assertEquals("Inception", movie.getTitle());
        assertEquals(2, movie.getAvailableCopies());
    }

    @Test 
    void addMovieAssignsConsecutiveIds() {
        RentalService service = new RentalService(); 

        Movie movie1 = service.addMovie("Inception", 2);
        Movie movie2 = service.addMovie("Fast and Furious", 4);
       assertEquals(1, movie1.getId());
       assertEquals(2, movie2.getId());
    }

    @Test 
    void getMovieByIdReturnsAddedMovie() {
        RentalService service = new RentalService();

        Movie movie1 = service.addMovie("Inception", 2);
        assertSame(movie1, service.getMovieById(1)); 
    }

    @Test 
    void getMovieByIdRejectsUnknownId() {
        RentalService service = new RentalService();
        assertThrows(IllegalArgumentException.class, () -> {
            service.getMovieById(1);
        });
    }

    @Test
    void addMovieRejectsNullEmptyAndBlankTitle() {
    RentalService service = new RentalService();

        assertThrows(IllegalArgumentException.class, () -> {
            service.addMovie(null, 2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            service.addMovie("", 2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            service.addMovie("   ", 2);
        });
    }

    @Test 
    void addMovieRejectsNegativeAvailableCopies() {
        RentalService service = new RentalService();
        assertThrows(IllegalArgumentException.class, () -> {
            service.addMovie("Inception", -1);
        });
    }

    @Test
    void addMovieDoesNotConsumeIdWhenAvailableCopiesAreInvalid() {
    RentalService service = new RentalService();

    assertThrows(IllegalArgumentException.class, () -> {
        service.addMovie("Inception", -1);
    });

    Movie movie = service.addMovie("Inception", 2);

    assertEquals(1, movie.getId());
    }

    @Test
    void addMovieAllowsZeroAvailableCopies() {
        RentalService service = new RentalService();

        Movie movie = service.addMovie("Inception", 0);

        assertEquals(0, movie.getAvailableCopies());
    }
    
    



     


}
