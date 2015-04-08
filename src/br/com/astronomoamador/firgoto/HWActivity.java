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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CheckBox;

public class HWActivity extends Activity {
	
	private String bufferCmd = "";
	private String response[] = { "#", "#", "#", "#", "#", "#", "#", "#", "#" };
	private String command[] = { ":Gt#", ":Gt#", ":GG#", ":GL#", ":GS#",
			":GC#", ":Gg#", ":Gt#" };
	private boolean podeescrever = true;
	private String commandAtual = null;
	private int icom = 0;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				if (readMessage.contains(response[icom])
						|| response[icom].contains(readMessage)) {
					podeescrever = false;
					bufferCmd = bufferCmd + readMessage;
					leResposta(bufferCmd);
					bufferCmd = "         ";
					bufferCmd = "";
					podeescrever = true;

				} else {
					bufferCmd = bufferCmd + readMessage;
				}
				readBuf = null;
				readMessage = null;
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hw);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hw, menu);
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
}
