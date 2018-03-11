package com.example.yc.midterm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Dialog;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.example.yc.midterm.DataStruct.Person;
import com.example.yc.midterm.Database.myDBOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by kaiminglee on 16/11/2017.
 */

public class ItemDetailActivity extends AppCompatActivity {
    private myDBOpenHelper myDBOpenHelper;
    private SQLiteDatabase db;
    private Context mcontext;
    private Dialog dialog;

    private ImageView imageView;
    private TextView name;
    private TextView stytleName;
    private TextView gender;
    private TextView birthday;
    private TextView birthPlace;
    private TextView birthPlaceNow;
    private TextView power;
    private Button edit;
    private Button exit;
    private Person person;

    private int imageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_style);
        mcontext = ItemDetailActivity.this;
        person = (Person)getIntent().getSerializableExtra("person");
        findView();
        init();
        clickEvent();
    }

    public void clickEvent() {
        edit.setOnClickListener(new View.OnClickListener() {//点击“修改”button
            public void onClick(View view) {
                showDialog(mcontext);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {//点击“退出”button
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void init() {
        imageView.setImageResource(person.getImageId());
        name.setText(person.getName());
        stytleName.setText("(字："+person.getZi()+")");
        gender.setText("性别："+person.getSex());
        birthday.setText("生卒："+person.getBirthday());
        birthPlace.setText("籍贯："+person.getBirthPlace());
        birthPlaceNow.setText("今为："+person.getBirthPlace_now());
        power.setText("势力："+person.getPower());
    }

    public void findView() {
        final Dialog dialog = new Dialog(mcontext);
        imageView = (ImageView) findViewById(R.id.img);
        name = (TextView) findViewById(R.id.name);
        stytleName = (TextView) findViewById(R.id.zi);
        gender = (TextView) findViewById(R.id.sex);
        birthday = (TextView) findViewById(R.id.birthday);
        birthPlace = (TextView) findViewById(R.id.birthplace);
        birthPlaceNow = (TextView) findViewById(R.id.birthplace_now);
        power = (TextView) findViewById(R.id.power);
        edit = (Button) findViewById(R.id.edit);
        exit = (Button) findViewById(R.id.exit);
    }

    /*
        *the following is some operations of dialog
        * ******************************************************************************************************************************************************
        */
    //创建一个自定义的dialog，里面包含人物信息
    public void showDialog(Context context) {

        final View view = LayoutInflater.from(context).inflate(R.layout.fixable_dialog_style, null);
        dialog = new Dialog(context);
        dialog.setContentView(view);

        //存放一组图片，用于人物头像的选择
        final int[] Images = {person.getImageId(),
                R.mipmap.girl1, R.mipmap.girl2, R.mipmap.girl3, R.mipmap.girl4,
                R.mipmap.wen1, R.mipmap.wen2, R.mipmap.wen3, R.mipmap.wen4, R.mipmap.wen5,
                R.mipmap.wu1, R.mipmap.wu2, R.mipmap.wu3, R.mipmap.wu4, R.mipmap.wu5, R.mipmap.wu6, R.mipmap.wu7};

        final ImageView img = (ImageView) view.findViewById(R.id.img);
        EditText editName = (EditText) view.findViewById(R.id.editName);
        EditText editZi = (EditText) view.findViewById(R.id.editZi);
        EditText chooseSex = (EditText) view.findViewById(R.id.chooseSex);
        EditText editBirthAndDeath = (EditText) view.findViewById(R.id.editBirthAndDeath);
        EditText editBirthPlace = (EditText) view.findViewById(R.id.editBirthPlace);
        EditText editNowPlace = (EditText) view.findViewById(R.id.editNowPlace);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.choosePower);
        RadioButton chooseWei = (RadioButton) view.findViewById(R.id.chooseWei);
        RadioButton chooseShu = (RadioButton) view.findViewById(R.id.chooseShu);
        RadioButton chooseWu = (RadioButton) view.findViewById(R.id.chooseWu);
        Button confirm = (Button) view.findViewById(R.id.confirm) ;
        Button cancel = (Button) view.findViewById(R.id.cancel) ;

        //Put person's info. to update page.
        img.setImageResource(person.getImageId());
        editName.setText(person.getName());
        editZi.setText(person.getZi());
        chooseSex.setText(person.getSex());
        editBirthAndDeath.setText(person.getBirthday());
        editBirthPlace.setText(person.getBirthPlace());
        editNowPlace.setText(person.getBirthPlace_now());
        if (person.getPower().equals("魏")) {
            radioGroup.check(chooseWei.getId());
        } else if (person.getPower().equals("蜀")) {
            radioGroup.check(chooseShu.getId());
        } else {
            radioGroup.check(chooseWu.getId());
        }


        //点击头像，图片发生改变
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageIndex == 17) {imageIndex = 0;}
                imageIndex++;
                img.setImageResource(Images[imageIndex]);
            }
        });

        //点击确认按钮，修改人物信息并录入到数据库
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDBOpenHelper = new myDBOpenHelper(mcontext, "my.db", null, 1);
                db = myDBOpenHelper.getWritableDatabase();

                //Get datas from EditTexts
                EditText editName = (EditText) view.findViewById(R.id.editName);
                EditText editZi = (EditText) view.findViewById(R.id.editZi);
                EditText chooseSex = (EditText) view.findViewById(R.id.chooseSex);
                EditText editBirthAndDeath = (EditText) view.findViewById(R.id.editBirthAndDeath);
                EditText editBirthPlace = (EditText) view.findViewById(R.id.editBirthPlace);
                EditText editNowPlace = (EditText) view.findViewById(R.id.editNowPlace);
                RadioButton chooseWei = (RadioButton) view.findViewById(R.id.chooseWei);
                RadioButton chooseShu = (RadioButton) view.findViewById(R.id.chooseShu);
                RadioButton chooseWu = (RadioButton) view.findViewById(R.id.chooseWu);

                String name = editName.getText().toString();
                String styleName = editZi.getText().toString();
                String gender = chooseSex.getText().toString();
                String birthDay = editBirthAndDeath.getText().toString();
                String birthPlace = editBirthPlace.getText().toString();
                String birthPlaceNow = editNowPlace.getText().toString();
                String power;
                if (chooseWei.isChecked()) {
                    power = "魏";
                } else if (chooseShu.isChecked()) {
                    power = "蜀";
                } else {
                    power = "吴";
                }

                //Update the data
                Object[] personInf = {person.getPersonId(), Images[imageIndex], name, gender, birthDay, styleName, birthPlace, birthPlaceNow, power};
                db.execSQL("DELETE FROM person WHERE personid = ?", new Object[]{person.getPersonId()});
                db.execSQL("INSERT INTO person(personid, imageId,name,sex,birthday,styleName,birthPlace,birthPlaceNow,power) VALUES(?,?,?,?,?,?,?,?,?)",
                        personInf);
                imageIndex = 0;

                //refresh the page
                person = findPersonFromDB("SELECT * FROM person WHERE personid = " + new String().valueOf(person.getPersonId()));
                init();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageIndex = 0;
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public Person findPersonFromDB(String statement) {
        Person persons = null;
        Cursor cursor =  db.rawQuery(statement, null);
        //存在数据才返回true
        if (cursor.moveToFirst())
        {
            int persenId = cursor.getInt(cursor.getColumnIndex("personid"));
            int imagId = cursor.getInt(cursor.getColumnIndex("imageId"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String sex = cursor.getString(cursor.getColumnIndex("sex"));
            String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
            String styleName = cursor.getString(cursor.getColumnIndex("styleName"));
            String birthPlace = cursor.getString(cursor.getColumnIndex("birthPlace"));
            String birthPlaceNow = cursor.getString(cursor.getColumnIndex("birthPlaceNow"));
            String power = cursor.getString(cursor.getColumnIndex("power"));
            persons = new Person(persenId, imagId, name, sex, birthday, styleName, birthPlace, birthPlaceNow, power);
        }
        cursor.close();
        return persons;
    }
    /*
        *the above is some operations of dialog
        * ******************************************************************************************************************************************************
        */
}
