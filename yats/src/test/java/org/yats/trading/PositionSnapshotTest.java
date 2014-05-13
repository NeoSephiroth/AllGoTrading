package org.yats.trading;

import org.testng.annotations.BeforeMethod;
import org.yats.common.Decimal;


public class PositionSnapshotTest {

//    @Test
//    public void canGetProductAccountPosition()
//    {
//        ProductAccountPosition p = positionSnapshot.getProductAccountPosition("prod1", "account1");
//        assert (p.getSize() == 1);
//    }


    @BeforeMethod
    public void setUp() {
        positionSnapshot = new PositionSnapshot();
        position1 = new ProductAccountPosition("prod1", "account1", Decimal.ONE);
        position2 = new ProductAccountPosition("prod2", "account1", Decimal.fromDouble(2));
        position3 = new ProductAccountPosition("prod3", "account1", Decimal.fromDouble(3));
        positionSnapshot.add(position1);
        positionSnapshot.add(position2);
        positionSnapshot.add(position3);
    }

    PositionSnapshot positionSnapshot;
    ProductAccountPosition position1;
    ProductAccountPosition position2;
    ProductAccountPosition position3;

} // class
