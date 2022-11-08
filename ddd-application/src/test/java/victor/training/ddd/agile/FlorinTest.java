package victor.training.ddd.agile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FlorinTest {
    @Test
    void test() {
        var uppercase = "JACK";
        var uppercaseUpperCased = uppercase.toUpperCase(); // JACK
        assertTrue(uppercaseUpperCased == uppercase.toUpperCase());

        var lowercase = "Jack";
        var lowercaseUpperCased = lowercase.toUpperCase(); // JACK
        assertFalse(lowercaseUpperCased == lowercase.toUpperCase());
        assertTrue(lowercaseUpperCased == lowercaseUpperCased.toUpperCase());
    }
}
