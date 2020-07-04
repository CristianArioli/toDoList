package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DbHelper extends SQLiteOpenHelper {

    //Outros atributos
    private SQLiteDatabase db;
    private static final String PATH_DB = "/data/data/com.example.todolist/databases/toDoList";

    //Atributos para o construtor
    private static final String NOME_DB = "toDoList";
    private static final int VERSAO_DB = 1;
    private static final String tb_todolist = "tb_todolist";
    private Context mContext;

    public DbHelper(Context context){
        super(context,NOME_DB,null,VERSAO_DB);
        this.mContext = context;
        db = getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Metodo para criar a tabela
    public boolean criarTBTarefa(){
        openDB();
        String createTable = "CREATE TABLE IF NOT EXISTS " + tb_todolist +
                " ("+
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " nome TEXT,"+
                " descricao TEXT,"+
                " importancia INTEGER,"+
                " data TEXT,"+
                " hora TEXT"+
                " )";

        String dropTable = "DROP TABLE " + tb_todolist;

        try {
            db.execSQL(createTable);
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            db.close();
        }

    }

    public boolean deletarTarefa(Tarefa tarefa){

        openDB();

        String query = "id = " + tarefa.getId();

        try {

            db.delete(tb_todolist,query,null);

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            db.close();
        }

    }

    public Tarefa pesquisarID(int id){
        openDB();

        Tarefa tarefaPesquisada = new Tarefa();

        String query = "SELECT * FROM " + tb_todolist + " WHERE id = " + id + ";";
        System.out.println(query);

        try {

            Cursor c = db.rawQuery(query,null);

            if(c.moveToFirst()){
                do {
                    tarefaPesquisada.setId(Integer.parseInt(c.getString(0)));
                    tarefaPesquisada.setNome(c.getString(1));
                    tarefaPesquisada.setDescricao(c.getString(2));
                    tarefaPesquisada.setImportancia(Integer.parseInt(c.getString(3)));
                    tarefaPesquisada.setData((c.getString(4)));
                    tarefaPesquisada.setHora((c.getString(5)));

                }while(c.moveToNext());
            }

            return tarefaPesquisada;


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            db.close();
        }
    }

    public ArrayList<Tarefa> getLista(){
        openDB();

        ArrayList<Tarefa> eventoArrayList = new ArrayList<>();

        String  query = "SELECT * FROM " + tb_todolist + " ORDER BY importancia DESC";

        try {

            Cursor c = db.rawQuery(query,null);

            if(c.moveToFirst()){
                do {
                    Tarefa tarefa = new Tarefa();

                    tarefa.setId(Integer.parseInt(c.getString(0)));
                    tarefa.setNome(c.getString(1));
                    tarefa.setDescricao(c.getString(2));
                    tarefa.setImportancia(Integer.parseInt(c.getString(3)));
                    tarefa.setData((c.getString(4)));
                    tarefa.setHora((c.getString(5)));

                    eventoArrayList.add(tarefa);

                }while(c.moveToNext());
            }

            return eventoArrayList;


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            db.close();
        }
    }


    //Metodo para inserir na tabela
    public boolean inserirTarefa(Tarefa tarefa){

        openDB();

        try {

            ContentValues values = new ContentValues();
            values.put("nome",tarefa.getNome());
            values.put("descricao",tarefa.getDescricao());
            values.put("importancia",tarefa.getImportancia());
            values.put("data",tarefa.getData());
            values.put("hora",tarefa.getHora());

            db.insert(tb_todolist, null,values);
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            db.close();
        }


    }

    public boolean alterarTarefa(Tarefa tarefa){

        openDB();

        try {

            String where = "id = " + tarefa.getId() ;

            ContentValues values = new ContentValues();

            values.put("nome",tarefa.getNome());
            values.put("descricao",tarefa.getDescricao());
            values.put("importancia",tarefa.getImportancia());
            values.put("data",tarefa.getData());
            values.put("hora",tarefa.getHora());

            db.update(tb_todolist, values,where,null);
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            db.close();
        }
    }

    //Metodo para abrir o banco de dados
    @SuppressLint("WrongConstant")
    private void openDB(){
        if(!db.isOpen()){
            db = mContext.openOrCreateDatabase(PATH_DB,SQLiteDatabase.OPEN_READWRITE,null);
        }
    }


}
