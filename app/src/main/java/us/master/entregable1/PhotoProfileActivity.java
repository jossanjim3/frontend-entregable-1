package us.master.entregable1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;

public class PhotoProfileActivity extends AppCompatActivity {

    private static final int TAKE_PHOTO_CODE = 0x154;
    private Button takePictureButton, savePictureButton;
    private ImageView takePictureImagen;
    private static final int CAMERA_PERMISSION_REQUEST = 0x512;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST = 0x513;
    private String file;
    private File filePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_profile);

        takePictureButton = findViewById(R.id.take_picture_button);
        takePictureImagen = findViewById(R.id.take_picture_image);
        savePictureButton = findViewById(R.id.save_picture_button);

        takePictureButton.setOnClickListener( l -> {
            takePicture();
        });

        savePictureButton.setOnClickListener( l -> {
            savePictureAsProfilePhoto();
        });

        cargarFotoPerfil();
    }

    private void cargarFotoPerfil() {
        // Toast.makeText(this, "Cargando foto perfil...", Toast.LENGTH_SHORT).show();
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading photo profile...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        com.google.firebase.storage.FirebaseStorage storage = com.google.firebase.storage.FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("photoProfile");

        if (storageReference != null) {
            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Glide.with(PhotoProfileActivity.this).load(task.getResult())
                                .placeholder(R.drawable.ic_launcher_background)
                                .centerCrop().into(takePictureImagen);
                        // takePictureImagen.setImageURI(task.getResult());
                        // Toast.makeText(PhotoProfileActivity.this, "Foto seleccionada como foto perfil!", Toast.LENGTH_SHORT).show();
                    } else {
                        // no existe foto de perfil
                        // Toast.makeText(PhotoProfileActivity.this, "No existe foto de perfil2...", Toast.LENGTH_LONG).show();
                        takePictureImagen.setImageResource(android.R.drawable.ic_menu_camera);
                        Snackbar.make(takePictureButton, "No existe foto de perfil...", Snackbar.LENGTH_LONG).show();
                    }
                    progress.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "No existe foto de perfil...", Toast.LENGTH_LONG).show();
        }
    }

    private void takePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Snackbar.make(takePictureButton,R.string.take_picture_camera_rationale, BaseTransientBottomBar.LENGTH_LONG)
                        .setAction(R.string.take_picture_camera_rationale_ok,
                                click ->  {
                                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                                });
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
            }
        } else {
            // permiso concedido

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Snackbar.make(takePictureButton,R.string.take_picture_camera_rationale, BaseTransientBottomBar.LENGTH_LONG)
                            .setAction(R.string.take_picture_camera_rationale_ok,
                                    click ->  {
                                        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST);
                                    });
                } else {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST);
                }
            } else {
                // tenemos todos los permisos
                // lanzamos la camara

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                String dir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/masterits/";
                File newFile = new File(dir);
                newFile.mkdirs();

                file = dir + Calendar.getInstance().getTimeInMillis() + ".jpg";
                File newFilePicture = new File(file);
                try {
                    newFilePicture.createNewFile();
                } catch (Exception ignore) {

                }

                Uri outputFiledire = Uri.fromFile(newFilePicture);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFiledire);
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            filePicture = new File(file);

            Glide.with(PhotoProfileActivity.this).load(filePicture)
                    .placeholder(R.drawable.ic_launcher_background)
                    .centerCrop().into(takePictureImagen);

        }
    }

    private void savePictureAsProfilePhoto() {

        Toast.makeText(PhotoProfileActivity.this, "Espere mientras se guarda como foto perfil...", Toast.LENGTH_LONG).show();

        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Saving");
        progress.setMessage("Wait while saving...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        com.google.firebase.storage.FirebaseStorage storage = com.google.firebase.storage.FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("photoProfile");
        // storageReference.delete();
        UploadTask uploadTask = storageReference.putFile(Uri.fromFile(filePicture));
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("JD" , "Firebase storage completed: " + task.getResult().getTotalByteCount());

                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Glide.with(PhotoProfileActivity.this).load(task.getResult())
                                        .placeholder(R.drawable.ic_launcher_background)
                                        .centerCrop().into(takePictureImagen);
                                // takePictureImagen.setImageURI(task.getResult());
                                Toast.makeText(PhotoProfileActivity.this, "Foto seleccionada como foto perfil!", Toast.LENGTH_SHORT).show();
                                // To dismiss the dialog
                                progress.dismiss();
                            }
                        }
                    });

                }
            }
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("JD" , "Firebase storage error: " + e.getMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Toast.makeText(this, R.string.camera_not_ganted, Toast.LENGTH_SHORT).show();
                }
                break;

            case WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Toast.makeText(this, R.string.storage_not_ganted, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
