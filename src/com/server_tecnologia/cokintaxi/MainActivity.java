package com.server_tecnologia.cokintaxi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.ispeech.SpeechSynthesis;
import org.ispeech.SpeechSynthesisEvent;
import org.ispeech.error.BusyException;
import org.ispeech.error.InvalidApiKeyException;
import org.ispeech.error.NoNetworkException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import android.content.ContentValues;
import android.support.v4.app.FragmentActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.OverlayItem;
import com.server_tecnologia.cokinlib.utils.CustomPinPoint;
import com.server_tecnologia.cokinlib.utils.GMapV2Direction;
import com.server_tecnologia.cokinlib.utils.GPSTracker;
import com.server_tecnologia.cokinlib.utils.ImageLoader;
import com.server_tecnologia.cokinlib.utils.JSONParser;
import com.server_tecnologia.cokinlib.utils.JSONParser2;


public class MainActivity extends FragmentActivity implements OnMarkerDragListener,OnMapClickListener, OnMarkerClickListener {

	public static Handler handler_taxi;
	public static Runnable taxiRunnable = null;
    public static Marker PosTaxista,PosCliente;
    public ArrayList<LatLng> rota_corrida = new ArrayList<LatLng>();    
	private GoogleMap mMap;
	  public static GPSTracker gps;
	  public static int seek_distancia = 2;
	  public static int vel_media = 25;  //
	  public static int cont_taxista = 0;
	  final int RQS_GooglePlayServices = 1;
      static LatLng MinhaLocalizacao;
      static LatLng PosicaoTaxista;
      GeoPoint geo;
      SupportMapFragment  mMapFragment;
	  MapController mapController;
	  CustomPinPoint itemizedOverlay;
	  private LocationManager locationManager;
	  private GeoUpdateHandler geoUpdateHandler;
	  public GMapV2Direction md;
	  public static ArrayList<HashMap<String, String>> txdisponiveisList;
	  public static ArrayList<HashMap<String, String>> txstatusList;
	  public static ListView lv_txdisponiveis;
	  public static String g_usuario= "";
	  public static int g_intervalo =  15000;
	  public static Boolean g_login = false;
	  public static Boolean g_forcasaida = false;
	  static TextView txtcha_distancia;
	  static TextView txtcha_tempo;
	  static TextView subtitulo1,subtitulo2;
	  TextView txtcha_qtdcarros;
	  Button txtbt_chamar;
	  Button txtbt_cancelar;
	  RelativeLayout panel_motorista;
      public static TextView  nome, placa, modelo;
	  RatingBar r_aval_motorista,r_aval_carro;
	  ImageView foto;
	  private static final String url_foto = "http://www.server-tecnologia.com/cokin_taxi/mot/";

		private static final String TAG = "Cokin +Táxi";
		static String url_taxisdisponiveis = "http://www.server-tecnologia.com/cokin_taxi/taxis_disponiveis.php";
		static String url_salvarchamada = "http://www.server-tecnologia.com/cokin_taxi/salvarchamada.php";
		static String url_ler_chamada = "http://www.server-tecnologia.com/cokin_taxi/ler_chamada.php";
		static String url_status_chamada = "http://www.server-tecnologia.com/cokin_taxi/status_chamada.php";
		
		// JSON Node names
		public static final String TAG_SUCCESS = "success";
		public static final String TAG_TXDISPONIVEIS = "tx_disponiveis";
		public static final String TAG_EMAIL = "mo_email";
		public static final String TAG_NOME = "mo_nome";
		public static final String TAG_CELULAR = "mo_celular";
		public static final String TAG_FOTO = "mo_foto";
		public static final String TAG_MODELO = "mo_modelo";
		public static final String TAG_PLACA = "mo_placa";
		public static final String TAG_LAT = "mo_lat";
		public static final String TAG_LNG = "mo_lng";
		public static final String TAG_AVAL_CARRO = "mo_aval_carro";
		public static final String TAG_AVAL_MOT = "mo_aval_mot";
		public static final String TAG_STATUS = "mo_status";
		public static final String TAG_STATUSCOKIN = "mo_statuscokin";
		public static final String TAG_DISTANCIA = "distancia";
		
		// Creating JSON Parser object
		JSONParser jParser = new JSONParser();
		JSONParser2 jParser2 = new JSONParser2();
	    private OverlayItem inDrag=null;
	    
		Context our_context;
		
		// products JSONArray
		JSONArray txdisponiveis = null;
		JSONArray txstatus = null;
		
