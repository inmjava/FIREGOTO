package br.com.astronomoamador.firgoto;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MostrarControleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostrar_controle);

		addListenerOnButton();



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

		Button bGuia = (Button) findViewById(R.id.ButtonGuia);

		bGuia.setOnClickListener(new OnClickListener()
		{
			TextView xTeste = (TextView) findViewById(R.id.TextViewHoraRA);

			@Override
			public void onClick(View v) {
				xTeste.setText("11");
			}
		});

		bGuia.setOnLongClickListener(new View.OnLongClickListener() 
		{
			TextView xTeste = (TextView) findViewById(R.id.TextViewHoraRA);

			    @Override
			    public boolean onLongClick(View v) {
					xTeste.setText("22");
			      return false;
			    }
			  });


		
		
		
		
		
	}

}