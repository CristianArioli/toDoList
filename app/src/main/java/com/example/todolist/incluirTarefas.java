package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import java.util.Calendar;

public class incluirTarefas extends AppCompatActivity implements View.OnClickListener{

    DbHelper db;
    EnumOperacao operacao;
    int id;
    EditText edtNome;
    EditText edtDescricao;
    int importancia;
    EditText edtHora;
    EditText edtData;
    Tarefa tarefa;
    RadioButton radioBaixo;
    RadioButton radioMedio;
    RadioButton radioAlto;

    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inclusao_tarefas);
        operacao = EnumOperacao.valueOf(getIntent().getExtras().getString("operacao"));

        id = Integer.parseInt(getIntent().getExtras().getString("id"));

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.edtData);
        txtTime=(EditText)findViewById(R.id.edtHora);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        db = new DbHelper(this);

        edtNome = findViewById(R.id.edtNome);
        edtData = findViewById(R.id.edtData);
        edtHora = findViewById(R.id.edtHora);
        edtDescricao = findViewById(R.id.edtDescricao);
        radioBaixo = findViewById(R.id.radioBaixo);
        radioMedio = findViewById(R.id.radioMedio);
        radioAlto = findViewById(R.id.radioAlto);

        if (id > 0){
            tarefa = db.pesquisarID(id);
            System.out.println(tarefa);
            if (tarefa != null) {
                edtNome.setText(tarefa.getNome());
                edtData.setText(tarefa.getData());
                edtHora.setText(tarefa.getHora());
                edtDescricao.setText(tarefa.getDescricao());
                if (tarefa.getImportancia() == 0){
                    radioBaixo.toggle();
                } else if (tarefa.getImportancia() == 1){
                    radioMedio.toggle();
                } else {
                    radioAlto.toggle();
                }
            }
        }

    }

    private int pegaRadioImportancia() {

        if(radioBaixo.isChecked()){
            importancia = 0;
        }else if( radioMedio.isChecked()){
            importancia = 1;
        }else if( radioAlto.isChecked()){
            importancia = 2;
        } return importancia;
    }

    public void confirmarDados(View v) {
        DbHelper db = new DbHelper(getApplicationContext());
        String nome = edtNome.getText().toString();
        String descricao = edtDescricao.getText().toString();
        int importancia = pegaRadioImportancia();
        String data = edtData.getText().toString();
        String hora = edtHora.getText().toString();
        System.out.println(operacao);

        if (operacao == EnumOperacao.Incluir) {
            Tarefa tarefa = new Tarefa();
            tarefa.setNome(nome);
            tarefa.setDescricao(descricao);
            tarefa.setImportancia(importancia);
            tarefa.setData(data);
            tarefa.setHora(hora);
            db.inserirTarefa(tarefa);
        }

        if (operacao == EnumOperacao.Alterar) {
            Tarefa tarefa = db.pesquisarID(id);
            tarefa.setNome(nome);
            tarefa.setDescricao(descricao);
            tarefa.setImportancia(importancia);
            tarefa.setData(data);
            tarefa.setHora(hora);
            db.alterarTarefa(tarefa);
        }

            finish();
    }

    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(String.format("%02d/%02d/%02d", dayOfMonth, (monthOfYear + 1), year));

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

}
