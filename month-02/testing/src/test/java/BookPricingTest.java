import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
// Ensure JUnit 5 (Jupiter) is in your classpath

public class BookPricingTest {
    /*Step 1: at this step before creating BookPricing therefore it doesn't 
              exist so we run the test and it fails, this is RED. */
    @Test 
    void shouldApplyDiscount() {
        double result = BookPricing.calculateDiscountedPrice(100,20);

        assertEquals(80, result);
    }

    /* This test will make us go back to red since it at this point 
        we are wroking with the minimum implementation that just returns 80 so we're back to RED */ 
    
    @Test 
    void shouldReturnOrginalPriceWhenDiscount0 () {
        double result = BookPricing.calculateDiscountedPrice(50,0 );
        assertEquals(50, result);
    }

    @Test 
    void shouldReturn0WhenDiscountIs100 () {
        double result = BookPricing.calculateDiscountedPrice(80,100 );
        assertEquals(0, result);
    }

     
    @Test
    void shouldThrowWhenPriceIsNegative() {
        assertThrows(
        IllegalArgumentException.class,
        () -> BookPricing.calculateDiscountedPrice(-10, 20)
        );
    }

    @Test 
    void shouldThrowWhenDiscountIsLessThan0() {
        assertThrows(
            IllegalArgumentException.class,
            () -> BookPricing.calculateDiscountedPrice(80, -1)
        );
    }

    @Test 
    void shouldThrowWhenDiscountIsMoreThan100() {
        assertThrows(
            IllegalArgumentException.class,
            () -> BookPricing.calculateDiscountedPrice(80, 101)
        );
    }

    
}
