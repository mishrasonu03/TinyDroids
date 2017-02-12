package com.android.sonumishra.justjava;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private int quantity = 1;
    private String[] emailID = new String[]{"mishrasonu1993@gmail.com"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText nameEditText = (EditText) findViewById(R.id.name_edit_text);
        String name = nameEditText.getText().toString();
        if(name == null || name.length() == 0) {
            Toast.makeText(getApplicationContext(), "please write your name",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        CheckBox creamCheckBox = (CheckBox) findViewById(R.id.cream_checkbox);
        boolean hasWhippedCream = creamCheckBox.isChecked();

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, name, hasWhippedCream, hasChocolate);
        String subjectMessage = "Order for " + name;

        composeEmail(emailID, subjectMessage, priceMessage);
    }

    /**
     * This method is sends the order summary through email.
     */
    public void composeEmail(String[] addresses, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method calculates the price of order
     */
    public int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int basePrice = 5 + (hasWhippedCream ? 1 : 0) + (hasChocolate ? 2 : 0);
        return quantity * basePrice;
    }

    /**
     * This method generates the summary message
     */
    public String createOrderSummary(int price, String name, boolean hasWhippedCream, boolean hasChocolate) {
        return "Name: " + name +
                "\nQuantity: " + quantity +
                "\nWhipped Cream: " + (hasWhippedCream ? "Yes" : "No") +
                "\nChocolate: " + (hasChocolate ? "Yes" : "No") +
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
        if(quantity == 100) {
            Toast.makeText(getApplicationContext(), "cannot order more than 100 cups of coffees",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        quantity++;
        display(quantity);
        //displayPrice(calculatePrice());
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        if(quantity == 1) {
            Toast.makeText(getApplicationContext(), "cannot order less than 1 cup of coffee",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        quantity--;
        display(quantity);
        //displayPrice(calculatePrice());
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
