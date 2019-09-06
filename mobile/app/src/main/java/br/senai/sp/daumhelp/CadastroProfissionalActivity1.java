package br.senai.sp.daumhelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;

public class CadastroProfissionalActivity1 extends AppCompatActivity {

    private Button btnProximo;
    private Button btnVoltar;
    private EditText etNome;
    private EditText etDataNasc;
    private EditText etCpf;
    private EditText etEmail;
    private EditText etSenha;
    private EditText etConfirmacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pro1);

        btnProximo = findViewById(R.id.btn_proximo);
        btnVoltar = findViewById(R.id.btn_voltar);
        etNome = findViewById(R.id.et_nome_pro);
        etDataNasc = findViewById(R.id.et_nasc_pro);
        etCpf = findViewById(R.id.et_cpf_pro);
        etEmail = findViewById(R.id.et_email_pro);
        etSenha = findViewById(R.id.et_senha_pro);
        etConfirmacao = findViewById(R.id.et_confirm_pro);

        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*PEGAR DADOS QUE O USUÁRIO DIGITOU*/
                String nomeProfissional = etNome.getText().toString();
                String dataNascProfissional = etDataNasc.getText().toString();
                String cpfProfissional = etCpf.getText().toString();
                String emailProfissional = etEmail.getText().toString();
                String senhaProfissional = etSenha.getText().toString();
                String confirmacaoProfissional = etConfirmacao.getText().toString();

                /*ARRAY DOS DADOS PESSOAIS PARA SER LEVADO PRA PRÓXIMA ACTIVITY*/
                String[] listaDados = new String[]{nomeProfissional, dataNascProfissional, cpfProfissional, emailProfissional, senhaProfissional};

                if(etConfirmacao.getText().toString().equals(etSenha.getText().toString())){

                        /*SERIALIZAÇÃO DOS DADOS*/
                        Intent intent = new Intent(CadastroProfissionalActivity1.this, ConfirmarEmailActivity.class);
                        intent.putExtra("dados_pessoais_pro", listaDados);
                        startActivity(intent);

                }else{
                    Toast.makeText(CadastroProfissionalActivity1.this, "As senhas não correspondem", Toast.LENGTH_SHORT).show();

                }



            }
        });

        /*VOLTAR PARA A PRÓXIMA ACTIVITY*/
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroProfissionalActivity1.this, EscolhaActivity.class);
                startActivity(intent);
            }
        });



    }
}
