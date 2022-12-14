package fr.cpe.dupuis.iotprojet;


import static java.lang.Integer.parseInt;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import fr.cpe.dupuis.iotprojet.MyThreadEventListener;

public class MainActivity extends AppCompatActivity implements View.OnDragListener, View.OnLongClickListener {

    /* Initialisation des variables */
    private Thread thread;
    EditText test;
    EditText monport;
    LinearLayout vue2;
    LinearLayout vue3;
    private BlockingQueue<String> queue;
    private MainActivity activity ;




    //******************************* On Create de l'application ***************************************************//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activity = this ;

        /* Initialisation recuperation port et adresse serveur */
        test = (EditText) findViewById (R.id.monip);
        monport = (EditText) findViewById (R.id.monport);

        /* Mise en place des listener pour les boutons à déplacer (Drag and drop) */
        Button btn0 = (Button) findViewById(R.id.valider);
        Button btn = (Button) findViewById(R.id.btnDrag);
        btn.setTag("DRAGGABLE BUTTON");
        btn.setOnLongClickListener(this);
        findViewById(R.id.layout2).setOnDragListener(this);
        findViewById(R.id.layout3).setOnDragListener(this);

        Button btn2 = (Button) findViewById(R.id.btnDrag2);
        btn2.setTag("DRAGGABLE BUTTON");
        btn2.setOnLongClickListener(this);
        findViewById(R.id.layout2).setOnDragListener(this);
        findViewById(R.id.layout3).setOnDragListener(this);

        /* Partie relative à l'envoi de donnée */
        queue=new ArrayBlockingQueue<String>(100); // Création de la file d'attente
        TextView temperatureTextView = (TextView) findViewById(R.id.resultat);
        TextView humiditeTextView = (TextView) findViewById(R.id.resultat2);
        temperatureTextView.setText(" Waiting...");
        humiditeTextView.setText(" Waiting...");
        MyThreadEventListener listener = new MyThreadEventListener() { // Création d'un listener pour récupérer les données reçues
            @Override
            public void onEventInMyThread(String message) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        String temperature = message.split(";")[0]; // Récupération de la température
                        String humidite = message.split(";")[1]; // Récupération de l'humidité
                        temperature = temperature.split(":")[1]; // Récupération de la température
                        temperature = temperature.substring(0, temperature.length() - 2) + "°C"; // Récupération de la température
                        humidite = humidite.split(":")[1]; // Récupération de l'humidité
                        humidite = humidite.substring(0, humidite.length() - 2) + "%"; // Récupération de l'humidité

                        temperatureTextView.setText(temperature);// Affichage de la température
                        humiditeTextView.setText(humidite);// Affichage de l'humidité
                    }
                });
            }

        };




        //**************************  Fin du On Create de l'application ***************************************************//



