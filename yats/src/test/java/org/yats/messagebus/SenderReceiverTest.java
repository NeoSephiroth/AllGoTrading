package org.yats.messagebus;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.yats.common.Decimal;
import org.yats.messagebus.messages.PriceDataMsg;
import org.yats.messagebus.messages.PositionSnapshotMsg;
import org.yats.trading.AccountPosition;
import org.yats.trading.PriceData;
import org.yats.trading.PositionSnapshot;


public class SenderReceiverTest {

    // can only be executed successfully if RabbitMQ server ip is set correctly
//    private static final String CATCH_ALL_TOPIC = "#";


    @Test(groups = { "inMemory" })
    public void canSendAndReceivePriceData()
    {
        senderPriceData.publish(dataMsg.getTopic(), dataMsg);
        PriceDataMsg newDataMsg = receiverPriceData.tryReceive(1000);
        PriceData newData = newDataMsg.toPriceData();

        assert (newDataMsg!=null);
        assert (newDataMsg.isSameAs(dataMsg));
        assert (newData.isSameAs(data));
    }

    @Test(groups = { "inMemory" })
    public void canSendAndReceivePositionSnapshot()
    {
        PositionSnapshotMsg positionSnapshotMsg = PositionSnapshotMsg.fromPositionSnapshot(positionSnapshot);
        senderPositionSnapshot.publish(positionSnapshotMsg.getTopic(), positionSnapshotMsg);
        PositionSnapshotMsg data = receiverPositionSnapshot.tryReceive(1000);
        PositionSnapshot newPositionSnapshot = data.toPositionSnapshot();

        assert (newPositionSnapshot!=null);
        assert (data.isSameAs(positionSnapshotMsg));
        assert (newPositionSnapshot.isSameAs(positionSnapshot));
    }


    @BeforeMethod(groups = { "inMemory" })
    public void setUp() {
        Config config = Config.DEFAULT_FOR_TESTS;
        senderPriceData = new Sender<PriceDataMsg>(config.getExchangePriceData(),config.getServerIP());
        receiverPriceData = new Receiver<PriceDataMsg>(
                PriceDataMsg.class,
                config.getExchangePriceData(),
                config.getTopicCatchAll(),
                config.getServerIP());
        data = new PriceData(new DateTime(DateTimeZone.UTC), "test",
                Decimal.fromDouble(11), Decimal.fromDouble(12), Decimal.fromDouble(12),
                Decimal.fromDouble(20), Decimal.fromDouble(30), Decimal.ONE );
        dataMsg = PriceDataMsg.createFrom(data);

        senderPositionSnapshot = new Sender<PositionSnapshotMsg>(config.getExchangePositionSnapshot(),config.getServerIP());
        receiverPositionSnapshot = new Receiver<PositionSnapshotMsg>(
                PositionSnapshotMsg.class,
                config.getExchangePositionSnapshot(),
                config.getTopicCatchAll(),
                config.getServerIP()
        );
        positionSnapshot = new PositionSnapshot();
        positionSnapshot.add(new AccountPosition("product1", "account1", Decimal.fromDouble(11)));
        positionSnapshot.add(new AccountPosition("product2", "account1", Decimal.fromDouble(12)));
        positionSnapshot.add(new AccountPosition("product1", "account2", Decimal.fromDouble(13)));
    }

    @AfterMethod
    public void tearDown() {
        senderPriceData.close();
        senderPositionSnapshot.close();
        receiverPriceData.close();
        receiverPositionSnapshot.close();
    }

    private Sender<PriceDataMsg> senderPriceData;
    private Receiver<PriceDataMsg> receiverPriceData;
    private PriceData data;
    private PriceDataMsg dataMsg;

    private PositionSnapshot positionSnapshot;
    private Sender<PositionSnapshotMsg> senderPositionSnapshot;
    private Receiver<PositionSnapshotMsg> receiverPositionSnapshot;


} // class
