package br.senai.sp.informatica.mobileb.pokedex.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.senai.sp.informatica.mobileb.pokedex.Main;

/**
 * Created by WEB on 30/01/2018.
 */

public class PokemonDao extends SQLiteOpenHelper {
    public static PokemonDao instance = new PokemonDao();

    private PokemonDao(){
        super(Main.getContext(),"DbPokemon",null,3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table pokemon ("+
                        "id integer primary key autoincrement," +
                        "nome text not null," +
                        "numDex int not null," +
                        "tipo1 int not null,"+
                        "tipo2 int," +
                        "dtCaptura long not null," +
                        "apaga int not null," +
                        "poke_image blob)"
                    );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
//                db.execSQL("alter table pokemon add column delA int not null");
            case 2:
//                db.execSQL("alter table pokemon add column delB int not null");
        }
    }

    private void setData(SQLiteStatement sql, Pokemon obj){
        sql.bindString(1, obj.getNome());
        sql.bindLong(2, obj.getDexNum());
        sql.bindString(3, obj.getTipo1());
        sql.bindString(4, obj.getTipo2());
        sql.bindLong(5, obj.getDtCaptura().getTime());
        sql.bindLong(6, (obj.isApagar() ? 1 : 0));
        sql.bindBlob(7, obj.getFotoPoke() != null ? obj.getFotoPoke() : new byte[] {});
    }

    private Pokemon getData(Cursor cursor) {
        Pokemon obj = new Pokemon();
        obj.setId(cursor.getLong(0));
        obj.setNome(cursor.getString(1));
        obj.setDexNum(cursor.getInt(2));
        obj.setTipo1(cursor.getString(3));
        obj.setTipo2(cursor.getString(4));
        obj.setDtCaptura(new Date(cursor.getLong(5)));
        obj.setApagar(cursor.getLong(6) == 1);
        obj.setFotoPoke(cursor.getBlob(7).length > 0 ? cursor.getBlob(7) : null);
        return obj;
    }

    public void salvar(Pokemon obj) {
        // Abre o banco de dados para escrita
        SQLiteDatabase db = getWritableDatabase();

        // Produra registro com o mesmo nome
        Cursor cursor = db.rawQuery("select * from pokemon " +
                                    "where lower(nome)=? and lower(tipo1)=?",
                new String[]{
                        obj.getNome().toLowerCase(),
                        obj.getTipo1().toLowerCase()

                }
        );

        if(cursor.getCount() == 0 ){ // não encontrado, salva
            String sql = "insert into pokemon (nome, numDex, tipo1, tipo2, dtCaptura, apaga, poke_image)"+
                         "values (?,?,?,?,?,?,?)";
            SQLiteStatement insert = db.compileStatement(sql);
            setData(insert, obj);
            insert.execute();
            cursor.close();
            cursor = db.rawQuery("select last_insert_rowid() from pokemon", null);
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                obj.setId(cursor.getLong(0));
            }
        }else{
            cursor.moveToFirst();
            obj.setId(cursor.getLong(0));

            String sql = "update pokemon set nome=?,numDex=?,tipo1=?, tipo2=?, dtCaptura=?, apaga=?, poke_image=?" +
                         "where id=?";
            SQLiteStatement update = db.compileStatement(sql);
            setData(update, obj);
            update.bindLong(7,obj.getId());
            update.execute();
        }
        cursor.close();
        db.close();
    }

    public void remover(Long id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from pokemon where id =" +id);
        db.close();
    }

    public List<Long> listarIds(String ordem) {
        String query;
        if(ordem.equals("Num Dex")){
            query = "select id from pokemon order by numDex";
        } else if (ordem.equals("Nome")){
            query = "select id from pokemon order by nome";
        }else {
            query = "select id from pokemon order by dt_captura";
        }

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<Long> lista = new ArrayList<>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++){
                lista.add(cursor.getLong(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return lista;
    }

    public Pokemon localizar(Long id) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from pokemon where id=?",
                new String[] {String.valueOf(id)});
        Pokemon obj = null;
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            obj = getData(cursor);
        }
        cursor.close();
        db.close();

        return obj;
    }

    public void removerMarcados() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from album where apaga = 1");
        db.close();
    }

    public boolean existeAlbunsADeletar() {
        boolean existe = false;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) " +
                "from album where apaga = 1", null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            if(cursor.getInt(0) > 0)
                existe = true;

            cursor.close();
        }

        db.close();

        return existe;
    }

    public void limpaMarcados() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("update pokemon set apaga = 0 where apaga = 1");
        db.close();
    }

}
