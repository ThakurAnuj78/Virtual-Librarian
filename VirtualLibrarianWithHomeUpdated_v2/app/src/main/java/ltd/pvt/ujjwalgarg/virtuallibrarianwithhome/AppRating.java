package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

import static ltd.pvt.ujjwalgarg.virtuallibrarianwithhome.R.id.name;
import static ltd.pvt.ujjwalgarg.virtuallibrarianwithhome.R.id.ratingBar;

public class AppRating extends AppCompatActivity {
    int flag=0;
    float rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_rating);
        final RatingBar rating=(RatingBar)findViewById(ratingBar);
        TextView txt= (TextView) findViewById(R.id.name);
        String uname=getIntent().getStringExtra("username");
        txt.setText(uname);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Submit the rating")
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        flag=0;
                        dialog.cancel();
                        rating.setRating(0);
                    }
                });
        final AlertDialog alert = builder.create();

        //Rating Bar
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float value, boolean fromUser) {
                alert.show();
                rate= rating.getRating();
                // if(flag==0)
                //   rating.setRating(0);
            }
        });
    }
}
