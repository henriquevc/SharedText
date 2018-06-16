package com.example.henrique.sharedtext;

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
    private List<Tag> tags;
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
        configuraFirebase();
        textEditText = (EditText) findViewById(R.id.textEditText);
        tagInputEditText = (TextInputEditText) findViewById(R.id.tagInputEditText);
        final Tag tag = new Tag();
        tags = new Stack<Tag>();
        tagsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tags.clear();
                for (DataSnapshot filho : dataSnapshot.getChildren()){
                    Tag tag = filho.getValue(Tag.class);
                    tag.setChave(filho.getKey());
                    tags.add(tag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, getString(R.string.erro_firebase),
                        Toast.LENGTH_SHORT).show();
            }
        });



        tagInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            //Objeto tag criado

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //Perde o foco do InputEditText
                if(!hasFocus){
                    //Verificar se a tag já existe no firebase
                    tag.setTitulo(tagInputEditText.getText().toString());
                    boolean existe = false;
                    for(Tag tagChild : tags){
                        if(tag.getTitulo().equals(tagChild.getTitulo().toString())){
                            existe = true;
                            tag.setChave(tagChild.getChave());
                            tag.setTexto(tagChild.getTexto());
                        }
                    }
                    //se a tag não existir, inserir
                    if(!existe){
                        String chave = tagsReference.push().getKey();
                        tag.setChave(chave);
                        tag.setTexto(textEditText.getText().toString());
                        tagsReference.child(tag.getChave()).setValue(tag);
                    }
                    textEditText.setText(tag.getTexto());
                }
            }
        });

        textEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tag.setTexto(s.toString());
                tagsReference.child(tag.getChave()).setValue(tag);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


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
