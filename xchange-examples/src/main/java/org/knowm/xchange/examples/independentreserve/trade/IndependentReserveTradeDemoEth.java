package org.knowm.xchange.examples.independentreserve.trade;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.examples.independentreserve.IndependentReserveDemoUtils;
import org.knowm.xchange.service.polling.trade.PollingTradeService;

/**
 * Author: Aleksey Baryshnikov Date: 8/31/2016. The logic should be refactored to define supported primary and secondary currencies. Now I had to
 * manually add ETH everywhere BTC was hardcoded. But this will still fail if you traded NZD or AUD currencies even for Bitcoin.
 */
public class IndependentReserveTradeDemoEth {
  public static void main(String[] args) throws IOException, InterruptedException {

    Exchange independentReserve = IndependentReserveDemoUtils.createExchange();
    PollingTradeService tradeService = independentReserve.getPollingTradeService();

    generic(tradeService);
  }

  private static void generic(PollingTradeService tradeService) throws IOException, InterruptedException {
    printOpenOrders(tradeService);

    // place a limit buy order
    LimitOrder limitOrder = new LimitOrder((Order.OrderType.ASK), new BigDecimal(".01"), CurrencyPair.ETH_USD, "", null, new BigDecimal("1000.00"));
    String limitOrderReturnValue = tradeService.placeLimitOrder(limitOrder);
    System.out.println("Limit Order return value: " + limitOrderReturnValue);

    printOpenOrders(tradeService);

    // Cancel the added order
    boolean cancelResult = tradeService.cancelOrder(limitOrderReturnValue);
    System.out.println("Canceling returned " + cancelResult);

    printOpenOrders(tradeService);

    UserTrades tradeHistory = tradeService.getTradeHistory(tradeService.createTradeHistoryParams());
    System.out.println("Trade history: " + tradeHistory.toString());
  }

  private static void printOpenOrders(PollingTradeService tradeService) throws IOException, InterruptedException {
    // IR API caches data for some time, so we can get the same set of orders as we saw before
    TimeUnit.SECONDS.sleep(1);
    OpenOrders openOrders = tradeService.getOpenOrders();
    System.out.println("Open Orders: " + openOrders.toString());
  }
}
