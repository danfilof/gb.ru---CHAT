package Tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class App01Test {
    private App01 app01;

    @BeforeEach
    public void init() {
        app01 = new App01();
    }

    @CsvSource({
            "{1, 2, 4, 4, 2, 3, 4, 1, 7}, [1, 7]",
            "{1, 2, 4, 4, 2, 4, 2, 1, 7}, [2, 1, 7]",
            "{1, 2, 4, 4, 2, 4, 2, 4, 7}, [7]"
    })


    @ParametrizedTest
    void taskOneMethod(Integer[] arr, Collection arr1) {
        Assertions.assertEquals(arr1, App01.taskOneMethod(arr));

    }
}