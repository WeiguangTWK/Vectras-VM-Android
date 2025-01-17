package com.vectras.vm;

import static android.content.Intent.ACTION_OPEN_DOCUMENT;
import static android.content.Intent.ACTION_VIEW;
import static android.view.View.GONE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class Minitools extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_minitools);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(getString(R.string.mini_tools));

        LinearLayout setupsoundfortermux = findViewById(R.id.setupsoundfortermux);
        LinearLayout cleanup = findViewById(R.id.cleanup);
        LinearLayout restore = findViewById(R.id.restore);
        LinearLayout deleteallvm = findViewById(R.id.deleteallvm);
        LinearLayout reinstallsystem = findViewById(R.id.reinstallsystem);

        setupsoundfortermux.setOnClickListener(v -> {
            if (VectrasApp.isAppInstalled("com.termux", getApplicationContext())) {
                AlertDialog alertDialog = new AlertDialog.Builder(Minitools.this, R.style.MainDialogTheme).create();
                alertDialog.setTitle(getResources().getString(R.string.setup_sound));
                alertDialog.setMessage(getResources().getString(R.string.setup_sound_guide_content));
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.start_setup), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Setup", "curl -o setup.sh https://raw.githubusercontent.com/AnBui2004/termux/refs/heads/main/installpulseaudio.sh && chmod +rwx setup.sh && ./setup.sh && rm setup.sh");
                        clipboard.setPrimaryClip(clip);
                        Intent intent = new Intent();
                        intent.setAction(ACTION_VIEW);
                        intent.setData(Uri.parse("android-app://com.termux"));
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.copied), Toast.LENGTH_LONG).show();
                        return;
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(Minitools.this, R.style.MainDialogTheme).create();
                alertDialog.setTitle(getResources().getString(R.string.termux_is_not_installed));
                alertDialog.setMessage(getResources().getString(R.string.you_need_to_install_termux));
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.install), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(ACTION_VIEW);
                        intent.setData(Uri.parse("https://github.com/termux/termux-app/releases"));
                        startActivity(intent);
                        return;
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }

        });

        cleanup.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(Minitools.this, R.style.MainDialogTheme).create();
            alertDialog.setTitle(getResources().getString(R.string.clean_up));
            alertDialog.setMessage(getResources().getString(R.string.clean_up_content));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.clean_up), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    VMManager.cleanUp();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.done), Toast.LENGTH_LONG).show();
                    restore.setVisibility(GONE);
                    cleanup.setVisibility(GONE);
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        });

        restore.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(Minitools.this, R.style.MainDialogTheme).create();
            alertDialog.setTitle(getResources().getString(R.string.restore));
            alertDialog.setMessage(getResources().getString(R.string.restore_content));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.continuetext), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    VMManager.restoreVMs();
                    VectrasApp.oneDialog(getResources().getString(R.string.done), getResources().getString(R.string.restored) + " " + String.valueOf(VMManager.restoredVMs) + ".", true, false, Minitools.this);
                    restore.setVisibility(GONE);
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        });

        deleteallvm.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(Minitools.this, R.style.MainDialogTheme).create();
            alertDialog.setTitle(getResources().getString(R.string.delete_all_vm));
            alertDialog.setMessage(getResources().getString(R.string.delete_all_vm_content));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.delete_all), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    VectrasApp.killallqemuprocesses(getApplicationContext());
                    VectrasApp.deleteDirectory(AppConfig.vmFolder);
                    VectrasApp.deleteDirectory(AppConfig.recyclebin);
                    VectrasApp.deleteDirectory(AppConfig.romsdatajson);
                    File vDir = new File(AppConfig.maindirpath);
                    vDir.mkdirs();
                    VectrasApp.writeToFile(AppConfig.maindirpath, "roms-data.json", "[]");
                    cleanup.setVisibility(GONE);
                    restore.setVisibility(GONE);
                    deleteallvm.setVisibility(GONE);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.done), Toast.LENGTH_LONG).show();
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        });

        reinstallsystem.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(Minitools.this, R.style.MainDialogTheme).create();
            alertDialog.setTitle(getResources().getString(R.string.reinstall_system));
            alertDialog.setMessage(getResources().getString(R.string.reinstall_system_content));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.continuetext), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.isActivate = false;
                    AppConfig.needreinstallsystem = true;
                    VectrasApp.killallqemuprocesses(Minitools.this);
                    VectrasApp.deleteDirectory(getFilesDir().getAbsolutePath() + "/data");
                    VectrasApp.deleteDirectory(getFilesDir().getAbsolutePath() + "/distro");
                    VectrasApp.deleteDirectory(getFilesDir().getAbsolutePath() + "/usr");
                    Intent intent = new Intent();
                    intent.setClass(Minitools.this, SetupQemuActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}