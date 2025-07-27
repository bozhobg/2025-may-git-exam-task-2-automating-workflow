package bg.softuni;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BasicCalcTest {

  private static final int A = 1;
  private static final int B = 2;

  private final BasicCalc calc = new BasicCalc();

  @Test
  public void testAddition() {
    Assertions.assertEquals(A + B, calc.add(A, B));
  }
}
