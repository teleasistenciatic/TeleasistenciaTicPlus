package com.local.android.teleasistenciaticplus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.local.android.teleasistenciaticplus.lib.helper.AppInfo;
import com.local.android.teleasistenciaticplus.lib.helper.AppLog;
import com.local.android.teleasistenciaticplus.lib.networking.HttpOperations;
import com.local.android.teleasistenciaticplus.lib.networking.HttpRequest;
import com.local.android.teleasistenciaticplus.lib.networking.Networking;

import java.util.concurrent.ExecutionException;

public class actMainDebug extends ActionBarActivity {

    /**
     * Creación de la actividad de depuración
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_debug);

        ////////////////////////////////////////////////////
        // Cálculo de información de la aplicación para depuración
        ////////////////////////////////////////////////////

        // Memoria usada (Solo API > 16 )
        TextView usedMemoryText = (TextView) findViewById(R.id.debug_used_memory);
        Long memoriaUsada = AppInfo.getUsedMemory();
        Long memoriaTotal = AppInfo.getTotalMemory();
        usedMemoryText.setText("Usada: " + String.valueOf(memoriaUsada) + " mb/ " + "Total: " + String.valueOf(memoriaTotal) + "mb");

        ProgressBar usedMemoryBar = (ProgressBar) findViewById(R.id.debug_progress_bar_used_memory);
        /* Escalamos a 100 como referencia para la barra de progreso */
        usedMemoryBar.setMax(100);
        usedMemoryBar.setProgress((int) ((memoriaUsada * 100.0f) / memoriaTotal));

        //Texto de la dirección de servidor
        TextView serverAddress = (TextView) findViewById(R.id.edit_server_adress);
        serverAddress.setText(Networking.getFullServerAdress());
    }

    /**
     * Menus de la actividad
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_main_debug, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_act_main_debug_exit) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void exit_button(View view) {
        finish();
    }

    /**
     * Comprobación online alternativa
     *
     * @param view
     */
    public void main_debug_button_check_online_alternative(View view) {
        ////////////////////////////////////////////////////
        // Comprobación de estado online servidor
        ////////////////////////////////////////////////////

        //Interfaz
        TextView serverAddress = (TextView) findViewById(R.id.edit_server_adress);

        String url = serverAddress.getText().toString(); //la introducida en la caja de texto

        if (url.length() == 0) {  //Si la cadena está vacia usamos la url por defecto
            serverAddress.setText(Networking.getFullServerAdress());
        }

        // Intentamos obtener una dirección de internet
        HttpRequest miHttpRequest = new HttpRequest(url);
        String textRead = miHttpRequest.httpGet();

        String resultado;

        if (textRead == null) {
            resultado = getResources().getString(R.string.ERROR);
        } else {
            resultado = getResources().getString(R.string.CORRECTO);
        }

        Toast toast1 = Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_SHORT);
        toast1.show();
    }

    /**
     * Pulsado el botón de comprobación online
     *
     */
    public void main_debug_button_check_online(View view) {
        ////////////////////////////////////////////////////
        // Comprobación de estado online servidor
        ////////////////////////////////////////////////////

        //Interfaz
        TextView serverAddress = (TextView) findViewById(R.id.edit_server_adress);

        String url = serverAddress.getText().toString(); //la introducida en la caja de texto

        if (url.length() == 0) {  //Si la cadena está vacia usamos la url por defecto
            serverAddress.setText(Networking.getFullServerAdress());
        }

        Boolean isNetworkAvailable = Networking.isConnectedToInternet();

        showConnectionToInternetInTextBackground(serverAddress); //Indicamos el estado online mediante color rojo o verde

        //Comprobación de el servidor está disponible (se hace mediante la lectura de un fichero en el mismo)
        //Sólo si existe conexión de internet

        if (isNetworkAvailable) {

            String textRead = null;

            try {
                HttpOperations miUrl = new HttpOperations(url);
                textRead = miUrl.getText();
            } catch (Exception e) {
                AppLog.d("actMainDebug", "Error leyendo el archivo");
            }

            String resultado;

            if (textRead == null) {
                resultado = getResources().getString(R.string.ERROR);
            } else {
                resultado = getResources().getString(R.string.CORRECTO);
            }

            Toast toast1 = Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_SHORT);
            toast1.show();
        }

    }

    /**
     * Mostramos si hay conexión a internet en el color de fondo de la caja de texto
     * @param serverAddress
     */

    private void showConnectionToInternetInTextBackground(TextView serverAddress) {
        //Comprobación de que exista conexión de datos en el teléfono
        final Boolean isNetworkAvailable = Networking.isConnectedToInternet();

        if(isNetworkAvailable)
        {
            //Server online fondo verde
            serverAddress.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else
        {
            //Server offline fondo rojo
            serverAddress.setBackgroundColor(getResources().getColor(R.color.red));
        }
    }
} // Fin actividadMainDebug

