package br.com.astronomoamador.firgoto;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import br.com.astronomoamador.firgoto.bluetooth.ConnectedThread;
import br.com.astronomoamador.firgoto.bluetooth.Constants;
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




public class MostrarControleActivity extends Activity {
	private BluetoothDevice mmDevice;
	private ConnectedThread connectedThread;
	////////variaveis geral
	private String command[] = {":GD#",":GR#",":GA#",":GZ#",":GD#",":GR#",":GA#",":GZ#"};
	private String response[] = {"#","#","#","#","#","#","#","#"};
	private String commandAtual = null;
	private String bufferCmd = "";
	private int icom = 0;
	private TextView txtAZG;
	private TextView txtAZM;
	private TextView txtAZS;
	private TextView txtALTG;
	private TextView txtALTM;
	private TextView txtALTS;
	private TextView txtRAH;
	private TextView txtRAM;
	private TextView txtRAS;
	private TextView txtDG;
	private TextView txtDM;
	private TextView txtDS;
	private TextView textCommand;
	Button bGuia,bCentro,bLocalizar,bRapido,bSync;
	Button buttonNE,buttonN,buttonNO,ButtonL,ButtonStop,ButtonO,ButtonSE,ButtonS,ButtonSO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostrar_controle);
		textCommand = (TextView) findViewById(R.id.textCommand);
		txtAZG = (TextView) findViewById(R.id.TextViewGrauAZ);
		txtAZM = (TextView) findViewById(R.id.TextViewMinAZ);
		txtAZS = (TextView) findViewById(R.id.TextViewSegAZ);
		txtALTG = (TextView) findViewById(R.id.TextViewGrauALT);
		txtALTM = (TextView) findViewById(R.id.TextViewMinALT);
		txtALTS = (TextView) findViewById(R.id.TextViewSegALT);
		txtRAH = (TextView) findViewById(R.id.TextViewHoraRA);
		txtRAM = (TextView) findViewById(R.id.TextViewMinRA);
		txtRAS = (TextView) findViewById(R.id.TextViewSegRA);
		txtDG = (TextView) findViewById(R.id.TextViewGrauDEC);
		txtDM = (TextView) findViewById(R.id.TextViewMinDEC);
		txtDS = (TextView) findViewById(R.id.TextViewSegDEC);
		buttonNE = (Button) findViewById(R.id.buttonNE);
		buttonN = (Button) findViewById(R.id.buttonN);
		buttonNO = (Button) findViewById(R.id.buttonNO);
		ButtonL = (Button) findViewById(R.id.ButtonL);
		ButtonStop = (Button) findViewById(R.id.ButtonStop);
		ButtonO = (Button) findViewById(R.id.ButtonO);
		ButtonSE = (Button) findViewById(R.id.ButtonSE);
		ButtonS = (Button) findViewById(R.id.ButtonS);
		ButtonSO = (Button) findViewById(R.id.ButtonSO);


		bGuia = (Button) findViewById(R.id.ButtonGuia);
		bCentro= (Button) findViewById(R.id.ButtonCentro);
		bLocalizar= (Button) findViewById(R.id.ButtonLocalizar);
		bRapido= (Button) findViewById(R.id.ButtonRapido);
		bSync= (Button) findViewById(R.id.ButtonSync);

		



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
		addListenerOnButton();
	}


	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				if (readMessage.contains(response[icom]) || response[icom].contains(readMessage))
				{
					bufferCmd=bufferCmd+readMessage;	
					leResposta(bufferCmd);
					String tmp = bufferCmd;
					bufferCmd="";
				}
				else
				{
					bufferCmd=bufferCmd+readMessage;
				}
				readBuf=null;
				readMessage=null;
				break;
			}
		}
	};

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



	private void MandaComando()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!connectedThread.isFinished()) {
					try {
						connectedThread.write(command[icom].getBytes());
						commandAtual=command[icom];
						Thread.sleep(400);
						if (icom == 0)
						{
							command[0]=":GD#";
							response[0] = "#";
						}
						if (icom == 1)
						{
							command[1]=":GR#";
							response[1] = "#";
						}
						if (icom == 2)
						{
							command[2]=":GZ#";
							response[2] = "#";
						}
						if (icom == 3)
						{
							command[3]=":GA#";
							response[3] = "#";
						}
						if (icom == 4)
						{
							command[4]=":GD#";
							response[4] = "#";
						}
						if (icom == 5)
						{
							command[5]=":GR#";
							response[5] = "#";
						}
						if (icom == 6)
						{
							command[6]=":GZ#";
							response[6] = "#";
						}
						if (icom == 7)
						{
							command[7]=":GA#";
							response[7] = "#";
						}



						icom++;
						if (icom > 7)
						{
							icom=0;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mostrar_controle, menu);
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


	public void addListenerOnButton() {



		bGuia.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				command[1]=":RG#";
				icom=0;
			}
		});
		bCentro.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				command[1]=":RC#";
				icom=0;
			}
		});
		bLocalizar.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				command[1]=":RM#";
				icom=0;
			}
		});
		bRapido.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				command[1]=":RS#";
				icom=0;
			}
		});

		bSync.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				command[1]=":CS#";
				icom=0;
			}
		});


		////////////////////////////////////////////////////////////////////////////////
		/*
		 * 
		 * Q – Movement Commands 
:Q# Halt all current slewing 
 Returns:Nothing 
:Qe# Halt eastward Slews 
 Returns: Nothing 
:Qn# Halt northward Slews 
 Returns: Nothing 
:Qs# Halt southward Slews 
 Returns: Nothing 
:Qw# Halt westward Slews
		 * 
		 */

		buttonNE.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				command[1]=":Qe#:Qn#";
				icom=0;
			}
		});
		buttonN.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				command[1]=":Qn#";
				icom=0;
			}
		});
		buttonNO.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				command[1]=":Qn#:Qw#";
				icom=0;
			}
		});
		ButtonL.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				command[1]=":Qe#";
				icom=0;
			}
		});
		ButtonStop.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				command[1]=":Q#";
				icom=0;
			}
		});
		ButtonO.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				command[1]=":Qw#";
				icom=0;
			}
		});
		ButtonSE.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				command[1]=":Qs#:Qe#";
				icom=0;
			}
		});
		ButtonS.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				command[1]=":Qs#";
				icom=0;
			}
		});
		ButtonSO.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				command[1]=":Qs#:Qw#";
				icom=0;
			}
		});

		///////////////////////////////////////////////////////////////////////////////////
		/*
		 * 
		 * 
		 * :Me# Move Telescope East at current slew rate 
 Returns: Nothing 
:Mn# Move Telescope North at current slew rate 
 Returns: Nothing 
:Ms# Move Telescope South at current slew rate 
 Returns: Nothing 
:Mw# Move Telescope West at current slew rate 
 Returns: Nothing 
		 * 
		 */

		buttonNE.setOnLongClickListener(new View.OnLongClickListener() 
		{

			@Override
			public boolean onLongClick(View v) {
				command[1]=":Me#:Mn#";
				icom=0;
				return false;
			}
		});
		buttonN.setOnLongClickListener(new View.OnLongClickListener() 
		{

			@Override
			public boolean onLongClick(View v) {
				command[1]=":Mn#";
				icom=0;
				return false;
			}
		});
		buttonNO.setOnLongClickListener(new View.OnLongClickListener() 
		{

			@Override
			public boolean onLongClick(View v) {
				command[1]=":Mw#:Mn#";
				icom=0;
				return false;
			}
		});
		ButtonL.setOnLongClickListener(new View.OnLongClickListener() 
		{

			@Override
			public boolean onLongClick(View v) {
				command[1]=":Me#";
				icom=0;
				return false;
			}
		});
		ButtonStop.setOnLongClickListener(new View.OnLongClickListener() 
		{

			@Override
			public boolean onLongClick(View v) {
				command[1]=":Q#";
				icom=0;
				return false;
			}
		});
		ButtonO.setOnLongClickListener(new View.OnLongClickListener() 
		{

			@Override
			public boolean onLongClick(View v) {
				command[1]=":Mw#";
				icom=0;
				return false;
			}
		});
		ButtonSE.setOnLongClickListener(new View.OnLongClickListener() 
		{

			@Override
			public boolean onLongClick(View v) {
				command[1]=":Me#:Ms#";
				icom=0;
				return false;
			}
		});
		ButtonS.setOnLongClickListener(new View.OnLongClickListener() 
		{

			@Override
			public boolean onLongClick(View v) {
				command[1]=":Ms#";
				icom=0;
				return false;
			}
		});
		ButtonSO.setOnLongClickListener(new View.OnLongClickListener() 
		{

			@Override
			public boolean onLongClick(View v) {
				command[1]=":Mw#:Ms#";
				icom=0;
				return false;
			}
		});






	}

	private void leResposta(String readMessage)
	{//	Reply: HH:MM:SS# 
		try{
			textCommand.setText(commandAtual);
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
			if (":GA#".equals(commandAtual)) {
				txtALTG.setText(readMessage.subSequence(0, 3));
				txtALTM.setText(readMessage.subSequence(4, 6));
				txtALTS.setText(readMessage.subSequence(7, 9));
			}
			//	Reply:  DDD*MM’SS# *
			if (":GZ#".equals(commandAtual)) {
				txtAZG.setText(readMessage.subSequence(0, 3));
				txtAZM.setText(readMessage.subSequence(4, 6));
				txtAZS.setText(readMessage.subSequence(7, 9));
			}
			if (":ST60#".equals(commandAtual) || ":CS#".equals(commandAtual)) {
				if (readMessage.equalsIgnoreCase("0"))
				{
					Toast.makeText(getApplicationContext(), R.string.vamos_la, Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(), R.string.abaixo_do_horizonte, Toast.LENGTH_LONG).show();
				}
			}

		} catch (Exception e) {
			String strtemp="error  " + commandAtual + " - " + readMessage.toString();
			Toast.makeText(getApplicationContext(), strtemp , Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		//		txtvTextListaDss.setText(readMessage);
	}


}