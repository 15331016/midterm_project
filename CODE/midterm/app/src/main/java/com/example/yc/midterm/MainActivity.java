package com.example.yc.midterm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.yc.midterm.DataStruct.people;
import com.example.yc.midterm.DataStruct.Person;
import com.example.yc.midterm.Database.myDBOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private myDBOpenHelper myDBOpenHelper;
    private SQLiteDatabase db;
    private Context mcontext;
    private Dialog dialog;

    private int imageIndex = 0;


    private MediaPlayer mp;//背景音乐
    private Button add;//"增加信息"的button
    private ImageView music;//音乐播放或停止的图标
    private Button wei;//“魏国”
    private Button shu;//“蜀国”
    private Button wu;//“吴国”
    private Button seach;
    private EditText searchName;



    private SimpleAdapter simpleAdapter;
    private List<Person> datas;
    private ArrayList<Map<String,Object>> peoples;
    private ListView listView;
//    //魏国人物列表
//    private ArrayList<Map<String,Object>> data_wei = new ArrayList<>();
//    private SimpleAdapter simpleAdapter_wei;
//    private ListView listview_wei;
//    private List<people> Data_wei = new ArrayList<>(); //魏国列表的数据存储！！！
//    //蜀国人物列表
//    private List<Map<String,Object>> data_shu = new ArrayList<>();
//    private SimpleAdapter simpleAdapter_shu;
//    private ListView listview_shu;
//    private List<people> Data_shu = new ArrayList<>(); //蜀国列表的数据存储！！！
//    //吴国人物列表
//    private List<Map<String,Object>> data_wu = new ArrayList<>();
//    private SimpleAdapter simpleAdapter_wu;
//    private ListView listview_wu;
//    private List<people> Data_wu = new ArrayList<>(); //吴国列表的数据存储！！！

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = MainActivity.this;
        initializeDB();
        findView();
        init();//界面初始化
        click();//点击事件
    }

    public void findView(){
        add = (Button) findViewById(R.id.add);//"增加信息"的button
        music = (ImageView) findViewById(R.id.music);//音乐播放或停止的图标
        wei = (Button) findViewById(R.id.wei);//“魏国”
        shu = (Button) findViewById(R.id.shu);//“蜀国”
        wu = (Button) findViewById(R.id.wu);//“吴国”
        seach = (Button) findViewById(R.id.search);
        searchName = (EditText) findViewById(R.id.search_name);
        listView = (ListView) findViewById(R.id.list);
    }

    /*
        *the following is some operations of database
        * ******************************************************************************************************************************************************
        */
    public void initializeDB() {
        //  if no exist database then creats database "my.db" and initialize datas otherwise opens database.
        if (!existDB("my.db")) {
            Object[][] person = {{R.mipmap.caocao, "曹操", "男", "155年~220年", "孟德", "沛国谯县", "安徽亳州", "魏"},
                    {R.mipmap.wen1, "郭嘉", "男", "170年~207年", "奉孝", "颍川阳翟", "河南禹州", "魏"},
                    {R.mipmap.wu1, "张辽", "男", "169年~222年", "文远", "雁门马邑", "山西朔州", "魏"},
                    {R.mipmap.wu2, "典韦", "男", "？~197年", "古之恶来", "陈留己吾", "河南商丘市", "魏"},
                    {R.mipmap.wu3, "于禁", "男", "？~221年", "文则", "泰山钜平", "山东泰安南", "魏"},
                    {R.mipmap.wen2, "王朗", "男", "？~228年", "景兴", "东海郯", "山东临沂市", "魏"},
                    {R.mipmap.wu4, "夏侯淳", "男", "？~220年年", "元让", "沛国谯", "安徽亳州", "魏"},
                    {R.mipmap.wu5, "夏侯渊", "男", "？~219年", "妙才", "沛国谯", "安徽亳州", "魏"},
                    {R.mipmap.wen3, "荀彧", "男", "163年~212年", "文若", "颍川颍阴", "河南许昌", "魏"},
                    {R.mipmap.wen4, "贾诩", "男", "147年~223年", "文和", "凉州姑臧", "甘肃武威市", "魏"},
                    {R.mipmap.liubei, "刘备", "男", "161年~223年", "玄德", "幽州涿郡涿县", "河北省涿州市", "蜀"},
                    {R.mipmap.guanyu, "关羽", "男", "？~220年", "云长", "河东郡解县", "今山西运城", "蜀"},
                    {R.mipmap.zhangfei, "张飞", "男", "？~221年", "益德", "幽州涿郡", "河北省保定市", "蜀"},
                    {R.mipmap.zhaoyun, "赵云", "男", "？~229年", "子龙", "常山真定", "河北省正定", "蜀"},
                    {R.mipmap.zhugeliang, "诸葛亮", "男", "181年~234年", "孔明", "徐州琅琊阳都", "山东临沂市", "蜀"},
                    {R.mipmap.wu6, "姜维", "男", "202年~264年", "伯约", "天水冀县", "甘肃甘谷东南", "蜀"},
                    {R.mipmap.wu7, "马超", "男", "176年~222年", "孟起", "扶风茂陵", "现址不详", "蜀"},
                    {R.mipmap.wu1, "黄忠", "男", "？~220年", "汉升", "南阳", "河南南阳", "蜀"},
                    {R.mipmap.wen5, "庞统", "男", "179年~214年", "士元", "荆州襄阳", "湖北襄阳", "蜀"},
                    {R.mipmap.wen1, "费祎", "男", "？~253年", "文伟", "荆州江夏鄳县", "河南省信阳市", "蜀"},
                    {R.mipmap.sunquan, "孙权", "男", "182年~252年", "仲谋", "吴郡富春", "浙江富阳", "吴"},
                    {R.mipmap.sunce, "孙策", "男", "175年~200年", "伯符", "吴郡富春", "浙江富阳", "吴"},
                    {R.mipmap.zhouyu, "周瑜", "男", "175年~210年", "公瑾", "庐江舒", "合肥庐江舒县", "吴"},
                    {R.mipmap.wu7, "鲁肃", "男", "172年~217年", "子敬", "临淮郡东城县", "安徽定远", "吴"},
                    {R.mipmap.wu6, "吕蒙", "男", "179年~220年", "子明", "汝南富陂人", "安徽阜南吕家岗", "吴"},
                    {R.mipmap.luxun, "陆逊", "男", "183年~245年", "伯言", "吴郡吴县", "江苏苏州", "吴"},
                    {R.mipmap.wu5, "黄盖", "男", "不详", "公覆", "零陵泉陵", "湖南省永州市", "吴"},
                    {R.mipmap.wen5, "张昭", "男", "156年~236年", "子布", "徐州彭城", "江苏徐州", "吴"},
                    {R.mipmap.wu4, "甘宁", "男", "？~220年", "兴霸", "巴郡临江", "重庆忠县", "吴"},
                    {R.mipmap.wu3, "马忠", "男", "？~249年", "德信", "巴西阆中", "四川阆中", "吴"},
                    };
            myDBOpenHelper = new myDBOpenHelper(mcontext, "my.db", null, 1);
            db = myDBOpenHelper.getWritableDatabase();

            //Add person to database
            for (int i = 0; i < person.length; i++) {
                db.execSQL("INSERT INTO person(imageId,name,sex,birthday,styleName,birthPlace,birthPlaceNow,power) VALUES(?,?,?,?,?,?,?,?)",
                        person[i]);
            }
        } else {
                myDBOpenHelper = new myDBOpenHelper(mcontext, "my.db", null, 1);
                db = myDBOpenHelper.getWritableDatabase();
        }
    }

    public boolean existDB(String tableName) {
        boolean result = false;
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(mcontext.getDatabasePath("my.db").getPath(), null, SQLiteDatabase.OPEN_READONLY|SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } catch (SQLiteException e) {

        }
        if (checkDB != null) {
            result = true;
            checkDB.close();
        }
        return result;
    }

    public  List<Person> findDB(String statement) {
        List<Person> persons = new ArrayList<>();
        Cursor cursor =  db.rawQuery(statement, null);
        //存在数据才返回true
        while (cursor.moveToNext())
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
            persons.add(new Person(persenId, imagId, name, sex, birthday, styleName, birthPlace, birthPlaceNow, power));
        }
        cursor.close();
        return persons;
    }
    /*
        *the above is some operations of database
        * *****************************************************************************************************************************************************
        */

    public void init() {
        //播放背景音乐
        mp = MediaPlayer.create(this,R.raw.music);
        mp.start();

        //一些点击变化的图标的初始tag设定
        music.setTag(0);//"0"表示播放音乐,"1"表示暂停音乐
        wei.setTag(1);//"0"表示"魏国"没有被选择,"1"表示"魏国"被选择
        shu.setTag(0);//"0"表示"蜀国"没有被选择,"1"表示"蜀国"被选择
        wu.setTag(0);//"0"表示"吴国"没有被选择,"1"表示"吴国"被选择

        //show 魏-list in the beginning.
        String SQLStatement = "SELECT * FROM person WHERE power = '魏'";
        changeListview(SQLStatement);
    }

    public void changeListview(String statement) {
        datas = new ArrayList<>();
        peoples = new ArrayList<>();
        List<Person> persons = findDB(statement);
        if (persons.size() >= 1) {
            for (int i = 0; i < persons.size(); i++) {
                datas.add(persons.get(i));
            }
        } else {
            Toast toast=Toast.makeText(getApplicationContext(), "抱歉，查无此人物",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
        for (Person c : datas) {
            Map<String,Object> temp = new LinkedHashMap<>();
            temp.put("icon",c.getImageId());
            temp.put("name",c.getName());
            peoples.add(temp);
        }
        simpleAdapter = new SimpleAdapter(this, peoples, R.layout.list, new String[] {"icon","name"}, new int[] {R.id.icon, R.id.name});
        listView.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
    }

    public void click() {

        music.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int flag = (Integer) view.getTag();
                if (flag == 0) {
                    music.setTag(1);
                    music.setImageResource(R.mipmap.pause);
                    mp.pause();
                    Toast toast=Toast.makeText(getApplicationContext(), "音乐已停止",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                } else {
                    music.setTag(0);
                    music.setImageResource(R.mipmap.play);
                    mp.start();
                    Toast toast=Toast.makeText(getApplicationContext(), "音乐已继续播放",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showDialog(mcontext);
            }
        });

        wei.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int flag = (Integer) view.getTag();
                if (flag == 0) {
                    wei.setTag(1);
                    shu.setTag(0);
                    wu.setTag(0);
                    wei.setBackgroundResource(R.mipmap.input_wei);
                    shu.setBackgroundResource(R.mipmap.shu);
                    wu.setBackgroundResource(R.mipmap.wu);
                    String SQLStatement = "SELECT * FROM person WHERE power = '魏'";
                    changeListview(SQLStatement);
                }
            }
        });

        shu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int flag = (Integer) view.getTag();
                if (flag == 0) {
                    wei.setTag(0);
                    shu.setTag(1);
                    wu.setTag(0);
                    wei.setBackgroundResource(R.mipmap.wei);
                    shu.setBackgroundResource(R.mipmap.input_shu);
                    wu.setBackgroundResource(R.mipmap.wu);
                    String SQLStatement = "SELECT * FROM person WHERE power = '蜀'";
                    changeListview(SQLStatement);
                }
            }
        });

        wu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int flag = (Integer) view.getTag();
                if (flag == 0) {
                    wei.setTag(0);
                    shu.setTag(0);
                    wu.setTag(1);
                    wei.setBackgroundResource(R.mipmap.wei);
                    shu.setBackgroundResource(R.mipmap.shu);
                    wu.setBackgroundResource(R.mipmap.input_wu);
                    String SQLStatement = "SELECT * FROM person WHERE power = '吴'";
                    changeListview(SQLStatement);
                }
            }
        });

        seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchContent = searchName.getText().toString();
                String SQLStatement = "SELECT * " +
                        "FROM person " +
                        "WHERE name LIKE '%" + searchContent +"%' " +
                        "OR sex LIKE '%" + searchContent +"%' " +
                        "OR birthday LIKE '%" + searchContent +"%' " +
                        "OR birthPlace LIKE '%" + searchContent +"%' " +
                        "OR birthPlaceNow LIKE '%" + searchContent +"%' " +
                        "OR power LIKE '%" + searchContent +"%' ";
                changeListview(SQLStatement);
