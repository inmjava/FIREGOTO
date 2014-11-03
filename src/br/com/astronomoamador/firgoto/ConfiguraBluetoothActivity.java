package br.com.astronomoamador.firgoto;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ConfiguraBluetoothActivity extends Activity {

	private static final int REQUEST_ENABLE_BT = 0;
	private BluetoothDevice mmDevice;
	private BluetoothSocket mmSocket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configura_bluetooth);
		
		final String[] nomes = new String[] { "Nome 1", "Nome 2", "Nome 3" };
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomes);
		ListView listaBluetooth = (ListView) findViewById(R.id.listaBluetooth);
		listaBluetooth.setOnClickListener(new OnClickListener() {
			
			String[] nomes2 = nomes;
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		listaBluetooth.setAdapter(arrayAdapter);
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

	public void findDevices(View v) {
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
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a
				// ListView
				// mArrayAdapter.add(device.getName() + "\n" +
				// device.getAddress());
				if (device.getName().equalsIgnoreCase("P017818")) {
					mmDevice = device;
					txtDispositivosEncontrados.setText(device.getName() + " encontrado");
				}
			}
		}
	}

	public void enviarInformacoes(View v) {
		try {
			UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // Standard
			// mmSocket =
			// device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
			mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
			mmSocket.connect();
			OutputStream mmOutputStream = mmSocket.getOutputStream();
			// InputStream mmInputStream = mmSocket.getInputStream();
			mmOutputStream.write("Olá Funcionei!!!".getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// beginListenForData();
		// connState = true;
		// myLabel.setText("Status: Bluetooth aberto");
		// connImageView.setBackgroundResource(R.drawable.comm1);
	}

}
