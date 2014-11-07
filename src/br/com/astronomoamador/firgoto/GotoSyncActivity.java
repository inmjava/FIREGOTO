package br.com.astronomoamador.firgoto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import br.com.astronomoamador.firgoto.bluetooth.ConnectedThread;
import br.com.astronomoamador.firgoto.bluetooth.Constants;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class GotoSyncActivity extends Activity {

	///Variaveis Bluetooth
	private BluetoothDevice mmDevice;
	private ConnectedThread connectedThread;


	////////variaveis geral
	private InputStream catalogue = null;
	private int filecatalogue=R.raw.cataloguemessier;
	boolean StarFile=false;
	boolean NorteSul=false;
	private EditText txtlocalizaDSS;
	private EditText txtRAH;
	private EditText txtRAM;
	private EditText txtRAS;
	private EditText txtDG;
	private EditText txtDM;
	private EditText txtDS;
	private TextView txtvTextListaDss;
	private ToggleButton toggleNorteSul;






	private final Handler mHandler = new Handler() {
	@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				txtvTextListaDss.setText(readMessage);
				leResposta(readMessage);
				break;
			}
		}
	};



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goto_sync);

		txtlocalizaDSS = (EditText) findViewById(R.id.editTextlocalizaDSS);
		txtRAH = (EditText) findViewById(R.id.editTextHoraAlvoRA);
		txtRAM = (EditText) findViewById(R.id.editTextMinAlvoRA);
		txtRAS = (EditText) findViewById(R.id.editTextSegAlvoRA);
		txtDG = (EditText) findViewById(R.id.editTextGrauAlvoDEC);
		txtDM = (EditText) findViewById(R.id.editTextMinAlvoDEC);
		txtDS = (EditText) findViewById(R.id.editTextSegAlvoDEC);
		txtvTextListaDss = (TextView) findViewById(R.id.textViewListaDss);
		toggleNorteSul = (ToggleButton) findViewById(R.id.toggleButtonSinalAlvoDEC);
		txtlocalizaDSS.setInputType(InputType.TYPE_CLASS_NUMBER);

		//Bluetooth
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
		getMenuInflater().inflate(R.menu.goto_sync, menu);
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


	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();
		// Check which radio button was clicked
		try
		{
			switch(view.getId()) {
			case R.id.radioC:
				if (checked)
					filecatalogue=R.raw.cataloguecaldwell;
				StarFile = false;
				txtlocalizaDSS.setInputType(InputType.TYPE_CLASS_NUMBER);
				break;
			case R.id.radioM:
				if (checked)
					filecatalogue=R.raw.cataloguemessier;
				StarFile = false;
				txtlocalizaDSS.setInputType(InputType.TYPE_CLASS_NUMBER);
				break;
			case R.id.radioIC:
				if (checked)
					filecatalogue=R.raw.catalogueic;
				StarFile = false;
				txtlocalizaDSS.setInputType(InputType.TYPE_CLASS_NUMBER);
				break; 
			case R.id.radioNGC:
				if (checked)
					filecatalogue=R.raw.cataloguengc;
				StarFile = false;
				txtlocalizaDSS.setInputType(InputType.TYPE_CLASS_NUMBER);
				break;
			case R.id.radioStar:
				if (checked)
					filecatalogue=R.raw.star;
				txtlocalizaDSS.setInputType(InputType.TYPE_CLASS_TEXT);
				StarFile = true;
				break;

			}
		}
		catch (Exception e) 
		{
			String error="";
			error=e.getMessage();
		}
	}

	public void localizaDeep(View v){

		try
		{
			catalogue = this.getResources().openRawResource(filecatalogue);

			if (catalogue != null)
			{
				InputStreamReader inputreader = new InputStreamReader(catalogue); 
				BufferedReader buffreader = new BufferedReader(inputreader); 
				String line = ""; 
				try
				{
					while ((line = buffreader.readLine()) != null)
					{
						String[] separated = line.split(";");

						String Texto=null;

						if (StarFile)
						{

							if (line.toUpperCase().contains((txtlocalizaDSS.getText().toString().toUpperCase())))
							{


								Texto = "Nome: "+separated[1]+" - "+separated[2]+"\n"+"RA: "+separated[3]+"h"+separated[4]+"m \n"+"DEC: "+separated[5]+separated[6]+"°"+separated[7]+"'"+"\n"+" VMAG: "+separated[9] +" Mag ABS: "+separated[10]+"\n"+"Spectral: "+separated[8]+"\n";


								txtvTextListaDss.setText(Texto);

								txtRAH.setText(separated[3]);
								txtRAM.setText(separated[4]);
								txtRAS.setText("0");

								if (separated[5].equalsIgnoreCase("-"))
								{
									toggleNorteSul.setChecked(true);
								}
								else
								{
									toggleNorteSul.setChecked(false);

								}
								txtDG.setText(separated[6]);
								txtDM.setText(separated[7]);
								txtDS.setText("0");
								break;

							}
						}
						else
						{





							if (separated[0].equalsIgnoreCase(txtlocalizaDSS.getText().toString()))
							{


								Texto = "Nome: "+separated[1]+separated[0]+" Const: "+separated[2]+"\n"+"RA: "+separated[3]+"h"+separated[4]+"m"+separated[5]+"s \n"+"DEC: "+separated[6]+separated[7]+"°"+separated[8]+"'"+separated[9]+"''"+"\n"+"BMAG: "+separated[10]+" VMAG: "+separated[11] +" SurfB: "+separated[12]+"\n"+"Type: "+separated[13]+"\n"+"Inf: "+separated[14];


								txtvTextListaDss.setText(Texto);

								txtRAH.setText(separated[3]);
								txtRAM.setText(separated[4]);
								txtRAS.setText(separated[5]);

								if (separated[6].equalsIgnoreCase("-"))
								{
									toggleNorteSul.setChecked(true);
								}
								else
								{
									toggleNorteSul.setChecked(false);

								}
								txtDG.setText(separated[7]);
								txtDM.setText(separated[8]);
								txtDS.setText(separated[9]);
								break;

							}

						}

						if(Texto==null)
						{
							txtvTextListaDss.setText("not found");
						}


					}
				}catch (Exception e) 
				{
					e.printStackTrace();
				}
				catalogue.close();
			}
			else
			{
				txtvTextListaDss.setText("Nao Achou");

			}

		}
		catch (Exception e) 
		{
			String error="";
			error=e.getMessage();
		}

	}


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
		txtvTextListaDss.setText(readMessage);
	}
}
