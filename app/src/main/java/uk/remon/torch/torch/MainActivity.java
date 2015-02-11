package uk.remon.torch.torch;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.camera2.*;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity {

    Button button;

    public boolean torch_status = false;
    Camera cam = Camera.open();

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
                    flashOff();
                }   else if (!torch_status) {
                    flashOn();
                }   else    {
                    displayToast("Error");
                }
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

    public void updateComponents() {
        if (torch_status)   {
            TextView t = (TextView)findViewById(R.id.header);
            t.setText("Torch is on");
        } else if (!torch_status)   {
            TextView t = (TextView)findViewById(R.id.header);
            t.setText("Torch is off");
        }   else    {
            displayToast("Error");
        }

    }

    private void flashOn()    {
        torch_status = true;
        Camera.Parameters p = cam.getParameters();

        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
        cam.startPreview();
    }

    private void flashOff() {
        torch_status = false;
        Camera.Parameters p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        cam.setParameters(p);
        cam.startPreview();
    }

    private void displayToast(String message)   {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        if (cam != null) {
            cam.release();
            cam = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // Get the Camera instance as the activity achieves full user focus
        if (cam == null) {
            torch_status = false;
            initializeCamera(); // Local method to handle camera init
        }
        updateComponents();
    }

    public void initializeCamera() {
        cam = Camera.open();

    }


}
