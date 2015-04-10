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
	private BluetoothDevice mmDevice;
	private ConnectedThread connectedThread;
	private EditText EditTextPassoRa;
	private CheckBox CheckBoxPassoRa;
	private EditText EditTextPassoDec;
	private CheckBox CheckBoxPassoDec;
	private EditText EditTextTimer;
	private CheckBox CheckBoxTimer;
	private String bufferCmd = "";
	private String response[] = { "#", "#", "#", "#", "#", "#", "#", "#", "#" };
	private String command[] = { ":HGT#", ":HGRB#", ":HGRA#", ":HGT#",
			":HGRB#", ":HGRA#", "", "" };
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
		EditTextPassoRa = (EditText) findViewById(R.id.editTextPassoRa);
		CheckBoxPassoRa = (CheckBox) findViewById(R.id.checkBoxPassoRa);
		EditTextPassoDec = (EditText) findViewById(R.id.editTextPassoDec);
		CheckBoxPassoDec = (CheckBox) findViewById(R.id.checkBoxPassoDec);
		EditTextTimer = (EditText) findViewById(R.id.editTextTimer);
		CheckBoxTimer = (CheckBox) findViewById(R.id.checkBoxTimer);
		mmDevice = getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		if (mmDevice != null) {
			try {
				connectedThread = new ConnectedThread(mmDevice, mHandler);
				new Thread(connectedThread).start();
				MandaComando();
			} catch (IOException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				// e.printStackTrace();
			}
		}
	}

	private void MandaComando() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!connectedThread.isFinished()) {
					if (podeescrever) {
						try {
							Thread.sleep(300);
							connectedThread.write(command[icom].getBytes());
							commandAtual = command[icom];

							if (icom == 0) {
								command[0] = "";
							}
							if (icom == 1) {
								command[1] = "";
							}
							if (icom == 2) {
								command[2] = "";
							}
							if (icom == 3) {
								command[3] = "";
							}
							if (icom == 4) {
								command[4] = "";
							}
							if (icom == 5) {
								command[5] = "";
							}
							if (icom == 6) {
								command[6] = "";
							}
							if (icom == 7) {
								command[7] = "";
							}
							icom++;
							if (icom > 7) {
								icom = 0;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (mmDevice != null) {
				Intent result = new Intent();
				result.putExtra(BluetoothDevice.EXTRA_DEVICE, mmDevice);
				setResult(RESULT_OK, result);
				connectedThread.finish();
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void leResposta(String readMessage) {
		String strtemp;
		try {
			if (":HGRA#".equals(commandAtual)) {

				EditTextPassoRa.setText(readMessage.subSequence(0, 7));

			}
			if (":HGT#".equals(commandAtual)) {

				EditTextTimer.setText(readMessage.subSequence(0, 7));

			}
			if (":HGRB#".equals(commandAtual)) {

				EditTextPassoDec.setText(readMessage.subSequence(0, 7));

			}
			if (commandAtual.contains(":HS"))
				if (readMessage.equalsIgnoreCase("1")) {
					Toast.makeText(getApplicationContext(),
							R.string.atualizado, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), R.string.deu_erro,
							Toast.LENGTH_LONG).show();
				}

		} catch (Exception e) {
			strtemp = "error  " + commandAtual + " - " + readMessage.toString();
			Toast.makeText(getApplicationContext(), strtemp, Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}

	}

	public void atualizarHW(View v) {
		String strtmp;
		int i;
		if (CheckBoxPassoRa.isChecked()) {
			// :HSRA0000000#
			i = Integer.parseInt(EditTextPassoRa.getText().toString());
			strtmp = ":HSRA";
			strtmp = strtmp + String.format("%07d", i) + "#";
			command[1] = strtmp;
			response[1] = "0123456789";
			CheckBoxPassoRa.setChecked(false);
		} else {
			command[1] = ":HGRA#";
			response[1] = "#";
		}

		if (CheckBoxPassoDec.isChecked()) {
			// :HSRA0000000#
			i = Integer.parseInt(EditTextPassoDec.getText().toString());
			strtmp = ":HSRB";
			strtmp = strtmp + String.format("%07d", i) + "#";
			command[2] = strtmp;
			response[2] = "0123456789";
			CheckBoxPassoDec.setChecked(false);
		} else {
			command[2] = ":HGRB#";
			response[2] = "#";
		}

		if (CheckBoxTimer.isChecked()) {
			// :HSRA0000000#
			i = Integer.parseInt(EditTextTimer.getText().toString());
			strtmp = ":HST";
			strtmp = strtmp + String.format("%07d", i) + "#";
			command[3] = strtmp;
			response[3] = "0123456789";
			CheckBoxTimer.setChecked(false);
		} else {
			command[3] = ":HGT#";
			response[3] = "#";
		}
		icom = 0;
	}
}
