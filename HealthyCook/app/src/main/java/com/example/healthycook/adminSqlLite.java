package com.example.healthycook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class adminSqlLite extends SQLiteOpenHelper {

    public adminSqlLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase DBReceta) {
        DBReceta.execSQL("CREATE TABLE Receta (idreceta TEXT PRIMARY KEY, Nombre    TEXT, Receta    TEXT, Image    TEXT, Calorias    REAL(50), Tipo    TEXT, Comida    TEXT, PLato    TEXT)");
        DBReceta.execSQL("CREATE TABLE Ingredientes (id_receta    TEXT,Nombre    TEXT,FOREIGN KEY(id_receta) REFERENCES Receta(idreceta))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
