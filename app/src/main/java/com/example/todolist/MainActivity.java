package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    DbHelper db;
    private ListView lista;
    Button btnListAmanha;
    Button btnListHoje;
    Button btnListEmBreve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DbHelper(this);
        db.criarTBTarefa();
        SharedPreferences preferences = getSharedPreferences("user",MODE_PRIVATE);

        Tarefa tarefa = new Tarefa(0, "sair", "descrição", 0, "20/02", "19:30");
        Tarefa tarefa2 = new Tarefa(1, "sair", "descrição", 0, "20/02", "19:30");

        //db.inserirAtividade(tarefa2);

        lista = findViewById(R.id.lstContatos);

        btnListHoje = findViewById(R.id.btnListHoje);
        btnListAmanha = findViewById(R.id.btnListAmanha);
        btnListEmBreve = findViewById(R.id.btnListEmBreve);

        btnListHoje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lista.setAdapter(new ContatoAdapter(MainActivity.this,getTarefasHoje()));
            }
        });

        btnListAmanha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lista.setAdapter(new ContatoAdapter(MainActivity.this,getTarefasAmanha()));
            }
        });

        btnListEmBreve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lista.setAdapter(new ContatoAdapter(MainActivity.this,getTarefasEmBreve()));
            }
        });

        this.lista.setAdapter(new ContatoAdapter(this,getTarefasHoje()));

        this.lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        DbHelper db = new DbHelper(getApplicationContext());
                        switch (item.getItemId()) {
                            case R.id.opcao1:
                                Tarefa tarefaRemover = ((Tarefa)adapterView.getAdapter().getItem(i));
                                Toast.makeText(MainActivity.this, "Tarefa de id " + tarefaRemover.getId() + " finalizada.", Toast.LENGTH_SHORT).show();
                                db.deletarTarefa(tarefaRemover);
                                recarregarLista();
                                return true;
                            case R.id.opcao2:
                                Tarefa tarefaAlterar = (Tarefa)adapterView.getAdapter().getItem(i);
                                executarActivity(EnumOperacao.Alterar, tarefaAlterar.getId());
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popup.inflate(R.menu.menu_opcoes);
                popup.show();

                return false;
            }
        });

    }

    private ArrayList<Tarefa> getTarefasEmBreve() {
        GregorianCalendar startTime = new GregorianCalendar();
        GregorianCalendar endTime = new GregorianCalendar();
        ArrayList<Tarefa> listaEmBreve = new ArrayList<>() ;
        for (Tarefa listaPersonalizada : db.getLista()
        ) {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date data = formato.parse(listaPersonalizada.getData());
                Calendar calendario = Calendar.getInstance();
                startTime.setTime(data);

                Date dataFinal = calendario.getTime();
                endTime.setTime(dataFinal);

                //System.out.println(data.compareTo(dataFinal));

                GregorianCalendar curTime = new GregorianCalendar();
                GregorianCalendar baseTime = new GregorianCalendar();

                int dif_multiplier = 1;

                // Verifica a ordem de inicio das datas
                if( data.compareTo( dataFinal ) < 0 ){
                    baseTime.setTime(dataFinal);
                    curTime.setTime(data);
                    dif_multiplier = 1;
                }else{
                    baseTime.setTime(data);
                    curTime.setTime(dataFinal);
                    dif_multiplier = -1;
                }

                int result_years = 0;
                int result_months = 0;
                int result_days = 0;

                // Para cada mes e ano, vai de mes em mes pegar o ultimo dia para import acumulando
                // no total de dias. Ja leva em consideracao ano bissesto
                while( curTime.get(GregorianCalendar.YEAR) < baseTime.get(GregorianCalendar.YEAR) ||
                        curTime.get(GregorianCalendar.MONTH) < baseTime.get(GregorianCalendar.MONTH)  )
                {

                    int max_day = curTime.getActualMaximum( GregorianCalendar.DAY_OF_MONTH );
                    result_months += max_day;
                    curTime.add(GregorianCalendar.MONTH, 1);

                }

                // Marca que é um saldo negativo ou positivo
                result_months = result_months*dif_multiplier;


                // Retirna a diferenca de dias do total dos meses
                result_days += (endTime.get(GregorianCalendar.DAY_OF_MONTH) - startTime.get(GregorianCalendar.DAY_OF_MONTH));

                System.out.println(listaPersonalizada.getData());
                System.out.println("ID " + listaPersonalizada.getId() + "Anos " + result_years + "Meses " + result_months + "Dias " + result_days);

                if(result_days < -1 || result_months < 0) {
                    listaEmBreve.add(listaPersonalizada);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return listaEmBreve;
    }

    private ArrayList<Tarefa> getTarefasAmanha() {
        GregorianCalendar startTime = new GregorianCalendar();
        GregorianCalendar endTime = new GregorianCalendar();
        ArrayList<Tarefa> listaAmanha = new ArrayList<>() ;
        for (Tarefa listaPersonalizada : db.getLista()
        ) {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date data = formato.parse(listaPersonalizada.getData());
                Calendar calendario = Calendar.getInstance();
                startTime.setTime(data);

                Date dataFinal = calendario.getTime();
                endTime.setTime(dataFinal);

                //System.out.println(data.compareTo(dataFinal));

                GregorianCalendar curTime = new GregorianCalendar();
                GregorianCalendar baseTime = new GregorianCalendar();

                int dif_multiplier = 1;

                // Verifica a ordem de inicio das datas
                if( data.compareTo( dataFinal ) < 0 ){
                    baseTime.setTime(dataFinal);
                    curTime.setTime(data);
                    dif_multiplier = 1;
                }else{
                    baseTime.setTime(data);
                    curTime.setTime(dataFinal);
                    dif_multiplier = -1;
                }

                int result_years = 0;
                int result_months = 0;
                int result_days = 0;

                // Para cada mes e ano, vai de mes em mes pegar o ultimo dia para import acumulando
                // no total de dias. Ja leva em consideracao ano bissesto
                while( curTime.get(GregorianCalendar.YEAR) < baseTime.get(GregorianCalendar.YEAR) ||
                        curTime.get(GregorianCalendar.MONTH) < baseTime.get(GregorianCalendar.MONTH)  )
                {

                    int max_day = curTime.getActualMaximum( GregorianCalendar.DAY_OF_MONTH );
                    result_months += max_day;
                    curTime.add(GregorianCalendar.MONTH, 1);

                }

                // Marca que é um saldo negativo ou positivo
                result_months = result_months*dif_multiplier;


                // Retirna a diferenca de dias do total dos meses
                result_days += (endTime.get(GregorianCalendar.DAY_OF_MONTH) - startTime.get(GregorianCalendar.DAY_OF_MONTH));

                System.out.println(listaPersonalizada.getData());
                System.out.println("ID " + listaPersonalizada.getId() + "Anos " + result_years + "Meses " + result_months + "Dias " + result_days);

                if(result_days == -1 && result_months == 0) {
                    listaAmanha.add(listaPersonalizada);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return listaAmanha;
    }

    private ArrayList<Tarefa> getTarefasHoje() {
        ArrayList<Tarefa> listaHoje = new ArrayList<>() ;
        for (Tarefa listaPersonalizada : db.getLista()
             ) {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date data = formato.parse(listaPersonalizada.getData());
                Calendar calendario = Calendar.getInstance();

                String mes;
                if ((calendario.get(Calendar.MONTH) + 1) < 10){
                    mes = "0" + (calendario.get(Calendar.MONTH) + 1);
                } else{
                    mes = "" + (calendario.get(Calendar.MONTH) + 1);
                }
                String dataAtual = calendario.get(Calendar.DAY_OF_MONTH) + "/" + mes + "/" + calendario.get(Calendar.YEAR);
                //System.out.println(dataAtual + listaPersonalizada.getData());
                if(listaPersonalizada.getData().equals(dataAtual)){
                    listaHoje.add(listaPersonalizada);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return listaHoje;
    }

    public void onResume(){
        super.onResume();
        lista.setAdapter(new ContatoAdapter(this,getTarefasHoje()));
    }

    public void recarregarLista(){
        lista.setAdapter(new ContatoAdapter(this,getTarefasHoje()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        executarActivity(EnumOperacao.Incluir, 0);

        return super.onOptionsItemSelected(item);
    }

    private void executarActivity(EnumOperacao operacao, int id){
        Bundle b = new Bundle();
        b.putString("operacao", operacao.name());
        b.putString("id", Integer.toString(id));
        Intent it = new Intent(this, incluirTarefas.class);
        it.putExtras(b);
        startActivity(it);
    }
}
