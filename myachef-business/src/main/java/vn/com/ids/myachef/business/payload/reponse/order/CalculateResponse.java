package vn.com.ids.myachef.business.payload.reponse.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculateResponse {
    private double totalAmount; // Tổng tiền hàng

    private double shipFee;

    private double totalMoneyDiscount;

    private double shipDiscount;

    private long totalMammyCoinReceive;

    private double totalOldProductPrice;

    private double totalShipFee;

    private double totalPayment;// Tổng số tiền

    private long coinDiscount;

    private boolean isPriceChangedNotification;
}
