package com.example.gestionconfeccionandroid;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    EditText etToken;
    TextView tvNombre;
    String tokenConsulta="";
    TextView tvModulo;
    TextView tvFuncion;
    TextView tvEstado;
    boolean estado;
    int idRows;
    int idRowFuncion;
    String Modulos="";
    Button btnEstado, VerPefil;
    TextView tvPeticiones;
    TextView tvPeticionesFI;
    TextView tvPeticionesFF;
    TextView tvMinutos;
    String ModulosPeticiones="";
    String PeticionesFi="";
    String PeticionesFF="";
    String Minutos="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etToken=findViewById(R.id.etToken);
        tvNombre=findViewById(R.id.tvNombre);
        tvModulo=findViewById(R.id.tVModulo);
        tvPeticiones=findViewById(R.id.tvPeticiones);
        tvPeticionesFI=findViewById(R.id.tvPeticionesFI);
        tvPeticionesFF=findViewById(R.id.tvPeticionesFF);
        tvMinutos=findViewById(R.id.tvMinutos);
        tvFuncion=findViewById(R.id.tvFuncion);
        VerPefil = findViewById(R.id.BtnVerPefil);
//        tvEstado=findViewById(R.id.tvEstado);
//        btnEstado=findViewById(R.id.btnEstado);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println( "Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast

                        System.out.println(token);
                        Toast.makeText(MainActivity.this,"tu codigo de verificacion es"+token, Toast.LENGTH_SHORT).show();
                        etToken.setText((token));
                        tokenConsulta=token;
                        obtenerInformacionPersonal();

                    }


                });


    }

    // funcion para obtener la informacion personal
    public void obtenerInformacionPersonal(){
        String conexion = "https://miclocal.com.co:9362/api/GestionPlanta/ConsultarPersonalFuncionesAndroid/"+tokenConsulta;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpsURLConnection conn;

        try {
            url = new URL(conexion);
            conn = (HttpsURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = "";

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            json = response.toString();

            JSONArray jsonArray = null;

            jsonArray = new JSONArray(json);

            for (int i =0;i < jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                tvNombre.setText((jsonObject.optString("nombre")).toUpperCase(Locale.ROOT));
                idRows=((jsonObject.optInt("idRows")));
                idRowFuncion=((jsonObject.optInt("idRowFuncion")));
                tvFuncion.setText((jsonObject.optString("funcion")).toUpperCase(Locale.ROOT));

            }
            obtenerModulosPersonal();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    public void copiarToken(View view){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(tokenConsulta, tokenConsulta);
        clipboard.setPrimaryClip(clip);



        Intent email = new Intent(Intent.ACTION_SEND);

        email.putExtra(Intent.EXTRA_SUBJECT, "TOKEN AUTENTICACIÓN " + tvNombre.getText().toString());
        email.putExtra(Intent.EXTRA_TEXT, tokenConsulta);

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));

    }

    // Funcion obtener modulos
    public void obtenerModulosPersonal(){
        String conexion = "https://miclocal.com.co:9362/api/GestionPlanta/ConsultarModuloPersonalAndroid/"+idRows;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpsURLConnection conn;

        try {
            url = new URL(conexion);
            conn = (HttpsURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = "";

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            json = response.toString();


            JSONArray jsonArray = null;

            jsonArray = new JSONArray(json);

            for (int i =0;i < jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Modulos=Modulos+" - "+((jsonObject.optString("modulo")));


            }

            tvModulo.setText(Modulos);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void obtenerPeticionesModulos(View view){
        ModulosPeticiones="MODULO"+"\r\n"+"\r\n";
        PeticionesFi="H INICIAL"+"\r\n"+"\r\n";
        PeticionesFF="H FINAL"+"\r\n"+"\r\n";
        Minutos="MINUTOS"+"\r\n"+"\r\n";
        String conexion = "https://miclocal.com.co:9362/api/GestionPlanta/ConsultarPeticionesModulosAndroid/"+idRowFuncion+"/"+idRows;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpsURLConnection conn;

        try {
            url = new URL(conexion);
            conn = (HttpsURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = "";

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            json = response.toString();


            JSONArray jsonArray = null;

            jsonArray = new JSONArray(json);

            for (int i =0;i < jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);


                ModulosPeticiones=ModulosPeticiones+(jsonObject.optString("modulo"))+"\r\n";
                PeticionesFi=PeticionesFi+(jsonObject.optString("fechaInicial"))+"\r\n";
                PeticionesFF=PeticionesFF+(jsonObject.optString("fechaFinal"))+"\r\n";
                Minutos=Minutos+(jsonObject.optString("minutos"))+"\r\n";

            }

            tvPeticiones.setText(ModulosPeticiones);
            tvPeticiones.setTextColor(Color.rgb(200,0,0));
            tvPeticionesFI.setText(PeticionesFi);
            tvPeticionesFF.setText(PeticionesFF);
            tvMinutos.setText(Minutos);
            tvMinutos.setTextColor(Color.	rgb(0,128,0));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void VerPerfil(View view)
    {
        Intent intent = new Intent(getBaseContext(), MainActivity2.class);
        intent.putExtra("nombre", tvNombre.getText().toString());
        intent.putExtra("cargo", tvFuncion.getText().toString());
        startActivity(intent);
    }

}