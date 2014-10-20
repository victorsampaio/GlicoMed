package android.com.glicomed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class GlicomedMainActivity extends Activity implements OnClickListener{
    private static final String ARCHIVE = "archive.txt";
    private static final String CATEGORY = "glicomed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glicomed_main);

        Button button;

        // Next Activity - ReportsActivity
        button = (Button)findViewById(R.id.btnNext);
        button.setOnClickListener(this);

        //CLEAR
        button = (Button)findViewById(R.id.btnClear);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = (EditText)findViewById(R.id.edtMgdLValue);
                text.setText("");
            }
        });

        //SAVE
        button = (Button)findViewById(R.id.btnSave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream out = openFileOutput(ARCHIVE, MODE_APPEND);
                    EditText text = (EditText)findViewById(R.id.edtMgdLValue);
                    String msg = text.getText().toString();
                    out.write("\n".getBytes());
                    out.write(msg.getBytes());
                    out.close();
                    Log.i(CATEGORY, msg + " - Write Success");
                    visualizeArchive();

                } catch (FileNotFoundException e){
                    Log.e(CATEGORY, e.getMessage(), e);
                }catch (IOException e){
                    Log.e(CATEGORY, e.getMessage(), e);
                }
            }
        });

        //DELETE
        button = (Button)findViewById(R.id.btnDelete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = deleteFile(ARCHIVE);
                Log.i(CATEGORY, "Archive deleted ?" + ok);
                visualizeArchive();
            }
        });

        visualizeArchive();
    }

    private void visualizeArchive(){
        TextView text = (TextView)findViewById(R.id.textViewSaved);

        try {

            File file = getFileStreamPath(ARCHIVE);
            Log.i(CATEGORY, "Opening archive: " + file.getAbsolutePath());

            if (file.exists()) {
                FileInputStream inputStream = openFileInput(ARCHIVE);
                int size = inputStream.available();
                byte bytes[] = new byte[size];
                inputStream.read(bytes);

                String s = new String(bytes);
                text.setText(s);
            }else {
                Log.i(CATEGORY, "Archive Not Exist or Deleted");
                text.setText("");
            }
        }catch (FileNotFoundException e){
            Log.e(CATEGORY, "Archive Not Found" + e.getMessage(), e);
        }catch (IOException e){
            Log.e(CATEGORY, "" + e.getMessage(),e );
        }
    }


    // Intent -> Next Activity - ReportsActivity - onClick
    @Override
    public void onClick(View v) {
        Intent it = new Intent(this, ReportsActivity.class);
        startActivity(it);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.glicomed_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
