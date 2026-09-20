

public class BookPricing {
    /*  Created the minimum implementation (yes looks stupid but the test now passes so we've reached GREEN)

    public static double calculateDiscountedPrice(double price, int discountPercent) {
        return 80;
    } 
        
    */

    // Improved version that works normally. And now back to GREEN

    public static double calculateDiscountedPrice (double price, int discountPercent) {
        if (price < 0) {
        throw new IllegalArgumentException ("Price cannot be negative");
        }

        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        return price - (price * discountPercent/100.0) ;
        
    }
}
