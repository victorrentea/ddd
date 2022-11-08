package victor.training.ddd.agile;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FlorinTest {
//    public static void main(String[] args) {
//        List<String> input;
//        Set<String> whatISaw;
//        for (String s : input) {
//            if (whatISaw.add(s.toLowerCase())) {
//                skip;//
//            }
//        }
//    }
    @Test
    void test() {
        var uppercase = "JACK";

//        if (uppercase == uppercase.toUpperCase()) {
//            //alread uppercase.
//        }
        var uppercaseUpperCased = uppercase.toUpperCase(); // JACK
        assertTrue(uppercaseUpperCased == uppercase.toUpperCase());

        var lowercase = "Jack";
        var lowercaseUpperCased = lowercase.toUpperCase(); // JACK
        assertFalse(lowercaseUpperCased == lowercase.toUpperCase());
        assertTrue(lowercaseUpperCased == lowercaseUpperCased.toUpperCase());
    }
}