	    ImageLoader mot_foto;
		SpeechSynthesis synthesis;
		Context _context;


	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);

		_context = this.getApplicationContext();
		//cont_taxista = 0;
        setupLogin();

	  }
	  
	  private void InicializaApp(){
			prepareTTSEngine();
			synthesis.setStreamType(AudioManager.STREAM_MUSIC);
		    
			txtcha_distancia = (TextView) findViewById(R.id.cha_distancia);
			txtcha_tempo = (TextView) findViewById(R.id.cha_tempo);
			subtitulo1 = (TextView) findViewById(R.id.subtitulo1);
			subtitulo2 = (TextView) findViewById(R.id.subtitulo2);
			txtcha_qtdcarros = (TextView) findViewById(R.id.cha_qtdcarros);
			txtbt_chamar = (Button) findViewById(R.id.bt_chamar);
			txtbt_cancelar = (Button) findViewById(R.id.bt_cancelar);
			panel_motorista = (RelativeLayout) findViewById(R.id.panel_motorista);

			nome = (TextView) findViewById(R.id.nome_motorista);
			placa = (TextView) findViewById(R.id.placa_veiculo);
			modelo = (TextView) findViewById(R.id.modelo_veiculo);
			r_aval_motorista = (RatingBar) findViewById(R.id.r_aval_motorista);
			r_aval_carro = (RatingBar) findViewById(R.id.r_aval_carro);
			foto = (ImageView) findViewById(R.id.foto_motorista);
			mot_foto = new ImageLoader( getApplicationContext() );
			
			
		    setUpMapIfNeeded();

		    geoUpdateHandler = new GeoUpdateHandler();
		    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
		        createGpsDisabledAlert();
		    } else {
		        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
		                0,geoUpdateHandler);
		    }
		    
			// button click event
			txtbt_chamar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					// creating new product in background thread
					if ( txdisponiveisList.size() > 0 ){
						
						if   (txtbt_chamar.getText().equals("Finalizar & Avaliar")){
							// Clicou em Finalizar & Avaliar
							// Volta ao estado inicial
					    	if (!(handler_taxi == null)){     // Para de verificar status da corrida
				   	        	handler_taxi.removeCallbacks(taxiRunnable);
				   	        }
						    	
						    // Reinicia para uma nova chamada 
							LayoutParams params_chamar = txtbt_chamar.getLayoutParams();
							params_chamar.width = LayoutParams.MATCH_PARENT;
							txtbt_chamar.setLayoutParams(params_chamar);
							txtbt_chamar.setText("Chamar Táxi");
							Esconde_Motorista();
							setUpMap();
							
						}else if   (txtbt_chamar.getText().equals("Confirmar")){
							// Clicou em confirmar
						      txstatusList = new ArrayList<HashMap<String, String>>();
					 		  SalvarChamada tsk_SalvarChamada =  new SalvarChamada();
					          tsk_SalvarChamada.execute();
							
						}else if (txtbt_chamar.getText().equals("Chamar Táxi")){

							LayoutParams params_chamar = txtbt_chamar.getLayoutParams();
							params_chamar.width = LayoutParams.WRAP_CONTENT;
							txtbt_chamar.setLayoutParams(params_chamar);
							txtbt_chamar.setText("Confirmar");
							txtbt_cancelar.setVisibility(View.VISIBLE);
						    if(txdisponiveisList!=null){
						    	// atualiza com dados
						    	nome.setText(MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_NOME));
						    	placa.setText(MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_PLACA));
						    	modelo.setText(MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_MODELO));
						    	r_aval_motorista.setRating(Float.parseFloat(MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_AVAL_MOT))); 
						    	r_aval_carro.setRating(Float.parseFloat(MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_AVAL_CARRO)));
								if ( MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_FOTO).equals("1") ){
								    mot_foto.DisplayImage(url_foto+MainActivity.txdisponiveisList.get(0).get(MainActivity.TAG_EMAIL)+".jpg", foto);
								} else {	
								    foto.setImageResource(R.drawable.ic_contact_picture);
								}
								// Toca saudação
								Fala_Saudacao();
								Exibe_Motorista();

						    }
						    
						} 
						  
					}  else {
						Toast.makeText(MainActivity.this, "Não existe táxis disponiveis no momento!", Toast.LENGTH_LONG).show();  
					}
				}
			});
			
			// button click event
			txtbt_cancelar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					Esconde_Motorista();
						
				}
			});

	  }
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      
		         // Login Efetuado com sucesso      
		    	 InicializaApp();
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         // Login Cancelado
		    	 //finaliza app
		    	 finish();
		     }
		  }
	  }
	  
	  
	  @Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
	      if (keyCode == KeyEvent.KEYCODE_BACK) {
	          moveTaskToBack(true);
	          return true;
	      }
	      return super.onKeyDown(keyCode, event);
	  }

	  public void Exibe_Motorista(){
			panel_motorista.setVisibility(View.VISIBLE);
			AnimationSet set = new AnimationSet(true);
			Animation animation = new AlphaAnimation(0.0f, 1.0f);
			animation.setDuration(100);
			set.addAnimation(animation);
			TranslateAnimation slide = new TranslateAnimation(0, 0, -280,0 ); 
			slide.setDuration(800); 
			slide.setFillAfter(true);
			set.addAnimation(slide);
		
			AnimationListener animListener = new AnimationListener() {
		        LayoutParams params = panel_motorista.getLayoutParams();
			     @Override
			     public void onAnimationStart(Animation arg0) {}            
			     @Override
			     public void onAnimationRepeat(Animation arg0) {

			     }                   
			     @Override
			     public void onAnimationEnd(Animation arg0) {
			    	 params.height = 280;
			    	 panel_motorista.setLayoutParams(params);
			    	 //
			     }
			 };

			slide.setAnimationListener(animListener);
			panel_motorista.startAnimation(slide);
	  }
	  
	  public void Esconde_Motorista(){
			LayoutParams params_cancelar = txtbt_chamar.getLayoutParams();
			params_cancelar.width = LayoutParams.MATCH_PARENT;
			txtbt_chamar.setLayoutParams(params_cancelar);
			txtbt_chamar.setText("Chamar Táxi");
			txtbt_cancelar.setVisibility(View.GONE);
			
			AnimationSet set_esconde = new AnimationSet(true);
			Animation animation = new AlphaAnimation(0.0f, 1.0f);
			animation.setDuration(100);
			set_esconde.addAnimation(animation);
			TranslateAnimation slide = new TranslateAnimation(0, 0, 0, -285); 
			slide.setDuration(800); 
			slide.setFillAfter(true);
			set_esconde.addAnimation(slide);
		
			AnimationListener anim_esconde = new AnimationListener() {
		        LayoutParams params_esconde = panel_motorista.getLayoutParams();
			     @Override
			     public void onAnimationStart(Animation arg0) {}            
			     @Override
			     public void onAnimationRepeat(Animation arg0) {

			     }                   
			     @Override
			     public void onAnimationEnd(Animation arg0) {
			    	 params_esconde.height = 280;
			    	 panel_motorista.setLayoutParams(params_esconde);
			    	 //
			     }
			 };

			slide.setAnimationListener(anim_esconde);
		    panel_motorista.startAnimation(slide);

	  }
  
	    public boolean setupLogin() {
	       if (!g_login){
	            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
	            startActivityForResult(intent,1);
	            return true;
	        } else{
	        	InicializaApp();
	        }
	 		return false;
	    }
  
	 
	  private void setUpMapIfNeeded() {
	      // Do a null check to confirm that we have not already instantiated the map.
	      if (mMap == null) {
	          // Try to obtain the map from the SupportMapFragment.
	          mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	          // Check if we were successful in obtaining the map.
	          if (mMap != null) {
	              setUpMap();        	  
	          }
	      }
	  }
	  

	  private void setUpMap() {
	      // set the zoom controls as the button panel .
  		// Carrega Posicao Atual quando abrir o app
		  cont_taxista = 0;
		  gps = new GPSTracker(MainActivity.this);
          // check if GPS enabled     
          if(gps.canGetLocation()){
               
              double latitude = gps.getLatitude();
              double longitude = gps.getLongitude();
               
              // \n is for new line
  			Log.d("GPS: ", "Sua localização é - Lat: " + latitude + "Long: " + longitude);                
          }else{
              // can't get location
              // GPS or Network is not enabled
              // Ask user to enable GPS/network in settings
              gps.showSettingsAlert();
          }  
          mMap.setMyLocationEnabled(true);
          mMap.setOnMapClickListener(this);
          mMap.setOnMarkerClickListener(this);          
          mMap.setOnMarkerDragListener(this);
          
	      mMap.getUiSettings().setZoomControlsEnabled(true);
	      
	      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(),gps.getLongitude() ), 17));

	      txdisponiveisList = new ArrayList<HashMap<String, String>>();
  		  LoadTaxisDisponiveis ler_txdisponiveis =  new LoadTaxisDisponiveis();
          ler_txdisponiveis.execute();
            
	      // Add lots of markers to the map.

	      
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
						

						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
				Toast.makeText( _context, "ERROR: Invalid API key", Toast.LENGTH_LONG).show();
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
		
		private void Fala_Saudacao() {

			try {
		        String words = Saudacao()+" ! Estou há "+txtcha_distancia.getText()+ " "+DistanciaTempo(subtitulo1.getText()+"")+
        		" ! Chegarei até você em "+txtcha_tempo.getText()+" "+DistanciaTempo(subtitulo2.getText()+"");
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
	  
	    
	  public boolean onKeyUp(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_MENU) {
	            // ...
	            Intent intent = new Intent(getApplicationContext(), Configuracoes.class);
	            startActivity(intent);
	            return true;
	        } else {
	            return super.onKeyUp(keyCode, event);
	        }
	    }
	  
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    //getMenuInflater().inflate(R.menu.main, menu);
	    return true;
	  }

	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case R.id.menu_config:
	             Intent intent = new Intent(getApplicationContext(), Configuracoes.class);
	             startActivity(intent);
	           return true;
	        case R.id.menu_conectar:
	           //startActivity(new Intent(this, Conectar.class));
	           return true;
	        case R.id.menu_ajuda:
	            //startActivity(new Intent(this, Ajuda.class));
	            return true;
	        case R.id.menu_sobre:
	            startActivity(new Intent(this, SobreActivity.class));
	            return true;
	        case R.id.menu_legalnotices:
	 	        String LicenseInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(
  	            getApplicationContext());
	 	 	    AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(MainActivity.this);
	 	 	    LicenseDialog.setTitle("Legal Notices");
	 	 	    LicenseDialog.setMessage(LicenseInfo);
	 	 	    LicenseDialog.show();
	            return true;

	        case R.id.menu_sair:
	            //startActivity(new Intent(this, Help.class));
	        	super.finish();
	            return true;

	        default:
	        }
	        return super.onOptionsItemSelected(item);
	    }
	    
	  public class GeoUpdateHandler implements LocationListener {

	      @Override
	      public void onLocationChanged(Location location) {
	          double lat = MainActivity.gps.getLatitude();
	          double lng = MainActivity.gps.getLongitude();
	          //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng ), 15));
	          
	      }

	      @Override
	      public void onProviderDisabled(String provider) {
	          // TODO Auto-generated method stub

	      }

	      @Override
	      public void onProviderEnabled(String provider) {
	      }

	      @Override
	      public void onStatusChanged(String provider, int status, Bundle extras) {
	      }
	  }
	  private void createGpsDisabledAlert() {
	      AlertDialog.Builder builder = new AlertDialog.Builder(this);
	      builder
	              .setMessage(
	                      "GPS está Desligado! Gostaria de ligar?")
	              .setCancelable(false).setPositiveButton("Ligar GPS",
	                      new DialogInterface.OnClickListener() {
	                          public void onClick(DialogInterface dialog, int id) {
	                              showGpsOptions();
	                          }
	                      });
	      builder.setNegativeButton("Cancelar",
	              new DialogInterface.OnClickListener() {
	                  public void onClick(DialogInterface dialog, int id) {
	                      dialog.cancel();
	                  }
	              });
	      AlertDialog alert = builder.create();
	      alert.show();
	  }

	  private void showGpsOptions() {
	      Intent gpsOptionsIntent = new Intent(
	              android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	      startActivity(gpsOptionsIntent);
	  }
	  
	  private void TracaRota(){
		  
	      md = new GMapV2Direction();
   	  	  LatLng fromPosition = new LatLng(MainActivity.PosicaoTaxista.latitude, MainActivity.PosicaoTaxista.longitude);
		  LatLng toPosition =  new LatLng(MainActivity.gps.getLatitude(), MainActivity.gps.getLongitude());
			
    	  findDirections(fromPosition.latitude,fromPosition.longitude,toPosition.latitude,
			toPosition.longitude , GMapV2Direction.MODE_DRIVING );
    	  
    	  // Atualiza posicao do taxi e verifica se a corrida foi finalizada
    	  handler_taxi = new Handler();
    	  handler_taxi.postDelayed( taxiRunnable = new Runnable() {
    		 @Override
	 		 public void run() {
    			 
    			 Ler_Chamada tsk_Ler_Chamada =  new Ler_Chamada();
		         tsk_Ler_Chamada.execute();

    			 handler_taxi.postDelayed(taxiRunnable, MainActivity.g_intervalo);

			}
		  }, 200);
    	  
	  }
	  private void RemoveMarkersMap(int taxista){

		  // Remove todas as markers e adiciona somente o taxista
		  mMap.clear();

	      MinhaLocalizacao = new LatLng(gps.getLatitude(),gps.getLongitude());
		  PosCliente = mMap.addMarker(new MarkerOptions()
	      .position(MinhaLocalizacao)
	      .title("Minha localização")
	      .icon(BitmapDescriptorFactory.fromResource(R.drawable.passageiro_marker))
	      .draggable(true)
	      .snippet("Estou Aqui"));
		  
		  // Salva posicao do taxista mais próximo
   	      PosicaoTaxista = new LatLng(Double.parseDouble(txdisponiveisList.get(taxista).get(TAG_LAT)),
			   Double.parseDouble(txdisponiveisList.get(taxista).get(TAG_LNG)));
          PosTaxista = mMap.addMarker(new MarkerOptions()
          		.position(PosicaoTaxista)
          		.title(txdisponiveisList.get(taxista).get(TAG_EMAIL))
          		.icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_marker2a))
          		.snippet(taxista+""));
          rota_corrida.clear();
		  rota_corrida.add(PosicaoTaxista);

	  }

	  private void addMarkersToMap() {

	      MinhaLocalizacao = new LatLng(gps.getLatitude(),gps.getLongitude());
	      mMap.clear();
		  mMap.addMarker(new MarkerOptions()
	      .position(MinhaLocalizacao)
	      .title("Minha localização")
	      .icon(BitmapDescriptorFactory.fromResource(R.drawable.passageiro_marker))
	      .draggable(true)
	      .snippet("Estou Aqui"));
		  		  
	      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MinhaLocalizacao, 15));

	      if(MainActivity.txdisponiveisList!=null){
	        for(int x=0;x<MainActivity.txdisponiveisList.size();x++){
	        	
	        	// Salva posicao do taxista mais próximo
	           mMap.addMarker(new MarkerOptions()
	           		.position(new LatLng((Double.parseDouble(txdisponiveisList.get(x).get(TAG_LAT)) ),
	           				(Double.parseDouble(txdisponiveisList.get(x).get(TAG_LNG)))))
	           		.title(txdisponiveisList.get(x).get(TAG_EMAIL))
	           		.icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_marker2a))
	           		.snippet(x+""));

	        	// Salva posicao do taxista mais próximo
	           Integer xnovo = x;
	           if (xnovo.equals(0)){
	        	   PosicaoTaxista = new LatLng(Double.parseDouble(txdisponiveisList.get(x).get(TAG_LAT)),
	        			   Double.parseDouble(txdisponiveisList.get(x).get(TAG_LNG)));

	           }

	        }
	      }
	      

	      //Add Circle
	      CircleOptions circleOptions = new CircleOptions()
	      .center(MinhaLocalizacao)
	      .fillColor(0x40ff0000)
	      .strokeColor(0x40ff0000)
	      .radius(MainActivity.seek_distancia*1000); // In meters
	      
	      mMap.addCircle(circleOptions);
	      
	      // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
	      CameraPosition cameraPosition = new CameraPosition.Builder()
	          .target(MinhaLocalizacao)      // Sets the center of the map to Mountain View
	          .zoom(  (float) (15-( seek_distancia * 0.4)) )                   // Sets the zoom
