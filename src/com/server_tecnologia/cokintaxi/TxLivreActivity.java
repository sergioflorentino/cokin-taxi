package com.server_tecnologia.cokintaxi;

import java.util.Calendar;

import org.ispeech.SpeechSynthesis;
import org.ispeech.SpeechSynthesisEvent;
import org.ispeech.error.BusyException;
import org.ispeech.error.InvalidApiKeyException;
import org.ispeech.error.NoNetworkException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class TxLivreActivity  extends Activity {
	
	ImageView foto;
	TextView  nome, placa, modelo, distancia, tempo;
	RatingBar r_aval_motorista,r_aval_carro;
	Button btn_confirmar;
    private static final String url_foto = "http://www.server-tecnologia.com/cokin_taxi/mot/";
    ImageLoader mot_foto;
	SpeechSynthesis synthesis;
	private static final String TAG = "TxLivreActivity";
	Context _context;
	boolean primeiravez = true;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seleciona_taxi);
		_context = this.getApplicationContext();
		
		prepareTTSEngine();
		synthesis.setStreamType(AudioManager.STREAM_MUSIC);
		
		// Inicializa campos do form
		btn_confirmar = (Button) findViewById(R.id.btn_confirmar);
		nome = (TextView) findViewById(R.id.nome_motorista);
		placa = (TextView) findViewById(R.id.placa_veiculo);
		modelo = (TextView) findViewById(R.id.modelo_veiculo);
		r_aval_motorista = (RatingBar) findViewById(R.id.r_aval_motorista);
		r_aval_carro = (RatingBar) findViewById(R.id.r_aval_carro);
		distancia = (TextView) findViewById(R.id.txt_distancia);
		tempo = (TextView) findViewById(R.id.txt_tempo);
		foto = (ImageView) findViewById(R.id.foto_motorista);
		mot_foto = new ImageLoader( getApplicationContext() );
		
		findViewById(R.id.foto_motorista).setOnClickListener(new OnSpeakListener());
		
		// save button click event
		btn_confirmar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Criar ocorrencia e atualizar status do motorista e do usuario
				// starting background task to update product
				Toast.makeText(getBaseContext(),"Clicou em confirmar", Toast.LENGTH_SHORT).show();
//		        String words = "Ola "+MainActivity.g_usuario+" ! Estou há "+distancia.getText()+
//		        		" ! Chegarei até você em "+tempo.getText()+"utes";
// 		        speakWords(words);
			}
		});
		
	    if(MainActivity.txdisponiveisList!=null){
	    	// atualiza com dados
	    	nome.setText(MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_NOME));
	    	placa.setText(MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_PLACA));
	    	modelo.setText(MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_MODELO));
	    	r_aval_motorista.setRating(Float.parseFloat(MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_AVAL_MOT))); 
	    	r_aval_carro.setRating(Float.parseFloat(MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_AVAL_CARRO)));
	    	distancia.setText(MainActivity.FormataDistancia(MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_DISTANCIA),true));
	    	tempo.setText(MainActivity.TempoMedio( MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_DISTANCIA),true));
			if ( MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_FOTO).equals("1") ){
			    mot_foto.DisplayImage(url_foto+MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_EMAIL)+".jpg", foto);
			} else {	
			    foto.setImageResource(R.drawable.ic_contact_picture);
			}
	    }
	    
	}
	
	  @Override
	  protected void onResume() {
		  // TODO Auto-generated method stub
		  super.onResume();
		  if (primeiravez){
			  foto.performClick();
			  primeiravez = false;
		  }
		  
	  }

	private void prepareTTSEngine() {
		try {
			synthesis = SpeechSynthesis.getInstance(this);
			synthesis.setSpeechSynthesisEvent(new SpeechSynthesisEvent() {

				public void onPlaySuccessful() {
					Log.i(TAG, "onPlaySuccessful");
				}

				public void onPlayStopped() {
					Log.i(TAG, "onPlayStopped");
				}

				public void onPlayFailed(Exception e) {
					Log.e(TAG, "onPlayFailed");
					

					AlertDialog.Builder builder = new AlertDialog.Builder(TxLivreActivity.this);
					builder.setMessage("Error[TTSActivity]: " + e.toString())
					       .setCancelable(false)
					       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
				}

				public void onPlayStart() {
					Log.i(TAG, "onPlayStart");
				}

				@Override
				public void onPlayCanceled() {
					Log.i(TAG, "onPlayCanceled");
				}
				
				
			});


		} catch (InvalidApiKeyException e) {
			Log.e(TAG, "Invalid API key\n" + e.getStackTrace());
			Toast.makeText(_context, "ERROR: Invalid API key", Toast.LENGTH_LONG).show();
		}

	}
	
	private class OnSpeakListener implements OnClickListener {

		public void onClick(View v) {
			
			try {
		        String words = Saudacao()+" ! Estou há "+distancia.getText()+
        		" ! Chegarei até você em "+tempo.getText()+"utos";
		        synthesis.setVoiceType("brportuguesefemale");
				synthesis.speak(words);

			} catch (BusyException e) {
				Log.e(TAG, "SDK is busy");
				e.printStackTrace();
				Toast.makeText(_context, "ERROR: SDK is busy", Toast.LENGTH_LONG).show();
			} catch (NoNetworkException e) {
				Log.e(TAG, "Network is not available\n" + e.getStackTrace());
				Toast.makeText(_context, "ERROR: Network is not available", Toast.LENGTH_LONG).show();
			}
		}
	}


    public String Saudacao(){
		String saudacao = null;
		int mhora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if (mhora < 12)
		    saudacao= "Bom dia";
		else if(mhora >=12 && mhora < 18)
		    saudacao= "Boa tarde";
		else if(mhora >= 18 && mhora < 24)
		    saudacao= "Boa noite";
    	return saudacao;
    	
    }
	public class OnStopListener implements OnClickListener {

		public void onClick(View v) {
			if (synthesis != null) {
				synthesis.stop();
			}
			
		}
	}



	@Override
	protected void onPause() {
		synthesis.stop();	//Optional to stop the playback when the activity is paused
		super.onPause();
	}




}
