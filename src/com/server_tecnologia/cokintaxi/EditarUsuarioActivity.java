package com.server_tecnologia.cokintaxi;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditarUsuarioActivity extends Activity {

	EditText txtNome;
	EditText txtSobrenome;
	EditText txtTelefone;
	EditText txtEmail;
	EditText txtIdioma;
	ImageView imgFoto;

	Button btnMudarSenha;
	Button btnBloqueados;
	Button btnSave;
	Button btnDelete;

	String id_usuario;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// single product url
	private static final String url_get_usuario = "http://www.server-tecnologia.com/cokin/get_usuario.php";
	
	// url to update product
	private static final String url_update_usuario = "http://www.server-tecnologia.com/cokin/update_usuario.php";
	
	// url to delete product
	private static final String url_delete_usuario = "http://www.server-tecnologia.com/cokin/delete_usuario.php";
    private static final String url_foto = "http://www.server-tecnologia.com/cokin/fotos/";
    
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_USUARIO = "usuario";
	private static final String TAG_NOME = "nome";
	private static final String TAG_SOBRENOME = "sobrenome";
	private static final String TAG_TELEFONE = "telefone";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_IDIOMA = "lingua";
	private static final String TAG_FOTO = "foto";
	
    boolean lEditar = false;
    JSONObject js_usuario;
    ImageLoader usu_foto;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contatos_editar);

		// edit's
		txtNome = (EditText) findViewById(R.id.ce_nome);
		txtSobrenome = (EditText) findViewById(R.id.ce_sobrenome);
		txtTelefone = (EditText) findViewById(R.id.ce_telefone);
		txtEmail = (EditText) findViewById(R.id.ce_email);
		txtIdioma = (EditText) findViewById(R.id.ce_idioma);
		imgFoto = (ImageView) findViewById(R.id.ce_foto);		
		
		// save button
		btnSave = (Button) findViewById(R.id.btnSave);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnMudarSenha = (Button) findViewById(R.id.btnMudarSenha);
		btnBloqueados = (Button) findViewById(R.id.btnBloqueados);

		// getting product details from intent
		Intent i = getIntent();
		
		// getting product id (pid) from intent
		id_usuario = i.getStringExtra(TAG_USUARIO);
		
		// Visualizar ou Editar?
		if (MainActivity.g_usuario.equals(id_usuario)){
			lEditar = true;
		}
		btnSave.setEnabled(lEditar);
		btnDelete.setEnabled(lEditar);
		btnMudarSenha.setEnabled(lEditar);
		btnBloqueados.setEnabled(lEditar);

		txtNome.setEnabled(lEditar);
		txtSobrenome.setEnabled(lEditar);
		txtTelefone.setEnabled(lEditar);
		txtEmail.setEnabled(lEditar);
		txtIdioma.setEnabled(lEditar);

		// Instancia objetos
		usu_foto = new ImageLoader( getApplicationContext() );
		
		new GetUsuarioDetails().execute();

		// save button click event
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// starting background task to update product
				new SaveUsuarioDetails().execute();
			}
		});

		// Delete button click event
		btnDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// deleting product in background thread
				new DeleteUsuario().execute();
			}
		});

	}
	
	/**
	 * Background Async Task to Get complete product details
	 * */
	class GetUsuarioDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditarContatosActivity.this);
			pDialog.setMessage("Carregando dados do Usuario. Aguarde...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			//pDialog.show();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... params) {

			// updating UI from Background Thread
			// Check for success tag
			int success;
			try {
				// Building Parameters
				List<NameValuePair> params1 = new ArrayList<NameValuePair>();
				params1.add(new BasicNameValuePair("get_usuario", id_usuario));

				// getting product details by making HTTP request
				// Note that product details url will use GET request
				JSONObject json = jsonParser.makeHttpRequest(url_get_usuario, "GET", params1);

				// check your log for json response
				Log.d("Detalhes do Usuario", json.toString());
						
				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// successfully received product details
					JSONArray usuarioObj = json
							.getJSONArray(TAG_USUARIO); // JSON Array
					
					// get first product object from JSON Array
					js_usuario = usuarioObj.getJSONObject(0);
					
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
			// dismiss the dialog once got all details
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into View
					 * */
					// Mostra dados do Usuario no EditText
					if (!(js_usuario == null)){
					   try {
					      						   
						  txtNome.setText(js_usuario.getString(TAG_NOME));
						  txtSobrenome.setText(js_usuario.getString(TAG_SOBRENOME));
						  txtTelefone.setText(js_usuario.getString(TAG_TELEFONE));
						  txtEmail.setText(js_usuario.getString(TAG_EMAIL));
						  txtIdioma.setText(js_usuario.getString(TAG_IDIOMA));

						  if ( js_usuario.getString(TAG_FOTO).equals("1") ){
					         usu_foto.DisplayImage(url_foto+id_usuario+".jpg", imgFoto);
					      } else {	
					        imgFoto.setImageResource(R.drawable.ic_contact_picture);
					      }						  
					   } catch (JSONException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
					   }
					}
				}
			});			
			
		}
	}

	/**
	 * Background Async Task to  Save product Details
	 * */
	class SaveUsuarioDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditarContatosActivity.this);
			pDialog.setMessage("Gravando Usuário ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Saving product
		 * */
		protected String doInBackground(String... args) {

			// getting updated data from EditTexts
			String nome = txtNome.getText().toString();
			String sobrenome = txtSobrenome.getText().toString();
			String telefone = txtTelefone.getText().toString();
			String email = txtEmail.getText().toString();
			String idioma = txtIdioma.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_USUARIO, id_usuario));
			params.add(new BasicNameValuePair(TAG_NOME, nome));
			params.add(new BasicNameValuePair(TAG_SOBRENOME, sobrenome));
			params.add(new BasicNameValuePair(TAG_TELEFONE, telefone));
			params.add(new BasicNameValuePair(TAG_EMAIL, email));
			params.add(new BasicNameValuePair(TAG_IDIOMA, idioma));


			// sending modified data through http request
			// Notice that update product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_update_usuario,
					"POST", params);

			// check json success tag
			try {
				int success = json.getInt(TAG_SUCCESS);
				
				if (success == 1) {
					// successfully updated
					Intent i = getIntent();
					// send result code 100 to notify about product update
					setResult(100, i);
					finish();
				} else {
					// failed to update product
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
			// dismiss the dialog once usuario update
			pDialog.dismiss();
		}
	}

	/*****************************************************************
	 * Background Async Task to Delete Usuario
	 * */
	class DeleteUsuario extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditarContatosActivity.this);
			pDialog.setMessage("Deleting Product...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Deleting product
		 * */
		protected String doInBackground(String... args) {

			// Check for success tag
			int success;
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("del_usuario", id_usuario));

				// getting product details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(
						url_delete_usuario, "POST", params);

				// check your log for json response
				Log.d("Delete Usuario", json.toString());
				
				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// usuario deletado com sucesso
					// notify previous activity by sending code 100
					Intent i = getIntent();
					// send result code 100 to notify about product deletion
					setResult(100, i);
					finish();
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
			// dismiss the dialog once product deleted
			pDialog.dismiss();

		}

	}
}