//	          .bearing(90)                // Sets the orientation of the camera to east
	          .tilt(45)                   // Sets the tilt of the camera to 30 degrees
	          .build();                   // Creates a CameraPosition from the builder
	      mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	      
	  }
	  
	  @Override
	  protected void onResume() {
		  // TODO Auto-generated method stub
		  super.onResume();
		  
		  int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		  
		  if (resultCode == ConnectionResult.SUCCESS){
			  //Toast.makeText(getApplicationContext(), "isGooglePlayServicesAvailable SUCCESS",
			  //Toast.LENGTH_LONG).show();
		  }else{
			  GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
		  }
		  
		  if ( g_forcasaida ){
			  MainActivity.g_forcasaida = false;
			  finish();
		  }
	  }
	  
		/**
		 * Background Async Task to Load all ocorrencias by making HTTP Request
		 * */
		public class LoadTaxisDisponiveis extends AsyncTask<String, String, String> {
			
			public void setContext(Context context){
		        our_context = context;
		    }
			
			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			/**
			 * getting All products from url
			 * */
			protected String doInBackground(String... args) {


				// Building Parameters            
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("kilometros", ""+seek_distancia));
				params.add(new BasicNameValuePair("lat", ""+gps.getLatitude() ));
				params.add(new BasicNameValuePair("lng", ""+gps.getLongitude() ));
				
				// getting JSON string from URL
				JSONObject json = jParser.makeHttpRequest(url_taxisdisponiveis, "POST", params);
				
				// Check your log cat for JSON response
				Log.d("Todos Ocorrencias:  ", json.toString());

				try {
					// Checking for SUCCESS TAG
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						// products found
						// Getting Array of Products
						txdisponiveis = json.getJSONArray(TAG_TXDISPONIVEIS);
						MainActivity.txdisponiveisList.clear();

						// looping through All Products
						for (int i = 0; i < txdisponiveis.length(); i++) {
							JSONObject c = txdisponiveis.getJSONObject(i);

							// Storing each json item in variable
							String email = c.getString(TAG_EMAIL);
							String nome = c.getString(TAG_NOME);
							String celular = c.getString(TAG_CELULAR);
							String foto = c.getString(TAG_FOTO);
							String placa = c.getString(TAG_PLACA);
							String modelo = c.getString(TAG_MODELO);
							String lat = c.getString(TAG_LAT);
							String lng = c.getString(TAG_LNG);						
							String aval_carro = c.getString(TAG_AVAL_CARRO);
							String aval_mot = c.getString(TAG_AVAL_MOT);
							String status = c.getString(TAG_STATUS);
							String statuscokin = c.getString(TAG_STATUSCOKIN);
							String distancia = c.getString(TAG_DISTANCIA);

							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							// adding each child node to HashMap key => value
							map.put(TAG_DISTANCIA, distancia);
							map.put(TAG_LAT, lat);
							map.put(TAG_LNG, lng);
							map.put(TAG_EMAIL, email);
							map.put(TAG_NOME, nome);
							map.put(TAG_CELULAR, celular);
							map.put(TAG_FOTO, foto);
							map.put(TAG_PLACA, placa);
							map.put(TAG_MODELO, modelo);
							map.put(TAG_AVAL_CARRO, aval_carro);
							map.put(TAG_AVAL_MOT, aval_mot);
							map.put(TAG_STATUS, status);
							map.put(TAG_STATUSCOKIN, statuscokin);

							// adding HashList to ArrayList
							MainActivity.txdisponiveisList.add(map);
						}
					} else {
						// no products found
						// Launch Add New product Activity
						//Intent i = new Intent(getApplicationContext(),
						//IncluirContatosActivity.class);
						// Closing all previous activities
						//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						//startActivity(i);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {

				// updating UI from Background Thread
			    addMarkersToMap();
			    if (txdisponiveisList.size() > 0) {
		    	   FormataDistancia(txdisponiveisList.get(0).get(TAG_DISTANCIA));
            	   txtcha_qtdcarros.setText( txdisponiveisList.size()+"" );
			    }

			}

		}

		/**
		 * Background Async Task to Load all ocorrencias by making HTTP Request
		 * */
		public class SalvarChamada extends AsyncTask<String, String, String> {
			
			public Boolean salvouchamada = false;
			
			public void setContext(Context context){
		        our_context = context;
		    }
			
			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			/**
			 * getting All products from url
			 * */
			protected String doInBackground(String... args) {


				// Building Parameters
				HashMap<String, String> params = new HashMap<String, String>();

				params.put("ch_us_email", g_usuario);
				params.put("ch_mo_email", txdisponiveisList.get(cont_taxista).get(TAG_EMAIL) );
				params.put("ch_lat", ""+gps.getLatitude() );
				params.put("ch_lng", ""+gps.getLongitude() );


	//			List<NameValuePair> params = new ArrayList<NameValuePair>();

	//			params.add(new BasicNameValuePair("ch_us_email", g_usuario));
	//			params.add(new BasicNameValuePair("ch_mo_email", txdisponiveisList.get(cont_taxista).get(TAG_EMAIL) ));
	//			params.add(new BasicNameValuePair("ch_lat", ""+gps.getLatitude() ));
	//			params.add(new BasicNameValuePair("ch_lng", ""+gps.getLongitude() ));
				
				// getting JSON string from URL
				JSONObject json = jParser.makeHttpRequest(url_salvarchamada, "POST", params);
				
				// Check your log cat for JSON response
				Log.d("Salvar Chamadada:  ", json.toString());

				try {
					// Checking for SUCCESS TAG
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						// chamada cadastrada
						salvouchamada = true;

					} else {
						salvouchamada = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				
			    if (salvouchamada) {
					LayoutParams params_chamar = txtbt_chamar.getLayoutParams();
					params_chamar.width = LayoutParams.MATCH_PARENT;
					txtbt_chamar.setLayoutParams(params_chamar);
					//txtbt_chamar.setEnabled(false);
					txtbt_chamar.setText("Finalizar & Avaliar");
					txtbt_cancelar.setVisibility(View.GONE);
					RemoveMarkersMap(cont_taxista);
					TracaRota();					
				
			    }

			}

		}

		public class Ler_Chamada extends AsyncTask<String, String, String> {
			
			public Boolean statuschamada = false;
			
			public void setContext(Context context){
		        our_context = context;
		    }
			
			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			/**
			 * getting All products from url
			 * */
			protected String doInBackground(String... args) {


				// Building Parameters            
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("ch_us_email", g_usuario));
				params.add(new BasicNameValuePair("ch_mo_email", txdisponiveisList.get(cont_taxista).get(TAG_EMAIL) ));
				
				// getting JSON string from URL
				JSONObject json = jParser.makeHttpRequest(url_ler_chamada, "POST", params);
				
				// Check your log cat for JSON response
				Log.d("Status da Chamadada:  ", json.toString());

				try {
					// Checking for SUCCESS TAG
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						// chamada cadastrada
						statuschamada = true;
						txstatus = json.getJSONArray("tx_status");
						txstatusList.clear();

						// looping through All Products
						for (int i = 0; i < txstatus.length(); i++) {
							JSONObject c = txstatus.getJSONObject(i);

							// Storing each json item in variable
							String s_mo_lat = c.getString("mo_lat");
							String s_mo_lng = c.getString("mo_lng");
							String s_ch_status = c.getString("ch_status");

							// creating new HashMap
							HashMap<String, String> mapstatus = new HashMap<String, String>();

							// adding each child node to HashMap key => value
							mapstatus.put("mo_lat", s_mo_lat);
							mapstatus.put("mo_lng", s_mo_lng);
							mapstatus.put("ch_status", s_ch_status);

							// adding HashList to ArrayList
							txstatusList.add(mapstatus);
						}

					} else {
						statuschamada = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				
				if (statuschamada) {   // Chamada localizada   
			    	
			    	String cStatus = txstatusList.get(0).get("ch_status");
		    		final LatLng pos_temp = new LatLng((Double.parseDouble(txstatusList.get(0).get("mo_lat"))),
	          				(Double.parseDouble(txstatusList.get(0).get("mo_lng"))));

			    	if (cStatus.equals("0")){   //0-Chamando Táxi // Solicitou corrida (Ainda não foi aceita pelo taxista)
			    		
						Toast.makeText(_context, "Localizando Taxista disponível!", Toast.LENGTH_LONG).show();
						//mudar o taxista na chamada para o próximo táxi
 			    		
			    	}else if (cStatus.equals("1")){   //  1-Táxi a Caminho (Taxista aceitou a chamada)
						
			    		Toast.makeText(_context, "Taxi está a caminho!", Toast.LENGTH_LONG).show();
			    		// Atualiza Posição do taxista
			    		PosTaxista.setPosition(pos_temp);

			    	}else if (cStatus.equals("2")){   //  2-Táxi chegou  (Táxista chegou ao Local)
						
			    		// Atualiza Posição do taxista
			    		PosTaxista.setPosition(pos_temp);
			    		Toast.makeText(_context, "Seu Táxi Chegou!", Toast.LENGTH_LONG).show();

			    		
			    	}else if (cStatus.equals("3")){  // 3-Corrida Iniciada (Passageiro embarcou e corrida foi iniciada)

			    		// Salvar caminho da corrida
			    		rota_corrida.add(pos_temp);
			    		
			    		// Atualiza Posição do taxista e do cliente
			    		PosCliente.setPosition(pos_temp);
			    		PosTaxista.setPosition(pos_temp);
 			    		
						Toast.makeText(_context, "Corrida em curso!", Toast.LENGTH_LONG).show();

			    	}else if (cStatus.equals("5")){  // 5-Chamada Recusada (Taxista recusou chamada)
			    		
			    		// Altera a chamada para o próximo taxista disponível mais próximo
			    		// mudando status para 0-Chamaando Taxi
			    		
				 		if (txdisponiveisList.size() > cont_taxista+1){   //incrementa para o próximo taxi
				 			cont_taxista = cont_taxista+1;
				    		Status_Chamada tsk_Status_Chamada =  new Status_Chamada();
					        tsk_Status_Chamada.execute("0", txdisponiveisList.get(cont_taxista).get(TAG_EMAIL));
				 		}else{
				 			// Volta ao estado inicial - apagar chamada
					    	if (!(handler_taxi == null)){     // Para de verificar status da corrida
				   	        	handler_taxi.removeCallbacks(taxiRunnable);
				   	        }
						    	
						    // Reinicia para uma nova chamada 
							LayoutParams params_chamar = txtbt_chamar.getLayoutParams();
							params_chamar.width = LayoutParams.MATCH_PARENT;
							txtbt_chamar.setLayoutParams(params_chamar);
							txtbt_chamar.setText("Chamar Táxi");
							Esconde_Motorista();
							setUpMap();
							Toast.makeText(MainActivity.this, "Não existe táxis disponiveis no momento!", Toast.LENGTH_LONG).show();
				 		}
			    		

			    	}else if (cStatus.equals("4")){  // 4-Corrida Finalizada (Passageiro desembarcou e corrida foi finalizada)

			    		// Salvar caminho da corrida
			    		rota_corrida.add(pos_temp);
			    		
			    		// Atualiza Posição do taxista e do cliente
			    		PosCliente.setPosition(pos_temp);
			    		PosTaxista.setPosition(pos_temp);
 			    		
						Toast.makeText(_context, "Corrida Finalizada!", Toast.LENGTH_LONG).show();
						
				    	if (!(handler_taxi == null)){     // Para de verificar status da corrida
			   	        	handler_taxi.removeCallbacks(taxiRunnable);
			   	        }
					    	
					    // Reinicia para uma nova chamada 
						LayoutParams params_chamar = txtbt_chamar.getLayoutParams();
						params_chamar.width = LayoutParams.MATCH_PARENT;
						txtbt_chamar.setLayoutParams(params_chamar);
						txtbt_chamar.setText("Chamar Táxi");
						Esconde_Motorista();
						setUpMap();

						// solicitar avaliação do motorista e do carro
						// salvando a avaliação
						// Mostra comparação entre a rota percorrida e rota prevista

			    		
			    	}

			    	
			    }else{  // Não localizou chamada
			    	
					Toast.makeText(_context, "Chamada não localizada!", Toast.LENGTH_LONG).show();

			    }

			}

		}
		
		// Atualiza status da chamada 
		public class Status_Chamada extends AsyncTask<String, String, String> {
			
			public Boolean atualizou_status = false;
			public String c_status;
			
			public void setContext(Context context){
		        our_context = context;
		    }
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			protected String doInBackground(String... args) {


				// Building Parameters            
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("ch_mo_email", args[1]));
				params.add(new BasicNameValuePair("ch_us_email", g_usuario ));
				params.add(new BasicNameValuePair("ch_status", args[0] ));
				
				// getting JSON string from URL
				JSONObject json = jParser.makeHttpRequest(url_status_chamada, "POST", params);
				
				// Check your log cat for JSON response
				Log.d("Alterando Status da Chamada:  ", json.toString());

				try {
					// Checking for SUCCESS TAG
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						c_status = args[0];
						atualizou_status = true;

					} else {
						atualizou_status = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}

			protected void onPostExecute(String file_url) {
				
			    if (atualizou_status) {
			    	
					Log.e(TAG, "Chamando o próximo taxista");			    	

			    }

			}

		}

		
		
		public String DistanciaTempo(String chave){
			if (chave.equals("km")) {
				chave = "kilometros";
			}else if (chave.equals("m")) {
				chave = "metros";
			}else if (chave.equals("MIN")) {
				chave = "minutos";
			}else if (chave.equals("SEG")) {
				chave = "segundos";
			}
			return chave;
			
		}
		
	    public static void FormataDistancia(String distancia){
	    	String Retorno;
	    	final Double dTempo;
	    	dTempo = (Double.valueOf(distancia)*3600)/vel_media;
	    	
	    	// Seta distancia e titulo
	    	if ((Double.valueOf(distancia)*1000) > 1000){
    			Retorno = String.format("%.2f",(Double.valueOf(distancia)*1));
    			txtcha_distancia.setText(Retorno);
    	    	subtitulo1.setText("KILOMETRO"+Plural(Retorno));
	    	}else{
	    		Retorno = ""+(int) (Double.valueOf(distancia)*1000);	    			
    			txtcha_distancia.setText(Retorno);
    	    	subtitulo1.setText("METRO"+Plural(Retorno));
	    	}
	    	
	    	// Seta tempo médio e e titulo	    	
	    	if (dTempo > 59){  // mais de 60 segundos
        		Retorno = ""+(int) (Double.valueOf(dTempo)/60) ;
    	    	txtcha_tempo.setText(Retorno);
    	    	subtitulo2.setText("MINUTO"+Plural(Retorno));
	    	}else{
    			Retorno = ""+(int) (Double.valueOf(dTempo)*1);
    	    	txtcha_tempo.setText(Retorno);
    	    	subtitulo2.setText("SEGUNDO"+Plural(Retorno));
	    	}
	    	
  	    }
	    
	    public static String Plural(String cvalor){
	    	cvalor="";
	    	if (!cvalor.equals("1")){
	    		cvalor = "S";
	    	}
			return cvalor;
	    	
	    }

	    public static String TempoMedio(String distancia, Boolean tipo){
            
	    	final String Retorno;
	    	final Double dTempo;
	    	dTempo = (Double.valueOf(distancia)*3600)/vel_media;
	    	
	    	if (dTempo > 59){  // mais de 60 segundos
	    		if (tipo){
	        		Retorno = ""+(int) (Double.valueOf(dTempo)/60) ;
	    		}else{
	    			Retorno = "MINUTOS"; 
	    		}
	    	}else{
	    		if (tipo){
	    			Retorno = ""+(int) (Double.valueOf(dTempo)*1);
	    		}else{
	    			Retorno = "SEGUNDOS";	
	    		}
	    	}
	    		
			return Retorno;
	    	
	    }
	    
	    
		public String ConvertPointToLocation(GeoPoint point) {   
		   String address = "";
		   Geocoder geoCoder = new Geocoder(
		   getBaseContext(), Locale.getDefault());
		   try {
			  List<Address> addresses = geoCoder.getFromLocation(
			  point.getLatitudeE6()  / 1E6, 
			  point.getLongitudeE6() / 1E6, 1);
			 
			  if (addresses.size() > 0) {
			     for (int index = 0;
			        index < addresses.get(0).getMaxAddressLineIndex(); index++)
			        address += addresses.get(0).getAddressLine(index) + " ";
			     }
			  }
			catch (IOException e) {        
			  e.printStackTrace();
			}   
			    
			return address;
		} 
		
		@Override
		public void onMarkerDrag(Marker arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMarkerDragEnd(Marker arg0) {
			// TODO Auto-generated method stub
  			arg0.setTitle("Lat: " + arg0.getPosition().latitude + " Long: " + arg0.getPosition().longitude);
    	    GeoPoint point = new GeoPoint(
    	    	    (int) (arg0.getPosition().latitude * 1E6), 
    	    	    (int) (arg0.getPosition().longitude * 1E6));
			String address = ConvertPointToLocation(point);
			Toast.makeText(getBaseContext(), address, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onMarkerDragStart(Marker arg0) {
			// TODO Auto-generated method stub
			
		}

//		@Override
//		protected boolean isRouteDisplayed() {
			// TODO Auto-generated method stub
//			return false;
//		}

		@Override
		public void onMapClick(LatLng arg0) {
			// TODO Auto-generated method stub
	    	Log.d("GPS: ", "Sua Nova localização é - Lat: " + arg0.latitude + "Long: " + arg0.longitude);
    	    GeoPoint point = new GeoPoint(
	    	    (int) (arg0.latitude * 1E6), 
	    	    (int) (arg0.longitude * 1E6));
	    	String address = ConvertPointToLocation(point);
	    	Toast.makeText(getBaseContext(), address, Toast.LENGTH_SHORT).show();
			
		}

		@Override
		public boolean onMarkerClick(Marker arg0) {
			// TODO Auto-generated method stub
            if (!arg0.getSnippet().equals("Estou Aqui")) {

            	int x_ele = Integer.parseInt(arg0.getSnippet());
            	String distancia = txdisponiveisList.get(x_ele).get(TAG_DISTANCIA);
            	FormataDistancia(distancia);
            }else{
            	txtcha_distancia.setText("0");
            	txtcha_tempo.setText("");
            }
			
			return false;
		}
		
		public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
		{
		    Map<String, String> map = new HashMap<String, String>();
		    map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
		    map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
		    map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
		    map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
		    map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);
		 
		    GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
		    asyncTask.execute(map);
		}
		
		public class GetDirectionsAsyncTask extends AsyncTask<Map<String, String>, Object, ArrayList>
		{
		    public static final String USER_CURRENT_LAT = "user_current_lat";
		    public static final String USER_CURRENT_LONG = "user_current_long";
		    public static final String DESTINATION_LAT = "destination_lat";
		    public static final String DESTINATION_LONG = "destination_long";
		    public static final String DIRECTIONS_MODE = "directions_mode";
		    private MainActivity activity;
		    private Exception exception;
		    private ProgressDialog progressDialog;
		 
		    public GetDirectionsAsyncTask(MainActivity tracaRota)
		    {
		        super();
		        this.activity = tracaRota;
		    }
		 
		    public void onPreExecute()
		    {
		        progressDialog = new ProgressDialog(activity);
		        progressDialog.setMessage("Calculating directions");
		        progressDialog.show();
		    }
		 
		    @Override
		    public void onPostExecute(ArrayList result)
		    {
		        progressDialog.dismiss();
		        if (exception == null)
		        {
		            activity.handleGetDirectionsResult(result);
		        }
		        else
		        {
		            processException();
		        }
		    }
		 
		    @Override
		    protected ArrayList doInBackground(Map<String, String>... params)
		    {
		        Map<String, String> paramMap = params[0];
		        try
		        {
		            LatLng fromPosition = new LatLng(Double.valueOf(paramMap.get(USER_CURRENT_LAT)) , Double.valueOf(paramMap.get(USER_CURRENT_LONG)));
		            LatLng toPosition = new LatLng(Double.valueOf(paramMap.get(DESTINATION_LAT)) , Double.valueOf(paramMap.get(DESTINATION_LONG)));
		            GMapV2Direction md = new GMapV2Direction();
		            Document doc = md.getDocument(fromPosition, toPosition, paramMap.get(DIRECTIONS_MODE));
		            ArrayList directionPoints = md.getDirection(doc);
		            return directionPoints;
		        }
		        catch (Exception e)
		        {
		            exception = e;
		            return null;
		        }
		    }
		 
		    private void processException()
		    {
		        //Toast.makeText(activity, activity.getString(R.string.error_when_retrieving_data), 3000).show();
		    }
		}
		
		public void handleGetDirectionsResult(ArrayList directionPoints)
		{
		    Polyline newPolyline;
		    PolylineOptions rectLine = new PolylineOptions().width(15).color(Color.BLUE);
		    for(int i = 0 ; i < directionPoints.size() ; i++)
		    {
		        rectLine.add((LatLng) directionPoints.get(i));
		    }
		    newPolyline = mMap.addPolyline(rectLine);
		}
	    
}