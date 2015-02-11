package uk.remon.torch.torch;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private Button button;
    private boolean torch_status = false;
    private Camera cam = Camera.open();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.torch_toggle);
        button.setOnClickListener(mClickListener);



    }



    private View.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                if (torch_status){
                    //Turn off the flash
                    flashOff();
                }   else if (!torch_status) {
                    //Turn on the flash
                    flashOn();
                }   else    {
                    displayToast("Error");
                }
                //Update the UI components
                updateComponents();
            } catch (Exception e)   {
                displayToast("Camera is in use");
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
                    updateComponents
                This method is responible for changing the UI
                elements when the torch is toggled or app is launched/resumed


     */


    void updateComponents() {
        TextView t = (TextView)findViewById(R.id.header); //The on/off text
        TextView a = (TextView)findViewById(R.id.appText); //Text throughout the whole application
        TextView s = (TextView)findViewById(R.id.statusbar); //Status bar
        a.setTextColor(Color.rgb(255, 255, 255));


        //If the torch is on
        if (torch_status)   {

            t.setText("on");
            t.setTextColor(Color.rgb(159, 187, 88));
            s.setBackgroundColor(Color.rgb(159, 187, 88));
        //If the torch is not on
        } else if (!torch_status)   {
            t.setText("off");
            t.setTextColor(Color.rgb(226, 84, 64));
            s.setBackgroundColor(Color.rgb(226, 84, 64));
        //Or if it is something else? huh but its a boolean. just in case...
        }   else    {
            displayToast("Error");
        }

    }


    //Switches the flash on the camera on
    private void flashOn()    {
        try {
            torch_status = true;
            Camera.Parameters p = cam.getParameters();

            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            cam.startPreview();
        }   catch (Exception e) {
            displayToast("Something went wrong..");
        }

    }

    private void flashOff() {
        try {
            torch_status = false;
            Camera.Parameters p = cam.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            cam.setParameters(p);
            cam.startPreview();
        }   catch (Exception e) {
            displayToast("Something went wrong..");
        }

    }

    private void displayToast(String message)   {

        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }



    //When the app is paused
    @Override
    public void onPause() {
        super.onPause();

        if (cam != null) {
            cam.release();
            cam = null;
        }
    }



    //Wen the app is resumed
    @Override
    public void onResume() {
        super.onResume();

        if (cam == null) {
            torch_status = false;
            initializeCamera(); //Start the camera
        }
        //Update the UI components
        updateComponents();
    }


    //Sets up the camera to be used by the app
    void initializeCamera() {
        cam = Camera.open();

    }


}
