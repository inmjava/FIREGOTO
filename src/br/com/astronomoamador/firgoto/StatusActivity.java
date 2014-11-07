package br.com.astronomoamador.firgoto;

import java.io.IOException;

import br.com.astronomoamador.firgoto.bluetooth.ConnectedThread;
import br.com.astronomoamador.firgoto.bluetooth.Constants;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends Activity {

	private BluetoothDevice mmDevice;
	private ConnectedThread connectedThread;
	private EditText editTextGrauLat;
	private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case Constants.MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                editTextGrauLat.setText(editTextGrauLat.getText().toString() + readMessage);
                break;
            }
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
		editTextGrauLat = (EditText) findViewById(R.id.editTextGrauLat);
		mmDevice = getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		if(mmDevice != null){
			try {
				connectedThread = new ConnectedThread(mmDevice, mHandler);
				new Thread(connectedThread).start();
				connectedThread.write("statusActivity".getBytes());
			} catch (IOException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//					e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.status, menu);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
			Intent result = new Intent();
			result.putExtra(BluetoothDevice.EXTRA_DEVICE, mmDevice);
			setResult(RESULT_OK, result);
			connectedThread.finish();
	        finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}	
}
