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

}
