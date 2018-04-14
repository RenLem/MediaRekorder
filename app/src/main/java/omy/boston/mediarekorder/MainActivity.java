package omy.boston.mediarekorder;

import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    MediaRecorder recorder;
    File audioFile;
    private Button btnStartRec;
    private Button btnStopRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartRec = (Button) findViewById(R.id.btnStartRec);
        btnStopRec = (Button) findViewById(R.id.btnStopRec);

        btnStartRec.setOnClickListener(this);
        btnStopRec.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnStartRec){
            startRecording();
        }
        if (view.getId() == R.id.btnStopRec){
            stopRecording();
        }
    }

    private void startRecording(){
        btnStartRec.setEnabled(false);// Zatamni gumb
        btnStopRec.setEnabled(true);

        File sampleDir = Environment.getExternalStorageDirectory();
        try {
            audioFile = File.createTempFile("zvuk", ".3gp", sampleDir);// postaviš naziv, format i dopuštenje za rec u direktori na SD
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        recorder = new MediaRecorder(); // Kreiraš recoder
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // Pomoću njega snima
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// Format u kojem snima
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // Encoder
        recorder.setOutputFile(audioFile.getAbsolutePath()); // Put gdje da spremi

        try {
            recorder.prepare();// Priprema ako dođe do pogreške
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        recorder.start();

    }

    private void stopRecording(){
        btnStartRec.setEnabled(true);
        btnStopRec.setEnabled(false);// Zatamni gumb

        recorder.stop();
        recorder.release();// Otpuštaš u memoriji

        addRecordToMediaLibrary();// Spremaš u library i pretražuješ ju pomoću MediaScannera
    }

    private void addRecordToMediaLibrary(){
        try {
            Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);// Stvoriš intent za Media Scanner
            i.setData(Uri.fromFile(audioFile)); // Postaviš uri koju datoteku želiš pretražiti
            sendBroadcast(i);// Pošalješ intent
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
