package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ContatoAdapter extends BaseAdapter {

    private ArrayList<Tarefa> lista;
    private LayoutInflater inflater;

    public ContatoAdapter(Context ctx, ArrayList<Tarefa> lista){
        this.lista = lista;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() { return lista.size(); }

    @Override
    public Object getItem(int position) { return lista.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ObjetosItemLista obj;
        if (view == null){
            view = inflater.inflate(R.layout.item_lista, null);
            obj = new ObjetosItemLista();
            obj.Id = view.findViewById(R.id.txtItemId);
            obj.Nome = view.findViewById(R.id.txtItemNome);
            obj.Data = view.findViewById(R.id.txtItemData);
            obj.Hora = view.findViewById(R.id.txtItemHora);
            obj.Descricao = view.findViewById(R.id.txtItemDescricao);
            view.setTag(obj);
        } else {
            obj = (ObjetosItemLista)view.getTag();
        }
        Tarefa p = lista.get(position);
        obj.Nome.setText(p.getNome());
        obj.Id.setText(Integer.toString(p.getId()));
        obj.Hora.setText(p.getHora());
        obj.Data.setText(p.getData());
        obj.Descricao.setText(p.getDescricao());

        return view;
    }

    static class ObjetosItemLista {
        TextView Nome;
        TextView Id;
        TextView Data;
        TextView Hora;
        TextView Descricao;
    }
}
