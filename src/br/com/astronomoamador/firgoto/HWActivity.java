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
	private void leResposta(String readMessage) {
		String strtemp;
		try {
			if (":Gt#".equals(commandAtual)) {
				strtemp = readMessage.subSequence(0, 1).toString();
				if (strtemp.equalsIgnoreCase("-")) {
					toggleLestOestLog.setChecked(true);
				} else {
					toggleLestOestLog.setChecked(false);
				}
				editTextGrauLat.setText(readMessage.subSequence(1, 3));
				editTextMinLat.setText(readMessage.subSequence(4, 6));
			}
			if (":Gg#".equals(commandAtual)) {
				strtemp = readMessage.subSequence(0, 1).toString();
				if (strtemp.equalsIgnoreCase("-")) {
					toggleNorteSulLat.setChecked(true);
				} else {
					toggleNorteSulLat.setChecked(false);
				}
				editTextGrauLog.setText(readMessage.subSequence(1, 4));
				editTextMinLog.setText(readMessage.subSequence(5, 7));
			}
			if (":GL#".equals(commandAtual)) {;
				editTextHoraTime.setText(readMessage.subSequence(0, 2));
				editTextMinTime.setText(readMessage.subSequence(3, 5));
				editTextSegTime.setText(readMessage.subSequence(6, 8));
				TextHoraUTC.setText(readMessage.subSequence(0, 2));
				TextMinUTC.setText(readMessage.subSequence(3, 5));
				TextSegUTC.setText(readMessage.subSequence(6, 8));
			}
			if (":GG#".equals(commandAtual)) {
				editTextUTCSet.setText(String.valueOf(readMessage.subSequence(
						1, 3)));
				strtemp = readMessage.subSequence(0, 1).toString();
				if (strtemp.equalsIgnoreCase("-")) {
					toggleUTC.setChecked(true);
					strtemp = readMessage.subSequence(1, 3).toString();
					UTC = (Integer.parseInt(strtemp)) * -1;
				} else {
					toggleUTC.setChecked(false);
					strtemp = readMessage.subSequence(1, 3).toString();
					UTC = Integer.parseInt(strtemp);
				}

			}

			if (":GS#".equals(commandAtual)) {
				TextHoraLST.setText(readMessage.subSequence(0, 2));
				TextMinLST.setText(readMessage.subSequence(3, 5));
				TextSegLST.setText(readMessage.subSequence(6, 8));
			}
			if (":GC#".equals(commandAtual)) {
				editTextMes.setText(readMessage.subSequence(0, 2));
				editTextDia.setText(readMessage.subSequence(3, 5));
				editTextAno.setText(readMessage.subSequence(6, 8));
			}
			if (commandAtual.contains(":St") || commandAtual.contains(":Sg")
					|| commandAtual.contains(":SL")
					|| commandAtual.contains(":SG")
					|| commandAtual.contains(":SC"))
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

	public void atualizar(View v) {
		String strtmp;
		int i;
		if (CheckLat.isChecked()) {
			if (!toggleNorteSulLat.isChecked()) {
				strtmp = ":St+";
			} else {
				strtmp = ":St-";
			}
			i = Integer.parseInt(editTextGrauLat.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "*";
			i = Integer.parseInt(editTextMinLat.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "#";
			command[6] = strtmp;
			response[6] = "0123456789";
			CheckLat.setChecked(false);
		} else {
			command[6] = ":Gt#";
			response[6] = "#";
			command[7] = ":Gt#";
			response[7] = "#";
		}
		if (CheckLog.isChecked()) {
			// :SgDDD*MM#
			if (!toggleLestOestLog.isChecked()) {
				strtmp = ":Sg+";
			} else {
				strtmp = ":Sg-";
			}
			i = Integer.parseInt(editTextGrauLog.getText().toString());
			strtmp = strtmp + String.format("%03d", i) + "*";
			i = Integer.parseInt(editTextMinLog.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "#";
			command[1] = strtmp;
			response[1] = "0123456789";
			CheckLog.setChecked(false);
		} else {
			command[1] = ":Gg#";
			response[1] = "#";
		}
		/*
		 * :SLHH:MM:SS# Set the local Time Returns: 0 � Invalid
		 */
		if (CheckTime.isChecked()) {
			// :SLHH:MM:SS#
			strtmp = ":SL";
			i = Integer.parseInt(editTextHoraTime.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + ":";
			i = Integer.parseInt(editTextMinTime.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + ":";
			i = Integer.parseInt(editTextSegTime.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "#";
			command[3] = strtmp;
			response[3] = "0123456789";
			CheckTime.setChecked(false);
		} else {
			command[3] = ":GL#";
			response[3] = "#";
		}
		/*
		 * :SGsHH.H# Set the number of hours added to local time to yield UTC
		 * Returns: 0 � Invalid 1 - Valid
		 */
		if (CheckUTC.isChecked()) {
			// :SGsHH#
			if (!toggleUTC.isChecked()) {
				strtmp = ":SG+";
			} else {
				strtmp = ":SG-";
			}
			i = Integer.parseInt(editTextUTCSet.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "#";
			command[2] = strtmp;
			response[2] = "0123456789";
			CheckUTC.setChecked(false);
		} else {
			command[2] = ":GG#";
			response[2] = "#";
		}
		command[4] = ":GS#";
		response[4] = "#";
		/*
		 * :SCMM/DD/YY# Change Handbox Date to MM/DD/YY Returns: <D><string> D =
		 * �0� if the date is invalid. The string is the null string. D = �1�
		 * for valid dates and the string is �Updating Planetary Data# #� Note:
		 * For LX200GPS this is the UTC data!
		 */
		if (CheckData.isChecked()) {
			// ::SCMM/DD/YY#
			strtmp = ":SC";
			i = Integer.parseInt(editTextMes.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "/";
			i = Integer.parseInt(editTextDia.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "/";
			i = Integer.parseInt(editTextAno.getText().toString());
			strtmp = strtmp + String.format("%02d", i) + "#";
			command[5] = strtmp;
			response[5] = "0123456789";
			CheckData.setChecked(false);
		} else {
			command[5] = ":GC#";
			response[5] = "#";
		}
		icom = 0;
	}


}
