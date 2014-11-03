package br.com.astronomoamador.firgoto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ToggleButton;


public class GotoSyncActivity extends Activity {

	////////variaveis geral
	InputStream catalogue = null;
	int filecatalogue=0;
	boolean NorteSul=false;






	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goto_sync);
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
					//	filecatalogue=R.raw.cataloguengc;
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

		EditText txtlocalizaDSS = (EditText) findViewById(R.id.editTextlocalizaDSS);
		EditText txtRAH = (EditText) findViewById(R.id.editTextHoraAlvoRA);
		EditText txtRAM = (EditText) findViewById(R.id.editTextMinAlvoRA);
		EditText txtRAS = (EditText) findViewById(R.id.editTextSegAlvoRA);
		EditText txtDG = (EditText) findViewById(R.id.editTextGrauAlvoDEC);
		EditText txtDM = (EditText) findViewById(R.id.editTextMinAlvoDEC);
		EditText txtDS = (EditText) findViewById(R.id.editTextSegAlvoDEC);
		TextView txtvTextListaDss = (TextView) findViewById(R.id.textViewListaDss);
		ToggleButton toggleNorteSul = (ToggleButton) findViewById(R.id.toggleButtonSinalAlvoDEC);

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
							txtvTextListaDss.setText(line);

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
