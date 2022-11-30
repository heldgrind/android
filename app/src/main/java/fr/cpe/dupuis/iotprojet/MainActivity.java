package fr.cpe.dupuis.iotprojet;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnDragListener, View.OnLongClickListener {

    EditText test;
    EditText monport;
    LinearLayout vue1;
    LinearLayout vue2;
    LinearLayout vue3;
    private MyThread thread;
    private BlockingQueue<String> queue;
    private MainActivity activity ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activity = this ;
        //Find all views and set Tag to all draggable views

        Button btn0 = (Button) findViewById(R.id.valider);

        Button btn = (Button) findViewById(R.id.btnDrag);
        btn.setTag("DRAGGABLE BUTTON");
        btn.setOnLongClickListener(this);
        //Set Drag Event Listeners for defined layouts
        findViewById(R.id.layout1).setOnDragListener(this);
        findViewById(R.id.layout2).setOnDragListener(this);
        findViewById(R.id.layout3).setOnDragListener(this);
        findViewById(R.id.layout3).setOnDragListener(this);
        findViewById(R.id.layout4).setOnDragListener(this);


        Button btn2 = (Button) findViewById(R.id.btnDrag2);
        btn2.setTag("DRAGGABLE BUTTON");
        btn2.setOnLongClickListener(this);
        //Set Drag Event Listeners for defined layouts
        findViewById(R.id.layout1).setOnDragListener(this);
        findViewById(R.id.layout2).setOnDragListener(this);
        findViewById(R.id.layout3).setOnDragListener(this);
        findViewById(R.id.layout3).setOnDragListener(this);
        findViewById(R.id.layout4).setOnDragListener(this);

        Button btn3 = (Button) findViewById(R.id.btnDrag3);
        btn3.setTag("DRAGGABLE BUTTON");
        btn3.setOnLongClickListener(this);
        //Set Drag Event Listeners for defined layouts
        findViewById(R.id.layout1).setOnDragListener(this);
        findViewById(R.id.layout2).setOnDragListener(this);
        findViewById(R.id.layout3).setOnDragListener(this);
        findViewById(R.id.layout3).setOnDragListener(this);
        findViewById(R.id.layout4).setOnDragListener(this);

        test = (EditText) findViewById (R.id.monip);
        monport = (EditText) findViewById (R.id.monport);
        queue=new ArrayBlockingQueue<String>(100);
        thread=new MyThread(queue);
        TextView textview = (TextView) findViewById(R.id.resultat);
        List<String> testList = new ArrayList<String>();
        while (queue.size() > 0) {
            testList.add(queue.poll());
        }


        textview.setText(testList.toString());





//************************** OnClick listener pour le bouton de validation des parametres IP*********************//

        btn0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Code here executes on main thread after user presses button



                vue1 = (LinearLayout) findViewById (R.id.layout1);
                vue2 = (LinearLayout) findViewById (R.id.layout2);
                vue3 = (LinearLayout) findViewById (R.id.layout3);

                AlertDialog.Builder test = new AlertDialog.Builder(activity);
                             test.setTitle("Attention");
                            test.setMessage ("Voulez-vous valider ces paramètres ?");
                            test.setPositiveButton( "oui",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(),"Clic sur oui", Toast.LENGTH_SHORT).show();
                            }
                        });

                            test.setNegativeButton("non", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getApplicationContext(),"Non", Toast.LENGTH_SHORT).show();

                                }
                            });

                test.show();

               // vue1.getChildAt() renvoi le nombre d'enfant du layout

               //         boucle sur le nombre d'enfant
               // vue1.getId() == R.id.btnDrag ; compare l'id de l'enfant a celui du boutondrag

               // queue.add("(T)");
               // queue.add("(H)");
            }
        });

        //**************************  FIN   de   OnClick listener pour le bouton de validation des parametres IP*********************//

        thread.start();


}
    @Override
    public boolean onLongClick(View v) {
        // Create a new ClipData.Item from the ImageView object's tag
        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);
        // Instantiates the drag shadow builder.
        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);
        // Starts the drag
        v.startDrag(data        // data to be dragged
                , dragshadow   // drag shadow builder
                , v           // local data about the drag and drop operation
                , 0          // flags (not currently used, set to 0)
        );
        return true;
    }
    // This is the method that the system calls when it dispatches a drag event to the listener.
    @Override
    public boolean onDrag(View v, DragEvent event) {
        // Defines a variable to store the action type for the incoming event
        int action = event.getAction();
        // Handles each of the expected events
        switch (action) {

            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    // if you want to apply color when drag started to your view you can uncomment below lines
                    // to give any color tint to the View to indicate that it can accept data.
                    // v.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                    // Invalidate the view to force a redraw in the new tint
                    //  v.invalidate();
                    // returns true to indicate that the View can accept the dragged data.
                    return true;
                }
                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                // Applies a GRAY or any color tint to the View. Return true; the return value is ignored.
                v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                // Invalidate the view to force a redraw in the new tint
                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                // Re-sets the color tint to blue. Returns true; the return value is ignored.
                // view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                //It will clear a color filter .
                v.getBackground().clearColorFilter();
                // Invalidate the view to force a redraw in the new tint
                v.invalidate();
                return true;

            case DragEvent.ACTION_DROP:
                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);
                // Gets the text data from the item.
                String dragData = item.getText().toString();
                // Displays a message containing the dragged data.
                Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();
                // Turns off any color tints
                v.getBackground().clearColorFilter();
                // Invalidates the view to force a redraw
                v.invalidate();

                View vw = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) vw.getParent();
                owner.removeView(vw); //remove the dragged view
                //caste the view into LinearLayout as our drag acceptable layout is LinearLayout
                LinearLayout container = (LinearLayout) v;
                container.addView(vw);//Add the dragged view
                vw.setVisibility(View.VISIBLE);//finally set Visibility to VISIBLE
                // Returns true. DragEvent.getResult() will return true.
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                // Turns off any color tinting
                v.getBackground().clearColorFilter();
                // Invalidates the view to force a redraw
                v.invalidate();
                // Does a getResult(), and displays what happened.
                if (event.getResult())
                    Toast.makeText(this, "The drop was handled.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show();
                // returns true; the value is ignored.
                return true;
            // An unknown action type was received.
            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }
}
