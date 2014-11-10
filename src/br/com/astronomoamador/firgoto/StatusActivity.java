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
import android.widget.Toast;

public class StatusActivity extends Activity {

	private BluetoothDevice mmDevice;
	private ConnectedThread connectedThread;

	private EditText editTextGrauLat;
	private EditText editTextMinLat;
	private EditText editTextSegLat;

	private EditText editTextGrauLog;
	private EditText editTextMinLog;
	private EditText editTextSegLog;

	private EditText editTextHoraTime;
	private EditText editTextMinTime;
	private EditText editTextSegTime;

	private EditText editTextUTCSet;

	private EditText editTextHoraUTC;
	private EditText editTextMinUTC;
	private EditText editTextSegUTC;

	private EditText editTextHoraLST;
	private EditText editTextMinLST;
	private EditText editTextSegLST;


	private String bufferCmd = "";
	private String commandAtual = null;


	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				if (!readMessage.contains("#"))
				{
					bufferCmd=bufferCmd+readMessage;	
				}
				else
				{
					bufferCmd=bufferCmd+readMessage;
					leResposta(bufferCmd);
					bufferCmd="";
				}
				readBuf=null;
				readMessage=null;
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);

		editTextGrauLat = (EditText) findViewById(R.id.editTextGrauLat);
		editTextMinLat = (EditText) findViewById(R.id.editTextMinLat);
		editTextSegLat = (EditText) findViewById(R.id.editTextSegLat);

		editTextGrauLog = (EditText) findViewById(R.id.editTextGrauLog);
		editTextMinLog = (EditText) findViewById(R.id.editTextMinLog);
		editTextSegLog = (EditText) findViewById(R.id.editTextSegLog);

		editTextHoraTime = (EditText) findViewById(R.id.editTextHoraTime);
		editTextMinTime = (EditText) findViewById(R.id.editTextMinTime);
		editTextSegTime = (EditText) findViewById(R.id.editTextSegTime);

		editTextUTCSet = (EditText) findViewById(R.id.editTextUTCSet);

		editTextHoraUTC = (EditText) findViewById(R.id.editTextHoraUTC);
		editTextMinUTC = (EditText) findViewById(R.id.editTextMinUTC);
		editTextSegUTC = (EditText) findViewById(R.id.editTextSegUTC);

		editTextHoraLST = (EditText) findViewById(R.id.editTextHoraLST);
		editTextMinLST = (EditText) findViewById(R.id.editTextMinLST);
		editTextSegLST = (EditText) findViewById(R.id.editTextSegLST);






		mmDevice = getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		if(mmDevice != null){
			try {
				connectedThread = new ConnectedThread(mmDevice, mHandler);
				new Thread(connectedThread).start();
				mandacommand();
			} catch (IOException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				//					e.printStackTrace();
			}
		}
	}

	void mandacommand()
	{ 
		try {
			commandAtual=":Gt#";
			connectedThread.write(commandAtual.getBytes());
			Thread.sleep(250);
			commandAtual=":Gg#";
			connectedThread.write(commandAtual.getBytes());
			Thread.sleep(250);
			commandAtual=":GG#";
			connectedThread.write(commandAtual.getBytes());
			Thread.sleep(250);
			commandAtual=":GL#";
			connectedThread.write(commandAtual.getBytes());
			Thread.sleep(250);
			commandAtual=":GS#";
			connectedThread.write(commandAtual.getBytes());
			Thread.sleep(250);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			if(mmDevice != null)

			{
				Intent result = new Intent();
				result.putExtra(BluetoothDevice.EXTRA_DEVICE, mmDevice);
				setResult(RESULT_OK, result);
				connectedThread.finish();
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}	


	private void leResposta(String readMessage)
	{
		if (":Gt#".equals(commandAtual)) {
		editTextGrauLat.setText(readMessage.subSequence(0, 2));
		editTextMinLat.setText(readMessage.subSequence(3, 5));
		editTextSegLat.setText(readMessage.subSequence(6, 8));
		}
		if (":Gg#".equals(commandAtual)) {
		editTextGrauLog.setText(readMessage.subSequence(0, 2));
		editTextMinLog.setText(readMessage.subSequence(3, 5));
		editTextSegLog.setText(readMessage.subSequence(6, 8));
		}
		if (":GLGL#".equals(commandAtual)) {
		editTextHoraTime.setText(readMessage.subSequence(0, 2));
		editTextMinTime.setText(readMessage.subSequence(3, 5));
		editTextSegTime.setText(readMessage.subSequence(6, 8));
		}
		if (":GG#".equals(commandAtual)) {
		editTextUTCSet.setText(readMessage.subSequence(0, 2));
		}
		if (":GL#".equals(commandAtual)) {
		editTextHoraUTC.setText(readMessage.subSequence(0, 2));
		editTextMinUTC.setText(readMessage.subSequence(3, 5));
		editTextSegUTC.setText(readMessage.subSequence(6, 8));
		}
		if (":GS#".equals(commandAtual)) {
		editTextHoraLST.setText(readMessage.subSequence(0, 2));
		editTextMinLST.setText(readMessage.subSequence(3, 5));
		editTextSegLST.setText(readMessage.subSequence(6, 8));
		}

	}

	public void atualizar(View v){
		mandacommand();
	}
	

}
