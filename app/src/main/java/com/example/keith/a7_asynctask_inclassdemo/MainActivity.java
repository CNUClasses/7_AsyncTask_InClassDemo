package com.example.keith.a7_asynctask_inclassdemo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int NUMBER_SECONDS = 4;
    private static final int ONE_SECOND = 500;
    private static final String RUNNING_CALC = "Running Calculation for thread ";
    private static final String DONE = "Done with thread ";
    private TextView myTextView;
    MyTask task;

    //if you want to shut out all UI interaction you can pop a progress dialog
    private ProgressDialog myDialog;
    private void startPD(){
        myDialog = new ProgressDialog(this);
        myDialog.setTitle("Please wait");
        myDialog.setMessage("Notice user cannot interecat with rest of activity");
        myDialog.setCancelable(false);
        myDialog.show();
    }
    private void stopPD(){
        if (myDialog != null)
            myDialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTextView = (TextView)findViewById(R.id.textView2);
    }

    public void doCalculation(View view) {
        //run calculation
         task = new MyTask(this);
        task.execute(NUMBER_SECONDS);
    }

    void runCalcs(Integer numb_updates)
    {
        for (int i = 0; i <= NUMBER_SECONDS; i++) {
            try {
                Thread.sleep(ONE_SECOND);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void doCancel(View view) {
        if (task != null)
            task.cancel(true);
    }

    public static class MyTask extends AsyncTask<Integer,Void,Void>{

        private MainActivity act;
        private static  int numberInstances =0;

        public MyTask(MainActivity act){
            //use to modify UI in parent when done
            this.act = act;
        }
        @Override
        protected Void doInBackground(Integer... integers) {
            for (int i =0;i<=integers[0];i++){
                act.runCalcs(i);
                if (this.isCancelled() == true)
                    break;
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            act.myTextView.setText(RUNNING_CALC + Integer.toString( ++numberInstances) );
            act.startPD();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            act.myTextView.setText(DONE + Integer.toString(numberInstances--));
            act.stopPD();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            act.myTextView.setText(DONE + "USER CANCELED");
            act.stopPD();
        }
    }
}
