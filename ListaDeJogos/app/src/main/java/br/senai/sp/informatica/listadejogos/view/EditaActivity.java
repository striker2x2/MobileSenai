package br.senai.sp.informatica.listadejogos.view;


import android.app.ActionBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

import br.senai.sp.informatica.listadejogos.R;
import br.senai.sp.informatica.listadejogos.model.Jogo;
import br.senai.sp.informatica.listadejogos.model.JogoDao;

/**
 * Created by Felipe on 03/11/2017.
 */

public class EditaActivity extends AppCompatActivity {

    private EditText nomeJogoView,generoView;
    private JogoDao dao = JogoDao.manager;
    private Jogo jogo;
    private Intent intent;
    private Long id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatelayout);
        jogo = new Jogo();
        nomeJogoView = (EditText) findViewById(R.id.edtNome) ;
        generoView = (EditText) findViewById(R.id.edtGenero) ;
        intent = new Intent(this, MainActivity.class);

        //Recebe ou não valores da Activity anterior via bundle
        Bundle bundleJogo = getIntent().getExtras();

        //Verifica se tem id e seta os campos para fazer alteração
        //sem ID a ação sera de cadastro
        if(bundleJogo != null) {
            id = bundleJogo.getLong("JogoID");
            jogo.setNome(dao.getJogo(id).getNome());
            jogo.setGenero(dao.getJogo(id).getGenero());
            nomeJogoView.setText(jogo.getNome());
            generoView.setText(jogo.getGenero());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editar, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if(id == R.id.voltar){
            intent.removeExtra("JogoID");
           // setResult(Activity.RESULT_CANCELED);
            finish();

        }
        return true;
    }

    //Metodo de inclusão e alteração

    public void cadastrarEditar(View view) {

        String msg;
        //A classe JogoDao faz o controle de cadastro ou inclusão pela nulidade do ID
        if (id != null) {
            jogo.setId(id);
            msg = getResources().getString(R.string.jogoAlterado);
        } else {
            msg = getResources().getString(R.string.jogoCadastrado);
        }

        jogo.setNome(nomeJogoView.getText().toString());
        jogo.setGenero(generoView.getText().toString());

        if (nomeJogoView.getText().toString().isEmpty() || generoView.getText().toString().isEmpty()) {

            msg = getResources().getString(R.string.semConteudo);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else{
            dao.salvar(jogo);


        setResult(Activity.RESULT_OK);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        finish();

        //startActivity(intent);
        }
        //finish();

    }

}
