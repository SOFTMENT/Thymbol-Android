package com.softment.thymbol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softment.thymbol.Model.Voucher;
import com.softment.thymbol.Utils.Services;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.makeramen.roundedimageview.RoundedImageView;

public class ShowVoucherActivity extends AppCompatActivity {

    private Voucher voucher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_voucher);

        voucher = (Voucher) getIntent().getSerializableExtra("voucher");


        if (voucher == null) {
            finish();
        }

        ImageView qrCode = findViewById(R.id.qrCode);
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(voucher.getVoucherCode().isEmpty() ? voucher.getVoucherRedeemId() : voucher.getVoucherCode(), BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCode.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        RoundedImageView shopLogo = findViewById(R.id.shopLogo);
        if (!voucher.getShopLogo().isEmpty()) {
            Glide.with(ShowVoucherActivity.this).load(voucher.getShopLogo()).placeholder(R.drawable.placeholder).into(shopLogo);
        }

        TextView shopName = findViewById(R.id.shopName);
        shopName.setText(voucher.getShopName());

        TextView offer = findViewById(R.id.offer);
        String message = "";
        if (voucher.isFree()) {
            offer.setText(voucher.getFreeMessage());
            message = voucher.shopName+"\n"+getString(R.string.free);
        }
        else {
            offer.setText(String.format("%s %d%s", getString(R.string.Get), voucher.getDiscount(), getString(R.string.OFF)));
            message = String.format("%s\n%s %d%%", voucher.shopName, getString(R.string.Discount), voucher.getDiscount());
        }

        findViewById(R.id.viewOnMapBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+voucher.getLatitude()+","+voucher.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        TextView voucherType = findViewById(R.id.voucherType);
        voucherType.setText(voucher.getVoucherCategory());

        TextView validity = findViewById(R.id.validity);
        validity.setText(String.format("%s %s", getString(R.string.Validuntil), Services.convertDateToStringWithoutDash(voucher.getVoucherExpireDate())));

        TextView shopAddress = findViewById(R.id.shopAddress);
        shopAddress.setText(voucher.getAddress());

        TextView voucherNumber = findViewById(R.id.voucherNumber);
        if (voucher.getVoucherCode().isEmpty()) {
            voucherNumber.setText("#"+voucher.getVoucherRedeemId());

        }
        else {
            voucherNumber.setText(voucher.getVoucherCode());
        }


        TextView conditions = findViewById(R.id.conditions);
        conditions.setText(voucher.getVoucherConditions());

        Services.sentPushNotificationToAdmin(this,getString(R.string.voucherredeemed),message);


    }
}
