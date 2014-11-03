package br.com.astronomoamador.firgoto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;


public class GotoSyncActivity extends Activity {

	////////variaveis geral
	InputStream catalogue = null;
	int filecatalogue=0;
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
					//	 filecatalogue=R.raw.cataloguengc;
					break;
			case R.id.radioM:
				if (checked)
					filecatalogue=R.raw.cataloguemessier;
					break;
			case R.id.radioIC:
				if (checked)
					filecatalogue=R.raw.catalogueic;
				break; 
			case R.id.radioNGC:
				if (checked)
					filecatalogue=R.raw.cataloguengc;
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
						if (separated[0].equalsIgnoreCase(txtlocalizaDSS.getText().toString()))
						{
							
							
							String Texto = "Nome: "+separated[1]+separated[0]+" Const: "+separated[2]+"\n"+"RA: "+separated[3]+"h"+separated[4]+"m"+separated[5]+"s \n"+"DEC: "+separated[6]+separated[7]+"°"+separated[8]+"'"+separated[9]+"''"+"\n"+"BMAG: "+separated[10]+" VMAG: "+separated[11] +" SurfB: "+separated[12]+"\n"+"Type: "+separated[13]+"\n"+"Inf: "+separated[14];
							
							
							txtvTextListaDss.setText(Texto);

							txtRAH.setText(separated[3]);
							txtRAM.setText(separated[4]);
							txtRAS.setText(separated[5]);

							if (separated[6].equalsIgnoreCase("+"))
							{
								toggleNorteSul.setChecked(false);
							}
							else
							{
								toggleNorteSul.setChecked(true);

							}
							txtDG.setText(separated[7]);
							txtDM.setText(separated[8]);
							txtDS.setText(separated[9]);


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

}
