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
	private String command[] = {":GD#",":GR#",":GD#",":GR#",":GD#",":GR#"};
	private String commandAtual = null;
	private String bufferCmd = "";

	private int icom = 0;
	private InputStream catalogue = null;
	private int filecatalogue=R.raw.cataloguemessier;
	private boolean StarFile=false;
	private EditText txtlocalizaDSS;
	private EditText txtaRAH;
	private EditText txtaRAM;
	private EditText txtaRAS;
	private EditText txtaDG;
	private EditText txtaDM;
	private EditText txtaDS;
	private TextView txtRAH;
	private TextView txtRAM;
	private TextView txtRAS;
	private TextView txtDG;
	private TextView txtDM;
	private TextView txtDS;
	private TextView txtvTextListaDss;
	private TextView txtvTextCommad;
	private ToggleButton toggleNorteSul;
	private ToggleButton toggleGotoSync;
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
		setContentView(R.layout.activity_goto_sync);
		txtlocalizaDSS = (EditText) findViewById(R.id.editTextlocalizaDSS);
		txtaRAH = (EditText) findViewById(R.id.editTextHoraAlvoRA);
		txtaRAM = (EditText) findViewById(R.id.editTextMinAlvoRA);
		txtaRAS = (EditText) findViewById(R.id.editTextSegAlvoRA);
		txtaDG = (EditText) findViewById(R.id.editTextGrauAlvoDEC);
		txtaDM = (EditText) findViewById(R.id.editTextMinAlvoDEC);
		txtaDS = (EditText) findViewById(R.id.editTextSegAlvoDEC);
		txtRAH = (TextView) findViewById(R.id.TextViewHoraRA);
		txtRAM = (TextView) findViewById(R.id.TextViewMinRA);
		txtRAS = (TextView) findViewById(R.id.TextViewSegRA);
		txtDG = (TextView) findViewById(R.id.TextViewGrauDEC);
		txtDM = (TextView) findViewById(R.id.TextViewMinDEC);
		txtDS = (TextView) findViewById(R.id.TextViewSegDEC);

		txtvTextListaDss = (TextView) findViewById(R.id.textViewListaDss);
		txtvTextCommad = (TextView) findViewById(R.id.textViewCommand);

		toggleNorteSul = (ToggleButton) findViewById(R.id.toggleButtonSinalAlvoDEC);
		toggleGotoSync = (ToggleButton) findViewById(R.id.toggleButtonGotoSync);
		txtlocalizaDSS.setInputType(InputType.TYPE_CLASS_NUMBER);
		//Bluetooth
		mmDevice = getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		if(mmDevice != null){
			try {
				connectedThread = new ConnectedThread(mmDevice, mHandler);
				new Thread(connectedThread).start();
			} catch (IOException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				//					e.printStackTrace();
			}
			MandaComando();
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
	public void GotoSync(View view) {
		icom=0;

		//SrHH:MM:SS# *
		command[1]="Sr"+txtaRAH.getText().toString() +":"+ txtaRAM.getText().toString() +":"+ txtaRAS.getText().toString()+"#"; 
		//SdsDD:MM:SS# *
		if (toggleNorteSul.isChecked())
		{
			command[2]="Sd+"+txtaDG.getText().toString() +":"+ txtaDM.getText().toString() +":"+ txtaDS.getText().toString()+"#";
		}
		else{
			command[2]="Sd-"+txtaDG.getText().toString() +":"+ txtaDM.getText().toString() +":"+ txtaDS.getText().toString()+"#"; 
		}

		if (toggleGotoSync.isChecked())
		{
			command[3] = "CS#";
		}
		else
		{
			command[3] = ":ST60#";
		}

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
								txtaRAH.setText(separated[3]);
								txtaRAM.setText(separated[4]);
								txtaRAS.setText("0");
								if (separated[5].equalsIgnoreCase("-"))
								{
									toggleNorteSul.setChecked(true);
								}
								else
								{
									toggleNorteSul.setChecked(false);
								}
								txtaDG.setText(separated[6]);
								txtaDM.setText(separated[7]);
								txtaDS.setText("0");
								break;
							}
						}
						else
						{
							if (separated[0].equalsIgnoreCase(txtlocalizaDSS.getText().toString()))
							{
								Texto = "Nome: "+separated[1]+separated[0]+" Const: "+separated[2]+"\n"+"RA: "+separated[3]+"h"+separated[4]+"m"+separated[5]+"s \n"+"DEC: "+separated[6]+separated[7]+"°"+separated[8]+"'"+separated[9]+"''"+"\n"+"BMAG: "+separated[10]+" VMAG: "+separated[11] +" SurfB: "+separated[12]+"\n"+"Type: "+separated[13]+"\n"+"Inf: "+separated[14];
								txtvTextListaDss.setText(Texto);
								txtaRAH.setText(separated[3]);
								txtaRAM.setText(separated[4]);
								txtaRAS.setText(separated[5]);
								if (separated[6].equalsIgnoreCase("-"))
								{
									toggleNorteSul.setChecked(true);
								}
								else
								{
									toggleNorteSul.setChecked(false);
								}
								txtaDG.setText(separated[7]);
								txtaDM.setText(separated[8]);
								txtaDS.setText(separated[9]);
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
	{//	Reply: HH:MM:SS# 

		txtvTextListaDss.setText(readMessage);
		if (":GR#".equals(commandAtual)) {
			txtRAH.setText(readMessage.subSequence(0, 2));
			txtRAM.setText(readMessage.subSequence(3, 5));
			txtRAS.setText(readMessage.subSequence(6, 8));
		}
		//	Reply: sDD*MM'SS# *
		if (":GD#".equals(commandAtual)) {
			txtDG.setText(readMessage.subSequence(0, 3));
			txtDM.setText(readMessage.subSequence(4, 6));
			txtDS.setText(readMessage.subSequence(7, 9));
		}

		//		txtvTextListaDss.setText(readMessage);
	}
	private void MandaComando()
	{

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!connectedThread.isFinished()) {
					try {
						connectedThread.write(command[icom].getBytes());
						commandAtual=command[icom];
						if (icom == 0)
						{
							command[0]=":GD#";
						}
						if (icom == 1)
						{
							command[1]=":GR#";
						}
						if (icom == 2)
						{
							command[2]=":GD#";
						}
						if (icom == 3)
						{
							command[3]=":GR#";
						}
						if (icom == 4)
						{
							command[4]=":GD#";
						}
						if (icom == 5)
						{
							command[5]=":GR#";
						}
						icom++;
						if (icom > 4)
						{
							icom=0;
						}
						Thread.sleep(500);

					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		}).start();
	}

}