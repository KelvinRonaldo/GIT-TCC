package br.net.daumhelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.Date;

import br.net.daumhelp.configretrofit.RetroFitConfig;
import br.net.daumhelp.model.Cidade;
import br.net.daumhelp.model.Endereco;
import br.net.daumhelp.model.Profissional;
import br.net.daumhelp.model.Subcategoria;
import br.net.daumhelp.recursos.Data;
import br.net.daumhelp.recursos.EncryptString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroTermosActivity extends AppCompatActivity {


    private Button btnVoltar;
    private Button btnProximo;
    private CheckBox checkBox;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termos);

        btnVoltar = findViewById(R.id.btn_voltar);
        btnProximo = findViewById(R.id.btn_proximo);
        checkBox = findViewById(R.id.cb_termos);

        btnProximo.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        if (intent.getSerializableExtra("dados_pessoais") != null) {
            final String[] listaDados = (String[]) intent.getSerializableExtra("dados_pessoais");

            if (intent.getSerializableExtra("endereco") != null) {
                final String[] listaEndereco = (String[]) intent.getSerializableExtra("endereco");

                if (intent.getSerializableExtra("serv_pro") != null) {
                    final String[] listaProfissao = (String[]) intent.getSerializableExtra("serv_pro");

                    //MONTANDO OBJETO ENDERECO PARA CADASTRAR NO BANCO
                    final Endereco endereco = new Endereco();
                    endereco.setCep(listaEndereco[0]);
                    endereco.setLogradouro(listaEndereco[1]);
                    endereco.setBairro(listaEndereco[2]);
                    Cidade cidade = new Cidade();
                    cidade.setIdCidade(Long.parseLong(listaEndereco[3]));
                    endereco.setCidade(cidade);

                    btnProximo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int i = 1;
                            Intent intent = new Intent(CadastroTermosActivity.this, MainActivity.class);
                            intent.putExtra("cadastro", i);
                            startActivity(intent);

                            Call<Endereco> call = new RetroFitConfig().getEnderecoService().cadastrarEndereco(endereco);
                            call.enqueue(new Callback<Endereco>() {
                                @Override
                                public void onResponse(Call<Endereco> call, Response<Endereco> response) {

                                    Endereco endereco1 = response.body();
                                    Subcategoria subcategoria = new Subcategoria();
                                    subcategoria.setIdSubcategoria(Long.parseLong(listaProfissao[2]));
                                    subcategoria.setCategoria(null);

                                    Profissional profissional = new Profissional();
                                    profissional.setNome(listaDados[0]);
                                    Date data = Data.stringToDate(listaDados[1]);
                                    String dataFormatada = Data.dataToString(data);
                                    profissional.setDataNasc(dataFormatada);
                                    profissional.setCpf(listaDados[2]);
                                    profissional.setEmail(listaDados[3]);
                                    profissional.setSenha(EncryptString.gerarHash(listaDados[4]));
                                    profissional.setResumoQualificacoes(listaProfissao[0]);
                                    profissional.setValorHora(Double.parseDouble(listaProfissao[1]));
                                    profissional.setSubcategoria(subcategoria);
                                    profissional.setEndereco(endereco1);
                                    profissional.setFoto("foto.png");

                                    Call<Profissional> callPro = new RetroFitConfig().getProfissionalService().cadastrarProfissional(profissional);
                                    callPro.enqueue(new Callback<Profissional>() {

                                        @Override
                                        public void onResponse(Call<Profissional> call, Response<Profissional> response) {
                                            response.body();
                                        }
                                        @Override
                                        public void onFailure(Call<Profissional> call, Throwable t) {
                                          Log.i("BOM DIA", t.getMessage());
                                        }


                                    });

                                }

                                @Override
                                public void onFailure(Call<Endereco> call, Throwable t) {
                                    Log.i("Retrofit Cadastro", t.getMessage());
                                }
                            });

                        }
                    });
                }

            }

        }

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroTermosActivity.this, CadastroServicoActivity.class);
                startActivity(intent);
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {

                    btnProximo.setVisibility(View.VISIBLE);
                } else {

                    btnProximo.setVisibility(View.INVISIBLE);
                }

            }
        });


    }
}