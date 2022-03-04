package Tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class App02Test {
    private App02 app02;


    @BeforeEach
    public void init() {
         app02 = new App02();
    }

    @CsvSource({
            "[1,1,1,4,4,1,4,4], true",
            "[1, 1, 1, 1, 1,1], false",
            "[4, 4, 4, 4], false"
    })

    @ParametrizedTest
    void taskTwoTest(int[] arr, boolean result) {
        Assertions.assertEquals(result, App02.taskTwo(arr));
    }
}