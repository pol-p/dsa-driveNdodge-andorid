package edu.upc.dsa_android_DriveNdodge.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.ui.profile.ViewProfileActivity;
import edu.upc.dsa_android_DriveNdodge.ui.ranking.ViewRankingActivity;
import edu.upc.dsa_android_DriveNdodge.ui.shop.ShopActivity;
import edu.upc.dsa_android_DriveNdodge.ui.inventario.InventarioActivity;

public class PortalPageActivity extends AppCompatActivity {

    private Button shopBttn, perfilBttn, rankBttn, inventoryBttn;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portalpage);

        shopBttn = findViewById(R.id.shopBttn);
        perfilBttn = findViewById(R.id.perfilBttn);
        rankBttn = findViewById(R.id.rankBttn);
        inventoryBttn = findViewById(R.id.inventoryBttn);

        shopBttn.setOnClickListener(v -> {
            Intent intent = new Intent(PortalPageActivity.this, ShopActivity.class);
            startActivity(intent);
        });

        perfilBttn.setOnClickListener(v -> {
            Intent intent = new Intent(PortalPageActivity.this, ViewProfileActivity.class);
            startActivity(intent);
        });

        rankBttn.setOnClickListener(v -> {
            Intent intent = new Intent(PortalPageActivity.this, ViewRankingActivity.class);
            startActivity(intent);
        });

        inventoryBttn.setOnClickListener(v -> {
            Intent intent = new Intent(PortalPageActivity.this, InventarioActivity.class);
            startActivity(intent);
        });

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());
    }

    private void logout() {
        getSharedPreferences("MyAppPrefs", MODE_PRIVATE).edit().clear().apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
