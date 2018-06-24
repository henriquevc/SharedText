package com.example.henrique.sharedtext;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    //Referência Database
    private FirebaseDatabase database;
    private DatabaseReference tagsReference;
    private EditText textEditText;
    private TextInputEditText tagInputEditText;
    private void configuraFirebase() {
        database = FirebaseDatabase.getInstance();
        tagsReference = database.getReference("tags");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textEditText = (EditText) findViewById(R.id.textEditText);
        tagInputEditText = (TextInputEditText) findViewById(R.id.tagInputEditText);

        configuraFirebase();

        tagInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //Perde o foco do InputEditText
                if(!hasFocus) {
                    //chama a função executa o listener do firebase
                    executarListener();
                    //Toast.makeText(MainActivity.this, "Tag vazia! digite uma tag", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textEditText.addTextChangedListener(tw);

        tagInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textEditText.removeTextChangedListener(tw);
                textEditText.setText("");
                textEditText.addTextChangedListener(tw);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String titulo = tagInputEditText.getText().toString();
            String texto = s.toString();
            Tag tag = new Tag(titulo, texto);
            //salva no firebase a tag alterada, acionando o listener do firebase
            tagsReference.child(tag.getTitulo()).setValue(tag);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



    private void executarListener() {
        //executa o listener do firebase
        tagsListener();
    }

    private void tagsListener() {
        tagsReference.addValueEventListener(new ValueEventListener() {
            //pega o nome da tag
            final String titulo = tagInputEditText.getText().toString();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tag tag = new Tag();
                //procura a tag no firebase pelo id, que é o titulo da tag
                tag = dataSnapshot.child(titulo).getValue(Tag.class);
                //se não existir, criar
                if(tag == null)
                {
                    tag = new Tag(titulo, "");
                    tagsReference.child(titulo).setValue(tag);
                }
                //se o titulo for igual ao titulo da tag e ao que ta escrito no inputEditText
                if(titulo.equals(tag.getTitulo()) && titulo.equals(tagInputEditText.getText().toString())){
                    String texto = tag.getTexto();
                    textEditText.removeTextChangedListener(tw);
                    int posicao = textEditText.getSelectionStart();
                    textEditText.setText(texto);
                    textEditText.addTextChangedListener(tw);
                    if(posicao <= texto.length()){
                        textEditText.setSelection(posicao);
                    }
                    else{
                        textEditText.setSelection(posicao-1);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, getString(R.string.erro_firebase),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        executarListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
