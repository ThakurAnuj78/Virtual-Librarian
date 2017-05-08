package ltd.pvt.ujjwalgarg.virtuallibrarianwithhome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BookReturn extends Fragment {

    private TextView br_name,br_user,br_date,br_fine;
    int is_fine=0;
    private Button br_scan,br_return;
    Barcode barcode;
    String username,issuedate;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_book_return,container,false);
        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        br_name = (TextView) getActivity().findViewById(R.id.br_name);
        br_user = (TextView) getActivity().findViewById(R.id.br_user);
        br_date = (TextView) getActivity().findViewById(R.id.br_date);
        br_fine = (TextView) getActivity().findViewById(R.id.br_fine);
        br_scan = (Button)   getActivity().findViewById(R.id.br_scan);
        br_return=(Button)   getActivity().findViewById(R.id.br_return);
    }

    @Override
    public void onStart() {
        super.onStart();
        br_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBarcode();


            }
        });

        br_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnBook();
            }
        });
    }
    public void scanBarcode(){
        Intent intent = new Intent(getActivity(),ScanBarcodeActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    String url = "http://libraryapp.esy.es/JSON3.php";
                    barcode = data.getParcelableExtra("barcode");
                    final ProgressDialog dialog=new ProgressDialog(getActivity());
                    dialog.setTitle("Searching");
                    dialog.setMessage("Fetching Data");
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.show();
                    StringRequest stringrequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialog.dismiss();

                            showJSON(response);
                            br_name.setText(barcode.displayValue);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map=new HashMap<>();
                            map.put("bookID",barcode.displayValue);
                            return map;
                        }
                    };
                    RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
                    requestQueue.add(stringrequest);
                }
                else{
                    Toast.makeText(getActivity(), "No barcode found", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void showJSON (String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject collegeData = result.getJSONObject(0);
            username=collegeData.getString("uname");
            br_user.setText(collegeData.getString("uname").toUpperCase());
            issuedate=collegeData.getString("date");
            br_date.setText(collegeData.getString("date"));String str=issuedate;
            String ar[]=str.split(" ");
            String stre= new String();
            str=ar[0];
            SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat target= new SimpleDateFormat("dd/MM/yyyy");
            try {
                str=target.format(source.parse(str));
                Date d=target.parse(str);
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.add(Calendar.DATE, 15);
                String output = target.format(c.getTime());
                Date intial= target.parse(output);
                c.setTime(new Date());
                Date finl=target.parse(target.format(c.getTime()));
                if(intial.after(finl)){
                    br_fine.setText("no fine");
                }else{
                    long diff = finl.getTime() - intial.getTime();
                    long ans= TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    br_fine.setText((int) (10*ans));
                    is_fine= (int) (10*ans);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void returnBook() {
        String url2="http://libraryapp.esy.es/returnBook.php";
        final ProgressDialog dialog1=new ProgressDialog(getActivity());
        dialog1.setTitle("Please wait...!!!");
        dialog1.setMessage("Sending data");
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.show();
        StringRequest stringrequest= new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog1.dismiss();
                if(response.contains("success")){
                    Toast.makeText(getActivity(), "Book Returned Successfully", Toast.LENGTH_LONG).show();
                    br_return.setText("Returned");
                    br_return.setClickable(false);
                }
                else{
                    Toast.makeText(getActivity(), "Book can't be returned. Please try again later", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("bookID",barcode.displayValue);
                map.put("username",username);
                map.put("date",issuedate);
                map.put("fine",is_fine+"");
                return map;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringrequest);
    }
}
