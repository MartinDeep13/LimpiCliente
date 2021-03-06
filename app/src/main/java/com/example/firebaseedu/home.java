package com.example.firebaseedu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.firebaseedu.Modelos.Usuario;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
    EditaPerfil.OnFragmentInteractionListener, MetodosDePago.OnFragmentInteractionListener, Membresias.OnFragmentInteractionListener, PedidosFragment.OnFragmentInteractionListener
{
    String uri_parse;
    TextView usuario;
    View btn;
    ActionBarDrawerToggle mToggle;
    DrawerLayout mDrawerLayout;

    public static FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser Users =  firebaseAuth.getCurrentUser();

        try {

            final String uid = Users.getUid();
            JSONObject datos = new JSONObject();
            datos.put("uid",uid);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://limpi.mipantano.com/api/usuario_info",
                    datos, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    TextView nombre, usuario;
                    Log.d("Info",response.toString());
                    Gson gson = new Gson();
                    Usuario json = gson.fromJson(response.toString(),Usuario.class);

                    nombre = findViewById(R.id.user);
                    usuario = findViewById(R.id.usuarioS);
                    nombre.setText(json.getNombre()+"!");
                    usuario.setText(json.getNombre()+"!");

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error",error.toString());
                }
            });

            RequestQueue queue = Volley.newRequestQueue(home.this);
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        //recibir datos
//        String name = getIntent().getStringExtra("nombre");
//        uri_parse=getIntent().getStringExtra("uriFoto");
//
//        TextView user_name =  findViewById(R.id.user);
//        user_name.setText(name);




        //quitar orientacion
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //ocultar barra
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

            mDrawerLayout=findViewById(R.id.drawer);
            mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
            btn=findViewById(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);

                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);

                }
            });

            NavigationView nav = (NavigationView) findViewById(R.id.nav);
            nav.setNavigationItemSelectedListener(this);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    private void goBack() {
        Intent intent = new Intent(home.this,InicioSesion.class);
        startActivity(intent);

        finish();
    }
    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goBack();
    }






    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        int id=menuItem.getItemId();


        Fragment fragment = null;
        Boolean FragmentSelected=false;

        if ( id==R.id.inicio)
        {
            fragment = new EditaPerfil();
            FragmentSelected=true;
        }
        else if(id==R.id.pagos)
        {
            fragment = new MetodosDePago();
            FragmentSelected=true;
        }
        else if(id==R.id.listapedidos)
        {
            fragment = new PedidosFragment();
            FragmentSelected=true;
        }
        else if (id == R.id.Membresias)
        {
            fragment = new Membresias();
            FragmentSelected=true;
        }
        else if (id == R.id.pagos)
        {
            fragment = new MetodosDePago();
            FragmentSelected=true;
        }
        else if (id == R.id.pedidos)
        {
           startActivity(new Intent(home.this, Servicios.class));
        }
        else if (id == R.id.mispedidos)
        {
            Toast.makeText(home.this, "nachos", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(home.this, Cesto.class));
        }
        else if (id == R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(home.this, InicioSesion.class));
        }
        if ( FragmentSelected ){

            getSupportFragmentManager().beginTransaction().replace(R.id.relative,fragment).commit();
            menuItem.setChecked(true);
        }

        mDrawerLayout=findViewById(R.id.drawer);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void membresias(View view) {
        Fragment fragment = null;
      fragment = new  Membresias();
        getSupportFragmentManager().beginTransaction().replace(R.id.relative,fragment).commit();
    }

    public void pedidos(View view) {
        startActivity(new Intent(home.this,Servicios.class));
    }
}
