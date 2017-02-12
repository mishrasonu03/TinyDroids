package com.android.sonumishra.justjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private int quantity = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        int price = calculatePrice();
        CheckBox toppingCheckBox = (CheckBox) findViewById(R.id.topping_checkbox);
        boolean hasWhippedCream = toppingCheckBox.isEnabled();
        String priceMessage = createOrderSummary(price, hasWhippedCream);
        displayMessage(priceMessage);
    }

    /**
     * This method calculates the price of order
     */
    public int calculatePrice() {
        return quantity * 5;
    }

    /**
     * This method generates the summary message
     */
    public String createOrderSummary(int price, boolean hasWhippedCream) {
        return "Name: Sonu Mishra" +
                "\nQuantity: " + quantity +
                "\nWhipped Cream: " + (hasWhippedCream ? "Yes" : "No") +
                "\nTotal: " + price +
                "\nThank You!";
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        quantity++;
        display(quantity);
        displayPrice(calculatePrice());
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        if(quantity <= 0) return;
        quantity--;
        display(quantity);
        displayPrice(calculatePrice());
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given price on the screen.
     */
    private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.order_summary_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }
}