//************************** OnClick listener pour le bouton de validation des parametres IP*********************//

        btn0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /* Initialisation des variables */
                vue2 = (LinearLayout) findViewById (R.id.layout2);
                vue3 = (LinearLayout) findViewById (R.id.layout3);
                String ip = test.getText().toString(); // Récupération de l'adresse IP
                String port = monport.getText().toString(); // Récupération du port


                /* Verification des champs */



                Reception reception = new Reception(listener, ip, parseInt(port), queue); // Création de la classe de réception



                /* POP-UP de validation */
                AlertDialog.Builder test = new AlertDialog.Builder(activity);
                test.setTitle("Attention");
                test.setMessage ("Voulez-vous valider ces paramètres ?");
                test.setPositiveButton( "oui",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        /* Initialisation des variables */
                        vue2 = (LinearLayout) findViewById (R.id.layout2);
                        vue3 = (LinearLayout) findViewById (R.id.layout3);
                        String idbouton = "btnDrag";
                        String idbouton2 = "btnDrag2";


                        /*@corentin try and catch sur la co au serveur, si fonctionne on fait le code juste en dessous, sinon rien */

                        /*try*/
                        /*  Récupération  enfants  layout2 pour verifier  button qu'il contient et renvoyer la valeur associée vers le serveur */
                        int nbenfant2 = vue2.getChildCount();
                        for (int k=0;k<nbenfant2;k++) {
                            View nextChild = vue2.getChildAt(k);
                            String str2 = String.valueOf(getResources().getResourceEntryName(nextChild.getId()));
                            if(str2.equals(idbouton)){
                                /* Partie relative à l'envoi de donnée */
                                //@Corentin envoi de la lettre T en premier caractere vers le serveur
                                queue.add("TH"); // si la température position 1 envoie TH
                                Log.i("valeurchoix1", "Temperature");
                            }else if(str2.equals(idbouton2)){
                                /* Partie relative à l'envoi de donnée */
                                //@Corentin envoi de la lettre H en premier caractere vers le serveur
                                queue.add("HT"); // si l'humidité position 1 envoie HT
                                Log.i("valeurchoix1", "Humidite");
                            }
                        }

                        /*  Récupération  enfants  layout3 pour verifier  button qu'il contient et renvoyer la valeur associée vers le serveur */
                        int nbenfant3 = vue3.getChildCount();
                        for (int j=0;j<nbenfant3;j++) {
                            View nextChild = vue3.getChildAt(j);
                            nextChild.getId();
                            String str2 = String.valueOf(getResources().getResourceEntryName(nextChild.getId()));

                            if(str2.equals(idbouton)){
                                /* Partie relative à l'envoi de donnée */
                                //@Corentin envoi de la lettre T en deuxieme caractere vers le serveur

                                Log.i("valeurchoix2", "Temperature");
                            }else if(str2.equals(idbouton2)){
                                /* Partie relative à l'envoi de donnée */
                                //@Corentin envoi de la lettre H en deuxieme caractere vers le serveur

                                Log.i("valeurchoix2", "Humidite");
                            }
                        }
                        /* fin du try*/
                        /* catch*/
                        //** erreur serveur//
                        reception.start(); // lancement du thread de reception
                    }
                });
                test.setNegativeButton("non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"Non", Toast.LENGTH_SHORT).show();
                    }
                });
                test.show();
                /*  FIN de POP-UP de validation */






            }


        });

        //**************************  FIN   de   OnClick listener pour le bouton de validation des parametres IP*********************//


    }










    //**************************  Drag and drop *************************************************************************************//
    @Override
    public boolean onLongClick(View v) {
        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);
        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);
        v.startDrag(data
                , dragshadow
                , v
                , 0
        );
        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        Log.i("DRAG", "onDrag: " + event.getAction());

        /* Initialisation des variables */
        boolean presenceBoutonDansView3 =false;
        boolean presenceBoutonDansView2=false;
        String idbouton = "btnDrag";
        String idbouton2 = "btnDrag2";
        String layout2string = "layout2";
        String layout3string = "layout3";
        Button btn = (Button) findViewById(R.id.btnDrag);
        Button btn2 = (Button) findViewById(R.id.btnDrag2);
        String lelayout2 = "layout2";
        String lelayout3 = "layout3";
        vue2 = (LinearLayout) findViewById (R.id.layout2);
        vue3 = (LinearLayout) findViewById (R.id.layout3);

        int action = event.getAction();



        switch (action) {

            case DragEvent.ACTION_DRAG_STARTED:
                // Verifie si la vue peux supporter un drag du button
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    return true;
                }
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;

            case DragEvent.ACTION_DRAG_EXITED:

            case DragEvent.ACTION_DRAG_ENDED:
                v.getBackground().clearColorFilter();
                v.invalidate();
                return true;

            case DragEvent.ACTION_DROP:
                /* Initialisation des variables */
                v.getBackground().clearColorFilter();
                v.invalidate();
                View vw = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) vw.getParent();
                owner.removeView(vw);
                LinearLayout container = (LinearLayout) v;

                //  vw correspond au button drag and drop
                //  owner correspond au layout source
                //  container correspond au layout destination

                String IDlayoutdest = String.valueOf(getResources().getResourceEntryName(owner.getId()));

                /* glisser un bouton de layout 2 vers 3 avec inversion des deux boutons */
                if((IDlayoutdest.equals(layout2string))){

                    int nbenfant3 = vue3.getChildCount();

                    for (int j=0;j<nbenfant3;j++) {
                        View nextChild = vue3.getChildAt(j);
                        nextChild.getId();
                        String str2 = String.valueOf(getResources().getResourceEntryName(nextChild.getId()));

                        if(str2.equals(idbouton)){
                            Log.i("debug", "debug2");
                            ViewGroup layout = (ViewGroup) btn.getParent();
                            layout.removeView(btn);
                            container.addView(vw);
                            vw.setVisibility(View.VISIBLE);
                            owner.addView(btn);

                        }else if(str2.equals(idbouton2)){
                            Log.i("debug", "debug1");
                            ViewGroup layout = (ViewGroup) btn2.getParent();
                            layout.removeView(btn2);
                            container.addView(vw);
                            vw.setVisibility(View.VISIBLE);
                            owner.addView(btn2);
                        }
                    }

                    /* glisser un bouton de layout 3 vers 2  avec inversion des deux boutons */
                }else if ((IDlayoutdest.equals(layout3string))){


                    int nbenfant = vue2.getChildCount();
                    for (int k=0;k<nbenfant;k++) {
                        View nextChild = vue2.getChildAt(k);
                        String str2 = String.valueOf(getResources().getResourceEntryName(nextChild.getId()));
                        if(str2.equals(idbouton)){
                            Log.i("debug", "debug2");
                            ViewGroup layout = (ViewGroup) btn.getParent();
                            layout.removeView(btn);
                            container.addView(vw);
                            vw.setVisibility(View.VISIBLE);
                            owner.addView(btn);

                        }else if(str2.equals(idbouton2)){
                            Log.i("debug", "debug1");
                            ViewGroup layout = (ViewGroup) btn2.getParent();
                            layout.removeView(btn2);
                            container.addView(vw);
                            vw.setVisibility(View.VISIBLE);
                            owner.addView(btn2);

                        }

                    }

                }


                return true;

            default:
                Log.e("DragDrop", "Un probleme est survenu sur le drag.");
                break;
        }








        return false;
    }

    //**************************  Fin Drag and drop *************************************************************************************//
}