package com.example.notificationapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "Confirm Notification";
    private static final int PI_REQ_CODE = 1;
    private static final int OCN_ID = 100;
    ImageView imgBack;
    EditText edtName, edtEmail, edtPhone, edtAddress;
    RadioGroup rgDeliveryType, rgPayType, rgPayApps;
    Button btnConfirmOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        imgBack = findViewById(R.id.imgBack);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_mail);
        edtPhone = findViewById(R.id.edt_number);
        edtAddress = findViewById(R.id.edt_address);

        rgDeliveryType = findViewById(R.id.rg_deliveryType);
        rgPayType = findViewById(R.id.rg_payType);
        rgPayApps = findViewById(R.id.rg_payApps);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edtName.getText().toString();
                String email = edtEmail.getText().toString();
                String phone = edtPhone.getText().toString();
                String address =edtAddress.getText().toString();

                boolean DeliveryType =  rgDeliveryType.getCheckedRadioButtonId() != -1;
                boolean payType = rgPayType.getCheckedRadioButtonId() != -1;
                boolean appPay = rgPayApps.getCheckedRadioButtonId() != -1;
                boolean b = DeliveryType && payType && appPay;

                if (!b || name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()){
                    Toast.makeText(MainActivity.this, "Required all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Intent iSuccess = new Intent(MainActivity.this, OrderSuccessful.class);
                    startActivity(iSuccess);
                }

                SendNotif();

            }
        });


    }

    public void SendNotif(){


        Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.order,null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        assert bitmapDrawable != null;
        Bitmap largeIcon = bitmapDrawable.getBitmap();

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification ;

        Intent iNotify = new Intent(getApplicationContext(), OrderSuccessful.class);
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pi = PendingIntent.getActivity(this,PI_REQ_CODE,iNotify,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle()
                .bigPicture(((BitmapDrawable)(ResourcesCompat.getDrawable(getResources(),R.drawable.malpua,null))).getBitmap())
                .bigLargeIcon(largeIcon)
                .setBigContentTitle("Thank you for your order!")
                .setSummaryText("Your order has been placed and a confirmation message will be sent to your number.");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification =  new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.outline_check_circle_24)
                    .setContentText("Message Order")
                    .setSubText("Your Order is Confirmed !")
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pi)
                    .setStyle(bigPictureStyle)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .build();
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"OCN Channel",NotificationManager.IMPORTANCE_HIGH));
        }else {
            notification =  new Notification.Builder(this)
                   .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.outline_check_circle_24)
                    .setContentText("Message Order")
                    .setSubText("Your Order is Confirmed !")
                    .setContentIntent(pi)
                    .setStyle(bigPictureStyle)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .build();
        }
        nm.notify(OCN_ID,notification);
    }
}
