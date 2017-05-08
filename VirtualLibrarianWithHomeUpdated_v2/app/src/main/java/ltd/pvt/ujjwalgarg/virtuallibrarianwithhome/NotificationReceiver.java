package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public  class NotificationReceiver extends BroadcastReceiver {

   String text = "";
    private void showJSON(Context context, String response) throws JSONException {

        JSONObject jsonObject= new JSONObject(response);
        JSONArray result=jsonObject.getJSONArray("result");
        for(int i=0;i<result.length();++i){
            JSONObject collegeData=result.getJSONObject(i);
            text=collegeData.getString("bname") + ", " + text + " are due.";
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeating_activity = new Intent(context, Splash_Screen.class);
        repeating_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_activity, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("Virtual Librarian")
                .setContentText(text)
                .setAutoCancel(true);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notificationManager.notify(100, builder.build());
    }
    @Override
    public void onReceive(final Context context, Intent intent) {
        final  String username = intent.getStringExtra("Text");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://libraryapp.esy.es/getNote.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    showJSON(context, s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
                map.put("name",username);
                return  map;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

}
