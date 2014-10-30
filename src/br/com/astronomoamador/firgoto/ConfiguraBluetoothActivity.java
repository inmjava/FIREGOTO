package br.com.astronomoamador.firgoto;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ConfiguraBluetoothActivity extends Activity {

	private static final int REQUEST_ENABLE_BT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configura_bluetooth);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configura_bluetooth, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void findDevices(View v){
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, R.string.este_dispositivo_n_suporta_bluetooth, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!mBluetoothAdapter.isEnabled()) {
			Toast.makeText(this, R.string.antes_de_continuar_configure_seu_bluetooth, Toast.LENGTH_SHORT).show();
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		    return;
		}
		
		EditText txtDispositivosEncontrados = (EditText) findViewById(R.id.dispositivosEncontrados);
		txtDispositivosEncontrados.setText("Procurando Dispositivos...");
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		String dispositivosEncontrados = "Dispositivos Encontrados: ";
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		        //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
		    	dispositivosEncontrados += ";" + device.getName() + "-" + device.getAddress();
				txtDispositivosEncontrados.setText(dispositivosEncontrados);
		    }
		}
		
		
	}
	
}
