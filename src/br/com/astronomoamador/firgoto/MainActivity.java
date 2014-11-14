package br.com.astronomoamador.firgoto;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	
	private static final int MOSTRAR_STATUS = 0;
	private static final int MOSTRAR_MOSTRAR_GOTO_SYNC = 0;
	private static final int MOSTRAR_CONTROLE = 0;
	public static int CONFIGURAR_BLUETOOTH = 1;
	private BluetoothDevice mmDevice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	public void configurarBluetooth(View v){
		startActivityForResult(new Intent(this, ConfiguraBluetoothActivity.class),MainActivity.CONFIGURAR_BLUETOOTH);
	}
	public void mostrarStatus(View v){
		Intent abrirMostrarStatus = new Intent(this, StatusActivity.class);
		abrirMostrarStatus.putExtra(BluetoothDevice.EXTRA_DEVICE, mmDevice);
		startActivityForResult(abrirMostrarStatus,MainActivity.MOSTRAR_STATUS);
	}
	public void mostrarControle(View v){
		Intent MostrarControleActivity = new Intent(this, MostrarControleActivity.class);
		MostrarControleActivity.putExtra(BluetoothDevice.EXTRA_DEVICE, mmDevice);
		startActivityForResult(MostrarControleActivity,MainActivity.MOSTRAR_CONTROLE);
	}

	public void mostrarGotosync(View v){
		
		Intent abrirMostrarGotosync = new Intent(this, GotoSyncActivity.class);
		abrirMostrarGotosync.putExtra(BluetoothDevice.EXTRA_DEVICE, mmDevice);
		startActivityForResult(abrirMostrarGotosync,MainActivity.MOSTRAR_MOSTRAR_GOTO_SYNC);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==Activity.RESULT_OK) {
			 mmDevice = data.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);	
		}						
	}
	
	public void mostrarHW(View v){
		startActivity(new Intent(this, HWActivity.class));
	}
	public void mostrarCredito(View v){
		startActivity(new Intent(this, CreditosActive.class));
	}
	
}