//                Toast.makeText(mcontext, searchContent, Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(mcontext, ItemDetailActivity.class);
                Person person = datas.get(arg2);
                intent.putExtra("person", person);
                startActivity(intent);
//                showDialog(MainActivity.this,datas.get(arg2));//创建一个自定义的dialog，里面包含人物信息
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final int arg = arg2;
                HashMap<String, String> map = (HashMap<String, String>) arg0.getItemAtPosition(arg2);
                String Name = map.get("name");//获取item人物名称
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("确定从列表中删除【" + Name + "】?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Person person = datas.get(arg);
                                peoples.remove(arg);
                                simpleAdapter.notifyDataSetChanged();
                                datas.remove(arg);
                                //从数据库中删除该项
                                myDBOpenHelper = new myDBOpenHelper(mcontext, "my.db", null, 1);
                                db = myDBOpenHelper.getWritableDatabase();
                                db.execSQL("DELETE FROM person WHERE personid = ?", new Object[]{person.getPersonId()});
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).create();
                alertDialog.show();
                return true;
            }
        });
    }

    /*
        *the following is some operations of dialog
        * ******************************************************************************************************************************************************
        */
    public void showDialog(Context context) {

        final View view = LayoutInflater.from(context).inflate(R.layout.fixable_dialog_style, null);
        dialog = new Dialog(context);
        dialog.setContentView(view);

        //存放一组图片，用于人物头像的选择
        final int[] Images = {
                R.mipmap.girl1, R.mipmap.girl2, R.mipmap.girl3, R.mipmap.girl4,
                R.mipmap.wen1, R.mipmap.wen2, R.mipmap.wen3, R.mipmap.wen4, R.mipmap.wen5,
                R.mipmap.wu1, R.mipmap.wu2, R.mipmap.wu3, R.mipmap.wu4, R.mipmap.wu5, R.mipmap.wu6, R.mipmap.wu7};

        Button confirm = (Button) view.findViewById(R.id.confirm) ;
        Button cancel = (Button) view.findViewById(R.id.cancel) ;
        final ImageView img = (ImageView) view.findViewById(R.id.img);

        //点击头像，图片发生改变
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageIndex == 16) {imageIndex = 0;}
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
                ImageView img = (ImageView) view.findViewById(R.id.img);
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
                Object[] personInf = {Images[imageIndex], name, gender, birthDay, styleName, birthPlace, birthPlaceNow, power};
                db.execSQL("INSERT INTO person(imageId,name,sex,birthday,styleName,birthPlace,birthPlaceNow,power) VALUES(?,?,?,?,?,?,?,?)",
                        personInf);
                imageIndex = 0;
                dialog.dismiss();
                Toast toast=Toast.makeText(getApplicationContext(), "增加人物成功",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                //增加人物后及时刷新列表页面
                if ((Integer) wei.getTag() == 1) {
                    String SQLStatement = "SELECT * FROM person WHERE power = '魏'";
                    changeListview(SQLStatement);
                } else if ((Integer) shu.getTag() == 1) {
                    String SQLStatement = "SELECT * FROM person WHERE power = '蜀'";
                    changeListview(SQLStatement);
                } else if ((Integer) wu.getTag() == 1) {
                    String SQLStatement = "SELECT * FROM person WHERE power = '吴'";
                    changeListview(SQLStatement);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /*
        *the above is some operations of dialog
        * ******************************************************************************************************************************************************
        */
    @Override
    public void onResume() {
        super.onResume();
        if ((Integer) wei.getTag() == 1) {
            String SQLStatement = "SELECT * FROM person WHERE power = '魏'";
            changeListview(SQLStatement);
        } else if ((Integer) shu.getTag() == 1) {
            String SQLStatement = "SELECT * FROM person WHERE power = '蜀'";
            changeListview(SQLStatement);
        } else if ((Integer) wu.getTag() == 1) {
            String SQLStatement = "SELECT * FROM person WHERE power = '吴'";
            changeListview(SQLStatement);
        }
    }
}
