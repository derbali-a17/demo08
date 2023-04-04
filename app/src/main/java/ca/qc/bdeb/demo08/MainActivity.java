package ca.qc.bdeb.demo08;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    Future<?> future;
    private TextView lblTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        progressBar.setProgress(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            progressBar.setMin(0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            progressBar.setMax(100);
        }

        lblTimer.setText("0");
        startTimer();
    }

    private void startTimer() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                //simuler un telechargement
                int valeurCourante = 0;
                int valeurMaximale = 10000;
                while (valeurCourante < valeurMaximale) {
                    try {
                        Thread.sleep(1000);
                        valeurCourante += 1;
                        final int valeur = valeurCourante;
                        //mise à jour de UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lblTimer.setText(String.valueOf(valeur));
                            }
                        });
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        service.shutdown();
    }

    public void annuler(View view) {
        if (future != null) {
            future.cancel(true);
            //mise à jour de l'affichage
            progressBar.setProgress(0);
        }
    }

    public void telecharger(View view) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        future = service.submit(new Runnable() {
            @Override
            public void run() {
                //simuler un telechargement
                int valeurCourante = 0;
                int valeurMaximale = 100;
                while (valeurCourante < valeurMaximale) {
                    try {
                        Thread.sleep(1000);
                        valeurCourante += 1;
                        final int valeur = valeurCourante;
                        //mise à jour de UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(valeur);
                            }
                        });
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        service.shutdown();
    }

    public void voirMessage(View view) {
        Toast.makeText(this, "Hello World", Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        lblTimer = (TextView) findViewById(R.id.lbl_timer);
    }
}