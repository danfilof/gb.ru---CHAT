package Tasks;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalcTest {
    private Calculator calculator;




    public void test() {

    }

    @Test
    void add() {
        calculator = new Calculator();
        Assertions.assertEquals(4, calculator.add(2, 2));

    }

    @Test
    void sub() {
        calculator = new Calculator();
        Assertions.assertEquals(3, calculator.sub(5, 2));

    }

    @Test
    void mul() {
    }

    @Test
    void div() {
    }
}

